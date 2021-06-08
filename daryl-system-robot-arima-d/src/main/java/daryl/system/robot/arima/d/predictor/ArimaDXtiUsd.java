package daryl.system.robot.arima.d.predictor;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.robot.arima.d.predictor.base.ArimaDPredictor;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaDXtiUsd  extends ArimaDPredictor{
	

	
}
