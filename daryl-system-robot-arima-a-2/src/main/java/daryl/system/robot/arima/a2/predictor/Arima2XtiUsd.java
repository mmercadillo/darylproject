package daryl.system.robot.arima.a2.predictor;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.robot.arima.a2.predictor.base.Arima2Predictor;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class Arima2XtiUsd  extends Arima2Predictor{

	
}
