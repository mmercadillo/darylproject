package daryl.system.robot.rna.inv.apachemq;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.rna.inv.predictor.RnaInvGdaxi10080;
import daryl.system.robot.rna.inv.predictor.RnaInvGdaxi240;
import daryl.system.robot.rna.inv.predictor.RnaInvGdaxi60;
import daryl.system.robot.rna.inv.predictor.RnaInvNdx10080;
import daryl.system.robot.rna.inv.predictor.RnaInvNdx1440;
import daryl.system.robot.rna.inv.predictor.RnaInvNdx240;
import daryl.system.robot.rna.inv.predictor.RnaInvNdx60;
import daryl.system.robot.rna.inv.predictor.RnaInvXauUsd10080;
import daryl.system.robot.rna.inv.predictor.RnaInvXauUsd1440;
import daryl.system.robot.rna.inv.predictor.RnaInvXauUsd240;
import daryl.system.robot.rna.inv.predictor.RnaInvXauUsd60;
import daryl.system.robot.rna.inv.predictor.base.RnaPredictor;

@Component
public class Receiver {

	
	@Autowired
	JmsListenerContainerFactory<?> factory;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@JmsListener(destination = "CHNL_RNA_INV")
	public void receiveMessage(String robotJson) {
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		System.out.println("Solicitud recibida en el canal CHNL_RNA_INV -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		//logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_RNA_INV -> Robot -> " + robot.getRobot());
		Timeframes timeframe = robot.getTimeframe();
		RnaPredictor predictor = null;
		
		if(timeframe == Timeframes.PERIOD_H1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(RnaInvGdaxi60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(RnaInvNdx60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(RnaInvXauUsd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(RnaInvGdaxi240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(RnaInvNdx240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(RnaInvXauUsd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(RnaInvNdx1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(RnaInvXauUsd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(RnaInvGdaxi10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(RnaInvNdx10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(RnaInvXauUsd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}
		
	}

}
