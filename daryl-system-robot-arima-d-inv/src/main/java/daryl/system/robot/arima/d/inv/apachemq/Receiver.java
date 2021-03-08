package daryl.system.robot.arima.d.inv.apachemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.arima.d.inv.predictor.base.ArimaPredictor;

@Component
public class Receiver {

	@Autowired
	JmsListenerContainerFactory<?> factory;

	
	//C
	@Autowired
	@Qualifier(value = "arimaDInvXauusd60")
	ArimaPredictor arimaDInvXauUsd60;

	@Autowired
	@Qualifier(value = "arimaDInvXauusd240")
	ArimaPredictor arimaDInvXauUsd240;
	
	@Autowired
	@Qualifier(value = "arimaDInvXauusd1440")
	ArimaPredictor arimaDInvXauUsd1440;
	
	@Autowired
	@Qualifier(value = "arimaDInvXauusd10080")
	ArimaPredictor arimaDInvXauUsd10080;	
	
	//C
	@Autowired
	@Qualifier(value = "arimaDInvNdx60")
	ArimaPredictor arimaDInvNdx60;
	
	@Autowired
	@Qualifier(value = "arimaDInvNdx240")
	ArimaPredictor arimaDInvNdx240;

	@Autowired
	@Qualifier(value = "arimaDInvNdx1440")
	ArimaPredictor arimaDInvNdx1440;
	
	@Autowired
	@Qualifier(value = "arimaDInvNdx10080")
	ArimaPredictor arimaDInvNdx10080;
	
	//C
	@Autowired
	@Qualifier(value = "arimaDInvGdaxi60")
	ArimaPredictor arimaDInvGdaxi60;

	@Autowired
	@Qualifier(value = "arimaDInvGdaxi240")
	ArimaPredictor arimaDInvGdaxi240;
	
	@Autowired
	@Qualifier(value = "arimaDInvGdaxi1440")
	ArimaPredictor arimaDInvGdaxi1440;
	
	@Autowired
	@Qualifier(value = "arimaDInvGdaxi10080")
	ArimaPredictor arimaDInvGdaxi10080;	

	
	//C
	@Autowired
	@Qualifier(value = "arimaDInvAudcad60")
	ArimaPredictor arimaDInvAudcad60;

	@Autowired
	@Qualifier(value = "arimaDInvAudcad240")
	ArimaPredictor arimaDInvAudcad240;
	
	@Autowired
	@Qualifier(value = "arimaDInvAudcad1440")
	ArimaPredictor arimaDInvAudcad1440;
	
	@Autowired
	@Qualifier(value = "arimaDInvAudcad10080")
	ArimaPredictor arimaDInvAudcad10080;
	
	//C
	@Autowired
	@Qualifier(value = "arimaDInvEurusd60")
	ArimaPredictor arimaDInvEurusd60;

	@Autowired
	@Qualifier(value = "arimaDInvEurusd240")
	ArimaPredictor arimaDInvEurusd240;
	
	@Autowired
	@Qualifier(value = "arimaDInvEurusd1440")
	ArimaPredictor arimaDInvEurusd1440;
	
	@Autowired
	@Qualifier(value = "arimaDInvEurusd10080")
	ArimaPredictor arimaDInvEurusd10080;

	
	//C
	@Autowired
	@Qualifier(value = "arimaDInvWti60")
	ArimaPredictor arimaDInvWti60;

	@Autowired
	@Qualifier(value = "arimaDInvWti240")
	ArimaPredictor arimaDInvWti240;
	
	@Autowired
	@Qualifier(value = "arimaDInvWti1440")
	ArimaPredictor arimaDInvWti1440;

	
	
	@JmsListener(destination = "CHNL_ARIMA_D_INV")
	public void receiveMessage(Robot robot) {
		
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			arimaDInvGdaxi60.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvNdx60.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvXauUsd60.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvAudcad60.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvEurusd60.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_H4) {
			arimaDInvGdaxi240.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvNdx240.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvXauUsd240.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvAudcad240.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvEurusd240.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_D1) {
			arimaDInvGdaxi1440.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvNdx1440.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvXauUsd1440.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvAudcad1440.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvEurusd1440.calculate(robot.getActivo(), robot.getRobot());
		}else if(timeframe == Timeframes.PERIOD_W1) {
			arimaDInvGdaxi10080.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvNdx10080.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvXauUsd10080.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvAudcad10080.calculate(robot.getActivo(), robot.getRobot());
			arimaDInvEurusd10080.calculate(robot.getActivo(), robot.getRobot());
		}
		
	}

}
