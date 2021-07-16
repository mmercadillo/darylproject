package daryl.system.robot.arima.c.apachemq;


import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import daryl.system.comun.enums.Activo;
import daryl.system.model.Robot;
import daryl.system.robot.arima.c.predictor.ArimaCAudcad;
import daryl.system.robot.arima.c.predictor.ArimaCEurusd;
import daryl.system.robot.arima.c.predictor.ArimaCGdaxi;
import daryl.system.robot.arima.c.predictor.ArimaCNdx;
import daryl.system.robot.arima.c.predictor.ArimaCXauUsd;
import daryl.system.robot.arima.c.predictor.ArimaCXtiUsd;
import daryl.system.robot.arima.c.predictor.base.ArimaCPredictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;
	
	
	@JmsListener(destination = "CHNL_ARIMA_C")
	public void receiveMessage(String robotJson) {
		
		final Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal()+ " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		Class activo = null;
		
		
		if(robot.getActivo() == Activo.GDAXI) {
			try{
				activo = ArimaCGdaxi.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				activo = ArimaCNdx.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{
				activo = ArimaCXauUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				activo = ArimaCAudcad.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XTIUSD) {
			try{
				activo = ArimaCXtiUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{
				activo = ArimaCEurusd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
		final ArimaCPredictor predictor = (ArimaCPredictor)applicationContext.getBean(activo);
		(new Thread() {
			
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
			
		}).start();

	}

	
}
