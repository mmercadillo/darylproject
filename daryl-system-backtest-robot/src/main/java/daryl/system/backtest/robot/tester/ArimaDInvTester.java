package daryl.system.backtest.robot.tester;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.espy.arima.ArimaForecaster;
import org.espy.arima.ArimaProcess;
import org.espy.arima.DefaultArimaForecaster;
import org.espy.arima.DefaultArimaProcess;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;

import daryl.system.backtest.robot.repository.IArimaConfigRepository;
import daryl.system.backtest.robot.repository.IHistoricoOperacionesBacktestRepository;
import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.ArimaConfig;
import daryl.system.model.Robot;
import daryl.system.model.backtest.HistoricoOperacionesBacktest;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ArimaDInvTester extends Tester implements Runnable{
	
	@Autowired
	Logger logger;
	@Autowired
	private IHistoricoOperacionesBacktestRepository operacionBacktestRepository;
	@Autowired
	IArimaConfigRepository arimaConfigRepository;
	@Autowired
	ConfigData config;


	private Robot robot;
	private BarSeries datosParaTest;
	private BarSeries cierres;

	public ArimaDInvTester() {
	}
	
	public void init(Robot robot, BarSeries datosParaTest, int inicio) {

		this.robot = robot;
		this.datosParaTest = datosParaTest;
		this.cierres =  this.datosParaTest.getSubSeries(0, inicio);
		
		
		//Dejamos los datos excepto los quitados anteriormente
		this.datosParaTest = this.datosParaTest.getSubSeries(inicio, this.datosParaTest.getBarCount());
	}
	

	private Double getPrediccionAnterior(BarSeries cierresAnt, DefaultArimaProcess arimaProcess, ArimaConfig arimaConfig) {
		
		
    	//List<Double> aux = cierresAnt;
    	//if(cierresAnt.size() > arimaConfig.getInicio()) {
    	//	aux = cierresAnt.subList((cierresAnt.size()-arimaConfig.getInicio()), cierresAnt.size());
    	//}

		//double[] observations = new double[aux.size()];
		//for(int i = 0; i < aux.size(); i++) {
		//	observations[i] = aux.get(i).doubleValue();
		//}
		
		BarSeries aux = cierres;
    	if(cierres.getBarCount() > arimaConfig.getInicio()) {
    		aux = cierres.getSubSeries((cierres.getBarCount()-arimaConfig.getInicio()), cierres.getBarCount());
    	}

    	double[] observations = aux.getBarData().stream().mapToDouble(bar -> bar.getClosePrice().doubleValue()).toArray();
		
		ArimaForecaster arimaForecaster = null;
		arimaForecaster = new DefaultArimaForecaster(arimaProcess, observations);
		
		double prediccionAnterior = arimaForecaster.next();	
		
		//logger.info("PREDICCIÓN ANTERIOR PARA EL ROBOT : {}", prediccionAnterior);
		return prediccionAnterior;
	}


	
	protected ArimaProcess getArimaProcess(ArimaConfig arimaConfig) {

		
		double[] coefficentsAr = null;
		try {
			
			String arTxt = arimaConfig.getArCoefficients();
			arTxt = arTxt.substring(1, arTxt.length()-1);
			
			String[] optionsAr = arTxt.trim().split(",");
			coefficentsAr = new double[optionsAr.length];
			for(int j = 0; j < optionsAr.length; j++) {
				coefficentsAr[j] = Double.parseDouble(optionsAr[j]);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		double[] coefficentsMa = null;
		try {
			
			String maTxt = arimaConfig.getMaCoefficients();
			maTxt = maTxt.substring(1, maTxt.length()-1);
			
			String[] optionsMa = maTxt.trim().split(",");
			coefficentsMa = new double[optionsMa.length];
			for(int j = 0; j < optionsMa.length; j++) {
				coefficentsMa[j] = Double.parseDouble(optionsMa[j]);
			}				
		}catch (Exception e) {
			e.printStackTrace();
		}

		
    	DefaultArimaProcess arimaProcess = new DefaultArimaProcess();
        //if(coefficentsMa != null) arimaProcess.setMaCoefficients(coefficentsMa);
        if(coefficentsAr != null) arimaProcess.setArCoefficients(coefficentsAr);
        arimaProcess.setIntegrationOrder(arimaConfig.getIntegrationOrder());
        arimaProcess.setStd(arimaConfig.getStandarDeviation());
        arimaProcess.setShockExpectation(arimaConfig.getShockExpectation());
        arimaProcess.setConstant(arimaConfig.getConstant());
        arimaProcess.setShockVariation(arimaConfig.getShockVariation());
        
        return arimaProcess;
		
		
	}


	public  void run() {


		//Recorremos los datos 
		for (int i = 0; i < datosParaTest.getBarCount()-1; i++) {
			
			this.cierres.addBar(datosParaTest.getBar(i));
			
			try {
				
				HistoricoOperacionesBacktest opBt = new HistoricoOperacionesBacktest();
				opBt.setRobot(this.robot.getRobot());
				
				
				
				ArimaConfig arimaConfig = arimaConfigRepository.findArimaConfigByRobot(robot.getArimaConfig());
				if(arimaConfig != null) {
					DefaultArimaProcess arimaProcess = (DefaultArimaProcess)getArimaProcess(arimaConfig);
					
					
					Double prediccionAnterior = getPrediccionAnterior(cierres.getSubSeries(0, cierres.getBarCount()-1), arimaProcess, arimaConfig);
		
					BarSeries aux = cierres;
			    	if(cierres.getBarCount() > arimaConfig.getInicio()) {
			    		aux = cierres.getSubSeries((cierres.getBarCount()-arimaConfig.getInicio()), cierres.getBarCount());
			    	}
					
			    	//List<Double> aux = cierres;
			    	//if(cierres.size() > arimaConfig.getInicio()) {
			    	//	aux = cierres.subList((cierres.size()-arimaConfig.getInicio()), cierres.size());
			    	//}
			    	
			    	//double[] observations = new double[aux.size()];
			    	//for(int j = 0; j < aux.size(); j++) {
			    	//	observations[j] = aux.get(j).doubleValue();
			    	//}
		
			    	double[] observations = aux.getBarData().stream().mapToDouble(bar -> bar.getClosePrice().doubleValue()).toArray();
			    	ArimaForecaster arimaForecaster = null;
		        	try {
		        		arimaForecaster = new DefaultArimaForecaster(arimaProcess, observations);	        	
				        double forecast = arimaForecaster.next();	
				        //logger.info("Robot -> " + this.robot.getRobot() + " PREDICCIÓN -> " + forecast + " ANTERIOR -> " + datos.get(datos.size()-1));
				        
						
						Double apertura = datosParaTest.getBar(i).getClosePrice().doubleValue();
						Double cierre = datosParaTest.getBar(i+1).getClosePrice().doubleValue(); 
						opBt.setApertura(apertura);
						opBt.setCierre(cierre);
						
						Long fechaHoraAperturaMillis = datosParaTest.getBar(i).getEndTime().toEpochSecond() * 1000;
						Long fechaHoraCierreMillis = datosParaTest.getBar(i+1).getEndTime().toEpochSecond() * 1000;

						opBt.setFapertura(fechaHoraAperturaMillis);
						opBt.setFcierre(fechaHoraCierreMillis);
				        
						opBt.setProfit(0.0);
						if(forecast > prediccionAnterior) {
		    	        	opBt.setTipo(TipoOrden.SELL);
							opBt.setProfit(apertura - cierre);
		    	        }
		    	        if(forecast < prediccionAnterior) {
							opBt.setTipo(TipoOrden.BUY);
							opBt.setProfit(cierre - apertura);

		    	        }
		
						opBt.setSwap(0.0);
						opBt.setComision(0.0);

						//Guardamos la operación de backtest
						operacionBacktestRepository.save(opBt);
						System.out.println("Operación guardada: " + opBt.toString());
				        
		        	}catch (Exception e) {
		        		//logger.error("No se ha podido calcular la prediccion para el robot: {}", this.robot.getRobot(), e);
		        	}
				}else {
					//logger.info("No existe config para el robot: {}", this.robot.getRobot());
				}

			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	
	}
	

}
