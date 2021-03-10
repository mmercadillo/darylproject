package daryl.system.robot.arima.a.inv.apachemq;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Activo;
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
			if(robot.getActivo() == Activo.GDAXI) arimaInvGdaxi60.calculate(robot);
			if(robot.getActivo() == Activo.NDX) arimaInvNdx60.calculate(robot);
			if(robot.getActivo() == Activo.XAUUSD) arimaInvXauUsd60.calculate(robot);
			if(robot.getActivo() == Activo.AUDCAD) arimaInvAudcad60.calculate(robot);
			if(robot.getActivo() == Activo.EURUSD) arimaInvEurusd60.calculate(robot);
		}else if(timeframe == Timeframes.PERIOD_H1) {
			if(robot.getActivo() == Activo.GDAXI) arimaInvGdaxi240.calculate(robot);
			if(robot.getActivo() == Activo.NDX) arimaInvNdx240.calculate(robot);
			if(robot.getActivo() == Activo.XAUUSD) arimaInvXauUsd240.calculate(robot);
			if(robot.getActivo() == Activo.AUDCAD) arimaInvAudcad240.calculate(robot);
			if(robot.getActivo() == Activo.EURUSD) arimaInvEurusd240.calculate(robot);
		}else if(timeframe == Timeframes.PERIOD_H1) {
			if(robot.getActivo() == Activo.GDAXI) arimaInvGdaxi1440.calculate(robot);
			if(robot.getActivo() == Activo.NDX) arimaInvNdx1440.calculate(robot);
			if(robot.getActivo() == Activo.XAUUSD) arimaInvXauUsd1440.calculate(robot);
			if(robot.getActivo() == Activo.AUDCAD) arimaInvAudcad1440.calculate(robot);
			if(robot.getActivo() == Activo.EURUSD) arimaInvEurusd1440.calculate(robot);
		}else if(timeframe == Timeframes.PERIOD_H1) {
			if(robot.getActivo() == Activo.GDAXI) arimaInvGdaxi10080.calculate(robot);
			if(robot.getActivo() == Activo.NDX) arimaInvNdx10080.calculate(robot);
			if(robot.getActivo() == Activo.XAUUSD) arimaInvXauUsd10080.calculate(robot);
			if(robot.getActivo() == Activo.AUDCAD) arimaInvAudcad10080.calculate(robot);
			if(robot.getActivo() == Activo.EURUSD) arimaInvEurusd10080.calculate(robot);
		}
		
	}

}
