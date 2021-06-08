package daryl.system.robot.arima.b2.apachemq;


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
		
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		ArimaB2Predictor predictor = null;

		if(robot.getActivo() == Activo.GDAXI) {
			try{
				predictor = applicationContext.getBean(ArimaB2Gdaxi.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				predictor = applicationContext.getBean(ArimaB2Ndx.class);
				predictor.calculate(robot);				
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{		
				predictor = applicationContext.getBean(ArimaB2XauUsd.class);
				predictor.calculate(robot);		
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				predictor = applicationContext.getBean(ArimaB2Audcad.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{				
				predictor = applicationContext.getBean(ArimaB2Eurusd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XTIUSD) {
			try{				
				predictor = applicationContext.getBean(ArimaB2XtiUsd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
	}

}
