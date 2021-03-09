package daryl.system.robot.arima.d.inv.apachemq;

import org.slf4j.Logger;
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
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

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
			arimaDInvGdaxi60.calculate(robot);
			arimaDInvNdx60.calculate(robot);
			arimaDInvXauUsd60.calculate(robot);
			arimaDInvAudcad60.calculate(robot);
			arimaDInvEurusd60.calculate(robot);
		}else if(timeframe == Timeframes.PERIOD_H4) {
			arimaDInvGdaxi240.calculate(robot);
			arimaDInvNdx240.calculate(robot);
			arimaDInvXauUsd240.calculate(robot);
			arimaDInvAudcad240.calculate(robot);
			arimaDInvEurusd240.calculate(robot);
		}else if(timeframe == Timeframes.PERIOD_D1) {
			arimaDInvGdaxi1440.calculate(robot);
			arimaDInvNdx1440.calculate(robot);
			arimaDInvXauUsd1440.calculate(robot);
			arimaDInvAudcad1440.calculate(robot);
			arimaDInvEurusd1440.calculate(robot);
		}else if(timeframe == Timeframes.PERIOD_W1) {
			arimaDInvGdaxi10080.calculate(robot);
			arimaDInvNdx10080.calculate(robot);
			arimaDInvXauUsd10080.calculate(robot);
			arimaDInvAudcad10080.calculate(robot);
			arimaDInvEurusd10080.calculate(robot);
		}
		
	}

}
