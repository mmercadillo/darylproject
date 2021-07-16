package daryl.system.robot.variance.b3.apachemq;

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
import daryl.system.robot.variance.b3.predictor.VarianceB3Audcad;
import daryl.system.robot.variance.b3.predictor.VarianceB3Eurusd;
import daryl.system.robot.variance.b3.predictor.VarianceB3Gdaxi;
import daryl.system.robot.variance.b3.predictor.VarianceB3Ndx;
import daryl.system.robot.variance.b3.predictor.VarianceB3XauUsd;
import daryl.system.robot.variance.b3.predictor.VarianceB3XtiUsd;
import daryl.system.robot.variance.b3.predictor.base.VarianceB3Predictor;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;

	@JmsListener(destination = "CHNL_VARIANCE_B3")
	public void receiveMessage(String robotJson) {
		
		final Robot robot = new Gson().fromJson(robotJson, Robot.class);
		logger.info("MENSAJE RECIBIDO POR CANAL -> " + robot.getCanal() + " -> Robot -> " + robot.getRobot() + " - " + new Date().toLocaleString());

		Class activo = null;
		
		
		if(robot.getActivo() == Activo.GDAXI) {
			try{
				activo = VarianceB3Gdaxi.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.NDX) {
			try{
				activo = VarianceB3Ndx.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XAUUSD) {
			try{
				activo = VarianceB3XauUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.AUDCAD) {
			try{
				activo = VarianceB3Audcad.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.XTIUSD) {
			try{
				activo = VarianceB3XtiUsd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}
		if(robot.getActivo() == Activo.EURUSD) {
			try{
				activo = VarianceB3Eurusd.class;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);		
			}
		}

		final VarianceB3Predictor predictor = (VarianceB3Predictor)applicationContext.getBean(activo);
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
