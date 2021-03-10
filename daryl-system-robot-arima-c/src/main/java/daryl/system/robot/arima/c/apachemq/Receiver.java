package daryl.system.robot.arima.c.apachemq;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.arima.c.predictor.base.ArimaPredictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

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
	public void receiveMessage(Robot robot) {
		logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_ARIMA_C -> Robot -> " + robot.getRobot());
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaCGdaxi60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaCNdx60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaCXauUsd60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaCAudcad60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaCEurusd60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaCGdaxi240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaCNdx240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaCXauUsd240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaCAudcad240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaCEurusd240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaCGdaxi1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaCNdx1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaCXauUsd1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaCAudcad1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaCEurusd1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaCGdaxi10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaCNdx10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaCXauUsd10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaCAudcad10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaCEurusd10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
	}

	
}
