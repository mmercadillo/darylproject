package daryl.system.robot.variance.b.predictor;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.robot.variance.b.predictor.base.VarianceBPredictor;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class VarianceBGdaxi  extends VarianceBPredictor{
	

	
	
}
