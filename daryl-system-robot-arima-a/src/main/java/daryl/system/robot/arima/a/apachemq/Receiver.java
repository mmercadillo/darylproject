package daryl.system.robot.arima.a.apachemq;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.arima.a.predictor.base.ArimaPredictor;

@Component
public class Receiver {

	
	@Autowired
	JmsListenerContainerFactory<?> factory;
	
	@Autowired
	@Qualifier(value = "arimaXauusd60")
	ArimaPredictor arimaXauUsd60;

	@Autowired
	@Qualifier(value = "arimaXauusd240")
	ArimaPredictor arimaXauUsd240;
	
	@Autowired
	@Qualifier(value = "arimaXauusd1440")
	ArimaPredictor arimaXauUsd1440;
	
	@Autowired
	@Qualifier(value = "arimaXauusd10080")
	ArimaPredictor arimaXauUsd10080;

	@Autowired
	@Qualifier(value = "arimaNdx60")
	ArimaPredictor arimaNdx60;
	
	@Autowired
	@Qualifier(value = "arimaNdx240")
	ArimaPredictor arimaNdx240;

	@Autowired
	@Qualifier(value = "arimaNdx1440")
	ArimaPredictor arimaNdx1440;
	
	@Autowired
	@Qualifier(value = "arimaNdx10080")
	ArimaPredictor arimaNdx10080;

	@Autowired
	@Qualifier(value = "arimaGdaxi60")
	ArimaPredictor arimaGdaxi60;

	@Autowired
	@Qualifier(value = "arimaGdaxi240")
	ArimaPredictor arimaGdaxi240;
	
	@Autowired
	@Qualifier(value = "arimaGdaxi1440")
	ArimaPredictor arimaGdaxi1440;
	
	@Autowired
	@Qualifier(value = "arimaGdaxi10080")
	ArimaPredictor arimaGdaxi10080;
	
	@Autowired
	@Qualifier(value = "arimaAudcad60")
	ArimaPredictor arimaAudcad60;

	@Autowired
	@Qualifier(value = "arimaAudcad240")
	ArimaPredictor arimaAudcad240;
	
	@Autowired
	@Qualifier(value = "arimaAudcad1440")
	ArimaPredictor arimaAudcad1440;
	
	@Autowired
	@Qualifier(value = "arimaAudcad10080")
	ArimaPredictor arimaAudcad10080;

	@Autowired
	@Qualifier(value = "arimaEurusd60")
	ArimaPredictor arimaEurusd60;

	@Autowired
	@Qualifier(value = "arimaEurusd240")
	ArimaPredictor arimaEurusd240;
	
	@Autowired
	@Qualifier(value = "arimaEurusd1440")
	ArimaPredictor arimaEurusd1440;
	
	@Autowired
	@Qualifier(value = "arimaEurusd10080")
	ArimaPredictor arimaEurusd10080;

	
	@JmsListener(destination = "CHNL_ARIMA_A")
	public void receiveMessage(String robotJson) {
		
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		System.out.println("Solicitud recibida en el canal CHNL_ARIMA_A -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		//logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_ARIMA_A -> Robot -> " + robot);		
		
		Timeframes timeframe = robot.getTimeframe();
		
		if(timeframe == Timeframes.PERIOD_H1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaGdaxi60.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaNdx60.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaXauUsd60.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaAudcad60.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaEurusd60.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaGdaxi240.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaNdx240.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaXauUsd240.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaAudcad240.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaEurusd240.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaGdaxi1440.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaNdx1440.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaXauUsd1440.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaAudcad1440.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaEurusd1440.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			try{if(robot.getActivo() == Activo.GDAXI) arimaGdaxi10080.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.NDX) arimaNdx10080.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.XAUUSD) arimaXauUsd10080.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.AUDCAD) arimaAudcad10080.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{if(robot.getActivo() == Activo.EURUSD) arimaEurusd10080.calculate(robot);}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}
		
	}

	
}
