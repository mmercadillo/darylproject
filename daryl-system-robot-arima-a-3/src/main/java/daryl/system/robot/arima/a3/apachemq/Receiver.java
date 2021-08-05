package daryl.system.robot.arima.a3.apachemq;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Activo;
import daryl.system.model.Orden;
import daryl.system.model.Robot;
import daryl.system.robot.arima.a3.predictor.Arima3Audcad;
import daryl.system.robot.arima.a3.predictor.Arima3Eurusd;
import daryl.system.robot.arima.a3.predictor.Arima3Gdaxi;
import daryl.system.robot.arima.a3.predictor.Arima3Ndx;
import daryl.system.robot.arima.a3.predictor.Arima3XauUsd;
import daryl.system.robot.arima.a3.predictor.Arima3XtiUsd;
import daryl.system.robot.arima.a3.predictor.base.Arima3Predictor;
import daryl.system.robot.arima.a3.repository.IOrdenRepository;

@Component
public class Receiver {

	private static final String CHANNEL = "CHNL_ARIMA_A3";
	
	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private IOrdenRepository ordenRepository;

	
	//private ExecutorService servicio;
	
	@PostConstruct
	public void init() {
		//this.servicio = Executors.newFixedThreadPool(ConfigData.MAX_NUM_OF_THREADS);
		//logger.info("EXECUTOR CREADO -> " + this.getClass().getName());
	}
	
	@PreDestroy
	public void destroy() {
		/*
		if(this.servicio != null) {
			this.servicio.shutdown();
			logger.info("EXECUTOR CERRADO -> " + this.getClass().getName());
		}*/
	}
	
	@JmsListener(destination = CHANNEL, concurrency = "4-8")
	public void receiveFullMessage(String listaRobotsJson) {
		
		
		ExecutorService servicio = Executors.newFixedThreadPool(ConfigData.MAX_NUM_OF_THREADS);
		logger.info("EXECUTOR CREADO -> " + this.getClass().getName());
		
		final List<Robot> robots = Arrays.asList(new Gson().fromJson(listaRobotsJson, Robot[].class));
		
		
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + CHANNEL + " " + new Date().toLocaleString());		
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + CHANNEL + " " + listaRobotsJson);		
		
		for(Robot robot : robots) {
			
			if(robot.getRobotActivo() == Boolean.TRUE) {
				Class activo = null;
				
				
				
				if(robot.getActivo() == Activo.GDAXI) {
					try{
						activo = Arima3Gdaxi.class;
					}catch (Exception e) {
						logger.error(e.getMessage(), e);		
					}
				}
				if(robot.getActivo() == Activo.NDX) {
					try{
						activo = Arima3Ndx.class;
					}catch (Exception e) {
						logger.error(e.getMessage(), e);		
					}
				}
				if(robot.getActivo() == Activo.XAUUSD) {
					try{
						activo = Arima3XauUsd.class;
					}catch (Exception e) {
						logger.error(e.getMessage(), e);		
					}
				}
				if(robot.getActivo() == Activo.AUDCAD) {
					try{
						activo = Arima3Audcad.class;
					}catch (Exception e) {
						logger.error(e.getMessage(), e);		
					}
				}
				if(robot.getActivo() == Activo.XTIUSD) {
					try{
						activo = Arima3XtiUsd.class;
					}catch (Exception e) {
						logger.error(e.getMessage(), e);		
					}
				}
				if(robot.getActivo() == Activo.EURUSD) {
					try{
						activo = Arima3Eurusd.class;
					}catch (Exception e) {
						logger.error(e.getMessage(), e);		
					}
				}
				final Arima3Predictor predictor = (Arima3Predictor)applicationContext.getBean(activo);
				
				
				servicio.submit(() -> {
					try {
						logger.info("PROCESO CALCULO LANZADO -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
						predictor.calculate(robot);
						logger.info("PROCESO CALCULO FINALIZADO -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				
				logger.info("PROCESO AÑADIDO AL EXECUTOR -> Robot -> " + robot.getRobot());
			}else {
				
				//Borramos las órdenes del robot desactivado
				logger.info("Robot -> " + robot.getRobot() + " DESACTIVADO - SE BORRAN SUS ÓRDENES SI EXIUSTEN");
				try {

					//Orden ultimaOrden = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, robot.getActivo(), robot.getEstrategia());
					List<Orden> ultimasOrdenes = ordenRepository.findAllBytipoActivoAndEstrategia(robot.getActivo(), robot.getEstrategia());
					if(ultimasOrdenes != null && ultimasOrdenes.size() > 0) {
						//ultimaOrden.setFBaja(fechaHoraMillis);
						for (Orden ord : ultimasOrdenes) {
							ordenRepository.delete(ord);
						}
						logger.info("Robot -> " + robot.getRobot() + " ÓRDENES BORRADAS ");
						
					}else {
						logger.info("Robot -> " + robot.getRobot() + " NO EXISTEN ÓRDENES PARA BORRAR");
					}
				}catch (Exception e) {
					logger.error("No se ha recuperado el valor de la última orden del robot: {}", robot.getRobot(), e);
				}
				
				
			}
		}
		
		servicio.shutdown();
		
	}
	
	/*
	@JmsListener(destination = CHANNEL)
	public void receiveMessage(String robotJson) {
		
		
		final Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot + " - " + new Date().toLocaleString());		
		
		Class activo = null;
		
		
		
		if(robot.getActivo() == Activo.GDAXI) {
			try{
				activo = Arima3Gdaxi.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				activo = Arima3Ndx.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{
				activo = Arima3XauUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				activo = Arima3Audcad.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XTIUSD) {
			try{
				activo = Arima3XtiUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{
				activo = Arima3Eurusd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		final Arima3Predictor predictor = (Arima3Predictor)applicationContext.getBean(activo);
		
		Future future = servicio.submit(() -> {
			
			try {
	
				logger.info("PROCESO CALCULO LANZADO -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
				predictor.calculate(robot);
				logger.info("PROCESO CALCULO FINALIZADO -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		logger.info("PROCESO AÑADIDO AL EXECUTOR -> Robot -> " + robot.getRobot());
	
		
	}
	*/
	
}
