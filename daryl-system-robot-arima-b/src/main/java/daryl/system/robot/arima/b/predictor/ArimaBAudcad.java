package daryl.system.robot.arima.b.predictor;

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
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;
import daryl.system.model.historicos.HistAudCad;
import daryl.system.robot.arima.b.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.b.repository.IHistAudCadRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaBAudcad  extends ArimaPredictor{

	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistAudCadRepository histAudCadRepository;
	

	private Integer getPrediccionAnterior(List<Datos> datosForecast) {
		
		//Lista para prediccionAnterior
		List<Datos> datosForecastAnterior = datosForecast.subList(0, datosForecast.size()-1);
		darylNormalizer.setDatos(datosForecastAnterior, Mode.CLOSE);
		List<Double> datosAnterior = darylNormalizer.getDatos();
		datosAnterior.stream().forEach(dato -> {
			int pos = datosAnterior.indexOf(dato);
			datosAnterior.set(pos, dato * 10000);
		});
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
		
			List<HistAudCad> historico = histAudCadRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());				
			List<Datos> datosForecast = toDatosList(historico);
		
			Integer prediccionAnterior = getPrediccionAnterior(datosForecast);
		
			darylNormalizer.setDatos(datosForecast, Mode.CLOSE);
		
			List<Double> datos = darylNormalizer.getDatos();
		
			datos.stream().forEach(dato -> {
				int pos = datos.indexOf(dato);
				datos.set(pos, dato * 10000);
			});
		
			ARIMA arima=new ARIMA(datos.stream().mapToDouble(Double::new).toArray());
			
			int []model=arima.getARIMAmodel();

			Integer forecast = arima.aftDeal(arima.predictValue(model[0],model[1]));
			logger.info("PREDICCIÓN ACTUAL PARA EL ROBOT : {}", forecast);
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

	
	private List<Datos> toDatosList(List<HistAudCad> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (HistAudCad hist : historico) {
			
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
