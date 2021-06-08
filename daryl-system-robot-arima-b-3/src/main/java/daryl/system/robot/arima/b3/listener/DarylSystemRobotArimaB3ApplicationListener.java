package daryl.system.robot.arima.b3.listener;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class DarylSystemRobotArimaB3ApplicationListener implements ApplicationListener<ApplicationReadyEvent>{
	
	@Autowired
	Logger logger;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		
		
		//Cargamos las colecciones correspondientes
		
		
	}

	
	
}
