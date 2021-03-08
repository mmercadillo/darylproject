package daryl.system.robot.arima.b.apachemq;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.arima.b.predictor.base.ArimaPredictor;

@Component
public class Receiver {
	
	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	//B
	@Autowired
	@Qualifier(value = "arimaBXauusd60")
	ArimaPredictor arimaBXauUsd60;

	@Autowired
	@Qualifier(value = "arimaBXauusd240")
	ArimaPredictor arimaBXauUsd240;
	
	@Autowired
	@Qualifier(value = "arimaBXauusd1440")
	ArimaPredictor arimaBXauUsd1440;
	
	@Autowired
	@Qualifier(value = "arimaBXauusd10080")
	ArimaPredictor arimaBXauUsd10080;

	
	//B
	@Autowired
	@Qualifier(value = "arimaBNdx60")
	ArimaPredictor arimaBNdx60;
	
	@Autowired
	@Qualifier(value = "arimaBNdx240")
	ArimaPredictor arimaBNdx240;

	@Autowired
	@Qualifier(value = "arimaBNdx1440")
	ArimaPredictor arimaBNdx1440;
	
	@Autowired
	@Qualifier(value = "arimaBNdx10080")
	ArimaPredictor arimaBNdx10080;

	
	//B
	@Autowired
	@Qualifier(value = "arimaBGdaxi60")
	ArimaPredictor arimaBGdaxi60;

	@Autowired
	@Qualifier(value = "arimaBGdaxi240")
	ArimaPredictor arimaBGdaxi240;
	
	@Autowired
	@Qualifier(value = "arimaBGdaxi1440")
	ArimaPredictor arimaBGdaxi1440;
	
	@Autowired
	@Qualifier(value = "arimaBGdaxi10080")
	ArimaPredictor arimaBGdaxi10080;

	//B
	@Autowired
	@Qualifier(value = "arimaBAudcad60")
	ArimaPredictor arimaBAudcad60;

	@Autowired
	@Qualifier(value = "arimaBAudcad240")
	ArimaPredictor arimaBAudcad240;
	
	@Autowired
	@Qualifier(value = "arimaBAudcad1440")
	ArimaPredictor arimaBAudcad1440;
	
	@Autowired
	@Qualifier(value = "arimaBAudcad10080")
	ArimaPredictor arimaBAudcad10080;

	//B
	@Autowired
	@Qualifier(value = "arimaBEurusd60")
	ArimaPredictor arimaBEurusd60;

	@Autowired
	@Qualifier(value = "arimaBEurusd240")
	ArimaPredictor arimaBEurusd240;
	
	@Autowired
	@Qualifier(value = "arimaBEurusd1440")
	ArimaPredictor arimaBEurusd1440;
	
	@Autowired
	@Qualifier(value = "arimaBEurusd10080")
	ArimaPredictor arimaBEurusd10080;

	

	@JmsListener(destination = "CHNL_ARIMA_B")
	public void receiveMessage(Robot robot) {
		logger.info("\"MENSAJE RECIBIDO POR CANAL -> CHNL_ARIMA_A -> Robot -> " + robot.getRobot());
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			arimaBGdaxi60.calculate(robot.getActivo(), robot.getRobot());
			arimaBNdx60.calculate(robot.getActivo(), robot.getRobot());
			arimaBXauUsd60.calculate(robot.getActivo(), robot.getRobot());
			arimaBAudcad60.calculate(robot.getActivo(), robot.getRobot());
			arimaBEurusd60.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_H4) {
			arimaBGdaxi240.calculate(robot.getActivo(), robot.getRobot());
			arimaBNdx240.calculate(robot.getActivo(), robot.getRobot());
			arimaBXauUsd240.calculate(robot.getActivo(), robot.getRobot());
			arimaBAudcad240.calculate(robot.getActivo(), robot.getRobot());
			arimaBEurusd240.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_D1) {
			arimaBGdaxi1440.calculate(robot.getActivo(), robot.getRobot());
			arimaBNdx1440.calculate(robot.getActivo(), robot.getRobot());
			arimaBXauUsd1440.calculate(robot.getActivo(), robot.getRobot());
			arimaBAudcad1440.calculate(robot.getActivo(), robot.getRobot());
			arimaBEurusd1440.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_W1) {
			arimaBGdaxi10080.calculate(robot.getActivo(), robot.getRobot());
			arimaBNdx10080.calculate(robot.getActivo(), robot.getRobot());
			arimaBXauUsd10080.calculate(robot.getActivo(), robot.getRobot());
			arimaBAudcad10080.calculate(robot.getActivo(), robot.getRobot());
			arimaBEurusd10080.calculate(robot.getActivo(), robot.getRobot());
		}
		
	}

}
