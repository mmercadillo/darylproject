package daryl.system.robot.arima.a2.apachemq;

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
import daryl.system.robot.arima.a2.predictor.Arima2Audcad;
import daryl.system.robot.arima.a2.predictor.Arima2Eurusd;
import daryl.system.robot.arima.a2.predictor.Arima2Gdaxi;
import daryl.system.robot.arima.a2.predictor.Arima2Ndx;
import daryl.system.robot.arima.a2.predictor.Arima2XauUsd;
import daryl.system.robot.arima.a2.predictor.Arima2XtiUsd;
import daryl.system.robot.arima.a2.predictor.base.Arima2Predictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;
	
	@JmsListener(destination = "CHNL_ARIMA_A2")
	public void receiveMessage(String robotJson) {
		
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot + " - " + new Date().toLocaleString());		
		
		Arima2Predictor predictor = null;
		
		if(robot.getActivo() == Activo.GDAXI) {
			try{
				predictor = applicationContext.getBean(Arima2Gdaxi.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				predictor = applicationContext.getBean(Arima2Ndx.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{
				predictor = applicationContext.getBean(Arima2XauUsd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				predictor = applicationContext.getBean(Arima2Audcad.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{
				predictor = applicationContext.getBean(Arima2Eurusd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XTIUSD) {
			try{
				predictor = applicationContext.getBean(Arima2XtiUsd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
	}

	
}
