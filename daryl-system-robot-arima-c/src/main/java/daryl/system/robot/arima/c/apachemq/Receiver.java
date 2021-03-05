package daryl.system.robot.arima.c.apachemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.arima.c.predictor.base.ArimaPredictor;

@Component
public class Receiver {

	@Autowired
	JmsListenerContainerFactory<?> factory;

	
	//C
	@Autowired
	@Qualifier(value = "arimaCXauusd60")
	ArimaPredictor arimaCXauUsd60;

	@Autowired
	@Qualifier(value = "arimaCXauusd240")
	ArimaPredictor arimaCXauUsd240;
	
	@Autowired
	@Qualifier(value = "arimaCXauusd1440")
	ArimaPredictor arimaCXauUsd1440;
	
	@Autowired
	@Qualifier(value = "arimaCXauusd10080")
	ArimaPredictor arimaCXauUsd10080;	
	
	//C
	@Autowired
	@Qualifier(value = "arimaCNdx60")
	ArimaPredictor arimaCNdx60;
	
	@Autowired
	@Qualifier(value = "arimaCNdx240")
	ArimaPredictor arimaCNdx240;

	@Autowired
	@Qualifier(value = "arimaCNdx1440")
	ArimaPredictor arimaCNdx1440;
	
	@Autowired
	@Qualifier(value = "arimaCNdx10080")
	ArimaPredictor arimaCNdx10080;
	
	//C
	@Autowired
	@Qualifier(value = "arimaCGdaxi60")
	ArimaPredictor arimaCGdaxi60;

	@Autowired
	@Qualifier(value = "arimaCGdaxi240")
	ArimaPredictor arimaCGdaxi240;
	
	@Autowired
	@Qualifier(value = "arimaCGdaxi1440")
	ArimaPredictor arimaCGdaxi1440;
	
	@Autowired
	@Qualifier(value = "arimaCGdaxi10080")
	ArimaPredictor arimaCGdaxi10080;	

	
	//C
	@Autowired
	@Qualifier(value = "arimaCAudcad60")
	ArimaPredictor arimaCAudcad60;

	@Autowired
	@Qualifier(value = "arimaCAudcad240")
	ArimaPredictor arimaCAudcad240;
	
	@Autowired
	@Qualifier(value = "arimaCAudcad1440")
	ArimaPredictor arimaCAudcad1440;
	
	@Autowired
	@Qualifier(value = "arimaCAudcad10080")
	ArimaPredictor arimaCAudcad10080;
	
	//C
	@Autowired
	@Qualifier(value = "arimaCEurusd60")
	ArimaPredictor arimaCEurusd60;

	@Autowired
	@Qualifier(value = "arimaCEurusd240")
	ArimaPredictor arimaCEurusd240;
	
	@Autowired
	@Qualifier(value = "arimaCEurusd1440")
	ArimaPredictor arimaCEurusd1440;
	
	@Autowired
	@Qualifier(value = "arimaCEurusd10080")
	ArimaPredictor arimaCEurusd10080;

	
	//C
	@Autowired
	@Qualifier(value = "arimaCWti60")
	ArimaPredictor arimaCWti60;

	@Autowired
	@Qualifier(value = "arimaCWti240")
	ArimaPredictor arimaCWti240;
	
	@Autowired
	@Qualifier(value = "arimaCWti1440")
	ArimaPredictor arimaCWti1440;

	
	@JmsListener(destination = "CHNL_ARIMA_C")
	public void receiveMessageGdaxi(Robot robot) {
		
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			arimaCGdaxi60.calculate(robot.getActivo(), robot.getRobot());
			arimaCNdx60.calculate(robot.getActivo(), robot.getRobot());
			arimaCXauUsd60.calculate(robot.getActivo(), robot.getRobot());
			arimaCAudcad60.calculate(robot.getActivo(), robot.getRobot());
			arimaCEurusd60.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_H4) {
			arimaCGdaxi240.calculate(robot.getActivo(), robot.getRobot());
			arimaCNdx240.calculate(robot.getActivo(), robot.getRobot());
			arimaCXauUsd240.calculate(robot.getActivo(), robot.getRobot());
			arimaCAudcad240.calculate(robot.getActivo(), robot.getRobot());
			arimaCEurusd240.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_D1) {
			arimaCGdaxi1440.calculate(robot.getActivo(), robot.getRobot());
			arimaCNdx1440.calculate(robot.getActivo(), robot.getRobot());
			arimaCXauUsd1440.calculate(robot.getActivo(), robot.getRobot());
			arimaCAudcad1440.calculate(robot.getActivo(), robot.getRobot());
			arimaCEurusd1440.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_W1) {
			arimaCGdaxi10080.calculate(robot.getActivo(), robot.getRobot());
			arimaCNdx10080.calculate(robot.getActivo(), robot.getRobot());
			arimaCXauUsd10080.calculate(robot.getActivo(), robot.getRobot());
			arimaCAudcad10080.calculate(robot.getActivo(), robot.getRobot());
			arimaCEurusd10080.calculate(robot.getActivo(), robot.getRobot());
		}
		
	}

	
}
