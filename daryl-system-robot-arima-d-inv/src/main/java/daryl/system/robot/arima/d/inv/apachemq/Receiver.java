package daryl.system.robot.arima.d.inv.apachemq;

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
	public void receiveMessage(String robotJson) {
		
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		System.out.println("Solicitud recibida en el canal CHNL_ARIMA_D_INV -> " + robot.getRobot());
		
		Timeframes timeframe = robot.getTimeframe();

		if(timeframe == Timeframes.PERIOD_H1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaDInvGdaxi60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaDInvNdx60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaDInvXauUsd60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaDInvAudcad60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaDInvEurusd60.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaDInvGdaxi240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaDInvNdx240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaDInvXauUsd240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaDInvAudcad240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaDInvEurusd240.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaDInvGdaxi1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaDInvNdx1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaDInvXauUsd1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaDInvAudcad1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaDInvEurusd1440.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaDInvGdaxi10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaDInvNdx10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaDInvXauUsd10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaDInvAudcad10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaDInvEurusd10080.calculate(robot);}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		
	}

}
