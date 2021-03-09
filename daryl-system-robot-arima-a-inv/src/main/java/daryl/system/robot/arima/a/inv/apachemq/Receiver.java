package daryl.system.robot.arima.a.inv.apachemq;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.arima.a.inv.predictor.base.ArimaPredictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	@Qualifier(value = "arimaInvXauusd60")
	ArimaPredictor arimaInvXauUsd60;

	@Autowired
	@Qualifier(value = "arimaInvXauusd240")
	ArimaPredictor arimaInvXauUsd240;
	
	@Autowired
	@Qualifier(value = "arimaInvXauusd1440")
	ArimaPredictor arimaInvXauUsd1440;
	
	@Autowired
	@Qualifier(value = "arimaInvXauusd10080")
	ArimaPredictor arimaInvXauUsd10080;

	@Autowired
	@Qualifier(value = "arimaInvNdx60")
	ArimaPredictor arimaInvNdx60;
	
	@Autowired
	@Qualifier(value = "arimaInvNdx240")
	ArimaPredictor arimaInvNdx240;

	@Autowired
	@Qualifier(value = "arimaInvNdx1440")
	ArimaPredictor arimaInvNdx1440;
	
	@Autowired
	@Qualifier(value = "arimaInvNdx10080")
	ArimaPredictor arimaInvNdx10080;
	
	@Autowired
	@Qualifier(value = "arimaInvGdaxi60")
	ArimaPredictor arimaInvGdaxi60;

	@Autowired
	@Qualifier(value = "arimaInvGdaxi240")
	ArimaPredictor arimaInvGdaxi240;
	
	@Autowired
	@Qualifier(value = "arimaInvGdaxi1440")
	ArimaPredictor arimaInvGdaxi1440;
	
	@Autowired
	@Qualifier(value = "arimaInvGdaxi10080")
	ArimaPredictor arimaInvGdaxi10080;
	
	@Autowired
	@Qualifier(value = "arimaInvAudcad60")
	ArimaPredictor arimaInvAudcad60;

	@Autowired
	@Qualifier(value = "arimaInvAudcad240")
	ArimaPredictor arimaInvAudcad240;
	
	@Autowired
	@Qualifier(value = "arimaInvAudcad1440")
	ArimaPredictor arimaInvAudcad1440;
	
	@Autowired
	@Qualifier(value = "arimaInvAudcad10080")
	ArimaPredictor arimaInvAudcad10080;
	
	@Autowired
	@Qualifier(value = "arimaInvEurusd60")
	ArimaPredictor arimaInvEurusd60;

	@Autowired
	@Qualifier(value = "arimaInvEurusd240")
	ArimaPredictor arimaInvEurusd240;
	
	@Autowired
	@Qualifier(value = "arimaInvEurusd1440")
	ArimaPredictor arimaInvEurusd1440;
	
	@Autowired
	@Qualifier(value = "arimaInvEurusd10080")
	ArimaPredictor arimaInvEurusd10080;
	

	@JmsListener(destination = "CHNL_ARIMA_A_INV")
	public void receiveMessage(Robot robot) {
		logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_ARIMA_A_INV -> Robot -> " + robot);
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			arimaInvGdaxi60.calculate(robot);
			arimaInvNdx60.calculate(robot);
			arimaInvXauUsd60.calculate(robot);
			arimaInvAudcad60.calculate(robot);
			arimaInvEurusd60.calculate(robot);
		}else if(timeframe == Timeframes.PERIOD_H1) {
			arimaInvGdaxi240.calculate(robot);
			arimaInvNdx240.calculate(robot);
			arimaInvXauUsd240.calculate(robot);
			arimaInvAudcad240.calculate(robot);
			arimaInvEurusd240.calculate(robot);
		}else if(timeframe == Timeframes.PERIOD_H1) {
			arimaInvGdaxi1440.calculate(robot);
			arimaInvNdx1440.calculate(robot);
			arimaInvXauUsd1440.calculate(robot);
			arimaInvAudcad1440.calculate(robot);
			arimaInvEurusd1440.calculate(robot);
		}else if(timeframe == Timeframes.PERIOD_H1) {
			arimaInvGdaxi10080.calculate(robot);
			arimaInvNdx10080.calculate(robot);
			arimaInvXauUsd10080.calculate(robot);
			arimaInvAudcad10080.calculate(robot);
			arimaInvEurusd10080.calculate(robot);
		}
		
	}

}
