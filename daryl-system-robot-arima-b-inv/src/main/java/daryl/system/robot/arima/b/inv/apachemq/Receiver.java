package daryl.system.robot.arima.b.inv.apachemq;

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
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvAudcad10080;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvAudcad1440;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvAudcad240;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvAudcad60;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvEurusd10080;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvEurusd1440;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvEurusd240;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvEurusd60;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvGdaxi10080;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvGdaxi1440;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvGdaxi240;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvGdaxi60;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvNdx10080;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvNdx1440;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvNdx240;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvNdx60;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvXauUsd10080;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvXauUsd1440;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvXauUsd240;
import daryl.system.robot.arima.b.inv.predictor.ArimaBInvXauUsd60;
import daryl.system.robot.arima.b.inv.predictor.base.ArimaPredictor;

@Component
public class Receiver {
	
	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;


	@JmsListener(destination = "CHNL_ARIMA_B_INV")
	public void receiveMessage(String robotJson) {
		
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		System.out.println("Solicitud recibida en el canal CHNL_ARIMA_B_INV -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		//logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_ARIMA_B_INV -> Robot -> " + robot.getRobot());
		Timeframes timeframe = robot.getTimeframe();
		ArimaPredictor predictor = null;

		if(timeframe == Timeframes.PERIOD_H1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaBInvGdaxi60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaBInvNdx60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaBInvXauUsd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaBInvAudcad60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaBInvEurusd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaBInvGdaxi240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaBInvNdx240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaBInvXauUsd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaBInvAudcad240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaBInvEurusd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaBInvGdaxi1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaBInvNdx1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaBInvXauUsd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaBInvAudcad1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaBInvEurusd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaBInvGdaxi10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaBInvNdx10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaBInvXauUsd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaBInvAudcad10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaBInvEurusd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}
		
	}
	
}
