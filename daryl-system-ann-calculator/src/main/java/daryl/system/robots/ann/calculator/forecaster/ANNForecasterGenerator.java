package daryl.system.robots.ann.calculator.forecaster;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.aspectj.weaver.patterns.IfPointcut.IfTruePointcut;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.nnet.learning.BackPropagation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.MaxMinNormalizer;
import org.ta4j.core.utils.BarSeriesUtils;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Mode;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.AnnConfigCalcs;
import daryl.system.model.historicos.Historico;
import daryl.system.robots.ann.calculator.repository.IAnnConfigCalcsRepository;
import daryl.system.robots.ann.calculator.repository.IHistoricoRepository;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ANNForecasterGenerator implements Runnable/*, LearningEventListener*/{
	
	//#transferFunctionTypes = LINEAR, RAMP, STEP, SIGMOID, TANH, GAUSSIAN, TRAPEZOID, SGN, SIN, LOG, RECTIFIED
	//#inputFunctionTypes = And, Difference, EuclideanRBF, Max, Min, Or, Product, Sum, SumSqr, WeightedSum
	
	@Autowired
	Logger logger;

	@Autowired
	ConfigData config;
	
	@Autowired
	private IAnnConfigCalcsRepository annConfigCalcsRepository;
	@Autowired
	private IHistoricoRepository historicoRepository;
	
	private String robot;
	private Activo tipoActivo;
	private Timeframes timeframe;
	private String estrategia;

	
	BarSeries datosForecast;
	MaxMinNormalizer darylNormalizer;
	
	//Parámetros de configuracion RNA
	private Integer maxNeuronasEntrada = 10;
	private Integer maxHiddenNeurons = 30;
	private Double minMomentum = 0.05;
	private Double minLearningRate = 0.05;
	
	
	//Parámetros para la creación de los Datasets*/
	private Double pctLearning = 0.70;
	private Double pctTest = 0.20;
	private Double pctForecast = 0.10;
	//////////////////////////////
	private String[] transferFunctionTypes = {"SIGMOID", "TANH"};


	private List<Double> dataForLearning;
	private List<Double> dataForTest;
	private List<Double> dataForForecast;
	
	int totalDatosLearn;
	int totalDatosTest;
	int totalDatosForecast;

	public ANNForecasterGenerator() {
	}
	
	public void init(String robot, Activo tipoActivo, Timeframes timeframe, int maxNeuronasEntrada, int maxCapasOcultas, int maxIteraciones, double errorMaximo) {
		
		logger.info("Iniciando -> " + robot);
		this.robot = robot;
		this.tipoActivo = tipoActivo;
		this.estrategia = robot;
		this.timeframe = timeframe;

		logger.info(this.robot + " Iniciado");
		
		logger.info("Cargando datos de -> " + this.robot);
		loadData();
		logger.info("Datos cargados de -> " + this.robot);
	}

	
    int total_array;
    int target_array;
    int test_array_size;
    int array_start = 25;
    int neuronasEntrada = 5;
    
    List<Double> data;
    
	public  void loadData() {
		
		List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(this.timeframe, this.tipoActivo, PageRequest.of(0,  1000));
		this.datosForecast = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + this.timeframe + "_" + this.tipoActivo, 1);
		
		this.darylNormalizer = new MaxMinNormalizer(this.datosForecast, Mode.CLOSE);
		data = darylNormalizer.getDatos();
		
		int totalDatos = data.size();
		
		totalDatosLearn = (int)(totalDatos * this.pctLearning);
		totalDatosTest = (int)(totalDatos * this.pctTest);
		totalDatosForecast = (int)(totalDatos * this.pctForecast);
		int diff = totalDatos - totalDatosLearn - totalDatosTest - totalDatosForecast;
		
		this.dataForLearning = data.subList(1, totalDatosLearn);
		this.dataForTest = data.subList(totalDatosLearn, totalDatosLearn + totalDatosTest);
		this.dataForForecast = data.subList(totalDatosLearn + totalDatosTest, totalDatosLearn + totalDatosTest + totalDatosForecast + diff);
		
		
	}


	public  void run() {
		
		int lastPasoLearnigRate = 0;
		int lastPasoMomentum = 0;
		int lastHiddenNeurons = 1;
		int lastTransferFunctionType = 0;
		int lastNeuronasEntrada = 2;

		Double accuracy = null;
		Double resultado = null;
		
		//recuperamos la configuración existente
		AnnConfigCalcs annConfig = annConfigCalcsRepository.findAnnConfigCalcsByRobot(robot);
		
		if(annConfig != null) {
			
			if(annConfig.getLastNeuronasEntrada() != null) lastNeuronasEntrada = annConfig.getLastNeuronasEntrada();
			if(annConfig.getLastPasoLearnigRate() != null) lastPasoLearnigRate = annConfig.getLastPasoLearnigRate();
			if(annConfig.getLastPasoMomentum() != null) lastPasoMomentum = annConfig.getLastPasoMomentum();
			if(annConfig.getLastHiddenNeurons() != null && annConfig.getLastHiddenNeurons() >= 1) lastHiddenNeurons = annConfig.getLastHiddenNeurons();
			if(annConfig.getLastTransferFunctionType() != null) lastTransferFunctionType = annConfig.getLastTransferFunctionType();
			if(annConfig.getAccuracy() != null) accuracy = annConfig.getAccuracy();
			
		}else {
			annConfig = new AnnConfigCalcs();
		}
		
		
		for(int fncTransf = lastTransferFunctionType; fncTransf < transferFunctionTypes.length; fncTransf++) {
			for(int pasoMomentum = lastPasoMomentum; pasoMomentum < 20; pasoMomentum++) {
				double momentum = minMomentum + (pasoMomentum * 0.05);
				
				for(int pasoLearningrate = lastPasoLearnigRate; pasoLearningrate < 20; pasoLearningrate++) {
					double learningRate = minLearningRate + (pasoLearningrate * 0.05);
					
					for(int hiddenNeurons = lastHiddenNeurons; hiddenNeurons <= maxHiddenNeurons; hiddenNeurons++) {
						for(int neuronasEntrada = lastNeuronasEntrada; neuronasEntrada <= maxNeuronasEntrada; neuronasEntrada++) {
							try {
	
								total_array = data.size();
						        target_array = totalDatosLearn;
						        test_array_size = totalDatosTest;
						        
						        DataFormat df = new DataFormat();
						        MovingAverages ma = new MovingAverages();
						        FischerTransform ft = new FischerTransform();
						
					               
						        double[] movement = new double[target_array];
						        double[][] input;
						        double[][] target;
								
						
						        double[] total_prices = data.stream().mapToDouble(dato -> dato.doubleValue()).toArray();
						        double[] training_prices = new double[target_array];
						        double[] test_prices = new double[test_array_size];
						        
						        System.arraycopy(total_prices, 0, training_prices, 0, target_array);
						        System.arraycopy(total_prices, target_array, test_prices, 0, test_array_size);
						        
						        
						        double[] fisher = ft.convert(training_prices);
						        double[] SP_avg = ma.SMA(fisher, neuronasEntrada);
						        
	
						        for (int i=1;i<SP_avg.length;i++){
						            movement[i-1] = df.checkMovement(SP_avg[i-1], SP_avg[i]);  
						        }
						        movement[SP_avg.length-1] = 1;
						        
	
					            input = df.timeSeries(SP_avg,20);
					            target = df.make2D(movement);
	
					            input = df.cropArray(input,array_start,input.length);
					            target = df.cropArray(target,array_start,target.length);
						        
					            
					            
						        ANN net = new ANN();
						        net.setHiddenNeurons(hiddenNeurons);
						        net.setErr(0.3);
						        net.setLrc(learningRate);
						        net.setMomentum(momentum);
						        net.setConvergenceLimit(Math.round(input.length * this.pctTest));
						        net.setModifyValues(false);
						        net.setModifyRate(0.005);
						        net.setDetails(false);
						        
						        if(fncTransf == 0) {
						        	net.setUseSigmoid(true);
						        	net.setUseTanh(false);
						        }else if(fncTransf == 1) {
						        	net.setUseSigmoid(false);
						        	net.setUseTanh(true);
						        }
						        
						        //net.printInputs(input, target);
						        net.train(input, target);   
						        
						        
						        //***** Testing Simulation
						        //Prepare data
						        double test_result = 0;
						        double[] test_fisher = ft.convert(test_prices);
						        double[] test_SMA = ma.SMA(test_fisher, neuronasEntrada);
						        double[][] test_values = df.timeSeries(test_SMA, 20);
						        double[] test_movement = new double[test_array_size];
						        for (int i=1;i<test_SMA.length;i++){
						            test_movement[i-1] = df.checkMovement(test_SMA[i-1], test_SMA[i]);  
						        }
						        movement[test_SMA.length-1] = 1;
						        target = df.make2D(test_movement);
						        
						        //Run Simulation
						        for (int i=neuronasEntrada; i<test_values.length; i++){
						            test_result += net.test(test_values[i], target[i]);
						        }
						        
						        //Print results
						        Double acc = ((test_array_size-test_result) / test_array_size) * 100;
						        //System.out.println("ANN Accuracy: "+acc+"%");        
				        
								logger.info("Robot -> " + this.robot);
								logger.info("Neuronas entrada -> " + neuronasEntrada);
								logger.info("Función de transferencia -> " + transferFunctionTypes[fncTransf]);
								logger.info("N. neuronas ocultas -> " + hiddenNeurons);
								logger.info("Momentum -> " + momentum);
								logger.info("Learning Rate -> " + learningRate);
								logger.info("ACC -> " + acc);
								logger.info("BEST ACC -> " + accuracy);
								logger.info("BEST RESULTADO -> " + resultado);
								
								double res = beginTradingANN(net);
								
								
						        System.out.println("===========================================================================================");
								
								
								//Actualizamos los datos de RnaConfig
								annConfig.setLastPasoLearnigRate(pasoLearningrate);
								annConfig.setLastPasoMomentum(pasoMomentum);
								annConfig.setLastHiddenNeurons(hiddenNeurons);
								annConfig.setLastTransferFunctionType(fncTransf);
								annConfig.setLastNeuronasEntrada(neuronasEntrada);
	
								boolean accUpdt = false;
								if(accuracy == null || acc > accuracy) {
									accuracy = acc;
									accUpdt = true;
								}
								boolean resUpdt = false;
								if(resultado == null || res > resultado) {
									resultado = res;	
									resUpdt = true;
								}
								
								
								Long fechaHoraMillis = System.currentTimeMillis();
								if(/*accUpdt == true &&*/ resUpdt == true) {

									logger.info("Guardamos la ANN con res -> " + acc);
									//neuralNetwork.save(this.rutaRna + this.tipoActivo + "_" + this.timeframe.valor + "_new.rna");
									
									//Actualizamos la configuración
									annConfig.setEstrategia(robot);
									annConfig.setRobot(robot);
									annConfig.setTipoActivo(tipoActivo);
									annConfig.setFicheroAnn("ANN_" + this.tipoActivo + "_" + this.timeframe.valor + "_" + fechaHoraMillis + ".rna");
									annConfig.setAccuracy(accuracy);
									annConfig.setAnn(rnaToByteArray(net));
									annConfig.setNeuronasEntrada(neuronasEntrada);
									annConfig.setResultado(resultado);
	
									annConfig.setFecha(config.getFechaInString(fechaHoraMillis));
									annConfig.setFModificacion(fechaHoraMillis);
									annConfig.setHora(config.getHoraInString(fechaHoraMillis));
									
									logger.info("Nueva configuración ANN detectada");
									
								}
								
								annConfigCalcsRepository.save(annConfig);
								//logger.info("Nueva configuración ANN guardada");
								//logger.info("========================================================================================");
								
							}catch (Exception e) {
								e.printStackTrace();
							}
						}
						lastNeuronasEntrada = 2;	
					}
					lastHiddenNeurons = 1;
				}
				lastPasoLearnigRate = 0;
			}
			lastPasoMomentum = 0;
		}
	}
	
	public byte[] rnaToByteArray(ANN ann){
		
		byte[] bytesFromRna = SerializationUtils.serialize(ann);
		return bytesFromRna;

		
	}
	
	public ANN annFromByteArray(byte[] byteArray) throws IOException, ClassNotFoundException {
		
		ANN ann = (ANN)SerializationUtils.deserialize(byteArray);
		return ann;

	}
	
    public double beginTradingANN(ANN net){
    	
    	double[] input = dataForForecast.stream().mapToDouble(dato -> dato.doubleValue()).toArray();
    	
        FischerTransform ft_ann = new FischerTransform();
        MovingAverages ma = new MovingAverages();
    	
        double[] ann_window = new double[neuronasEntrada];
        
        double resultado = 0.0;
        

        for (int i=neuronasEntrada; i<input.length;i++){
            try {
	            //Place past 20 values in ann_window
	            System.arraycopy(input, (i-neuronasEntrada), ann_window, 0, neuronasEntrada);
	            ann_window = ft_ann.convert(ann_window);
	            ann_window = ma.SMA(ann_window, 5);
	            double[] annSignalTemp = net.run(ann_window);
	            long annSignal = Math.round(annSignalTemp[0]);
	            
	            if (annSignal == 0.0) {
	            	//Vendemos
	            	resultado += input[i]-input[i+1];
	            }
	            else if (annSignal == 1.0){
	            	//Compramos
	            	resultado += input[i+1]-input[i];
	            }
            }catch (Exception e) {
				// TODO: handle exception
			}
        }
        
        System.out.println("RESULTADO -> " + resultado);
        return resultado;
    }
	
	
	/*
	public double testNeuralNetwork(ANN ann) {

		
		
		
		 double[] annSignalTemp = net.run(ann_window);
         long annSignal = Math.round(annSignalTemp[0]);
	   	   
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
	*/
	
	/*
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
	*/
		
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
