package daryl.system.backtest.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import daryl.system.backtest.robot.repository.IHistoricoRepository;
import daryl.system.backtest.robot.repository.IRobotsRepository;
import daryl.system.backtest.robot.tester.ArimaAInvTester;
import daryl.system.backtest.robot.tester.ArimaATester;
import daryl.system.backtest.robot.tester.RnaTester;
import daryl.system.backtest.robot.tester.Tester;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;

@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EntityScan("daryl.system.model")
@EnableTransactionManagement
public class DarylSystemTesterRobotApplication {

	
	public static void main(String[] args) {
		
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DarylSystemTesterRobotApplication.class);
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	    
	    startBacktests(context);
	    
	}
	
	@Bean
    public Logger darylLogger() {
        return LoggerFactory.getLogger("daryl");
    }
	
	private static void startBacktests(ConfigurableApplicationContext context) {
		
		IHistoricoRepository historicoRepository = context.getBean(IHistoricoRepository.class);
		
		List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes.PERIOD_H1, Activo.XAUUSD);
		List<Datos> datosParaTest = toDatosList(historico);
		
		IRobotsRepository robotsRepository = context.getBean(IRobotsRepository.class);
		Robot robot = robotsRepository.findRobotByRobot("RNA_NDX_240");
		
		ExecutorService servicio = Executors.newFixedThreadPool(25);
		
		RnaTester rnaTester = context.getBean(RnaTester.class);
		rnaTester.init(robot, datosParaTest, 10);
		rnaTester.run();
		
		/*
		ArimaATester arimaATester = context.getBean(ArimaATester.class);
		arimaATester.init("ARIMA_XAUUSD_60", datosParaTest, 10);
		servicio.submit(arimaATester);
		
		ArimaAInvTester arimaAInvTester = context.getBean(ArimaAInvTester.class);
		arimaAInvTester.init("ARIMA_I_XAUUSD_60", datosParaTest, 10);
		servicio.submit(arimaAInvTester);
		*/
    	servicio.shutdown();
		
	}

	private static List<Datos> toDatosList(List<Historico> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (Historico hist : historico) {
			
			Datos dato = Datos.builder().fecha(hist.getFecha())
										.hora(hist.getHora())
										.apertura(hist.getApertura())
										.maximo(hist.getMaximo())
										.minimo(hist.getMinimo())
										.cierre(hist.getCierre())
										.volumen(hist.getVolumen())
										.build();
			datos.add(dato);
			
		}
		
		return datos;
		
		
	}
	
}
