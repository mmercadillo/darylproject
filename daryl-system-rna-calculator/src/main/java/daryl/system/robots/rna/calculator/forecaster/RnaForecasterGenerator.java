package daryl.system.robots.rna.calculator.forecaster;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.dataset.DataSetLoader;
import daryl.system.comun.dataset.DataSetLoader3C;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.RnaConfig;
import daryl.system.model.RnaConfigCalcs;
import daryl.system.model.historicos.Historico;
import daryl.system.robots.rna.calculator.repository.IHistoricoRepository;
import daryl.system.robots.rna.calculator.repository.IRnaConfigCalcsRepository;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RnaForecasterGenerator implements Runnable, LearningEventListener{
	
	//#transferFunctionTypes = LINEAR, RAMP, STEP, SIGMOID, TANH, GAUSSIAN, TRAPEZOID, SGN, SIN, LOG, RECTIFIED
	//#inputFunctionTypes = And, Difference, EuclideanRBF, Max, Min, Or, Product, Sum, SumSqr, WeightedSum
	
	@Autowired
	Logger logger;

	@Autowired
	ConfigData config;
	
	@Autowired
	private IRnaConfigCalcsRepository rnaConfigCalcsRepository;
	@Autowired
	private IHistoricoRepository historicoRepository;
	
	private String robot;
	private Activo tipoActivo;
	private Timeframes timeframe;
	private String estrategia;

	//Parámetros de configuracion RNA
	private Integer maxNeuronasEntrada = 10;
	private Integer maxHiddenLayers = 1;
	private Integer maxHiddenNeurons = 10;
	private Double minMomentum = 0.05;
	private Double minLearningRate = 0.05;
	
	private final int neuronasSalida = 1;
	
	private String[] transferFunctionTypes = {"LINEAR", "SIGMOID", "TANH", "GAUSSIAN", "LOG", "RECTIFIED", "RAMP", "STEP", "TRAPEZOID", "SGN", "SIN"};
	
	private Double maxError = 0.0000001;
	private Integer maxIterations = 100;

	
	/////////////////////////////////////////////////////
	
	//Parámetros para la creación de los Datasets*/
	private Double pctLearning = 0.70;
	private Double pctTest = 0.05;
	private Double pctForecast = 0.25;
	//////////////////////////////
	
	
	private DataSet trainingSet;
	private DataSet testSet;
	private DataSet forecastSet;
	
	List<Datos> datosForecast;
	DarylMaxMinNormalizer darylNormalizer;
	

	private List<Double> normalizeDataForLearning;
	private List<Double> normalizeDataForTest;
	private List<Double> normalizeDataForForecast;

	public RnaForecasterGenerator() {
	}
	
	public void init(String robot, Activo tipoActivo, Timeframes timeframe, int maxNeuronasEntrada, int maxCapasOcultas, int maxIteraciones, double errorMaximo) {
		
		logger.info("Iniciando -> " + robot);
		this.robot = robot;
		this.tipoActivo = tipoActivo;
		this.estrategia = robot;
		this.timeframe = timeframe;
		
		this.maxNeuronasEntrada = maxNeuronasEntrada;
		this.maxHiddenLayers = maxCapasOcultas;
		this.maxIterations = maxIteraciones;
		this.maxError = errorMaximo;
		logger.info(this.robot + " Iniciado");
		
		logger.info("Cargando datos de -> " + this.robot);
		loadData();
		logger.info("Datos cargados de -> " + this.robot);
	}
	
	private List<Datos> toDatosList(List<Historico> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (Historico hist : historico) {
			
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
	
	public  void loadData() {
		
		List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(this.timeframe, this.tipoActivo);
		this.datosForecast = toDatosList(historico);
		
		this.darylNormalizer = new DarylMaxMinNormalizer(this.datosForecast, Mode.CLOSE);
		List<Double> normalizedData = darylNormalizer.getNormalizedList();
		
		
		int totalDatos = normalizedData.size();
		
		int totalDatosLearn = (int)(totalDatos * this.pctLearning);
		int totalDatosTest = (int)(totalDatos * this.pctTest);
		int totalDatosForecast = (int)(totalDatos * this.pctForecast);
		
		
		int diff = totalDatos - totalDatosLearn - totalDatosTest - totalDatosForecast;
		
		//System.out.println(totalDatos + "-" + totalDatosLearn + "-" + totalDatosTest + "-" + totalDatosForecast);
		
		this.normalizeDataForLearning = normalizedData.subList(1, totalDatosLearn);
		this.normalizeDataForTest = normalizedData.subList(totalDatosLearn, totalDatosLearn + totalDatosTest);
		this.normalizeDataForForecast = normalizedData.subList(totalDatosLearn + totalDatosTest, totalDatosLearn + totalDatosTest + totalDatosForecast + diff);

		//System.out.println(normalizeDataForLearning.size() + "-" + normalizeDataForTest.size() + "-" + normalizeDataForForecast.size());
		
		
	}

	
	public void generateDatasets(int neuronasEntrada) {
		
		logger.info("Creando Datasets de -> " + this.robot);
		
		DataSetLoader dataSetLoader = DataSetLoader3C.getInstance();
		trainingSet = new DataSet(neuronasEntrada, neuronasSalida);
		dataSetLoader.loadRawData(trainingSet, normalizeDataForLearning, neuronasEntrada);
		testSet = new DataSet(neuronasEntrada, neuronasSalida);
		dataSetLoader.loadRawData(testSet, normalizeDataForTest, neuronasEntrada);
		forecastSet = new DataSet(neuronasEntrada, neuronasSalida);
		dataSetLoader.loadRawData(forecastSet, normalizeDataForForecast, neuronasEntrada);
		
		logger.info("Datasets creados de -> " + this.robot);
	}

	public  void run() {
		
		int lastNeuronasEntrada = 1;
		int lastCapasOcultas = 1;
		int lastPasoLearnigRate = 0;
		int lastPasoMomentum = 0;
		int lastHiddenNeurons = 1;
		int lastTransferFunctionType = 0;
		int lastBias = 0;
		Double resultado = null;
		
		//recuperamos la configuración existente
		RnaConfigCalcs rnaConfig = rnaConfigCalcsRepository.findRnaConfigCalcsByRobot(robot);
		
		if(rnaConfig != null) {
			
			if(rnaConfig.getLastNeuronasEntrada() != null && rnaConfig.getLastNeuronasEntrada() >= 1) lastNeuronasEntrada = rnaConfig.getLastNeuronasEntrada();
			if(rnaConfig.getLastCapasOcultas() != null && rnaConfig.getLastCapasOcultas() >= 1) lastCapasOcultas = rnaConfig.getLastCapasOcultas();
			if(rnaConfig.getLastPasoLearnigRate() != null) lastPasoLearnigRate = rnaConfig.getLastPasoLearnigRate();
			if(rnaConfig.getLastPasoMomentum() != null) lastPasoMomentum = rnaConfig.getLastPasoMomentum();
			if(rnaConfig.getLastHiddenNeurons() != null && rnaConfig.getLastHiddenNeurons() >= 1) lastHiddenNeurons = rnaConfig.getLastHiddenNeurons();
			if(rnaConfig.getLastTransferFunctionType() != null) lastTransferFunctionType = rnaConfig.getLastTransferFunctionType();
			if(rnaConfig.getLastBias() != null) lastTransferFunctionType = rnaConfig.getLastBias();
			if(rnaConfig.getResultado() != null) resultado = rnaConfig.getResultado();
			
		}else {
			rnaConfig = new RnaConfigCalcs();
		}
		
		
		for(int fncTransf = lastTransferFunctionType; fncTransf < transferFunctionTypes.length; fncTransf++) {
			for(int neuronasEntrada = lastNeuronasEntrada; neuronasEntrada <= maxNeuronasEntrada; neuronasEntrada++) {
				//Creamos los dataset
				generateDatasets(neuronasEntrada);
				
				maxHiddenNeurons = (2 * neuronasEntrada + 1);
				
				for(int bias = lastBias; bias <= 1; bias++) {
					for(int pasoMomentum = lastPasoMomentum; pasoMomentum < 20; pasoMomentum++) {
						double momentum = minMomentum + (pasoMomentum * 0.05);
						
						for(int pasoLearningrate = lastPasoLearnigRate; pasoLearningrate < 20; pasoLearningrate++) {
							double learningRate = minLearningRate + (pasoLearningrate * 0.05);
							
							for(int hiddenLayers = lastCapasOcultas; hiddenLayers <= maxHiddenLayers; hiddenLayers++) {
								for(int hiddenNeurons = lastHiddenNeurons; hiddenNeurons <= maxHiddenNeurons; hiddenNeurons++) {
									
									try {
										
										List<Integer> neuronasPorLayer = new ArrayList<Integer>();
										List<Layer> layers = new ArrayList<Layer>();
										
								        //Configuramos la capa de entrada
								        Layer inputLayer = new Layer();
								        for(int i = 0; i < neuronasEntrada; i++) {
								        	inputLayer.addNeuron(new Neuron());
								        }
								        layers.add(inputLayer);
								        neuronasPorLayer.add(neuronasEntrada);
										
								        //Configuramos las capas ocultas, al menos 1
								        for(int k = 0; k < hiddenLayers; k++) {
									        //Hidden1
									        int neuronsCountHiddenLayer1 = hiddenNeurons;
									        Layer hiddenLayer1 = null;
									        try {
									        	Boolean useBias = (bias == 0)?Boolean.FALSE : Boolean.TRUE;
									        	hiddenLayer1 = new Layer(neuronsCountHiddenLayer1, new NeuronProperties(TransferFunctionType.valueOf(transferFunctionTypes[fncTransf]), useBias));
									        }catch (Exception e) {
									        	//hiddenLayer1 = new Layer(neuronsCountHiddenLayer1, new NeuronProperties(TransferFunctionType.valueOf(this.type),this.useBias));
											}																			
									        for(int i = 0; i < neuronsCountHiddenLayer1; i++) {
									            //And, Difference, EuclideanRBF, Max, Min, Or, Product, Sum, SumSqr, WeightedSum
									        	Neuron neuron = new Neuron();
									        	//neuron.setInputFunction(new EuclideanRBF());
									        	hiddenLayer1.addNeuron(neuron);
									        }
									        layers.add(hiddenLayer1);
									        neuronasPorLayer.add(hiddenNeurons);	 
								        }
								        
								        
								      //Configuramos la capa de salida
								        Layer outputLayer = new Layer();
								        for(int i = 0; i < neuronasSalida; i++) {
								        	outputLayer.addNeuron(new Neuron());
								        }
								        layers.add(outputLayer);
								        neuronasPorLayer.add(neuronasSalida);
								        
								        //Creamos la RNA
								        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(	TransferFunctionType.valueOf(transferFunctionTypes[fncTransf]), 
																					neuronasPorLayer.stream().mapToInt(Integer::intValue).toArray());
								        
										
										int i = 0;
										neuralNetwork.addLayer(i++, inputLayer); 
										//Añadimos las capas ocultas
										for(int j = 1; j < layers.size()-1; j++) {
											neuralNetwork.addLayer(i++, layers.get(j));
										}
								        neuralNetwork.addLayer(i++, outputLayer);
							
								        MomentumBackpropagation learningRule = ((MomentumBackpropagation) neuralNetwork.getLearningRule());
								    	
										//learningRule.setNeuralNetwork(neuralNetwork);
										learningRule.setTrainingSet(trainingSet);
										learningRule.setLearningRate(learningRate);
										learningRule.setMaxError(this.maxError);
										//learningRule.setMinErrorChange(0.000005);
										learningRule.setMaxIterations(this.maxIterations);
										learningRule.setMomentum(momentum);
										learningRule.setBatchMode(Boolean.FALSE);
										learningRule.setErrorFunction(new MeanSquaredError());
										//learningRule.setMinErrorChange(0.000000001);
										learningRule.addListener(this);
								
										//neuralNetwork.setLearningRule(learningRule);
										// neuralNetwork.learn(trainingSet);
										
										learningRule.learn(trainingSet);	
										double res = testNeuralNetwork(neuralNetwork);
										
										logger.info("Robot -> " + this.robot);
										logger.info("Función de transferencia -> " + transferFunctionTypes[fncTransf]);
										logger.info("N. entrada -> " + neuronasEntrada);
										logger.info("Capas ocultas -> " + hiddenLayers);
										logger.info("N. ocultas -> " + hiddenNeurons);
										logger.info("N. salida -> " + neuronasSalida);
										logger.info("Bias -> " + ((bias==0)?Boolean.FALSE:Boolean.TRUE));
										logger.info("Momentum -> " + momentum);
										logger.info("Learning Rate -> " + learningRate);
										logger.info("RES -> " + res);

										
										//Actualizamos los datos de RnaConfig
										rnaConfig.setLastNeuronasEntrada(neuronasEntrada);
										rnaConfig.setLastCapasOcultas(hiddenLayers);
										rnaConfig.setLastPasoLearnigRate(pasoLearningrate);
										rnaConfig.setLastPasoMomentum(pasoMomentum);
										rnaConfig.setLastHiddenNeurons(hiddenNeurons);
										rnaConfig.setLastTransferFunctionType(fncTransf);
										rnaConfig.setLastBias(bias);
										
										Long fechaHoraMillis = System.currentTimeMillis();
										if(resultado == null || res > resultado) {
											resultado = res;
											logger.info("Guardamos la RNA con res -> " + res);
											//neuralNetwork.save(this.rutaRna + this.tipoActivo + "_" + this.timeframe.valor + "_new.rna");
											
											//Actualizamos la configuración
											rnaConfig.setEstrategia(robot);
											rnaConfig.setRobot(robot);
											rnaConfig.setTipoActivo(tipoActivo);
											rnaConfig.setFicheroRna("RNA_" + this.tipoActivo + "_" + this.timeframe.valor + "_" + fechaHoraMillis + ".rna");
											rnaConfig.setNeuronasEntrada(neuronasEntrada);
											rnaConfig.setResultado(resultado);
											rnaConfig.setRna(rnaToByteArray(neuralNetwork));

											
											rnaConfig.setFecha(config.getFechaInString(fechaHoraMillis));
											rnaConfig.setFModificacion(fechaHoraMillis);
											rnaConfig.setHora(config.getHoraInString(fechaHoraMillis));
																					
										}
										rnaConfigCalcsRepository.save(rnaConfig);
										logger.info("Nueva configuración RNA guardada");
										logger.info("========================================================================================");
										
									}catch (Exception e) {
										e.printStackTrace();
									}
								}
								lastHiddenNeurons = 1;
							}
							lastCapasOcultas = 1;
						}
						lastPasoLearnigRate = 0;
					}
					lastPasoMomentum = 0;
				}
				lastBias = 0;
			}
			lastNeuronasEntrada = 1;
		}

	}
	
	
	public byte[] rnaToByteArray(NeuralNetwork rna){
		
		byte[] bytesFromRna = SerializationUtils.serialize(rna);
		return bytesFromRna;

		
	}
	
	public NeuralNetwork rnaFromByteArray(byte[] byteArray) throws IOException, ClassNotFoundException {
		
		NeuralNetwork rna = (NeuralNetwork)SerializationUtils.deserialize(byteArray);
		return rna;

	}
	
	
	public double testNeuralNetwork(NeuralNetwork neuralNet) {

	   	   
	   List<Double> esperados = new ArrayList<Double>();
	   List<Double> predicciones = new ArrayList<Double>();
	   List<Double> periods = new ArrayList<Double>();

	   System.out.println("**********************RESULT TEST**********************");
	   double period = 0D;
	   for (DataSetRow testSetRow : testSet.getRows()) {
		   neuralNet.setInput(testSetRow.getInput());
		   neuralNet.calculate();
		
		   // get network output
		   double[] networkOutput = neuralNet.getOutput();
		   double predicted = networkOutput[0];
		    
		   // get target/desired output
		   double[] desiredOutput = testSetRow.getDesiredOutput();
		   double target = desiredOutput[0];
		      
		   esperados.add(darylNormalizer.denormData(target));
		   predicciones.add(darylNormalizer.denormData(predicted));
		  
		   periods.add(period++);
        }
        
        double res = estrategia(esperados, predicciones);
        return res;

    }

	private Double minError = null;
	private Integer iteration = 0;
	@Override
	public void handleLearningEvent(LearningEvent event) {
			
			
		BackPropagation bp = (BackPropagation) event.getSource();
        //SupervisedHebbianLearning bp = (SupervisedHebbianLearning)event.getSource();
		if (event.getEventType().equals(LearningEvent.Type.LEARNING_STOPPED)) {
            double error = bp.getTotalNetworkError();
            //System.out.println("Training completed in " + bp.getCurrentIteration() + " iterations, ");
            //System.out.println("With total error: " + formatDecimalNumber(error));
            //System.out.println("With MIN error: " + formatDecimalNumber(minError) + " in iteration: " + iteration);
            
        } else {
        	

            //System.out.println("Iteration: " + bp.getCurrentIteration() + " | Network error: " + formatDecimalNumber(bp.getTotalNetworkError()));
            
            if(minError == null || minError > bp.getTotalNetworkError()) {
            	minError = bp.getTotalNetworkError();
            	iteration = bp.getCurrentIteration();
            }
            
    	
            
        }
			

			
	}
		
    //Formating decimal number to have 3 decimal places
    public String formatDecimalNumber(double number) {
    	return new BigDecimal(number).setScale(20, RoundingMode.HALF_UP).toString();
    }
		
    public double estrategia(List<Double> esperados, List<Double> predicciones) {
	    	
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 		int aciertos = 0;
 		int fallos = 0;
 		double ptsGanados = 0.0;
 		double ptsPerdidos = 0.0;
 		int perdidasConsecutivas = 0;
 		int gananciasConsecutivas = 0;
 		int maxPerdidasConsecutivas = 0;
 		int maxGanaciasConsecutivas = 0;
 		double maxPtsConsecutivosGanados = 0.0;
 		double maxPtsConsecutivosPerdidos = 0.0;
 		double ptsConsecutivosGanados = 0.0;
 		double ptsConsecutivosPerdidos = 0.0;
 		//Evaluamos los resultados

 		for(int i = 2; i < esperados.size(); i++) {
 						
 			if(predicciones.get(i) < predicciones.get(i-1)) {
 				//B
 				if(esperados.get(i) > esperados.get(i-1)) {
 					aciertos++;
 					double res =  (esperados.get(i) - esperados.get(i-1));
 					ptsGanados = ptsGanados + res;
 					
 					
 					gananciasConsecutivas++;
 					if(perdidasConsecutivas > 0) {
 						if(perdidasConsecutivas > maxPerdidasConsecutivas) {
 							maxPerdidasConsecutivas = perdidasConsecutivas;
 						}
 						perdidasConsecutivas = 0;
 					}
 					
 					ptsConsecutivosGanados += res;
 					if(ptsConsecutivosPerdidos < 0) {
 						if(ptsConsecutivosPerdidos < maxPtsConsecutivosPerdidos) {
 							maxPtsConsecutivosPerdidos = ptsConsecutivosPerdidos;
 						}
 						ptsConsecutivosPerdidos = 0.0;
 					}
 					
 					
 					
 				}else {
 					fallos++;
 					double res =  (esperados.get(i) - esperados.get(i-1));
 					ptsPerdidos = ptsPerdidos + res;
 					
 					perdidasConsecutivas++;
 					if(gananciasConsecutivas > 0) {
 						if(gananciasConsecutivas > maxGanaciasConsecutivas) {
 							maxGanaciasConsecutivas = gananciasConsecutivas;
 						}
 						gananciasConsecutivas = 0;
 					}
 					
 					ptsConsecutivosPerdidos += res;
 					if(ptsConsecutivosGanados > 0) {
 						if(ptsConsecutivosGanados > maxPtsConsecutivosGanados) {
 							maxPtsConsecutivosGanados = ptsConsecutivosGanados;
 						}
 						ptsConsecutivosGanados = 0.0;
 					}
 				}
 			}

 			if(predicciones.get(i) > predicciones.get(i-1)) {
 				//S
 				if(esperados.get(i) < esperados.get(i-1)) {
 					aciertos++;
 					double res = (esperados.get(i-1) - esperados.get(i));
 					ptsGanados = ptsGanados + res;
 					
 					gananciasConsecutivas++;
 					if(perdidasConsecutivas > 0) {
 						if(perdidasConsecutivas > maxPerdidasConsecutivas) {
 							maxPerdidasConsecutivas = perdidasConsecutivas;
 						}
 						perdidasConsecutivas = 0;
 					}
 					
 					ptsConsecutivosGanados += res;
 					if(ptsConsecutivosPerdidos < 0) {
 						if(ptsConsecutivosPerdidos < maxPtsConsecutivosPerdidos) {
 							maxPtsConsecutivosPerdidos = ptsConsecutivosPerdidos;
 						}
 						ptsConsecutivosPerdidos = 0.0;
 					}
 					
 				}else {
 					fallos++;
 					double res = (esperados.get(i-1) - esperados.get(i));
 					ptsPerdidos = ptsPerdidos + res;

 					
 					perdidasConsecutivas++;
 					if(gananciasConsecutivas > 0) {
 						if(gananciasConsecutivas > maxGanaciasConsecutivas) {
 							maxGanaciasConsecutivas = gananciasConsecutivas;
 						}
 						gananciasConsecutivas = 0;
 					}
 					
 					ptsConsecutivosPerdidos += res;
 					if(ptsConsecutivosGanados > 0) {
 						if(ptsConsecutivosGanados > maxPtsConsecutivosGanados) {
 							maxPtsConsecutivosGanados = ptsConsecutivosGanados;
 						}
 						ptsConsecutivosGanados = 0.0;
 					}
 				}
 			}
 			
 		}
 		
 		//System.out.println("======================================");
 		//System.out.println("RES -> " + (ptsGanados + ptsPerdidos));
 		//System.out.println("A: " + aciertos + " - F: " + fallos + " - PTS_P: " + ptsPerdidos + " - PTS_G: " + ptsGanados + " - MG: " + (ptsGanados/aciertos) + " - MP: " + (ptsPerdidos/fallos));
 		
 		double pctAciertos = (double)aciertos / (aciertos + fallos);
 		double pctFallos = (double)fallos / (aciertos + fallos);
 		double mediaGPorOpGanadora = (double)ptsGanados / aciertos;
 		double mediaPPorOpPerdedora = (double)ptsPerdidos / fallos;
 		
 		//System.out.println("EM -> " + ((pctAciertos * mediaGPorOpGanadora ) + (pctFallos * mediaPPorOpPerdedora))  );
 		//System.out.println("PC -> " + maxPerdidasConsecutivas + " GC -> " + maxGanaciasConsecutivas);
 		//System.out.println("MPP -> " + maxPtsConsecutivosPerdidos + " MPG -> " + maxPtsConsecutivosGanados);
 		
 		return ptsGanados + ptsPerdidos;
 		
    }
	    
		
    
}
