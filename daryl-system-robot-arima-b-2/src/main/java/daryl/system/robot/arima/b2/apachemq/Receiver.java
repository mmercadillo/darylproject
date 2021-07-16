package daryl.system.robot.arima.b2.apachemq;


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
import daryl.system.robot.arima.b2.predictor.ArimaB2Audcad;
import daryl.system.robot.arima.b2.predictor.ArimaB2Eurusd;
import daryl.system.robot.arima.b2.predictor.ArimaB2Gdaxi;
import daryl.system.robot.arima.b2.predictor.ArimaB2Ndx;
import daryl.system.robot.arima.b2.predictor.ArimaB2XauUsd;
import daryl.system.robot.arima.b2.predictor.ArimaB2XtiUsd;
import daryl.system.robot.arima.b2.predictor.base.ArimaB2Predictor;

@Component
public class Receiver {
	
	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;

	@JmsListener(destination = "CHNL_ARIMA_B2")
	public void receiveMessage(String robotJson) {
		
		
		final Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		Class activo = null;
		

		if(robot.getActivo() == Activo.GDAXI) {
			try{
				activo = ArimaB2Gdaxi.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				activo = ArimaB2Ndx.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{
				activo = ArimaB2XauUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				activo = ArimaB2Audcad.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XTIUSD) {
			try{
				activo = ArimaB2XtiUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{
				activo = ArimaB2Eurusd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
		final ArimaB2Predictor predictor = (ArimaB2Predictor)applicationContext.getBean(activo);
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
