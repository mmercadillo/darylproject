package daryl.system.robot.rna.inv.apachemq;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

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
		
		logger.info("Invocación de ", robot);
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			rnaInvGdaxi60.calculate(robot.getActivo(), robot.getRobot());
			rnaInvNdx60.calculate(robot.getActivo(), robot.getRobot());
			rnaInvXauUsd60.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_H4) {
			rnaInvGdaxi240.calculate(robot.getActivo(), robot.getRobot());
			rnaInvNdx240.calculate(robot.getActivo(), robot.getRobot());
			rnaInvXauUsd240.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_D1) {
			rnaInvNdx1440.calculate(robot.getActivo(), robot.getRobot());
			rnaInvXauUsd1440.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_W1) {
			rnaInvGdaxi10080.calculate(robot.getActivo(), robot.getRobot());
			rnaInvNdx10080.calculate(robot.getActivo(), robot.getRobot());
			rnaInvXauUsd10080.calculate(robot.getActivo(), robot.getRobot());
		}
		
	}

}
