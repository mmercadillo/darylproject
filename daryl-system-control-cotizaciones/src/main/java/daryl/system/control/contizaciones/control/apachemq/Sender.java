package daryl.system.control.contizaciones.control.apachemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;

@Component
public class Sender{


	@Autowired
	JmsTemplate jmsTemplate;
	
    public void send(String destination, Robot robot) {
    	
        jmsTemplate.convertAndSend(destination, robot);
    }
	
	
}
