package daryl.system.robot.arima.c2;

import java.io.IOException;

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

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.robot.arima.c2.predictor.ArimaC2Gdaxi;

@SpringBootApplication(scanBasePackages = {"daryl.system"})
@EnableJpaRepositories
@EntityScan("daryl.system.model")
@EnableJms
@EnableTransactionManagement
public class DarylSystemRobotArimaC2Application {

	
	
	public static void main(String[] args) {
		
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DarylSystemRobotArimaC2Application.class);
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	    //test(context);
	}

	
	public static void test(ConfigurableApplicationContext context) {
		
		String robot = "ARIMA_C_GDAXI_60";
		Robot bot = new Robot();
			bot.setInverso(true);
			bot.setRobot(robot);
			bot.setArimaConfig(robot);
			bot.setTimeframe(Timeframes.PERIOD_H1);
		
		ArimaC2Gdaxi a = context.getBean(ArimaC2Gdaxi.class);
			try {
				a.calculate(bot);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		System.out.println(a);
		
	}
	
	
	@Bean
    public Logger darylLogger() {
        return LoggerFactory.getLogger("daryl");
    }
}
