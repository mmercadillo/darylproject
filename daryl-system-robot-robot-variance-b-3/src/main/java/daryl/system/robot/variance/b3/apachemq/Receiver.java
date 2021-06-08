package daryl.system.robot.variance.b3.apachemq;

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
import daryl.system.robot.variance.b3.predictor.VarianceB3Audcad;
import daryl.system.robot.variance.b3.predictor.VarianceB3Eurusd;
import daryl.system.robot.variance.b3.predictor.VarianceB3Gdaxi;
import daryl.system.robot.variance.b3.predictor.VarianceB3Ndx;
import daryl.system.robot.variance.b3.predictor.VarianceB3XauUsd;
import daryl.system.robot.variance.b3.predictor.VarianceB3XtiUsd;
import daryl.system.robot.variance.b3.predictor.base.VarianceB3Predictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;

	@JmsListener(destination = "CHNL_VARIANCE_B3")
	public void receiveMessage(String robotJson) {
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());

		VarianceB3Predictor predictor = null;
		
		if(robot.getActivo() == Activo.GDAXI) {
			try{
				predictor = applicationContext.getBean(VarianceB3Gdaxi.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				predictor = applicationContext.getBean(VarianceB3Ndx.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{
				predictor = applicationContext.getBean(VarianceB3XauUsd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				predictor = applicationContext.getBean(VarianceB3Audcad.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{
				predictor = applicationContext.getBean(VarianceB3Eurusd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XTIUSD) {
			try{
				predictor = applicationContext.getBean(VarianceB3XtiUsd.class);
				predictor.calculate(robot);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}

	}

}
