package daryl.system.robot.ann.apachemq;


import java.io.IOException;
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
import daryl.system.robot.ann.predictor.AnnAudCad;
import daryl.system.robot.ann.predictor.AnnEurUsd;
import daryl.system.robot.ann.predictor.AnnGdaxi;
import daryl.system.robot.ann.predictor.AnnNdx;
import daryl.system.robot.ann.predictor.AnnWti;
import daryl.system.robot.ann.predictor.AnnXauUsd;
import daryl.system.robot.ann.predictor.base.AnnPredictor;

@Component
public class Receiver {
	
	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@JmsListener(destination = "CHNL_ANN")
	public void receiveMessage(String robotJson) {
		
		final Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());

		Class activo = null;
		if(robot.getActivo() == Activo.GDAXI) {
			try{
				activo = AnnGdaxi.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				activo = AnnNdx.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{
				activo = AnnAudCad.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				activo = AnnAudCad.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XTIUSD) {
			try{
				activo = AnnWti.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{
				activo = AnnEurUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		final AnnPredictor predictor = (AnnPredictor)applicationContext.getBean(activo);
		
		(new Thread() {
			
			public void run() {
				
				try {
					predictor.calculate(robot);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}).start();
		

	}

}
