package daryl.system.robot.arima.c.apachemq;


import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.arima.c.predictor.ArimaCAudcad10080;
import daryl.system.robot.arima.c.predictor.ArimaCAudcad1440;
import daryl.system.robot.arima.c.predictor.ArimaCAudcad240;
import daryl.system.robot.arima.c.predictor.ArimaCAudcad60;
import daryl.system.robot.arima.c.predictor.ArimaCEurusd10080;
import daryl.system.robot.arima.c.predictor.ArimaCEurusd1440;
import daryl.system.robot.arima.c.predictor.ArimaCEurusd240;
import daryl.system.robot.arima.c.predictor.ArimaCEurusd60;
import daryl.system.robot.arima.c.predictor.ArimaCGdaxi10080;
import daryl.system.robot.arima.c.predictor.ArimaCGdaxi1440;
import daryl.system.robot.arima.c.predictor.ArimaCGdaxi240;
import daryl.system.robot.arima.c.predictor.ArimaCGdaxi60;
import daryl.system.robot.arima.c.predictor.ArimaCNdx10080;
import daryl.system.robot.arima.c.predictor.ArimaCNdx1440;
import daryl.system.robot.arima.c.predictor.ArimaCNdx240;
import daryl.system.robot.arima.c.predictor.ArimaCNdx60;
import daryl.system.robot.arima.c.predictor.ArimaCXauUsd10080;
import daryl.system.robot.arima.c.predictor.ArimaCXauUsd1440;
import daryl.system.robot.arima.c.predictor.ArimaCXauUsd240;
import daryl.system.robot.arima.c.predictor.ArimaCXauUsd60;
import daryl.system.robot.arima.c.predictor.base.ArimaPredictor;

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
		
		
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal()+ " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		Timeframes timeframe = robot.getTimeframe();
		ArimaPredictor predictor = null;

		if(timeframe == Timeframes.PERIOD_H1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaCGdaxi60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaCNdx60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaCXauUsd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaCAudcad60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaCEurusd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaCGdaxi240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaCNdx240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaCXauUsd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaCAudcad240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaCEurusd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaCGdaxi1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaCNdx1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaCXauUsd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaCAudcad1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaCEurusd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaCGdaxi10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaCNdx10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaCXauUsd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaCAudcad10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaCEurusd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
	}

	
}
