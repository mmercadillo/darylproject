package daryl.system.robot.rna.inv.apachemq;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.rna.inv.predictor.base.RnaPredictor;

@Component
public class Receiver {

	
	@Autowired
	JmsListenerContainerFactory<?> factory;
	
	@Autowired
	@Qualifier(value = "rnaInvXauusd60")
	RnaPredictor rnaInvXauUsd60;
	
	@Autowired
	@Qualifier(value = "rnaInvXauusd240")
	RnaPredictor rnaInvXauUsd240;
	
	@Autowired
	@Qualifier(value = "rnaInvXauusd1440")
	RnaPredictor rnaInvXauUsd1440;
	
	@Autowired
	@Qualifier(value = "rnaInvXauusd10080")
	RnaPredictor rnaInvXauUsd10080;
	
	@Autowired
	@Qualifier(value = "rnaInvAudcad60")
	RnaPredictor rnaInvAudCad60;
	
	@Autowired
	@Qualifier(value = "rnaInvAudcad240")
	RnaPredictor rnaInvAudCad240;
	
	@Autowired
	@Qualifier(value = "rnaInvAudcad1440")
	RnaPredictor rnaInvAudCad1440;
	
	@Autowired
	@Qualifier(value = "rnaInvAudcad10080")
	RnaPredictor rnaInvAudCad10080;

	@Autowired
	@Qualifier(value = "rnaInvNdx60")
	RnaPredictor rnaInvNdx60;
	
	@Autowired
	@Qualifier(value = "rnaInvNdx240")
	RnaPredictor rnaInvNdx240;
	
	@Autowired
	@Qualifier(value = "rnaInvNdx1440")
	RnaPredictor rnaInvNdx1440;
	
	@Autowired
	@Qualifier(value = "rnaInvNdx10080")
	RnaPredictor rnaInvNdx10080;

	@Autowired
	@Qualifier(value = "rnaInvGdaxi60")
	RnaPredictor rnaInvGdaxi60;

	@Autowired
	@Qualifier(value = "rnaInvGdaxi240")
	RnaPredictor rnaInvGdaxi240;
	
	@Autowired
	@Qualifier(value = "rnaInvGdaxi1440")
	RnaPredictor rnaInvGdaxi1440;
	
	@Autowired
	@Qualifier(value = "rnaInvGdaxi10080")
	RnaPredictor rnaInvGdaxi10080;
	
	@JmsListener(destination = "CHNL_RNA_INV")
	public void receiveMessage(String robotJson) {
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		System.out.println("Solicitud recibida en el canal CHNL_RNA_INV -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		//logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_RNA_INV -> Robot -> " + robot.getRobot());
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			try{if(robot.getActivo() == Activo.GDAXI) rnaInvGdaxi60.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) rnaInvNdx60.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) rnaInvXauUsd60.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			try{if(robot.getActivo() == Activo.GDAXI) rnaInvGdaxi240.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) rnaInvNdx240.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) rnaInvXauUsd240.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{if(robot.getActivo() == Activo.NDX) rnaInvNdx1440.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) rnaInvXauUsd1440.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			try{if(robot.getActivo() == Activo.GDAXI) rnaInvGdaxi10080.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) rnaInvNdx10080.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) rnaInvXauUsd10080.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}
		
	}

}
