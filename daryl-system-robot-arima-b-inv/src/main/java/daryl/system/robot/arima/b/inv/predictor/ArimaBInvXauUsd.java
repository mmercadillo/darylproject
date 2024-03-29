package daryl.system.robot.arima.b.inv.predictor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.arima.gen.ARIMA;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.model.Robot;
import daryl.system.model.historicos.HistXauUsd;
import daryl.system.robot.arima.b.inv.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.b.inv.repository.IHistXauUsdRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaBInvXauUsd  extends ArimaPredictor{


	@Autowired
	private IHistXauUsdRepository histXauUsdRepository;


	private Integer getPrediccionAnterior(List<Datos> datosForecast) {
		
		//Lista para prediccionAnterior
		List<Datos> datosForecastAnterior = datosForecast.subList(0, datosForecast.size()-1);
		
		//Recuperamos los cierres de cada Dato
		DarylMaxMinNormalizer darylNormalizer = new DarylMaxMinNormalizer(datosForecastAnterior, Mode.CLOSE);
		List<Double> datosAnterior = darylNormalizer.getDatos();

		ARIMA arima=new ARIMA(datosAnterior.stream().mapToDouble(Double::new).toArray());
		int []model=arima.getARIMAmodel();
		Integer prediccionAnterior = arima.aftDeal(arima.predictValue(model[0],model[1]));
		logger.info("PREDICCIÓN ANTERIOR PARA EL ROBOT : {}", prediccionAnterior);
		return prediccionAnterior;
	}
	
	@Override
	protected Double calcularPrediccion(Robot bot) {

		Double prediccion = 0.0;
		try {
		
			List<HistXauUsd> historico = histXauUsdRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());
			List<Datos> datosForecast = toDatosList(historico);
		
			Integer prediccionAnterior = getPrediccionAnterior(datosForecast);
			
			
			//Recuperamos los cierres de cada Dato
			DarylMaxMinNormalizer darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
			List<Double> datos = darylNormalizer.getDatos();

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

	
	private List<Datos> toDatosList(List<HistXauUsd> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (HistXauUsd hist : historico) {
			
			Datos dato = Datos.builder().fecha(hist.getFecha())
										.hora(hist.getHora())
										.apertura(hist.getApertura())
										.maximo(hist.getMaximo())
										.minimo(hist.getMinimo())
										.cierre(hist.getCierre())
										.volumen(hist.getVolumen())
										.build();
			datos.add(dato);
			
		}
		
		return datos;
		
		
	}
	

	
}
