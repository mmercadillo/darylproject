package daryl.system.robot.ann.predictor.base;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang3.SerializationUtils;
import org.neuroph.core.NeuralNetwork;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.MaxMinNormalizer;
import org.ta4j.core.utils.BarSeriesUtils;

import daryl.ann.ANN;
import daryl.ann.FischerTransform;
import daryl.ann.MovingAverages;
import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Mode;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.AnnConfig;
import daryl.system.model.Orden;
import daryl.system.model.Prediccion;
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;
import daryl.system.robot.ann.repository.IAnnConfigRepository;
import daryl.system.robot.ann.repository.IHistoricoRepository;
import daryl.system.robot.ann.repository.IOrdenRepository;
import daryl.system.robot.ann.repository.IPrediccionRepository;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class AnnPredictor {

	
	@Autowired
	protected Logger logger;

	@Autowired
	protected ConfigData config;
		
	@Autowired
	protected IOrdenRepository ordenRepository;
	@Autowired
	protected IPrediccionRepository prediccionRepository;
	@Autowired
	private IAnnConfigRepository annConfigRepository;

	@Autowired
	private IHistoricoRepository historicoRepository; 

	protected Double calcularPrediccion(Robot bot) throws IOException {

		Double prediccion = 0.0;
		AnnConfig annConfig = getAnnConfig(bot.getAnnConfig());

		if(annConfig != null) {
			
			ANN ann  = null;
			try {
				ann = annFromByteArray(annConfig.getAnn());
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
			
			if(ann != null) {		
				
				List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraDesc(bot.getTimeframe(), bot.getActivo(), PageRequest.of(0,  annConfig.getNeuronasEntrada()));
				Collections.reverse(historico);
				BarSeries serieParaCalculo = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + bot.getTimeframe() + "_" + bot.getActivo(), bot.getActivo().getMultiplicador());
				MaxMinNormalizer darylNormalizer =  new MaxMinNormalizer(serieParaCalculo, Mode.CLOSE);
				List<Double> datos = darylNormalizer.getDatos();
				
				//Cogemos los últimos equivalentes al número de neuronas de entrada
				prediccion = prediccionANN(	ann, 
											datos.subList(datos.size()-annConfig.getNeuronasEntrada(), datos.size()),
											annConfig.getNeuronasEntrada());
				
				try {
					
					//Actualizamos la ann
					annConfig.setAnn(annToByteArray(ann));
					annConfigRepository.save(annConfig);
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
        return prediccion;
	
	}
	

	public ANN annFromByteArray(byte[] byteArray) throws IOException, ClassNotFoundException {
		
		ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object obj2 = ois.readObject();
		
		//Object obj = SerializationUtils.deserialize(byteArray);
		ANN ann = (ANN)obj2;
		return ann;
	}
	
    private double prediccionANN(ANN net, List<Double> datos, int neuronasEntrada){
    	
    	double prediccion = 0.0;
    	
    	try {

        	double[] input = datos.stream().mapToDouble(dato -> dato.doubleValue()).toArray();
        	
            FischerTransform ft_ann = net.getFt_ann();
            MovingAverages ma = new MovingAverages();
        	
            double[] ann_window = new double[neuronasEntrada];
                        
            //for (int i=neuronasEntrada; i<input.length;i++){
                try {
    	            //Place past 20 values in ann_window
    	            System.arraycopy(input, 0, ann_window, 0, neuronasEntrada);
    	            
    	            ann_window = ft_ann.convert(ann_window);
    	            ann_window = ma.SMA(ann_window, neuronasEntrada);
    	            double[] annSignalTemp = net.run(ann_window);
    	            
    	            logger.info("PREDICCIÓN ACTUAL PARA EL ROBOT : {}", annSignalTemp[0]);
    	            long annSignal = Math.round(annSignalTemp[0]);
    	            
    	            if (annSignal == 0.0) {
    	            	//Vendemos
    	            	prediccion = -1.0;
    	            }else if (annSignal == 1.0){
    	            	//Compramos
    	            	prediccion = 1.0;
    	            }
                }catch (Exception e) {
    			}
            //}           
    		
            net.setFt_ann(ft_ann);
    	}catch (Exception e) {
		}
    	

        return prediccion;
    }
    
	public byte[] annToByteArray(ANN ann){
		
		byte[] bytesFromRna = SerializationUtils.serialize(ann);
		return bytesFromRna;

		
	}
    /*
    private double prediccionANN(ANN net, List<Double> datos){
    	
    	double prediccion = 0.0;
    	
    	try {
	    	double[] input = datos.stream().mapToDouble(dato -> dato.doubleValue()).toArray();
	    	logger.info("INPUT -> " + Arrays.toString(input) );
	        FischerTransform ft_ann = new FischerTransform();
	        MovingAverages ma = new MovingAverages();
	        
	        input = ft_ann.convert(input);
	        input = ma.SMA(input, 5);
	        
	        double[] annSignalTemp = net.run(input);
	        logger.info("SIGNAL -> " +annSignalTemp[0]);
	        long annSignal = Math.round(annSignalTemp[0]);
	        
	        if (annSignal == 0.0) {
	        	//Vendemos
	        	prediccion = -1.0;
	        }
	        else if (annSignal == 1.0){
	        	//Compramos
	        	prediccion = 1.0;
	        }
	        
    	}catch (Exception e) {
    		e.printStackTrace();
		}
        return prediccion;
    }
	*/
	protected Double getPrediccionAnterior(int neuronasEntrada, Robot bot, NeuralNetwork neuralNetwork, List<Double> datosForecast, MaxMinNormalizer darylNormalizer) {

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
	
	
	private AnnConfig getAnnConfig(String robot) {
		
		return annConfigRepository.findAnnConfigByRobot(robot);
		
	}
	

	
	
	private void actualizarPrediccionBDs(Robot robot, TipoOrden orden, Double prediccionCierre, Long fechaHoraMillis) {
		try {
			
			//Creamos el bean prediccion
			Prediccion prediccion = new Prediccion();
				prediccion.setCierre(prediccionCierre);
				prediccion.setEstrategia(robot.getEstrategia());
				prediccion.setTipoActivo(robot.getActivo());
				prediccion.setTipoOrden(orden);
				prediccion.setFechaHora(fechaHoraMillis);
				prediccion.setFecha(config.getFechaInString(fechaHoraMillis));
				prediccion.setHora(config.getHoraInString(fechaHoraMillis));
				prediccion.setRobot(robot.getRobot());
				
			prediccionRepository.save(prediccion);
		} catch (Exception e) {
			logger.error("No se ha podido guardar la prediccion para el robot: {}", robot.getRobot(), e);
		}
	}
	
	private void actualizarUltimaOrden(Robot robot, Orden nuevaOrden, Long fechaHoraMillis) {
		
		try {
			long millis = System.currentTimeMillis();
			
			Orden ordenGuardada = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, robot.getActivo(), robot.getEstrategia());
			
			if(ordenGuardada != null && ordenGuardada.getTipoOrden() != nuevaOrden.getTipoOrden()) {
				
				logger.info("ACTUALIZAMOS LA ORDEN PARA EL ROBOT " + robot.getRobot());
				ordenGuardada.setFecha(config.getFechaInString(millis));
				ordenGuardada.setHora(config.getHoraInString(millis));
				ordenGuardada.setTipoOrden(nuevaOrden.getTipoOrden());
				ordenGuardada.setFAlta(nuevaOrden.getFAlta());
				ordenRepository.save(ordenGuardada);
				logger.info("ORDEN PARA EL ROBOT " + robot.getRobot() + " ACTUALIZADA -> " + ordenGuardada.getTipoOrden());
				
			}else if(ordenGuardada == null){
				
				logger.info("NO EXISTE ORDEN PARA EL ROBOT " + robot.getRobot() + " DAMOS DE ALTA UNA NUEVA -> " + nuevaOrden.getTipoOrden());
				
				ordenRepository.save(nuevaOrden);
				
				logger.info("ORDEN PARA EL ROBOT " + robot.getRobot() + " DADA DE ALTA -> " + nuevaOrden.getTipoOrden());
			}else {
				logger.info("NO ES NECESARIO ACTUALIZAR LA ORDEN PARA EL ROBOT " + robot.getRobot());
			}
			
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		/*
		try {

			//Orden ultimaOrden = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, robot.getActivo(), robot.getEstrategia());
			List<Orden> ultimasOrdenes = ordenRepository.findAllBytipoActivoAndEstrategia(robot.getActivo(), robot.getEstrategia());
			if(ultimasOrdenes != null && ultimasOrdenes.size() > 0) {
				//ultimaOrden.setFBaja(fechaHoraMillis);
				for (Orden ord : ultimasOrdenes) {
					ordenRepository.delete(ord);
				}
				
				
			}else {
				logger.info("No hay orden para {} para actualzar del robot", robot.getRobot());
			}
		}catch (Exception e) {
			logger.error("No se ha recuperado el valor de la última orden del robot: {}", robot.getRobot(), e);
		}
		*/
	}
	
	
	private void guardarNuevaOrden(Orden orden, Long fechaHoraMillis) {
		try {
			orden.setFAlta(fechaHoraMillis);
			ordenRepository.save(orden);
		}catch (Exception e) {
			logger.error("No se ha podido guardar la nueva orden para el robot: {}", orden.getRobot(), e);
		}
	}
	
	private final Orden calcularOperacion(Robot robot, Double prediccion, Boolean inv) {
		
		long millis = System.currentTimeMillis();
		Orden orden = new Orden();
			orden.setFAlta(millis);
			orden.setFBaja(null);
			orden.setEstrategia(robot.getEstrategia());
			orden.setTipoActivo(robot.getActivo());
			orden.setTipoOrden(TipoOrden.CLOSE);
			orden.setRobot(robot.getRobot());
			orden.setFecha(config.getFechaInString(millis));
			orden.setHora(config.getHoraInString(millis));
		if(prediccion < 0.0) {
			orden.setTipoOrden(TipoOrden.SELL);
			if(inv == Boolean.TRUE) orden.setTipoOrden(TipoOrden.BUY);
		}else if(prediccion > 0.0) {
			orden.setTipoOrden(TipoOrden.BUY);
			if(inv == Boolean.TRUE) orden.setTipoOrden(TipoOrden.SELL);
		}else {
			orden.setTipoOrden(TipoOrden.CLOSE);	
		}
		
		return orden;
	}

	@Transactional
	public void calculate(Robot bot) throws IOException {
		
		if(bot.getRobotActivo() == Boolean.TRUE) {
			
			logger.info("SE CALCULA LA PREDICCIÓN -> Robot -> " + bot.getRobot());		
			Double prediccion = calcularPrediccion(bot);
			logger.info("PREDICCIÓN CALCULADA -> Robot -> " + bot.getRobot() + " Predicción -> " + prediccion);
			
			logger.info("SE CALCULA LA ORDEN -> Robot -> " + bot.getRobot());		
			Orden orden = calcularOperacion(bot, prediccion, bot.getInverso());
			logger.info("ORDEN CALCULADA -> Robot -> " + bot.getRobot() + " -> Orden -> " + orden.getTipoOrden());
			
			Long fechaHoraMillis = System.currentTimeMillis();
			
			//actualizarPrediccionBDs(bot, orden.getTipoOrden(), prediccion, fechaHoraMillis);
			logger.info("PREDICCIÓN ACTUALZIDA -> Robot -> " + bot.getRobot() + " Predicciñon -> " + prediccion);
			actualizarUltimaOrden(bot, orden, fechaHoraMillis);
			//logger.info("ORDEN ANTERIOR ELIMINADA -> Robot -> " + bot.getRobot());
			//guardarNuevaOrden(orden, fechaHoraMillis);
			//logger.info("NUEVA ORDEN GUARDADA -> Robot -> " + bot.getRobot() + " -> Orden -> " + orden.getTipoOrden());
			
		}
	}
	


}
