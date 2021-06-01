package daryl.system.robot.rna.predictor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.neuroph.core.NeuralNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.MaxMinNormalizer;
import org.ta4j.core.utils.BarSeriesUtils;

import daryl.system.comun.enums.Mode;
import daryl.system.model.RnaConfig;
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;
import daryl.system.robot.rna.predictor.base.RnaPredictor;
import daryl.system.robot.rna.repository.IHistoricoRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class RnaAudCad  extends RnaPredictor{


	@Autowired
	ApplicationContext ctx;
	@Autowired
	private IHistoricoRepository historicoRepository; 
	

	@Override
	protected Double calcularPrediccion(Robot bot) throws IOException {

		Double prediccion = 0.0;
		RnaConfig rnaConfig = super.getRnaConfig(bot);
		
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


	

}
