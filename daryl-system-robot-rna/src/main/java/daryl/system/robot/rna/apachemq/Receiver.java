package daryl.system.robot.rna.apachemq;


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
import daryl.system.robot.rna.predictor.RnaGdaxi10080;
import daryl.system.robot.rna.predictor.RnaGdaxi240;
import daryl.system.robot.rna.predictor.RnaGdaxi60;
import daryl.system.robot.rna.predictor.RnaNdx10080;
import daryl.system.robot.rna.predictor.RnaNdx1440;
import daryl.system.robot.rna.predictor.RnaNdx240;
import daryl.system.robot.rna.predictor.RnaNdx60;
import daryl.system.robot.rna.predictor.RnaXauUsd10080;
import daryl.system.robot.rna.predictor.RnaXauUsd1440;
import daryl.system.robot.rna.predictor.RnaXauUsd240;
import daryl.system.robot.rna.predictor.RnaXauUsd60;
import daryl.system.robot.rna.predictor.base.RnaPredictor;

@Component
public class Receiver {
	
	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@JmsListener(destination = "CHNL_RNA")
	public void receiveMessage(String robotJson) {
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		//System.out.println("Solicitud recibida en el canal CHNL_RNA -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_RNA -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		Timeframes timeframe = robot.getTimeframe();
		RnaPredictor predictor = null;
		
		if(timeframe == Timeframes.PERIOD_H1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(RnaGdaxi60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(RnaNdx60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(RnaXauUsd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(RnaGdaxi240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(RnaNdx240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(RnaXauUsd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(RnaNdx1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(RnaXauUsd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(RnaGdaxi10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(RnaNdx10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(RnaXauUsd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
	}

}
