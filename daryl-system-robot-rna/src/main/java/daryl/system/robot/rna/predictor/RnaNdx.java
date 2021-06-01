package daryl.system.robot.rna.predictor;

import java.io.File;
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
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;
import daryl.system.robot.rna.predictor.base.RnaPredictor;
import daryl.system.robot.rna.repository.IHistoricoRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class RnaNdx  extends RnaPredictor{
	
	@Autowired
	ApplicationContext ctx;
	@Autowired
	private IHistoricoRepository historicoRepository; 
	


	@Override
	protected Double calcularPrediccion(Robot bot) throws IOException {

		Double prediccion = 0.0;
		
		File rna = null;
		System.out.println("SE CARGA EL FICHERO: " + bot.getFicheroRna());
		try {
			System.out.println("SE CARGA DESDE EL CLASSPATH");
			rna = ctx.getResource("classpath:/rnas/" + bot.getFicheroRna()).getFile();
		}catch (Exception e) {
			System.out.println("SE HACE LA FORMA TRADICIONAL");
			String fileName = "F:\\DarylSystem\\rnas\\"+bot.getFicheroRna();
	        rna = new File(fileName);
		}
		
		
		/*Alternativa a la carga del fichero*/
		//RnaConfig rnaConfig = super.getRnaConfig(bot);
		//NeuralNetwork neuralNetwork = rnaFromByteArray(rnaConfig.getRna());
		
		NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(rna);
		
		List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(bot.getTimeframe(), bot.getActivo());
		BarSeries serieParaCalculo = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + bot.getTimeframe() + "_" + bot.getActivo(), bot.getActivo().getMultiplicador());
		MaxMinNormalizer darylNormalizer =  new MaxMinNormalizer(serieParaCalculo, Mode.CLOSE);
		List<Double> datos = darylNormalizer.getDatos();
		
		//Double prediccionAnterior = getPrediccionAnterior(rnaConfig.getNeuronasEntrada(), bot, neuralNetwork, datosForecast);
		Double prediccionAnterior = getPrediccionAnterior(bot, neuralNetwork, datos, darylNormalizer);


		
		List<Double> inputs = new ArrayList<Double>();
		int index = 0;
		do {
			index++;
			inputs.add(darylNormalizer.normData(datos.get(datos.size()-index)));		
		//}while(index < rnaConfig.getNeuronasEntrada());
		}while(index < bot.getNeuronasEntrada());

		Collections.reverse(inputs);

		neuralNetwork.setInput(inputs.stream().mapToDouble(Double::doubleValue).toArray());
		neuralNetwork.calculate();
		
        // get network output
        double[] networkOutput = neuralNetwork.getOutput();
        //double predicted = interpretOutput(networkOutput);
        double forecast = darylNormalizer.denormData(networkOutput[0]);
        logger.info("Robot -> " + bot.getRobot() + " PREDICCIÓN -> " + forecast + " ANTERIOR -> " + prediccionAnterior);
        
        if(forecast > prediccionAnterior) {
        	prediccion = 1.0;
        }else if(forecast < prediccionAnterior) {
        	prediccion = -1.0;
        }
        
        
		return prediccion;
	
	}


	

}
