package daryl.system.backtest.robot.tester;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;

import daryl.arima.gen.ARIMA;
import daryl.system.backtest.robot.repository.IHistoricoOperacionesBacktestRepository;
import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Robot;
import daryl.system.model.backtest.HistoricoOperacionesBacktest;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ArimaAInvTester extends Tester implements Runnable{
	
	@Autowired
	Logger logger;
	@Autowired
	private IHistoricoOperacionesBacktestRepository operacionBacktestRepository;
	@Autowired
	ConfigData config;

	
	private Robot robot;
	private BarSeries datosParaTest;
	private BarSeries cierres;

	public ArimaAInvTester() {
	}
	
	public void init(Robot robot, BarSeries datosParaTest, int inicio) {

		this.robot = robot;
		this.datosParaTest = datosParaTest;
		this.cierres =  this.datosParaTest.getSubSeries(0, inicio);
		
		
		//Dejamos los datos excepto los quitados anteriormente
		this.datosParaTest = this.datosParaTest.getSubSeries(inicio, this.datosParaTest.getBarCount());
	}

	public  void run() {

		BarSeries serieParaCalculo = null;
		//Recorremos los datos 
		for (int i = 0; i < datosParaTest.getBarCount()-1; i++) {
			
			this.cierres.addBar(datosParaTest.getBar(i));
			try {
				serieParaCalculo = this.cierres.getSubSeries(this.cierres.getBarCount()-1000, this.cierres.getBarCount());
			}catch (Exception e) {
				serieParaCalculo = this.cierres;
			}
			
			try {
				
				HistoricoOperacionesBacktest opBt = new HistoricoOperacionesBacktest();
				opBt.setRobot(this.robot.getRobot());
				
				double[] datos = serieParaCalculo.getBarData().stream().mapToDouble(bar -> bar.getClosePrice().doubleValue()).toArray();
				ARIMA arima=new ARIMA(datos);
				
				int []model=arima.getARIMAmodel();

				double forecast = (double)arima.aftDeal(arima.predictValue(model[0],model[1]));
				//logger.info("Robot -> " + robot + " PREDICCIÓN -> " + forecast + " ANTERIOR -> " + cierres.get(cierres.size()-1));


				Double apertura = datosParaTest.getBar(i).getClosePrice().doubleValue() / robot.getActivo().getMultiplicador();
				Double cierre = datosParaTest.getBar(i+1).getClosePrice().doubleValue() / robot.getActivo().getMultiplicador(); 
				opBt.setApertura(apertura);
				opBt.setCierre(cierre);
				
				
				Long fechaHoraAperturaMillis = datosParaTest.getBar(i).getEndTime().toEpochSecond() * 1000;
				Long fechaHoraCierreMillis = datosParaTest.getBar(i+1).getEndTime().toEpochSecond() * 1000;

				opBt.setFapertura(fechaHoraAperturaMillis);
				opBt.setFcierre(fechaHoraCierreMillis);


				opBt.setFaperturaTxt(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date(fechaHoraAperturaMillis)));
				opBt.setFcierreTxt(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date(fechaHoraCierreMillis)));
				
				opBt.setProfit(0.0);
				if(forecast > cierres.getBar(cierres.getBarCount()-1).getClosePrice().doubleValue()) {

					opBt.setTipo(TipoOrden.SELL);
					opBt.setProfit(apertura - cierre );

										
				}else if(forecast < cierres.getBar(cierres.getBarCount()-1).getClosePrice().doubleValue()) {

					opBt.setTipo(TipoOrden.BUY);
					opBt.setProfit(cierre - apertura);
					
				}
				
				
				
				opBt.setSwap(0.0);
				opBt.setComision(0.0);

				//Guardamos la operación de backtest
				operacionBacktestRepository.save(opBt);
				System.out.println("Operación guardada: " + opBt.toString());

				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	
	}
	

}
