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
import daryl.system.model.historicos.HistAudCad;
import daryl.system.robot.rna.inv.predictor.base.RnaPredictor;
import daryl.system.robot.rna.inv.predictor.config.ConfiguracionRnaAudCad1440;
import daryl.system.robot.rna.inv.repository.IHistAudCadRepository;
import lombok.ToString;

@Component(value = "rnaInvAudcad1440")
@ToString
public class RnaInvAudCad1440  extends RnaPredictor{
	
	@Autowired
	Logger logger;
	
	@Autowired(required = true)
	ConfiguracionRnaAudCad1440 configuracion;
	@Autowired
	private DataSetLoader dataSetLoader;
	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistAudCadRepository histAudCadRepository;
	
	private List<HistAudCad> historico;
	private List<Datos> datosTotal;
	

	public final String robot = "RNA_I_AUDCAD_1440";
	public final Boolean inv = Boolean.TRUE;
	
	@PostConstruct
	public void load() {
		
		DatosLoader loader = DatosLoaderOHLC.getInstance();
		datosTotal = loader.loadDatos(configuracion.getFHistoricoLearn());
		//List<Datos> datosTotal = loader.loadDatos(configuracion.getFHistoricoLearn());
	}

	@Override
	public void calculate(Activo activo, String estrategia) {
		//Calcular la predicción
		System.out.println("-----------------------------------------------------------------------------------------------------------------");
		//System.out.println("PREDICCION ANTERIOR -> " + prediccionAnterior);
		Double prediccion = calcularPrediccion();
		//logger.info("Nueva predicción para el AUDCAD D1: {} a las: {}" , prediccion, config.getActualDateFormattedInString());
				
		//actualizamos el fichero de ordenes
		Orden orden = calcularOperacion(activo, estrategia, prediccion, robot, inv);
		
		//Enviamos al controlador para q esté disponible lo antes posible
		//AudCad1440Controller.orden = orden.getTipoOrden();

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//Cerramos la operacion anterior en caso q hubiera
		Long fechaHoraMillis = System.currentTimeMillis();
		
		//Actualizamos la tabla con la predicción
		super.actualizarPrediccionBDs(activo, estrategia, orden.getTipoOrden(), prediccion, fechaHoraMillis);
		super.actualizarUltimaOrden(activo, estrategia, orden, fechaHoraMillis);
		super.guardarNuevaOrden(orden, fechaHoraMillis);
		///// 
		
	}

	@Override
	protected Double calcularPrediccion() {
		Double prediccionAnterior = null;
		Double prediccion = 0.0;
		
		NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(configuracion.getRutaRNA());
		
		historico = histAudCadRepository.findAllByTimeframeOrderByFechaHoraAsc(Timeframes.PERIOD_D1);
		
		List<Datos> datosForecast = toDatosList(historico);
		
		datosTotal.addAll(datosForecast);
		darylNormalizer.setDatos(datosTotal, Mode.valueOf(configuracion.getMode()));
		
		List<Double> datos = darylNormalizer.getDatos();
		//Double anteAnterior = 0.0, anterior = 0.0, ultimo = 0.0;
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
			
			System.out.println(inputs);
			Collections.reverse(inputs);
			neuralNetwork.setInput(inputs.stream().mapToDouble(Double::doubleValue).toArray());
			neuralNetwork.calculate();
			
	        // get network output
	        double[] networkOutput = neuralNetwork.getOutput();
	        //double predicted = interpretOutput(networkOutput);
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

		System.out.println(inputs);
		Collections.reverse(inputs);
		neuralNetwork.setInput(inputs.stream().mapToDouble(Double::doubleValue).toArray());
		neuralNetwork.calculate();
		
        // get network output
        double[] networkOutput = neuralNetwork.getOutput();
        //double predicted = interpretOutput(networkOutput);
        double nuevaPrediccion = darylNormalizer.denormData(networkOutput[0]);
		
        double media = media(configuracion.getPeriodosMedia(), datos);
        if(nuevaPrediccion > prediccionAnterior /*&& datos.get(datos.size()-1) > media && media > 0*/) {
        	//B
        	prediccion = 1.0;
        }else if(nuevaPrediccion < prediccionAnterior /*&& datos.get(datos.size()-1) < media && media > 0*/) {
        	prediccion = -1.0;
        }else {
        	prediccion = 0.0;
        }
        
        //prediccion = nuevaPrediccion - prediccionAnterior;

        try {
			//System.out.println("PRED AUDCAD H1 ANT -> " + prediccionAnterior + " PRED AUDCAD H1 NUEVA -> " + nuevaPrediccion);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return prediccion;
	
	}

	
	/*
	@Override
	protected Orden calcularOperacion(TipoActivo activo, Estrategia estrategia, Double prediccion) {
		
		Orden orden = new Orden();
			orden.setFAlta(System.currentTimeMillis());
			orden.setFBaja(null);
			orden.setEstrategia(Estrategia.RNA_AUDCAD_1H);
			orden.setTipoActivo(TipoActivo.AUDCAD);
			orden.setTipoOrden(TipoOrden.CLOSE);
		if(prediccion < 0.0) {
			orden.setTipoOrden(TipoOrden.SELL);
		}else if(prediccion > 0.0) {
			orden.setTipoOrden(TipoOrden.BUY);
		}else {
			orden.setTipoOrden(TipoOrden.CLOSE);	
		}
		
		return orden;
	}
	*/
	private List<Datos> toDatosList(List<HistAudCad> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (HistAudCad hist : historico) {
			
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
