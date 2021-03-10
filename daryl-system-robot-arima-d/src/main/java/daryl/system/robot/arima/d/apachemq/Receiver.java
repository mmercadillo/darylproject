package daryl.system.robot.arima.d.apachemq;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.arima.d.predictor.base.ArimaPredictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	@Qualifier(value = "arimaDXauusd60")
	ArimaPredictor arimaDXauUsd60;

	@Autowired
	@Qualifier(value = "arimaDXauusd240")
	ArimaPredictor arimaDXauUsd240;
	
	@Autowired
	@Qualifier(value = "arimaDXauusd1440")
	ArimaPredictor arimaDXauUsd1440;
	
	@Autowired
	@Qualifier(value = "arimaDXauusd10080")
	ArimaPredictor arimaDXauUsd10080;	

	@Autowired
	@Qualifier(value = "arimaDNdx60")
	ArimaPredictor arimaDNdx60;
	
	@Autowired
	@Qualifier(value = "arimaDNdx240")
	ArimaPredictor arimaDNdx240;

	@Autowired
	@Qualifier(value = "arimaDNdx1440")
	ArimaPredictor arimaDNdx1440;
	
	@Autowired
	@Qualifier(value = "arimaDNdx10080")
	ArimaPredictor arimaDNdx10080;
	
	@Autowired
	@Qualifier(value = "arimaDGdaxi60")
	ArimaPredictor arimaDGdaxi60;

	@Autowired
	@Qualifier(value = "arimaDGdaxi240")
	ArimaPredictor arimaDGdaxi240;
	
	@Autowired
	@Qualifier(value = "arimaDGdaxi1440")
	ArimaPredictor arimaDGdaxi1440;
	
	@Autowired
	@Qualifier(value = "arimaDGdaxi10080")
	ArimaPredictor arimaDGdaxi10080;	

	@Autowired
	@Qualifier(value = "arimaDAudcad60")
	ArimaPredictor arimaDAudcad60;

	@Autowired
	@Qualifier(value = "arimaDAudcad240")
	ArimaPredictor arimaDAudcad240;
	
	@Autowired
	@Qualifier(value = "arimaDAudcad1440")
	ArimaPredictor arimaDAudcad1440;
	
	@Autowired
	@Qualifier(value = "arimaDAudcad10080")
	ArimaPredictor arimaDAudcad10080;
	
	@Autowired
	@Qualifier(value = "arimaDEurusd60")
	ArimaPredictor arimaDEurusd60;

	@Autowired
	@Qualifier(value = "arimaDEurusd240")
	ArimaPredictor arimaDEurusd240;
	
	@Autowired
	@Qualifier(value = "arimaDEurusd1440")
	ArimaPredictor arimaDEurusd1440;
	
	@Autowired
	@Qualifier(value = "arimaDEurusd10080")
	ArimaPredictor arimaDEurusd10080;

	@Autowired
	@Qualifier(value = "arimaDWti60")
	ArimaPredictor arimaDWti60;

	@Autowired
	@Qualifier(value = "arimaDWti240")
	ArimaPredictor arimaDWti240;
	
	@Autowired
	@Qualifier(value = "arimaDWti1440")
	ArimaPredictor arimaDWti1440;

	
	@JmsListener(destination = "CHNL_ARIMA_D")
	public void receiveMessage(Robot robot) {
		logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_ARIMA_D -> Robot -> " + robot.getRobot());
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaDGdaxi60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaDNdx60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaDXauUsd60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaDAudcad60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaDEurusd60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaDGdaxi240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaDNdx240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaDXauUsd240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaDAudcad240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaDEurusd240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaDGdaxi1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaDNdx1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaDXauUsd1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaDAudcad1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaDEurusd1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaDGdaxi10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaDNdx10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaDXauUsd10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaDAudcad10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaDEurusd10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
	}

}
