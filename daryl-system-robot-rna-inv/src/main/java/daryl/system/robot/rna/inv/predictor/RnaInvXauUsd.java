package daryl.system.robot.rna.inv.predictor;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.model.Robot;
import daryl.system.model.historicos.HistXauUsd;
import daryl.system.robot.rna.inv.predictor.base.RnaPredictor;
import daryl.system.robot.rna.inv.repository.IHistXauUsdRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class RnaInvXauUsd  extends RnaPredictor{

	@Autowired
	ApplicationContext ctx;
	@Autowired
	private IHistXauUsdRepository histXauUsdRepository;
	
	private Double getPrediccionAnterior(Robot bot, NeuralNetwork neuralNetwork, List<Datos> datosForecast) {
		
		DarylMaxMinNormalizer darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		List<Double> inputs = new ArrayList<Double>();
			
		int index = 1;
		do {
			index++;
			if(bot.getMode() == Mode.CLOSE) {
				inputs.add(darylNormalizer.normData(datosForecast.get(datosForecast.size()-index).getCierre()));
			}
			if(bot.getMode() == Mode.HIGH) {
				inputs.add(darylNormalizer.normData(datosForecast.get(datosForecast.size()-index).getMaximo()));
			}
			if(bot.getMode() == Mode.LOW) {
				inputs.add(darylNormalizer.normData(datosForecast.get(datosForecast.size()-index).getMinimo()));
			}
			if(bot.getMode() == Mode.OPEN) {
				inputs.add(darylNormalizer.normData(datosForecast.get(datosForecast.size()-index).getApertura()));
			}			
		}while(index < bot.getNeuronasEntrada()+1);			
		
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
		NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(rna);
		
		List<HistXauUsd> historico = histXauUsdRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());
		
		List<Datos> datosForecast = toDatosList(historico);

		Double prediccionAnterior = getPrediccionAnterior(bot, neuralNetwork, datosForecast);

		DarylMaxMinNormalizer darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
		
		List<Double> inputs = new ArrayList<Double>();
		int index = 0;
		do {
			index++;
			if(bot.getMode() == Mode.CLOSE) {
				inputs.add(darylNormalizer.normData(datosForecast.get(datosForecast.size()-index).getCierre()));
			}
			if(bot.getMode() == Mode.HIGH) {
				inputs.add(darylNormalizer.normData(datosForecast.get(datosForecast.size()-index).getMaximo()));
			}
			if(bot.getMode() == Mode.LOW) {
				inputs.add(darylNormalizer.normData(datosForecast.get(datosForecast.size()-index).getMinimo()));
			}
			if(bot.getMode() == Mode.OPEN) {
				inputs.add(darylNormalizer.normData(datosForecast.get(datosForecast.size()-index).getApertura()));
			}			
		}while(index < bot.getNeuronasEntrada());

		Collections.reverse(inputs);

		neuralNetwork.setInput(inputs.stream().mapToDouble(Double::doubleValue).toArray());
		neuralNetwork.calculate();
		
        // get network output
        double[] networkOutput = neuralNetwork.getOutput();
        double forecast = darylNormalizer.denormData(networkOutput[0]);
        logger.info("Robot -> " + bot.getRobot() + " PREDICCIÓN -> " + forecast + " ANTERIOR -> " + prediccionAnterior);
        
        if(forecast > prediccionAnterior) {
        	prediccion = 1.0;
        }else if(forecast < prediccionAnterior) {
        	prediccion = -1.0;
        }
        

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
