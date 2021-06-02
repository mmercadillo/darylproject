package daryl.system.robot.variance2.apachemq;

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
import daryl.system.robot.variance2.predictor.Variance2Audcad;
import daryl.system.robot.variance2.predictor.Variance2Eurusd;
import daryl.system.robot.variance2.predictor.Variance2Gdaxi;
import daryl.system.robot.variance2.predictor.Variance2Ndx;
import daryl.system.robot.variance2.predictor.Variance2XtiUsd;
import daryl.system.robot.variance2.predictor.Variance2XauUsd;
import daryl.system.robot.variance2.predictor.base.Variance2Predictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;

	@JmsListener(destination = "CHNL_VARIANCE_2")
	public void receiveMessage(String robotJson) {
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());

		Variance2Predictor predictor = null;
		
		if(robot.getActivo() == Activo.GDAXI) {
			try{
				predictor = applicationContext.getBean(Variance2Gdaxi.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				predictor = applicationContext.getBean(Variance2Ndx.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{
				predictor = applicationContext.getBean(Variance2XauUsd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				predictor = applicationContext.getBean(Variance2Audcad.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{
				predictor = applicationContext.getBean(Variance2Eurusd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XTIUSD) {
			try{
				predictor = applicationContext.getBean(Variance2XtiUsd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}

	}

}
