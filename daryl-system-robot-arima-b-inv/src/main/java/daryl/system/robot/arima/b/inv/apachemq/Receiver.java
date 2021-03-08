package daryl.system.robot.arima.b.inv.apachemq;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.arima.b.inv.predictor.base.ArimaPredictor;

@Component
public class Receiver {
	
	@Autowired
	Logger logger;

	@Autowired
	JmsListenerContainerFactory<?> factory;

	
	//B
	@Autowired
	@Qualifier(value = "arimaBInvXauusd60")
	ArimaPredictor arimaBInvXauUsd60;

	@Autowired
	@Qualifier(value = "arimaBInvXauusd240")
	ArimaPredictor arimaBInvXauUsd240;
	
	@Autowired
	@Qualifier(value = "arimaBInvXauusd1440")
	ArimaPredictor arimaBInvXauUsd1440;
	
	@Autowired
	@Qualifier(value = "arimaBInvXauusd10080")
	ArimaPredictor arimaBInvXauUsd10080;

	
	//B
	@Autowired
	@Qualifier(value = "arimaBInvNdx60")
	ArimaPredictor arimaBInvNdx60;
	
	@Autowired
	@Qualifier(value = "arimaBInvNdx240")
	ArimaPredictor arimaBInvNdx240;

	@Autowired
	@Qualifier(value = "arimaBInvNdx1440")
	ArimaPredictor arimaBInvNdx1440;
	
	@Autowired
	@Qualifier(value = "arimaBInvNdx10080")
	ArimaPredictor arimaBInvNdx10080;

	
	//B
	@Autowired
	@Qualifier(value = "arimaBInvGdaxi60")
	ArimaPredictor arimaBInvGdaxi60;

	@Autowired
	@Qualifier(value = "arimaBInvGdaxi240")
	ArimaPredictor arimaBInvGdaxi240;
	
	@Autowired
	@Qualifier(value = "arimaBInvGdaxi1440")
	ArimaPredictor arimaBInvGdaxi1440;
	
	@Autowired
	@Qualifier(value = "arimaBInvGdaxi10080")
	ArimaPredictor arimaBInvGdaxi10080;

	//B
	@Autowired
	@Qualifier(value = "arimaBInvAudcad60")
	ArimaPredictor arimaBInvAudcad60;

	@Autowired
	@Qualifier(value = "arimaBInvAudcad240")
	ArimaPredictor arimaBInvAudcad240;
	
	@Autowired
	@Qualifier(value = "arimaBInvAudcad1440")
	ArimaPredictor arimaBInvAudcad1440;
	
	@Autowired
	@Qualifier(value = "arimaBInvAudcad10080")
	ArimaPredictor arimaBInvAudcad10080;

	//B
	@Autowired
	@Qualifier(value = "arimaBInvEurusd60")
	ArimaPredictor arimaBInvEurusd60;

	@Autowired
	@Qualifier(value = "arimaBInvEurusd240")
	ArimaPredictor arimaBInvEurusd240;
	
	@Autowired
	@Qualifier(value = "arimaBInvEurusd1440")
	ArimaPredictor arimaBInvEurusd1440;
	
	@Autowired
	@Qualifier(value = "arimaBInvEurusd10080")
	ArimaPredictor arimaBInvEurusd10080;

	

	
	@JmsListener(destination = "CHNL_ARIMA_B_INV")
	public void receiveMessage(Robot robot) {
		logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_ARIMA_B_INV -> Robot -> " + robot.getRobot());
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			arimaBInvGdaxi60.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvNdx60.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvXauUsd60.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvAudcad60.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvEurusd60.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_H4) {
			arimaBInvGdaxi240.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvNdx240.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvXauUsd240.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvAudcad240.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvEurusd240.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_D1) {
			arimaBInvGdaxi1440.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvNdx1440.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvXauUsd1440.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvAudcad1440.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvEurusd1440.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_W1) {
			arimaBInvGdaxi10080.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvNdx10080.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvXauUsd10080.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvAudcad10080.calculate(robot.getActivo(), robot.getRobot());
			arimaBInvEurusd10080.calculate(robot.getActivo(), robot.getRobot());
		}
		
	}
	
}
