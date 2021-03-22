package daryl.system.robot.arima.b.apachemq;


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
import daryl.system.robot.arima.b.predictor.ArimaBAudcad10080;
import daryl.system.robot.arima.b.predictor.ArimaBAudcad1440;
import daryl.system.robot.arima.b.predictor.ArimaBAudcad240;
import daryl.system.robot.arima.b.predictor.ArimaBAudcad60;
import daryl.system.robot.arima.b.predictor.ArimaBEurusd10080;
import daryl.system.robot.arima.b.predictor.ArimaBEurusd1440;
import daryl.system.robot.arima.b.predictor.ArimaBEurusd240;
import daryl.system.robot.arima.b.predictor.ArimaBEurusd60;
import daryl.system.robot.arima.b.predictor.ArimaBGdaxi10080;
import daryl.system.robot.arima.b.predictor.ArimaBGdaxi1440;
import daryl.system.robot.arima.b.predictor.ArimaBGdaxi240;
import daryl.system.robot.arima.b.predictor.ArimaBGdaxi60;
import daryl.system.robot.arima.b.predictor.ArimaBNdx10080;
import daryl.system.robot.arima.b.predictor.ArimaBNdx1440;
import daryl.system.robot.arima.b.predictor.ArimaBNdx240;
import daryl.system.robot.arima.b.predictor.ArimaBNdx60;
import daryl.system.robot.arima.b.predictor.ArimaBXauUsd10080;
import daryl.system.robot.arima.b.predictor.ArimaBXauUsd1440;
import daryl.system.robot.arima.b.predictor.ArimaBXauUsd240;
import daryl.system.robot.arima.b.predictor.ArimaBXauUsd60;
import daryl.system.robot.arima.b.predictor.base.ArimaPredictor;

@Component
public class Receiver {
	
	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;

	@JmsListener(destination = "CHNL_ARIMA_B")
	public void receiveMessage(String robotJson) {
		
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		ArimaPredictor predictor = null;

		if(robot.getActivo() == Activo.GDAXI) {
			try{
				predictor = applicationContext.getBean(ArimaBGdaxi60.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				predictor = applicationContext.getBean(ArimaBNdx60.class);
				predictor.calculate(robot);				
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{		
				predictor = applicationContext.getBean(ArimaBXauUsd60.class);
				predictor.calculate(robot);		
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				predictor = applicationContext.getBean(ArimaBAudcad60.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{				
				predictor = applicationContext.getBean(ArimaBEurusd60.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
	}

}
