package daryl.system.robot.arima.d.apachemq;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.arima.d.predictor.ArimaDAudcad10080;
import daryl.system.robot.arima.d.predictor.ArimaDAudcad1440;
import daryl.system.robot.arima.d.predictor.ArimaDAudcad240;
import daryl.system.robot.arima.d.predictor.ArimaDAudcad60;
import daryl.system.robot.arima.d.predictor.ArimaDEurusd10080;
import daryl.system.robot.arima.d.predictor.ArimaDEurusd1440;
import daryl.system.robot.arima.d.predictor.ArimaDEurusd240;
import daryl.system.robot.arima.d.predictor.ArimaDEurusd60;
import daryl.system.robot.arima.d.predictor.ArimaDGdaxi10080;
import daryl.system.robot.arima.d.predictor.ArimaDGdaxi1440;
import daryl.system.robot.arima.d.predictor.ArimaDGdaxi240;
import daryl.system.robot.arima.d.predictor.ArimaDGdaxi60;
import daryl.system.robot.arima.d.predictor.ArimaDNdx10080;
import daryl.system.robot.arima.d.predictor.ArimaDNdx1440;
import daryl.system.robot.arima.d.predictor.ArimaDNdx240;
import daryl.system.robot.arima.d.predictor.ArimaDNdx60;
import daryl.system.robot.arima.d.predictor.ArimaDXauUsd10080;
import daryl.system.robot.arima.d.predictor.ArimaDXauUsd1440;
import daryl.system.robot.arima.d.predictor.ArimaDXauUsd240;
import daryl.system.robot.arima.d.predictor.ArimaDXauUsd60;
import daryl.system.robot.arima.d.predictor.base.ArimaPredictor;

@Component
public class Receiver {


	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;

<<<<<<< HEAD
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

=======
	
>>>>>>> branch 'main' of https://github.com/mmercadillo/darylproject.git
	@JmsListener(destination = "CHNL_ARIMA_D")
	public void receiveMessage(String robotJson) {
		
		Robot robot = new Gson().fromJson(robotJson, Robot.class);
		System.out.println("Solicitud recibida en el canal CHNL_ARIMA_D -> " + robot.getRobot() + " - " + new Date().toLocaleString());
		
		//logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_ARIMA_D -> Robot -> " + robot.getRobot());
		Timeframes timeframe = robot.getTimeframe();
		ArimaPredictor predictor = null;
		
		if(timeframe == Timeframes.PERIOD_H1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaDGdaxi60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaDNdx60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaDXauUsd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaDAudcad60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaDEurusd60.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_H4) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaDGdaxi240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaDNdx240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaDXauUsd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaDAudcad240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaDEurusd240.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_D1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaDGdaxi1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaDNdx1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaDXauUsd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaDAudcad1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaDEurusd1440.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}else if(timeframe == Timeframes.PERIOD_W1) {
			try{
				if(robot.getActivo() == Activo.GDAXI) {
					predictor = applicationContext.getBean(ArimaDGdaxi10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.NDX) {
					predictor = applicationContext.getBean(ArimaDNdx10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.XAUUSD) {
					predictor = applicationContext.getBean(ArimaDXauUsd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.AUDCAD) {
					predictor = applicationContext.getBean(ArimaDAudcad10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
			try{
				if(robot.getActivo() == Activo.EURUSD) {
					predictor = applicationContext.getBean(ArimaDEurusd10080.class);
					predictor.calculate(robot);
				}
			}catch (Exception e) {
				//logger.error(e.getMessage(), e);		
			}
		}
		
	}

}
