package daryl.system.robot.arima.c3.apachemq;


import java.io.IOException;
import java.util.Date;
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
import daryl.system.model.Robot;
import daryl.system.robot.arima.c3.predictor.ArimaC3Audcad;
import daryl.system.robot.arima.c3.predictor.ArimaC3Eurusd;
import daryl.system.robot.arima.c3.predictor.ArimaC3Gdaxi;
import daryl.system.robot.arima.c3.predictor.ArimaC3Ndx;
import daryl.system.robot.arima.c3.predictor.ArimaC3XauUsd;
import daryl.system.robot.arima.c3.predictor.ArimaC3XtiUsd;
import daryl.system.robot.arima.c3.predictor.base.ArimaC3Predictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;

	
	private ExecutorService servicio;
	
	@PostConstruct
	public void init() {
		this.servicio = Executors.newFixedThreadPool(ConfigData.MAX_NUM_OF_THREADS);
		logger.info("EXECUTOR CREADO -> " + this.getClass().getName());
	}
	
	@PreDestroy
	public void destroy() {
		if(this.servicio != null) {
			this.servicio.shutdown();
			logger.info("EXECUTOR CERRADO -> " + this.getClass().getName());
		}
	}

	@JmsListener(destination = "CHNL_ARIMA_C3")
	public void receiveMessage2(String robotJson) {
		
		
		final Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal()+ " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		Class activo = null;
		
		
		
		if(robot.getActivo() == Activo.GDAXI) {
			try{
				activo = ArimaC3Gdaxi.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				activo = ArimaC3Ndx.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{
				activo = ArimaC3XauUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				activo = ArimaC3Audcad.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XTIUSD) {
			try{
				activo = ArimaC3XtiUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{
				activo = ArimaC3Eurusd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		final ArimaC3Predictor predictor = (ArimaC3Predictor)applicationContext.getBean(activo);
		Thread t = new Thread() {
			
			public void run() {
				
				try {

					logger.info("PROCESO CALCULO LANZADO -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
					predictor.calculate(robot);
					logger.info("PROCESO CALCULO FINALIZADO -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		};
		
		servicio.submit(t);
		logger.info("PROCESO AÑADIDO AL EXECUTOR -> Robot -> " + robot.getRobot());

	}
	
}
