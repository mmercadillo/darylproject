package daryl.system.robot.full.node.predictor.ann;

import org.springframework.beans.factory.annotation.Autowired;

import daryl.system.model.Robot;
import daryl.system.robot.full.node.predictor.base.Forecaster;
import daryl.system.robot.full.node.repository.IAnnConfigRepository;

//@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//@ToString
public class AnnForecaster  extends Forecaster{

	@Autowired
	private IAnnConfigRepository annConfigRepository;

	@Override
	public Double calcularPrediccion(Robot bot){

		Double prediccion = 0.0;

		
        return prediccion;
	
	}


}
