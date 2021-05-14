package daryl.system.backtest.robot.tester;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
	private List<Datos> datosParaTest;
	List<Double> cierres;

	public ArimaAInvTester() {
	}
	
	public void init(Robot robot, List<Datos> datosParaTest, int inicio) {

		this.robot = robot;
		this.datosParaTest = datosParaTest;
		this.cierres =  this.datosParaTest.subList(0, inicio).stream().map(d -> d.getCierre()).collect(Collectors.toList());
		//Dejamos los datos excepto los quitados anteriormente
		this.datosParaTest = this.datosParaTest.subList(inicio, this.datosParaTest.size());
	}

	public  void run() {


		//Recorremos los datos 
		for (int i = 0; i < datosParaTest.size()-1; i++) {
			
			cierres.add(datosParaTest.get(i).getCierre());
			
			try {
				
				HistoricoOperacionesBacktest opBt = new HistoricoOperacionesBacktest();
				opBt.setRobot(this.robot.getRobot());
				
				ARIMA arima=new ARIMA(cierres.stream().mapToDouble(Double::new).toArray());
				
				int []model=arima.getARIMAmodel();

				double forecast = (double)arima.aftDeal(arima.predictValue(model[0],model[1]));
				//logger.info("Robot -> " + robot + " PREDICCIÓN -> " + forecast + " ANTERIOR -> " + cierres.get(cierres.size()-1));

				Double apertura = datosParaTest.get(i).getCierre();
				Double cierre = datosParaTest.get(i+1).getCierre(); 
				opBt.setApertura(apertura);
				opBt.setCierre(cierre);
				
				String fechaHoraApertura = datosParaTest.get(i).getFecha() + " " + datosParaTest.get(i).getHora();
				String fechaHoraCierre = datosParaTest.get(i+1).getFecha() + " " + datosParaTest.get(i+1).getHora();
				opBt.setFaperturaTxt(fechaHoraApertura);
				opBt.setFcierreTxt(fechaHoraCierre);
				
				opBt.setFapertura(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").parse(fechaHoraApertura).getTime());
				opBt.setFcierre(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").parse(fechaHoraCierre).getTime());
				
				
				if(forecast > cierres.get(cierres.size()-1) ) {

					opBt.setTipo(TipoOrden.SELL);
					opBt.setProfit(apertura - cierre);

										
				}else if(forecast < cierres.get(cierres.size()-1)) {

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
