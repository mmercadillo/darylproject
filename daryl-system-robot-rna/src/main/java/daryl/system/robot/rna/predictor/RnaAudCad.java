package daryl.system.robot.rna.predictor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.neuroph.core.NeuralNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.model.Robot;
import daryl.system.model.historicos.HistAudCad;
import daryl.system.robot.rna.predictor.base.RnaPredictor;
import daryl.system.robot.rna.repository.IHistAudCadRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class RnaAudCad  extends RnaPredictor{


	@Autowired
	private IHistAudCadRepository histAudCadRepository;

	
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
		
		File rna = new ClassPathResource(ConfigData.BASE_PATH_RNAS + bot.getFicheroRna()).getFile();
		NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(rna);
		
		List<HistAudCad> historico = histAudCadRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());
		
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
	

}
