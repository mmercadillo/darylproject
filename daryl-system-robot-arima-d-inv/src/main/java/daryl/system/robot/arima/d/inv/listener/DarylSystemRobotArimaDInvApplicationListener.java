package daryl.system.robot.arima.d.inv.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class DarylSystemRobotArimaDInvApplicationListener implements ApplicationListener<ApplicationReadyEvent>{

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		
		
		//Cargamos las colecciones correspondientes
		
		
	}

	
	
}