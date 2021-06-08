package daryl.system.control.contizaciones.zeromq;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender{

	@Autowired
	Logger logger;
	
	
	@Autowired
	JmsTemplate jmsTemplate;
	
    public void send(String destination, String robot) {
        jmsTemplate.convertAndSend(destination, robot);
    }
	
	
}
