package daryl.system.robot.arima.c2.predictor;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.robot.arima.c2.predictor.base.ArimaC2Predictor;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaC2XauUsd  extends ArimaC2Predictor{
	

}
