package daryl.system.robot.arima.d.inv.apachemq;


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
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvAudcad10080;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvAudcad1440;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvAudcad240;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvAudcad60;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvEurusd10080;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvEurusd1440;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvEurusd240;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvEurusd60;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvGdaxi10080;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvGdaxi1440;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvGdaxi240;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvGdaxi60;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvNdx10080;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvNdx1440;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvNdx240;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvNdx60;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvXauUsd10080;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvXauUsd1440;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvXauUsd240;
import daryl.system.robot.arima.d.inv.predictor.ArimaDInvXauUsd60;
import daryl.system.robot.arima.d.inv.predictor.base.ArimaPredictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;

	@JmsListener(destination = "CHNL_ARIMA_D_INV")
	public void receiveMessage(String robotJson) {
		
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
			
		 
		Timeframes timeframe = robot.getTimeframe();
		ArimaPredictor predictor = null;
		
		if(timeframe == Timeframes.PERIOD_H1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaDInvGdaxi60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaDInvNdx60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaDInvXauUsd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaDInvAudcad60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaDInvEurusd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaDInvGdaxi240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaDInvNdx240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaDInvXauUsd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaDInvAudcad240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaDInvEurusd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaDInvGdaxi1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaDInvNdx1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaDInvXauUsd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaDInvAudcad1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaDInvEurusd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaDInvGdaxi10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaDInvNdx10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaDInvXauUsd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaDInvAudcad10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaDInvEurusd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
	}

}
