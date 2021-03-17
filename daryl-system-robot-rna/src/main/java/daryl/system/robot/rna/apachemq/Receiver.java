package daryl.system.robot.rna.apachemq;


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
import daryl.system.robot.rna.predictor.base.RnaPredictor;

@Component
public class Receiver {
	

	
	@Autowired
	JmsListenerContainerFactory<?> factory;
	
	@Autowired
	@Qualifier(value = "rnaXauusd60")
	RnaPredictor rnaXauUsd60;
	
	@Autowired
	@Qualifier(value = "rnaXauusd240")
	RnaPredictor rnaXauUsd240;
	
	@Autowired
	@Qualifier(value = "rnaXauusd1440")
	RnaPredictor rnaXauUsd1440;
	
	@Autowired
	@Qualifier(value = "rnaXauusd10080")
	RnaPredictor rnaXauUsd10080;
	
	@Autowired
	@Qualifier(value = "rnaAudcad60")
	RnaPredictor rnaAudCad60;
	
	@Autowired
	@Qualifier(value = "rnaAudcad240")
	RnaPredictor rnaAudCad240;
	
	@Autowired
	@Qualifier(value = "rnaAudcad1440")
	RnaPredictor rnaAudCad1440;
	
	@Autowired
	@Qualifier(value = "rnaAudcad10080")
	RnaPredictor rnaAudCad10080;

	@Autowired
	@Qualifier(value = "rnaNdx60")
	RnaPredictor rnaNdx60;
	
	@Autowired
	@Qualifier(value = "rnaNdx240")
	RnaPredictor rnaNdx240;
	
	@Autowired
	@Qualifier(value = "rnaNdx1440")
	RnaPredictor rnaNdx1440;
	
	@Autowired
	@Qualifier(value = "rnaNdx10080")
	RnaPredictor rnaNdx10080;
	
	@Autowired
	@Qualifier(value = "rnaGdaxi60")
	RnaPredictor rnaGdaxi60;

	@Autowired
	@Qualifier(value = "rnaGdaxi240")
	RnaPredictor rnaGdaxi240;
	
	@Autowired
	@Qualifier(value = "rnaGdaxi1440")
	RnaPredictor rnapGdaxi1440;
	
	@Autowired
	@Qualifier(value = "rnaGdaxi10080")
	RnaPredictor rnaGdaxi10080;
	
	@JmsListener(destination = "CHNL_RNA")
	public void receiveMessage(String robotJson) {
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		System.out.println("Solicitud recibida en el canal CHNL_RNA -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		//logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_RNA -> Robot -> " + robot.getRobot());
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			try{if(robot.getActivo() == Activo.GDAXI) rnaGdaxi60.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) rnaNdx60.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) rnaXauUsd60.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			try{if(robot.getActivo() == Activo.GDAXI) rnaGdaxi240.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) rnaNdx240.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) rnaXauUsd240.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{if(robot.getActivo() == Activo.NDX) rnaNdx1440.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) rnaXauUsd1440.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			try{if(robot.getActivo() == Activo.GDAXI) rnaGdaxi10080.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) rnaNdx10080.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) rnaXauUsd10080.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}
		
	}

}
