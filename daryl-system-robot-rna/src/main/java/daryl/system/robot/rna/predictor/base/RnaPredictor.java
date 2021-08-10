package daryl.system.robot.rna.predictor.base;

import java.io.IOException;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.MaxMinNormalizer;
import org.ta4j.core.utils.BarSeriesUtils;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Mode;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Orden;
import daryl.system.model.Prediccion;
import daryl.system.model.RnaConfig;
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;
import daryl.system.robot.rna.repository.IHistoricoRepository;
import daryl.system.robot.rna.repository.IOrdenRepository;
import daryl.system.robot.rna.repository.IPrediccionRepository;
import daryl.system.robot.rna.repository.IRnaConfigRepository;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class RnaPredictor {

	@Autowired
	protected Logger logger;


	@Autowired
	protected ConfigData config;
		
	@Autowired
	protected IOrdenRepository ordenRepository;
	@Autowired
	protected IPrediccionRepository prediccionRepository;
	@Autowired
	private IRnaConfigRepository rnaConfigRepository;


	@Autowired
	private IHistoricoRepository historicoRepository; 

	protected Double calcularPrediccion(Robot bot) throws IOException {

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
							
				//List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(bot.getTimeframe(), bot.getActivo());
				List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraDesc(bot.getTimeframe(), bot.getActivo(), PageRequest.of(0,  rnaConfig.getNeuronasEntrada()+1));
				Collections.reverse(historico);
				
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
	
	
	protected RnaConfig getRnaConfig(Robot robot) {
		
		return rnaConfigRepository.findRnaConfigByRobot(robot.getRnaConfig());
		
	}
	
	protected NeuralNetwork rnaFromByteArray(byte[] byteArray) throws IOException, ClassNotFoundException {
		
		NeuralNetwork rna = (NeuralNetwork)SerializationUtils.deserialize(byteArray);
		return rna;
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
	
	private void actualizarUltimaOrden(Robot robot, Orden orden, Long fechaHoraMillis) {
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
		
		logger.info("SE CALCULA LA PREDICCIÓN -> Robot -> " + bot.getRobot());		
		Double prediccion = calcularPrediccion(bot);
		logger.info("PREDICCIÓN CALCULADA -> Robot -> " + bot.getRobot() + " Predicción -> " + prediccion);
		
		logger.info("SE CALCULA LA ORDEN -> Robot -> " + bot.getRobot());		
		Orden orden = calcularOperacion(bot, prediccion, bot.getInverso());
		logger.info("ORDEN CALCULADA -> Robot -> " + bot.getRobot() + " -> Orden -> " + orden.getTipoOrden());
		
		Long fechaHoraMillis = System.currentTimeMillis();
		
		actualizarPrediccionBDs(bot, orden.getTipoOrden(), prediccion, fechaHoraMillis);
		logger.info("PREDICCIÓN ACTUALZIDA -> Robot -> " + bot.getRobot() + " Predicciñon -> " + prediccion);
		actualizarUltimaOrden(bot, orden, fechaHoraMillis);
		logger.info("ORDEN ANTERIOR ELIMINADA -> Robot -> " + bot.getRobot());
		guardarNuevaOrden(orden, fechaHoraMillis);
		logger.info("NUEVA ORDEN GUARDADA -> Robot -> " + bot.getRobot() + " -> Orden -> " + orden.getTipoOrden());

		
	}
	


}
