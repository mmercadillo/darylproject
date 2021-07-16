package daryl.system.robot.arima.a2.apachemq;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import daryl.system.comun.enums.Activo;
import daryl.system.model.Robot;
import daryl.system.robot.arima.a2.predictor.Arima2Audcad;
import daryl.system.robot.arima.a2.predictor.Arima2Eurusd;
import daryl.system.robot.arima.a2.predictor.Arima2Gdaxi;
import daryl.system.robot.arima.a2.predictor.Arima2Ndx;
import daryl.system.robot.arima.a2.predictor.Arima2XauUsd;
import daryl.system.robot.arima.a2.predictor.Arima2XtiUsd;
import daryl.system.robot.arima.a2.predictor.base.Arima2Predictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;
	
	@JmsListener(destination = "CHNL_ARIMA_A2")
	public void receiveMessage(String robotJson) {
		
		
		final Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot + " - " + new Date().toLocaleString());		
		
		Class activo = null;
		
		
		if(robot.getActivo() == Activo.GDAXI) {
			try{
				activo = Arima2Gdaxi.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				activo = Arima2Ndx.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{
				activo = Arima2XauUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				activo = Arima2Audcad.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XTIUSD) {
			try{
				activo = Arima2XtiUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{
				activo = Arima2Eurusd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		final Arima2Predictor predictor = (Arima2Predictor)applicationContext.getBean(activo);
		(new Thread() {
			
			public void run() {
				
				try {

					logger.info("PROCESO CALCULO LANZADO -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
					predictor.calculate(robot);
					logger.info("PROCESO CALCULO FINALIZADO -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}).start();
		
	}

	
}
