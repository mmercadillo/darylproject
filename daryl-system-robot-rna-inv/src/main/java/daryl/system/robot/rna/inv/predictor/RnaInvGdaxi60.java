package daryl.system.robot.rna.inv.predictor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.neuroph.core.NeuralNetwork;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import daryl.system.comun.dataset.DataSetLoader;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.loader.DatosLoader;
import daryl.system.comun.dataset.loader.DatosLoaderOHLC;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Orden;
import daryl.system.model.Robot;
import daryl.system.model.historicos.HistGdaxi;
import daryl.system.robot.rna.inv.predictor.base.RnaPredictor;
import daryl.system.robot.rna.inv.predictor.config.ConfiguracionRnaGdaxi60;
import daryl.system.robot.rna.inv.repository.IHistGdaxiRepository;
import lombok.ToString;

@Component(value = "rnaInvGdaxi60")
@ToString
public class RnaInvGdaxi60  extends RnaPredictor{
	
	@Autowired
	Logger logger;
	
	@Autowired(required = true)
	ConfiguracionRnaGdaxi60 configuracion;
	@Autowired
	private DataSetLoader dataSetLoader;
	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistGdaxiRepository histGdaxiRepository;
	
	private List<HistGdaxi> historico;
	private List<Datos> datosTotal;

	
	@PostConstruct
	public void load() {
		
		DatosLoader loader = DatosLoaderOHLC.getInstance();
		datosTotal = loader.loadDatos(configuracion.getFHistoricoLearn());
	}

	@Override
	public void calculate(Robot bot) {
				
		Double prediccion = calcularPrediccion(bot);
				
		//actualizamos el fichero de ordenes
		Orden orden = calcularOperacion(bot.getActivo(), bot.getEstrategia(), prediccion, bot.getRobot(), bot.getInverso());
		logger.info("ORDEN GENERADA " + orden.getTipoOrden().name() + " ROBOT -> " + bot);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//Cerramos la operacion anterior en caso q hubiera
		Long fechaHoraMillis = System.currentTimeMillis();
		
		//Actualizamos la tabla con la predicci??n
		super.actualizarPrediccionBDs(bot.getActivo(), bot.getEstrategia(), orden.getTipoOrden(), prediccion, fechaHoraMillis);
		super.actualizarUltimaOrden(bot.getActivo(), bot.getEstrategia(), orden, fechaHoraMillis);
		super.guardarNuevaOrden(orden, fechaHoraMillis);
		///// 
		
	}

	@Override
	protected Double calcularPrediccion(Robot bot) {
		Double prediccionAnterior = null;
		Double prediccion = 0.0;
		
		NeuralNetwork neuralNetwork = NeuralNetwork.load(configuracion.getRutaRNA());
		
		historico = histGdaxiRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());

		List<Datos> datosForecast = toDatosList(historico);
		
		datosTotal.addAll(datosForecast);
		darylNormalizer.setDatos(datosTotal, Mode.valueOf(configuracion.getMode()));
		
		List<Double> datos = darylNormalizer.getDatos();
		List<Double> inputs = null;
		
		if(prediccionAnterior == null) {
			
			inputs = new ArrayList<Double>();
			
			int index = 1;
			do {
				index++;
				if(Mode.valueOf(configuracion.getMode()) == Mode.CLOSE) {
					inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getCierre()));
				}
				if(Mode.valueOf(configuracion.getMode()) == Mode.HIGH) {
					inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getMaximo()));
				}
				if(Mode.valueOf(configuracion.getMode()) == Mode.LOW) {
					inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getMinimo()));
				}
				if(Mode.valueOf(configuracion.getMode()) == Mode.OPEN) {
					inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getApertura()));
				}			
			}while(index < configuracion.getNeuronasEntrada()+1);			
			
			Collections.reverse(inputs);
			verInputs(inputs);
			neuralNetwork.setInput(inputs.stream().mapToDouble(Double::doubleValue).toArray());
			neuralNetwork.calculate();
			
	        // get network output
	        double[] networkOutput = neuralNetwork.getOutput();
	        prediccionAnterior = darylNormalizer.denormData(networkOutput[0]);
			
		}
		
		
		inputs = new ArrayList<Double>();
		int index = 0;
		do {
			index++;
			if(Mode.valueOf(configuracion.getMode()) == Mode.CLOSE) {
				inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getCierre()));
			}
			if(Mode.valueOf(configuracion.getMode()) == Mode.HIGH) {
				inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getMaximo()));
			}
			if(Mode.valueOf(configuracion.getMode()) == Mode.LOW) {
				inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getMinimo()));
			}
			if(Mode.valueOf(configuracion.getMode()) == Mode.OPEN) {
				inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getApertura()));
			}			
		}while(index < configuracion.getNeuronasEntrada());

		Collections.reverse(inputs);
		verInputs(inputs);
		neuralNetwork.setInput(inputs.stream().mapToDouble(Double::doubleValue).toArray());
		neuralNetwork.calculate();
		
        // get network output
        double[] networkOutput = neuralNetwork.getOutput();
        double nuevaPrediccion = darylNormalizer.denormData(networkOutput[0]);
		
        if(nuevaPrediccion > datos.get(datos.size()-1) ) {
        	//B
        	prediccion = 1.0;
        }else if(nuevaPrediccion < datos.get(datos.size()-1) ) {
        	prediccion = -1.0;
        }else {
        	prediccion = 0.0;
        }

        
		return prediccion;
	}

	private List<Datos> toDatosList(List<HistGdaxi> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (HistGdaxi hist : historico) {
			
			Datos dato = Datos.builder().fecha(hist.getFecha())
										.hora(hist.getHora())
										.apertura(hist.getApertura())
										.maximo(hist.getMaximo())
										.minimo(hist.getMinimo())
										.cierre(hist.getCierre())
										.volumen(hist.getVolumen())
										.build();
			datos.add(dato);
			
		}
		
		return datos;
		
		
	}
	
	protected void verInputs(List<Double> inputs) {
		StringBuffer buffer = new StringBuffer();
		for (Double input : inputs) {
			buffer.append(darylNormalizer.denormData(input)).append("-");
		}
		System.out.println(buffer.toString());
	}
	
	
}
