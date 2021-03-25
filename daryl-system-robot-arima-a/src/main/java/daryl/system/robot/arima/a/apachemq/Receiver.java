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
import daryl.system.model.Robot;
import daryl.system.robot.arima.a.predictor.ArimaAudcad;
import daryl.system.robot.arima.a.predictor.ArimaEurusd;
import daryl.system.robot.arima.a.predictor.ArimaGdaxi;
import daryl.system.robot.arima.a.predictor.ArimaNdx;
import daryl.system.robot.arima.a.predictor.ArimaXauUsd;
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
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot + " - " + new Date().toLocaleString());		
		
		ArimaPredictor predictor = null;
		
		if(robot.getActivo() == Activo.GDAXI) {
			try{
				predictor = applicationContext.getBean(ArimaGdaxi.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				predictor = applicationContext.getBean(ArimaNdx.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{
				predictor = applicationContext.getBean(ArimaXauUsd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				predictor = applicationContext.getBean(ArimaAudcad.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{
				predictor = applicationContext.getBean(ArimaEurusd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
	}

	
}
