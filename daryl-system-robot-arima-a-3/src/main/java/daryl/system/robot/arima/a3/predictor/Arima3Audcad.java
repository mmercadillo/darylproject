package daryl.system.robot.arima.a3.predictor;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.robot.arima.a3.predictor.base.Arima3Predictor;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class Arima3Audcad  extends Arima3Predictor{
	


	
}
