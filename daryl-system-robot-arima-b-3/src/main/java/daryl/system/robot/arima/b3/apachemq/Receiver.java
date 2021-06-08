package daryl.system.robot.arima.b3.apachemq;


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
import daryl.system.robot.arima.b3.predictor.ArimaB3Audcad;
import daryl.system.robot.arima.b3.predictor.ArimaB3Eurusd;
import daryl.system.robot.arima.b3.predictor.ArimaB3Gdaxi;
import daryl.system.robot.arima.b3.predictor.ArimaB3Ndx;
import daryl.system.robot.arima.b3.predictor.ArimaB3XauUsd;
import daryl.system.robot.arima.b3.predictor.ArimaB3XtiUsd;
import daryl.system.robot.arima.b3.predictor.base.ArimaB3Predictor;

@Component
public class Receiver {
	
	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;

	@JmsListener(destination = "CHNL_ARIMA_B3")
	public void receiveMessage(String robotJson) {
		
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		ArimaB3Predictor predictor = null;

		if(robot.getActivo() == Activo.GDAXI) {
			try{
				predictor = applicationContext.getBean(ArimaB3Gdaxi.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				predictor = applicationContext.getBean(ArimaB3Ndx.class);
				predictor.calculate(robot);				
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{		
				predictor = applicationContext.getBean(ArimaB3XauUsd.class);
				predictor.calculate(robot);		
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				predictor = applicationContext.getBean(ArimaB3Audcad.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{				
				predictor = applicationContext.getBean(ArimaB3Eurusd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XTIUSD) {
			try{				
				predictor = applicationContext.getBean(ArimaB3XtiUsd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
	}

}
