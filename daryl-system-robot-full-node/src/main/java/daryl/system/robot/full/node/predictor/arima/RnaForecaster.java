package daryl.system.robot.full.node.predictor.arima;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.neuroph.core.NeuralNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.MaxMinNormalizer;
import org.ta4j.core.utils.BarSeriesUtils;

import daryl.system.comun.enums.Mode;
import daryl.system.model.RnaConfig;
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;
import daryl.system.robot.full.node.predictor.base.Forecaster;
import daryl.system.robot.full.node.repository.IRnaConfigRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class RnaForecaster  extends Forecaster{

	@Autowired
	private IRnaConfigRepository rnaConfigRepository;

	@Override
	protected Double calcularPrediccion(Robot bot){

		Double prediccion = 0.0;
		RnaConfig rnaConfig = getRnaConfig(bot);
		
		if(rnaConfig != null) {
		
			NeuralNetwork neuralNetwork  = null;
			try {
				neuralNetwork = rnaFromByteArray(rnaConfig.getRna());
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
			
			if(neuralNetwork != null) {		
							
				List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(bot.getTimeframe(), bot.getActivo());
				BarSeries serieParaCalculo = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + bot.getTimeframe() + "_" + bot.getActivo(), bot.getActivo().getMultiplicador());
				MaxMinNormalizer darylNormalizer =  new MaxMinNormalizer(serieParaCalculo, Mode.CLOSE);
				List<Double> datos = darylNormalizer.getDatos();
						
				Double prediccionAnterior = getPrediccionAnterior(rnaConfig.getNeuronasEntrada(), bot, neuralNetwork, datos, darylNormalizer);
				
				List<Double> inputs = new ArrayList<Double>();
				int index = 0;
				do {
					index++;
					inputs.add(darylNormalizer.normData(datos.get(datos.size()-index)));		
				}while(index < rnaConfig.getNeuronasEntrada());
		
				Collections.reverse(inputs);
						
				neuralNetwork.setInput(inputs.stream().mapToDouble(Double::doubleValue).toArray());
				neuralNetwork.calculate();
				
		        double[] networkOutput = neuralNetwork.getOutput();
		        double forecast = darylNormalizer.denormData(networkOutput[0]);
		        logger.info("Robot -> " + bot.getRobot() + " PREDICCIÓN -> " + forecast + " ANTERIOR -> " + prediccionAnterior);
		        
		        if(forecast > prediccionAnterior) {
		        	prediccion = 1.0;
		        }else if(forecast < prediccionAnterior) {
		        	prediccion = -1.0;
		        }
		        
			}
		}
		
        return prediccion;
	
	}
	

	private Double getPrediccionAnterior(int neuronasEntrada, Robot bot, NeuralNetwork neuralNetwork, List<Double> datosForecast, MaxMinNormalizer darylNormalizer) {

		List<Double> inputs = new ArrayList<Double>();
			
		int index = 1;
		do {
			index++;
			inputs.add(darylNormalizer.normData(datosForecast.get(datosForecast.size()-index)));	
		}while(index < neuronasEntrada+1);
		
			
		Collections.reverse(inputs);

		
		neuralNetwork.setInput(inputs.stream().mapToDouble(Double::doubleValue).toArray());
		neuralNetwork.calculate();
		
        // get network output
        double[] networkOutput = neuralNetwork.getOutput();
        //double predicted = interpretOutput(networkOutput);
        double prediccionAnterior =  darylNormalizer.denormData(networkOutput[0]);

        logger.info("PREDICCIÓN ANTERIOR PARA EL ROBOT : {}", prediccionAnterior);
        return prediccionAnterior;
	}
	
	
	private RnaConfig getRnaConfig(Robot robot) {
		
		return rnaConfigRepository.findRnaConfigByRobot(robot.getRnaConfig());
		
	}
	
	
	private NeuralNetwork rnaFromByteArray(byte[] byteArray) throws IOException, ClassNotFoundException {
		
		NeuralNetwork rna = (NeuralNetwork)SerializationUtils.deserialize(byteArray);
		return rna;
	}

}
