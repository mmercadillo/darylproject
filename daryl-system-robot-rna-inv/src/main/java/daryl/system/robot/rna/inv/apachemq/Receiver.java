package daryl.system.robot.rna.inv.apachemq;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.rna.inv.predictor.base.RnaPredictor;

@Component
public class Receiver {
	
	@Autowired
	Logger logger;
	
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
	public void receiveMessage(Robot robot) {
		
		logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_RNA_INV -> Robot -> " + robot.getRobot());
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			if(robot.getActivo() == Activo.GDAXI) rnaInvGdaxi60.calculate(robot);
			if(robot.getActivo() == Activo.NDX) rnaInvNdx60.calculate(robot);
			if(robot.getActivo() == Activo.XAUUSD) rnaInvXauUsd60.calculate(robot);
		}else if(timeframe == Timeframes.PERIOD_H4) {
			if(robot.getActivo() == Activo.GDAXI) rnaInvGdaxi240.calculate(robot);
			if(robot.getActivo() == Activo.NDX) rnaInvNdx240.calculate(robot);
			if(robot.getActivo() == Activo.XAUUSD) rnaInvXauUsd240.calculate(robot);
		}else if(timeframe == Timeframes.PERIOD_D1) {
			if(robot.getActivo() == Activo.NDX) rnaInvNdx1440.calculate(robot);
			if(robot.getActivo() == Activo.XAUUSD) rnaInvXauUsd1440.calculate(robot);
		}else if(timeframe == Timeframes.PERIOD_W1) {
			if(robot.getActivo() == Activo.GDAXI) rnaInvGdaxi10080.calculate(robot);
			if(robot.getActivo() == Activo.NDX) rnaInvNdx10080.calculate(robot);
			if(robot.getActivo() == Activo.XAUUSD) rnaInvXauUsd10080.calculate(robot);
		}
		
	}

}
