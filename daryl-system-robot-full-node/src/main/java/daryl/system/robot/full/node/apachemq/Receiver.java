package daryl.system.robot.full.node.apachemq;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import daryl.system.comun.enums.TipoRobot;
import daryl.system.model.Robot;
import daryl.system.robot.full.node.predictor.arima.Arima2Forecaster;
import daryl.system.robot.full.node.predictor.arima.Arima3Forecaster;
import daryl.system.robot.full.node.predictor.arima.ArimaB2Forecaster;
import daryl.system.robot.full.node.predictor.arima.ArimaB3Forecaster;
import daryl.system.robot.full.node.predictor.arima.ArimaBForecaster;
import daryl.system.robot.full.node.predictor.arima.ArimaC2Forecaster;
import daryl.system.robot.full.node.predictor.arima.ArimaC3Forecaster;
import daryl.system.robot.full.node.predictor.arima.ArimaCForecaster;
import daryl.system.robot.full.node.predictor.arima.ArimaDForecaster;
import daryl.system.robot.full.node.predictor.arima.ArimaForecaster;
import daryl.system.robot.full.node.predictor.arima.RnaForecaster;
import daryl.system.robot.full.node.predictor.arima.Variance2Forecaster;
import daryl.system.robot.full.node.predictor.arima.Variance3Forecaster;
import daryl.system.robot.full.node.predictor.arima.VarianceB2Forecaster;
import daryl.system.robot.full.node.predictor.arima.VarianceB3Forecaster;
import daryl.system.robot.full.node.predictor.arima.VarianceBForecaster;
import daryl.system.robot.full.node.predictor.arima.VarianceForecaster;
import daryl.system.robot.full.node.predictor.base.Forecaster;

@Component
public class Receiver {

	@Autowired
	Logger logger;
	
	@Autowired
	JmsListenerContainerFactory<?> factory;

	@Autowired
	private ApplicationContext applicationContext;
	
	@JmsListener(destination = "CHNL_FULL_NODE")
	public void receiveMessage(String listaRobotsJson) {
		
		
		List<Robot> robots = Arrays.asList(new Gson().fromJson(listaRobotsJson, Robot[].class));
		
		logger.info("MENSAJE RECIBIDO POR CANAL -> CHNL_FULL_NODE " + new Date().toLocaleString());		
		
		Forecaster predictor = null;
		
		for(Robot robot : robots) {
			logger.info("CALCULANDO PREDICCIÓN PARA EL ROBOT -> " + robot.getRobot());
			if(robot.getTipoRobot() == TipoRobot.ARIMA_A || robot.getTipoRobot() == TipoRobot.ARIMA_A_I) {
				try{
					predictor = applicationContext.getBean(ArimaForecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.ARIMA_A2 || robot.getTipoRobot() == TipoRobot.ARIMA_A2_I) {
				try{
					predictor = applicationContext.getBean(Arima2Forecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.ARIMA_A3 || robot.getTipoRobot() == TipoRobot.ARIMA_A3_I) {
				try{
					predictor = applicationContext.getBean(Arima3Forecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.ARIMA_B || robot.getTipoRobot() == TipoRobot.ARIMA_B_I) {
				try{
					predictor = applicationContext.getBean(ArimaBForecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.ARIMA_B2 || robot.getTipoRobot() == TipoRobot.ARIMA_B2_I) {
				try{
					predictor = applicationContext.getBean(ArimaB2Forecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.ARIMA_B3 || robot.getTipoRobot() == TipoRobot.ARIMA_B3_I) {
				try{
					predictor = applicationContext.getBean(ArimaB3Forecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.ARIMA_C || robot.getTipoRobot() == TipoRobot.ARIMA_C_I) {
				try{
					predictor = applicationContext.getBean(ArimaCForecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.ARIMA_C2 || robot.getTipoRobot() == TipoRobot.ARIMA_C2_I) {
				try{
					predictor = applicationContext.getBean(ArimaC2Forecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.ARIMA_C3 || robot.getTipoRobot() == TipoRobot.ARIMA_C3_I) {
				try{
					predictor = applicationContext.getBean(ArimaC3Forecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.ARIMA_D || robot.getTipoRobot() == TipoRobot.ARIMA_D_I) {
				try{
					predictor = applicationContext.getBean(ArimaDForecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.RNA || robot.getTipoRobot() == TipoRobot.RNA_I) {
				
				try{
					predictor = applicationContext.getBean(RnaForecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}	
			}
			if(robot.getTipoRobot() == TipoRobot.VARIANCE || robot.getTipoRobot() == TipoRobot.VARIANCE_I) {
				try{
					predictor = applicationContext.getBean(VarianceForecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.VARIANCE_2 || robot.getTipoRobot() == TipoRobot.VARIANCE_2_I) {
				try{
					predictor = applicationContext.getBean(Variance2Forecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.VARIANCE_3 || robot.getTipoRobot() == TipoRobot.VARIANCE_3_I) {
				try{
					predictor = applicationContext.getBean(Variance3Forecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.VARIANCE_B || robot.getTipoRobot() == TipoRobot.VARIANCE_B_I) {
				try{
					predictor = applicationContext.getBean(VarianceBForecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.VARIANCE_B || robot.getTipoRobot() == TipoRobot.VARIANCE_B_I) {
				try{
					predictor = applicationContext.getBean(VarianceBForecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.VARIANCE_B2 || robot.getTipoRobot() == TipoRobot.VARIANCE_B2_I) {
				try{
					predictor = applicationContext.getBean(VarianceB2Forecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			if(robot.getTipoRobot() == TipoRobot.VARIANCE_B3 || robot.getTipoRobot() == TipoRobot.VARIANCE_B3_I) {
				try{
					predictor = applicationContext.getBean(VarianceB3Forecaster.class);
					predictor.calculate(robot);
				}catch (Exception e) {
					logger.error(e.getMessage(), e);		
				}
			}
			logger.info("CALCULADA PREDICCIÓN PARA EL ROBOT -> " + robot.getRobot());
			
		}
		
	}

}
