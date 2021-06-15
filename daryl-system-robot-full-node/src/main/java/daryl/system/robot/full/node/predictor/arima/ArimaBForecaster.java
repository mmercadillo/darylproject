package daryl.system.robot.full.node.predictor.arima;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.MaxMinNormalizer;
import org.ta4j.core.utils.BarSeriesUtils;

import daryl.arima.gen.ARIMA;
import daryl.system.comun.enums.Mode;
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;
import daryl.system.robot.full.node.predictor.base.Forecaster;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaBForecaster  extends Forecaster{

	private Integer getPrediccionAnterior(List<Double> datosForecast) {
		
		//Lista para prediccionAnterior
		List<Double> datosForecastAnterior = datosForecast.subList(0, datosForecast.size()-1);
		
		
		ARIMA arima=new ARIMA(datosForecastAnterior.stream().mapToDouble(Double::new).toArray());
		int []model=arima.getARIMAmodel();
		Integer prediccionAnterior = arima.aftDeal(arima.predictValue(model[0],model[1]));
		logger.info("PREDICCIÓN ANTERIOR PARA EL ROBOT : {}", prediccionAnterior);
		return prediccionAnterior;
	}
	
	
	protected Double calcularPrediccion(Robot bot) {
		

		Double prediccion = 0.0;
		try {
		
			List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(bot.getTimeframe(), bot.getActivo());
			BarSeries serieParaCalculo = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + bot.getTimeframe() + "_" + bot.getActivo(), bot.getActivo().getMultiplicador());
			MaxMinNormalizer darylNormalizer =  new MaxMinNormalizer(serieParaCalculo, Mode.CLOSE);
			List<Double> datos = darylNormalizer.getDatos();
			
			Integer prediccionAnterior = getPrediccionAnterior(datos);
			
			ARIMA arima=new ARIMA(datos.stream().mapToDouble(Double::new).toArray());
			
			int []model=arima.getARIMAmodel();

			Integer forecast = arima.aftDeal(arima.predictValue(model[0],model[1]));
			logger.info("Robot -> " + bot.getRobot() + " PREDICCIÓN -> " + forecast + " ANTERIOR -> " + prediccionAnterior);
			if(forecast > prediccionAnterior) {
				prediccion = 1.0;
			}else if(forecast < prediccionAnterior) {
				prediccion = -1.0;
			}else {
				prediccion = 0.0;
			}

		}catch (Exception e) {
			logger.error("No se ha podido calcular la prediccion para el robot: {}", bot.getRobot(), e);
		}

		
		return prediccion;
	
	}


	
}
