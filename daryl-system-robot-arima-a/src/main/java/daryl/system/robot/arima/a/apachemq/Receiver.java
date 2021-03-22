package daryl.system.robot.arima.a.apachemq;

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
import daryl.system.robot.arima.a.predictor.ArimaAudcad10080;
import daryl.system.robot.arima.a.predictor.ArimaAudcad1440;
import daryl.system.robot.arima.a.predictor.ArimaAudcad240;
import daryl.system.robot.arima.a.predictor.ArimaAudcad60;
import daryl.system.robot.arima.a.predictor.ArimaEurusd10080;
import daryl.system.robot.arima.a.predictor.ArimaEurusd1440;
import daryl.system.robot.arima.a.predictor.ArimaEurusd240;
import daryl.system.robot.arima.a.predictor.ArimaEurusd60;
import daryl.system.robot.arima.a.predictor.ArimaGdaxi10080;
import daryl.system.robot.arima.a.predictor.ArimaGdaxi1440;
import daryl.system.robot.arima.a.predictor.ArimaGdaxi240;
import daryl.system.robot.arima.a.predictor.ArimaGdaxi60;
import daryl.system.robot.arima.a.predictor.ArimaNdx10080;
import daryl.system.robot.arima.a.predictor.ArimaNdx1440;
import daryl.system.robot.arima.a.predictor.ArimaNdx240;
import daryl.system.robot.arima.a.predictor.ArimaNdx60;
import daryl.system.robot.arima.a.predictor.ArimaXauUsd10080;
import daryl.system.robot.arima.a.predictor.ArimaXauUsd1440;
import daryl.system.robot.arima.a.predictor.ArimaXauUsd240;
import daryl.system.robot.arima.a.predictor.ArimaXauUsd60;
import daryl.system.robot.arima.a.predictor.base.ArimaPredictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;
	
	@JmsListener(destination = "CHNL_ARIMA_A")
	public void receiveMessage(String robotJson) {
		
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		//System.out.println("Solicitud recibida en el canal CHNL_ARIMA_A -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_ARIMA_A -> Robot -> " + robot + " - " + new Date().toLocaleString());		
		
		Timeframes timeframe = robot.getTimeframe();
		ArimaPredictor predictor = null;
		
		if(timeframe == Timeframes.PERIOD_H1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaGdaxi60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaNdx60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaXauUsd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaAudcad60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaEurusd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaGdaxi240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaNdx240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaXauUsd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaAudcad240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaEurusd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaGdaxi1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaNdx1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaXauUsd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaAudcad1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaEurusd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaGdaxi10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaNdx10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaXauUsd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaAudcad10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaEurusd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
	}

	
}
