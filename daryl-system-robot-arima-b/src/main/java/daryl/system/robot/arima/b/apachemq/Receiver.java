package daryl.system.robot.arima.b.apachemq;


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
import daryl.system.robot.arima.b.predictor.ArimaBAudcad;
import daryl.system.robot.arima.b.predictor.ArimaBEurusd;
import daryl.system.robot.arima.b.predictor.ArimaBGdaxi;
import daryl.system.robot.arima.b.predictor.ArimaBNdx;
import daryl.system.robot.arima.b.predictor.ArimaBXauUsd;
import daryl.system.robot.arima.b.predictor.ArimaBXtiUsd;
import daryl.system.robot.arima.b.predictor.base.ArimaBPredictor;

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
	
	
	@JmsListener(destination = "CHNL_ARIMA_B")
	public void receiveMessage(String robotJson) {
		
		
		final Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		Class activo = null;
		

		if(robot.getActivo() == Activo.GDAXI) {
			try{
				activo = ArimaBGdaxi.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				activo = ArimaBNdx.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{
				activo = ArimaBXauUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				activo = ArimaBAudcad.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XTIUSD) {
			try{
				activo = ArimaBXtiUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{
				activo = ArimaBEurusd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
	
		final ArimaBPredictor predictor = (ArimaBPredictor)applicationContext.getBean(activo);
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
