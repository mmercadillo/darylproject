package daryl.system.robot.arima.c2.predictor;

import java.util.List;

import org.espy.arima.ArimaForecaster;
import org.espy.arima.DefaultArimaForecaster;
import org.espy.arima.DefaultArimaProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.MaxMinNormalizer;
import org.ta4j.core.utils.BarSeriesUtils;

import daryl.system.comun.enums.Mode;
import daryl.system.model.ArimaConfig;
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;
import daryl.system.robot.arima.c2.predictor.base.ArimaC2Predictor;
import daryl.system.robot.arima.c2.repository.IArimaConfigRepository;
import daryl.system.robot.arima.c2.repository.IHistoricoRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaC2Audcad  extends ArimaC2Predictor{
	



	
}
