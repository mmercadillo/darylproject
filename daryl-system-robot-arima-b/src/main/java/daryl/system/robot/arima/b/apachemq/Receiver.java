package daryl.system.robot.arima.b.apachemq;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.arima.b.predictor.base.ArimaPredictor;

@Component
public class Receiver {
	
	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

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
	public void receiveMessage(String robotJson) {
		
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		System.out.println("Solicitud recibida en el canal CHNL_ARIMA_B -> " + robot.getRobot());
		
		logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_ARIMA_B -> Robot -> " + robot.getRobot());
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaBGdaxi60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaBNdx60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaBXauUsd60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaBAudcad60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaBEurusd60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaBGdaxi240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaBNdx240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaBXauUsd240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaBAudcad240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaBEurusd240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaBGdaxi1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaBNdx1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaBXauUsd1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaBAudcad1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaBEurusd1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaBGdaxi10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaBNdx10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaBXauUsd10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaBAudcad10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaBEurusd10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
	}

}
