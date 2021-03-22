package daryl.system.robot.arima.a.inv.apachemq;


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
import daryl.system.robot.arima.a.inv.predictor.ArimaInvAudcad10080;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvAudcad1440;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvAudcad240;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvAudcad60;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvEurusd10080;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvEurusd1440;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvEurusd240;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvEurusd60;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvGdaxi10080;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvGdaxi1440;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvGdaxi240;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvGdaxi60;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvNdx10080;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvNdx1440;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvNdx240;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvNdx60;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvXauUsd10080;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvXauUsd1440;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvXauUsd240;
import daryl.system.robot.arima.a.inv.predictor.ArimaInvXauUsd60;
import daryl.system.robot.arima.a.inv.predictor.base.ArimaPredictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	
	@Autowired
	JmsListenerContainerFactory<?> factory;
	
	@Autowired
	private ApplicationContext applicationContext;

	@JmsListener(destination = "CHNL_ARIMA_A_INV")
	public void receiveMessage(String robotJson) {
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		//System.out.println("Solicitud recibida en el canal CHNL_ARIMA_A_INV -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_ARIMA_A_INV -> Robot -> " + robot + " - " + new Date().toLocaleString());
		
		Timeframes timeframe = robot.getTimeframe();
		ArimaPredictor predictor = null;
		if(timeframe == Timeframes.PERIOD_H1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaInvGdaxi60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);	
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaInvNdx60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaInvXauUsd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaInvAudcad60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaInvEurusd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaInvGdaxi240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaInvNdx240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaInvXauUsd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaInvAudcad240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaInvEurusd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaInvGdaxi1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaInvNdx1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaInvXauUsd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaInvAudcad1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaInvEurusd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaInvGdaxi10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaInvNdx10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaInvXauUsd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaInvAudcad10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaInvEurusd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
	}

}
