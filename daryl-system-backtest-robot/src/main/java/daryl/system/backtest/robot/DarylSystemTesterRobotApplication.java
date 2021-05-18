package daryl.system.backtest.robot;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

import daryl.system.backtest.robot.repository.IHistoricoRepository;
import daryl.system.backtest.robot.repository.IRobotsRepository;
import daryl.system.backtest.robot.resumen.ControlHistoricoOperacionesBacktest;
import daryl.system.backtest.robot.tester.ArimaAInvTester;
import daryl.system.backtest.robot.tester.ArimaATester;
import daryl.system.backtest.robot.tester.ArimaBInvTester;
import daryl.system.backtest.robot.tester.ArimaBTester;
import daryl.system.backtest.robot.tester.ArimaCInvTester;
import daryl.system.backtest.robot.tester.ArimaCTester;
import daryl.system.backtest.robot.tester.ArimaDInvTester;
import daryl.system.backtest.robot.tester.ArimaDTester;
import daryl.system.backtest.robot.tester.RnaInvTester;
import daryl.system.backtest.robot.tester.RnaTester;
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
		
		IRobotsRepository robotsRepository = context.getBean(IRobotsRepository.class);
		IHistoricoRepository historicoRepository = context.getBean(IHistoricoRepository.class);
		
		ExecutorService servicio = Executors.newFixedThreadPool(25);
		
		List<Robot> robots = robotsRepository.findAll();
		if(robots != null) {
			
			//Para robots de tipo RNA
			for (Robot robot : robots) {
				
				List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(robot.getTimeframe(), robot.getActivo());

				System.out.println("================================================== " + robot.getRobot() + " =============================================================================");
				
				if(robot.getRobot().startsWith("RNA_I_" + robot.getActivo())) {
					
					System.out.println("=============EMPIEZA===================== " + robot.getRobot() + " =============================================================================");
					RnaInvTester rnaInvTester = context.getBean(RnaInvTester.class);
					rnaInvTester.init(robot, generateBarList(historico, "BarSeries_" + robot.getActivo(), robot.getActivo().getMultiplicador()), 20);
					//servicio.submit(rnaInvTester);
					rnaInvTester.run();
					System.out.println("=============TERMINA===================== " + robot.getRobot() + " =============================================================================");

				} else if(robot.getRobot().startsWith("RNA_" + robot.getActivo())) {
					
					System.out.println("=============EMPIEZA===================== " + robot.getRobot() + " =============================================================================");
					RnaTester rnaTester = context.getBean(RnaTester.class);
					rnaTester.init(robot, generateBarList(historico, "BarSeries_" + robot.getActivo(), robot.getActivo().getMultiplicador()), 20);
					//servicio.submit(rnaTester);
					rnaTester.run();
					System.out.println("=============TERMINA===================== " + robot.getRobot() + " =============================================================================");

				} else if(robot.getRobot().startsWith("ARIMA_D_" + robot.getActivo())) {
					
					System.out.println("=============EMPIEZA===================== " + robot.getRobot() + " =============================================================================");
					ArimaDTester arimaDTester = context.getBean(ArimaDTester.class);
					arimaDTester.init(robot, generateBarList(historico, "BarSeries_" + robot.getActivo(), robot.getActivo().getMultiplicador()), 20);
					//servicio.submit(arimaCTester);
					arimaDTester.run();
					System.out.println("=============TERMINA===================== " + robot.getRobot() + " =============================================================================");
				
				} else if(robot.getRobot().startsWith("ARIMA_I_D_" + robot.getActivo())) {
				
					System.out.println("=============EMPIEZA===================== " + robot.getRobot() + " =============================================================================");
					ArimaDInvTester arimaDInvTester = context.getBean(ArimaDInvTester.class);
					arimaDInvTester.init(robot, generateBarList(historico, "BarSeries_" + robot.getActivo(), robot.getActivo().getMultiplicador()), 20);
					//servicio.submit(arimaCInvTester);
					arimaDInvTester.run();
					System.out.println("=============TERMINA===================== " + robot.getRobot() + " =============================================================================");
				
				} else if(robot.getRobot().startsWith("ARIMA_I_C_" + robot.getActivo())) {
					
					System.out.println("=============EMPIEZA===================== " + robot.getRobot() + " =============================================================================");
					ArimaCInvTester arimaCInvTester = context.getBean(ArimaCInvTester.class);
					arimaCInvTester.init(robot, generateBarList(historico, "BarSeries_" + robot.getActivo(), robot.getActivo().getMultiplicador()), 20);
					//servicio.submit(arimaCInvTester);
					arimaCInvTester.run();
					System.out.println("=============TERMINA===================== " + robot.getRobot() + " =============================================================================");
				
				} else if(robot.getRobot().startsWith("ARIMA_C_" + robot.getActivo())) {
					
					System.out.println("=============EMPIEZA===================== " + robot.getRobot() + " =============================================================================");
					ArimaCTester arimaCTester = context.getBean(ArimaCTester.class);
					arimaCTester.init(robot, generateBarList(historico, "BarSeries_" + robot.getActivo(), robot.getActivo().getMultiplicador()), 20);
					//servicio.submit(arimaCTester);
					arimaCTester.run();
					System.out.println("=============TERMINA===================== " + robot.getRobot() + " =============================================================================");
				
				} else if(robot.getRobot().startsWith("ARIMA_I_B_" + robot.getActivo())) {
					
					System.out.println("=============EMPIEZA===================== " + robot.getRobot() + " =============================================================================");
					ArimaBInvTester arimaBInvTester = context.getBean(ArimaBInvTester.class);
					arimaBInvTester.init(robot, generateBarList(historico, "BarSeries_" + robot.getActivo(), robot.getActivo().getMultiplicador()), 20);
					//servicio.submit(arimaBInvTester);
					arimaBInvTester.run();
					System.out.println("=============TERMINA===================== " + robot.getRobot() + " =============================================================================");
				
				}   else if(robot.getRobot().startsWith("ARIMA_B_" + robot.getActivo())) {
					
					System.out.println("=============EMPIEZA===================== " + robot.getRobot() + " =============================================================================");
					ArimaBTester arimaBTester = context.getBean(ArimaBTester.class);
					arimaBTester.init(robot, generateBarList(historico, "BarSeries_" + robot.getActivo(), robot.getActivo().getMultiplicador()), 100);
					//servicio.submit(arimaBTester);
					arimaBTester.run();
					System.out.println("=============TERMINA===================== " + robot.getRobot() + " =============================================================================");
				
				} else if(robot.getRobot().startsWith("ARIMA_I_" + robot.getActivo())) {

					ArimaAInvTester arimaAInvTester = context.getBean(ArimaAInvTester.class);
					arimaAInvTester.init(robot, generateBarList(historico, "BarSeries_" + robot.getActivo(), robot.getActivo().getMultiplicador()), 20);
					//servicio.submit(arimaAInvTester);
					arimaAInvTester.run();
					System.out.println("=============TERMINA===================== " + robot.getRobot() + " =============================================================================");
					
				} else if(robot.getRobot().startsWith("ARIMA_" + robot.getActivo()) && !robot.getRobot().startsWith("RNA_I_" + robot.getActivo())) {
					
					System.out.println("=============EMPIEZA===================== " + robot.getRobot() + " =============================================================================");
					ArimaATester arimaATester = context.getBean(ArimaATester.class);
					arimaATester.init(robot, generateBarList(historico, "BarSeries_" + robot.getActivo(), robot.getActivo().getMultiplicador()), 20);
					//servicio.submit(arimaATester);
					arimaATester.run();
					System.out.println("=============TERMINA===================== " + robot.getRobot() + " =============================================================================");
					
				}
								
				System.out.println("=============INICIO CALCULO DE LOS RESÚMENES============== " + robot.getRobot() + " ================================================================");
				ControlHistoricoOperacionesBacktest controlHistoricoOperacionesBacktest = context.getBean(ControlHistoricoOperacionesBacktest.class);
				controlHistoricoOperacionesBacktest.init(robot);
				controlHistoricoOperacionesBacktest.run();
				controlHistoricoOperacionesBacktest.calcularMaximaRachaPerdedora();
				controlHistoricoOperacionesBacktest.calcularMaxMinDD();
				System.out.println("=============FIN CALCULO DE LOS RESÚMENES================= " + robot.getRobot() + " ================================================================");
				
			}
			
		}
		servicio.shutdown();
		
	}

	
	private static BarSeries  generateBarList(List<Historico> historico, String name, int multiplicador){
		
		BarSeries series = new BaseBarSeriesBuilder().withName(name).build();
			
		for (Historico hist : historico) {
			
			Long millis = hist.getFechaHora();
			
			Instant instant = Instant.ofEpochMilli(millis);
			ZonedDateTime barDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
			
			series.addBar(	barDateTime, 
							hist.getApertura() * multiplicador, 
							hist.getMaximo() * multiplicador, 
							hist.getMinimo() * multiplicador, 
							hist.getCierre() * multiplicador, 
							hist.getVolumen() * multiplicador);
			
		}
		
		return series;
		
		
	}
	
}
