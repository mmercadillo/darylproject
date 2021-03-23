package daryl.system.robot.rna.inv.predictor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.neuroph.core.NeuralNetwork;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.comun.configuration.ConfigData;
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
import daryl.system.model.historicos.HistNdx;
import daryl.system.model.historicos.HistXauUsd;
import daryl.system.robot.rna.inv.predictor.base.RnaPredictor;
import daryl.system.robot.rna.inv.predictor.config.ConfiguracionRnaXauUsd60;
import daryl.system.robot.rna.inv.repository.IHistXauUsdRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class RnaInvXauUsd  extends RnaPredictor{


	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistXauUsdRepository histXauUsdRepository;
	

	private static Double prediccionAnterior = null;
	


	@Override
	protected Double calcularPrediccion(Robot bot) {
		

		Double prediccion = 0.0;
		
		NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(ConfigData.BASE_PATH_RNAS + bot.getFicheroRna());
		
		List<HistXauUsd> historico = histXauUsdRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());
		
		List<Datos> datosForecast = toDatosList(historico);
		//List<Datos> datosT = loader.loadDatos(configuracion.getFHistoricoLearn());
		List<Datos> datosTotal = new ArrayList<Datos>();
		datosTotal.addAll(datosForecast);
		darylNormalizer.setDatos(datosTotal, bot.getMode());

		List<Double> inputs = null;
		
		if(prediccionAnterior == null) {
			
			inputs = new ArrayList<Double>();
			
			int index = 1;
			do {
				index++;
				if(bot.getMode() == Mode.CLOSE) {
					inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getCierre()));
				}
				if(bot.getMode() == Mode.HIGH) {
					inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getMaximo()));
				}
				if(bot.getMode() == Mode.LOW) {
					inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getMinimo()));
				}
				if(bot.getMode() == Mode.OPEN) {
					inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getApertura()));
				}			
			}while(index < bot.getNeuronasEntrada()+1);			

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
			if(bot.getMode() == Mode.CLOSE) {
				inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getCierre()));
			}
			if(bot.getMode() == Mode.HIGH) {
				inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getMaximo()));
			}
			if(bot.getMode() == Mode.LOW) {
				inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getMinimo()));
			}
			if(bot.getMode() == Mode.OPEN) {
				inputs.add(darylNormalizer.normData(datosTotal.get(datosTotal.size()-index).getApertura()));
			}			
		}while(index < bot.getNeuronasEntrada());

		Collections.reverse(inputs);

		neuralNetwork.setInput(inputs.stream().mapToDouble(Double::doubleValue).toArray());
		neuralNetwork.calculate();
		
        // get network output
        double[] networkOutput = neuralNetwork.getOutput();
        //double predicted = interpretOutput(networkOutput);
        double nuevaPrediccion = darylNormalizer.denormData(networkOutput[0]);
        
        prediccion = nuevaPrediccion - RnaInvXauUsd.prediccionAnterior;
        RnaInvXauUsd.prediccionAnterior = nuevaPrediccion;

		return prediccion;
	
	}

	
	private List<Datos> toDatosList(List<HistXauUsd> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (HistXauUsd hist : historico) {
			
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
	

	
	
}