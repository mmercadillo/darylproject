package daryl.system.robot.rna.apachemq;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.rna.predictor.base.RnaPredictor;

@Component
public class Receiver {
	
	@Autowired
	Logger logger;
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
	
	//ndx
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
	///////////////////////////////////////
	
	//Gdaxi
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
	public void receiveMessage(Robot robot) {
		
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			rnaGdaxi60.calculate(robot.getActivo(), robot.getRobot());
			rnaNdx60.calculate(robot.getActivo(), robot.getRobot());
			rnaXauUsd60.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_H4) {
			rnaGdaxi240.calculate(robot.getActivo(), robot.getRobot());
			rnaNdx240.calculate(robot.getActivo(), robot.getRobot());
			rnaXauUsd240.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_D1) {
			rnaNdx1440.calculate(robot.getActivo(), robot.getRobot());
			rnaXauUsd1440.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_W1) {
			rnaGdaxi10080.calculate(robot.getActivo(), robot.getRobot());
			rnaNdx10080.calculate(robot.getActivo(), robot.getRobot());
			rnaXauUsd10080.calculate(robot.getActivo(), robot.getRobot());
		}
		
	}

}
