package daryl.system.robots.markovmodels.calculator;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.utils.BarSeriesUtils;

import DA.Processing.DataDecoding;
import ML.HMM.HiddenMarkovModel;
import ML.HMM.Pair;
import Util.Parser.JsonParser;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.Historico;
import daryl.system.robots.markovmodels.calculator.repository.IHistoricoRepository;

@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan("daryl.system.model")
public class DarylSystemMarkovModelsCalculatorApplication {


	@Autowired
	Logger logger;
	
	@Bean
    public Logger darylLogger() {
        return LoggerFactory.getLogger("daryl");
    }
	
	public static void main(String[] args) throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(DarylSystemMarkovModelsCalculatorApplication.class);
		
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	    
	    startForecaster(context);
	}
	
	private static void startForecaster(ConfigurableApplicationContext context) throws Exception {
		
		
		ExecutorService servicio = Executors.newFixedThreadPool(30);
		//4-20-120-480
		
		IHistoricoRepository historicoRepository = context.getBean(IHistoricoRepository.class);
		
		List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H4, Activo.XAUUSD);
		BarSeries serieParaCalculo = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + Timeframes.PERIOD_H4 + "_" + Activo.XAUUSD, 1);
		System.out.println("Cargado historico de XAUUSD 60");
		
		//calculo de probabilidades
		Long fromHHtoHL = 0L;
		Long fromHHtoHH = 0L;
		Long fromHHtoLH = 0L;
		Long fromHHtoLL = 0L;
		
		Long fromHLtoHH = 0L;
		Long fromHLtoHL = 0L;
		Long fromHLtoLH = 0L;
		Long fromHLtoLL = 0L;

		Long fromLHtoHH = 0L;
		Long fromLHtoHL = 0L;
		Long fromLHtoLH = 0L;
		Long fromLHtoLL = 0L;
		
		Long fromLLtoHH = 0L;
		Long fromLLtoHL = 0L;
		Long fromLLtoLH = 0L;
		Long fromLLtoLL = 0L;
		
		Long hhPU = 0L;
		Long hhPD = 0L;
		Long hlPU = 0L;
		Long hlPD = 0L;
		Long lhPU = 0L;
		Long lhPD = 0L;
		Long llPU = 0L;
		Long llPD = 0L;
		
		//Estado inicial
		Boolean hh = false;
		Boolean hl = false;
		Boolean lh = false;
		Boolean ll = false;
		
        Vector<String> sampleStates = new Vector<String>();
        Vector<String> sampleO = new Vector<String>();
		BarSeries aux = serieParaCalculo.getSubSeries(0, serieParaCalculo.getBarCount()-300);
		
		Bar barCero = aux.getBar(0);
		Bar barUno = aux.getBar(1);
		
		System.out.println(aux.getBar(aux.getBarCount()-1));
		
		if(barUno.getClosePrice().doubleValue() > barCero.getClosePrice().doubleValue() && barUno.getVolume().doubleValue() > barCero.getVolume().doubleValue()) {
			hh = true;
			hhPU++;
			sampleStates.add("HH");
			sampleO.add("HH");
		}
		if(barUno.getClosePrice().doubleValue() > barCero.getClosePrice().doubleValue() && barUno.getVolume().doubleValue() < barCero.getVolume().doubleValue()) {
			hl = true;
			hlPD++;
			sampleStates.add("HL");
			sampleO.add("HL");
		}
		if(barUno.getClosePrice().doubleValue() < barCero.getClosePrice().doubleValue() && barUno.getVolume().doubleValue() > barCero.getVolume().doubleValue()) {
			lh = true;
			lhPU++;
			sampleStates.add("LH");
			sampleO.add("LH");
		}
		if(barUno.getClosePrice().doubleValue() < barCero.getClosePrice().doubleValue() && barUno.getVolume().doubleValue() < barCero.getVolume().doubleValue()) {
			ll = true;
			llPD++;
			sampleStates.add("LL");
			sampleO.add("LL");
		}
		
		//int maxIndex = 200;
		for(int i = 1; i < aux.getBarCount()-1; i++) {
			
			Bar barAnterior = aux.getBar(i);
			Bar bar = aux.getBar(i+1);

			if(bar.getClosePrice().doubleValue() > barAnterior.getClosePrice().doubleValue() && bar.getVolume().doubleValue() > barAnterior.getVolume().doubleValue()) {
				
				if(hh) fromHHtoHH++;
				if(hl) fromHLtoHH++;
				if(lh) fromLHtoHH++;
				if(ll) fromLLtoHH++;
				
				hhPU++;
				hh = true;
				//if(i >= aux.getBarCount()-maxIndex) {
					sampleStates.add("HH");
					sampleO.add("HH");
				//}
			}
			if(bar.getClosePrice().doubleValue() > barAnterior.getClosePrice().doubleValue() && bar.getVolume().doubleValue() < barAnterior.getVolume().doubleValue()) {
				
				if(hh) fromHHtoHL++;
				if(hl) fromHLtoHL++;
				if(lh) fromLHtoHL++;
				if(ll) fromLLtoHL++;
				
				hlPD++;
				hl = true;
				//if(i >= aux.getBarCount()-maxIndex) {
					sampleStates.add("HL");
					sampleO.add("HL");
				//}
			}
			if(bar.getClosePrice().doubleValue() < barAnterior.getClosePrice().doubleValue() && bar.getVolume().doubleValue() > barAnterior.getVolume().doubleValue()) {
				
				if(hh) fromHHtoLH++;
				if(hl) fromHLtoLH++;
				if(lh) fromLHtoLH++;
				if(ll) fromLLtoLH++;
				
				lhPU++;
				lh = true;
				
				//if(i >= aux.getBarCount()-maxIndex) {
					sampleStates.add("LH");
					sampleO.add("LH");
				//}
			}
			if(bar.getClosePrice().doubleValue() < barAnterior.getClosePrice().doubleValue() && bar.getVolume().doubleValue() < barAnterior.getVolume().doubleValue()) {
				
				if(hh) fromHHtoLL++;
				if(hl) fromHLtoLL++;
				if(lh) fromLHtoLL++;
				if(ll) fromLLtoLL++;
				
				llPD++;
				ll = true;
				
				//if(i >= aux.getBarCount()-maxIndex) {
					sampleStates.add("LL");
					sampleO.add("LL");
				//}
				
			}
			
		}
		
		//Probabilidades
		
		double probHHHH = fromHHtoHH / (double)(fromHHtoHH + fromHHtoHL + fromHHtoLH + fromHHtoLL);
		System.out.println("probHHHH -> " + probHHHH);
		
		double probHHHL = fromHHtoHL / (double)(fromHHtoHH + fromHHtoHL + fromHHtoLH + fromHHtoLL);
		System.out.println("probHHHL -> " + probHHHL);
		
		double probHHLH = fromHHtoLH / (double)(fromHHtoHH + fromHHtoHL + fromHHtoLH + fromHHtoLL);
		System.out.println("probHHLH -> " + probHHLH);
		
		double probHHLL = fromHHtoLL / (double)(fromHHtoHH + fromHHtoHL + fromHHtoLH + fromHHtoLL);
		System.out.println("probHHLL -> " + probHHLL);
		
		double probHLHH = fromHLtoHH / (double)(fromHLtoHH + fromHLtoHL + fromHLtoLH + fromHLtoLL);
		System.out.println("probHLHH -> " + probHLHH);
		
		double probHLHL = fromHLtoHL / (double)(fromHLtoHH + fromHLtoHL + fromHLtoLH + fromHLtoLL);
		System.out.println("probHLHL -> " + probHLHL);
		
		double probHLLH = fromHLtoLH / (double)(fromHLtoHH + fromHLtoHL + fromHLtoLH + fromHLtoLL);
		System.out.println("probHLLH -> " + probHLLH);
		
		double probHLLL = fromHLtoLL / (double)(fromHLtoHH + fromHLtoHL + fromHLtoLH + fromHLtoLL);
		System.out.println("probHLLL -> " + probHLLL);
		
		double probLHHH = fromLHtoHH / (double)(fromLHtoHH + fromLHtoHL + fromLHtoLH + fromLHtoLL);
		System.out.println("probLHHH -> " + probLHHH);
		
		double probLHHL = fromLHtoHL / (double)(fromLHtoHH + fromLHtoHL + fromLHtoLH + fromLHtoLL);
		System.out.println("probLHHL -> " + probLHHL);
		
		double probLHLH = fromLHtoLH / (double)(fromLHtoHH + fromLHtoHL + fromLHtoLH + fromLHtoLL);
		System.out.println("probLHLH -> " + probLHLH);
		
		double probLHLL = fromLHtoLL / (double)(fromLHtoHH + fromLHtoHL + fromLHtoLH + fromLHtoLL);
		System.out.println("probLHLL -> " + probLHLL);
		
		double probLLHH = fromLLtoHH / (double)(fromLLtoHH + fromLLtoHL + fromLLtoLH + fromLLtoLL);
		System.out.println("probLLHH -> " + probLLHH);
		
		double probLLHL = fromLLtoHL / (double)(fromLLtoHH + fromLLtoHL + fromLLtoLH + fromLLtoLL);
		System.out.println("probLLHL -> " + probLLHL);
		
		double probLLLH = fromLLtoLH / (double)(fromLLtoHH + fromLLtoHL + fromLLtoLH + fromLLtoLL);
		System.out.println("probLLLH -> " + probLLLH);
		
		double probLLLL = fromLLtoLL / (double)(fromLLtoHH + fromLLtoHL + fromLLtoLH + fromLLtoLL);
		System.out.println("probLLLL -> " + probLLLL);

		
		servicio.shutdown();
		
		
        JsonParser jp = new JsonParser("F:\\Hidden-Markov-Model-master\\Resources\\test_HMM.json");
        
        String name = DataDecoding.getInstance().getModelName(jp.getName());
        Vector<String> states = DataDecoding.getInstance().getStates(jp.getStates());
        Vector<String> observations = DataDecoding.getInstance().getObservations(jp.getObservations());
        Hashtable<String, Double> initialProbabilities = DataDecoding.getInstance().getInitialProbabilities(jp.getInitialProbabilities());
        Hashtable<Pair<String, String>, Double> transitionMatrix = DataDecoding.getInstance().getTransitionMatrix(jp.getTransitionMatrix());
        Hashtable<Pair<String, String>, Double> emissionMatrix = DataDecoding.getInstance().getEmissionMatrix(jp.getEmissionMatrix());
        
        
        HiddenMarkovModel hmm = new HiddenMarkovModel(name, states, observations, initialProbabilities, transitionMatrix, emissionMatrix);
        
        Bar ultimaBar = aux.getLastBar();
        BarSeries aux2 = serieParaCalculo.getSubSeries(serieParaCalculo.getBarCount()-300, serieParaCalculo.getBarCount());
        
        //Elegimos los 50 últimos
        int size = 5;
        int gana = 0;
        int pierde = 0;
        
        for(int i = 0; i < aux2.getBarCount(); i++) {

            Vector<String> sampleStatesAux = new Vector<String>(sampleStates.subList(sampleStates.size()-size, sampleStates.size()));
            Vector<String> sampleObsAux = new Vector<String>(sampleO.subList(sampleO.size()-size, sampleO.size()));
        	
        	System.out.println("------------------------ i -> " + i );
        	
        	
        	sampleStatesAux.add("LH");
        	sampleObsAux.add("LL");
        	
        	System.out.println(hmm.evaluateUsingForwardAlgorithm(sampleStatesAux, sampleObsAux));
            
        	
        	
        	/*
            String estadoAnterior = sampleStatesAux.get(sampleStatesAux.size()-1);
            Hashtable<Pair<String, String>, Double> transMatrix = hmm.getTransitionMatrix();
            double probMax = 0.0;
            Pair<String, String> pairSel = null;
            for (Pair<String, String> pair : transMatrix.keySet()) {
            	if(pair.getKey().equals(estadoAnterior)) {
            		double prob = transMatrix.get(pair);
     	        	if(prob > probMax) {
     	        		pairSel = pair;
     	        		probMax = prob;
     	        	}
             	}
     		}
            
            
            System.out.println(hmm.getTransitionMatrix());
            
                        
            Bar bar = aux2.getBar(i);
            String estadoEsperado = "";
            if(bar.getClosePrice().doubleValue() > ultimaBar.getClosePrice().doubleValue() && bar.getVolume().doubleValue() > ultimaBar.getVolume().doubleValue()) {				
            	sampleO.add("HH");
            	sampleStates.add("HH");
            	
            	estadoEsperado = "HH";
            	if(pairSel.getValue().endsWith("H")) gana++;
            	else pierde++;
            	
			}
			if(bar.getClosePrice().doubleValue() > ultimaBar.getClosePrice().doubleValue() && bar.getVolume().doubleValue() < ultimaBar.getVolume().doubleValue()) {
				sampleO.add("HL");
				sampleStates.add("HL");
				
				estadoEsperado = "HL";
				if(pairSel.getValue().endsWith("L")) pierde++;
				else pierde++;
			}
			if(bar.getClosePrice().doubleValue() < ultimaBar.getClosePrice().doubleValue() && bar.getVolume().doubleValue() > ultimaBar.getVolume().doubleValue()) {
				sampleO.add("LH");
				sampleStates.add("LH");
				
				estadoEsperado = "LH";
				if(pairSel.getValue().endsWith("H")) gana++;
				else pierde++;
			}
			if(bar.getClosePrice().doubleValue() < ultimaBar.getClosePrice().doubleValue() && bar.getVolume().doubleValue() < ultimaBar.getVolume().doubleValue()) {
				sampleO.add("LL");
				sampleStates.add("LL");
				
				estadoEsperado = "LL";
				if(pairSel.getValue().endsWith("L")) pierde++;
				else pierde++;
			}
            
			ultimaBar = bar;
			
			System.out.println(estadoAnterior + " - " + pairSel.getKey() + " - " + pairSel.getValue() + " - " + probMax + " - " + estadoEsperado );
			
			if(i > 20) break; 
        	*/
        	break;

        }

        System.out.println("G -> " + gana + " P -> " + pierde);
        
		
	}

	

}
