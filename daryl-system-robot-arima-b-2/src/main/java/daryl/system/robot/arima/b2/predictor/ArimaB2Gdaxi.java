package daryl.system.robot.arima.b2.predictor;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.robot.arima.b2.predictor.base.ArimaB2Predictor;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaB2Gdaxi  extends ArimaB2Predictor{
	

	
}
