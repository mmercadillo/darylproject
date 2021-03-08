package daryl.system.robot.arima.c.inv.apachemq;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.arima.c.inv.predictor.base.ArimaPredictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	
	//C
	@Autowired
	@Qualifier(value = "arimaCInvXauusd60")
	ArimaPredictor arimaCInvXauUsd60;

	@Autowired
	@Qualifier(value = "arimaCInvXauusd240")
	ArimaPredictor arimaCInvXauUsd240;
	
	@Autowired
	@Qualifier(value = "arimaCInvXauusd1440")
	ArimaPredictor arimaCInvXauUsd1440;
	
	@Autowired
	@Qualifier(value = "arimaCInvXauusd10080")
	ArimaPredictor arimaCInvXauUsd10080;	
	
	//C
	@Autowired
	@Qualifier(value = "arimaCInvNdx60")
	ArimaPredictor arimaCInvNdx60;
	
	@Autowired
	@Qualifier(value = "arimaCInvNdx240")
	ArimaPredictor arimaCInvNdx240;

	@Autowired
	@Qualifier(value = "arimaCInvNdx1440")
	ArimaPredictor arimaCInvNdx1440;
	
	@Autowired
	@Qualifier(value = "arimaCInvNdx10080")
	ArimaPredictor arimaCInvNdx10080;
	
	//C
	@Autowired
	@Qualifier(value = "arimaCInvGdaxi60")
	ArimaPredictor arimaCInvGdaxi60;

	@Autowired
	@Qualifier(value = "arimaCInvGdaxi240")
	ArimaPredictor arimaCInvGdaxi240;
	
	@Autowired
	@Qualifier(value = "arimaCInvGdaxi1440")
	ArimaPredictor arimaCInvGdaxi1440;
	
	@Autowired
	@Qualifier(value = "arimaCInvGdaxi10080")
	ArimaPredictor arimaCInvGdaxi10080;	

	
	//C
	@Autowired
	@Qualifier(value = "arimaCInvAudcad60")
	ArimaPredictor arimaCInvAudcad60;

	@Autowired
	@Qualifier(value = "arimaCInvAudcad240")
	ArimaPredictor arimaCInvAudcad240;
	
	@Autowired
	@Qualifier(value = "arimaCInvAudcad1440")
	ArimaPredictor arimaCInvAudcad1440;
	
	@Autowired
	@Qualifier(value = "arimaCInvAudcad10080")
	ArimaPredictor arimaCInvAudcad10080;
	
	//C
	@Autowired
	@Qualifier(value = "arimaCInvEurusd60")
	ArimaPredictor arimaCInvEurusd60;

	@Autowired
	@Qualifier(value = "arimaCInvEurusd240")
	ArimaPredictor arimaCInvEurusd240;
	
	@Autowired
	@Qualifier(value = "arimaCInvEurusd1440")
	ArimaPredictor arimaCInvEurusd1440;
	
	@Autowired
	@Qualifier(value = "arimaCInvEurusd10080")
	ArimaPredictor arimaCInvEurusd10080;

	
	//C
	@Autowired
	@Qualifier(value = "arimaCInvWti60")
	ArimaPredictor arimaCInvWti60;

	@Autowired
	@Qualifier(value = "arimaCInvWti240")
	ArimaPredictor arimaCInvWti240;
	
	@Autowired
	@Qualifier(value = "arimaCInvWti1440")
	ArimaPredictor arimaCInvWti1440;

	
	
	@JmsListener(destination = "CHNL_ARIMA_C_INV")
	public void receiveMessage(Robot robot) {
		logger.info("\"MENSAJE RECIBIDO POR CANAL -> CHNL_ARIMA_A -> Robot -> " + robot.getRobot());
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			arimaCInvGdaxi60.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvNdx60.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvXauUsd60.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvAudcad60.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvEurusd60.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_H4) {
			arimaCInvGdaxi240.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvNdx240.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvXauUsd240.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvAudcad240.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvEurusd240.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_D1) {
			arimaCInvGdaxi1440.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvNdx1440.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvXauUsd1440.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvAudcad1440.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvEurusd1440.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_W1) {
			arimaCInvGdaxi10080.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvNdx10080.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvXauUsd10080.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvAudcad10080.calculate(robot.getActivo(), robot.getRobot());
			arimaCInvEurusd10080.calculate(robot.getActivo(), robot.getRobot());
		}
		
	}

	
}
