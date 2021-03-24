package daryl.system.robot.arima.a.predictor;

import java.util.ArrayList;
import java.util.List;

//logger;
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
import daryl.system.robot.arima.a.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.a.repository.IHistXauUsdRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaXauUsd  extends ArimaPredictor{


	
	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistXauUsdRepository histXauUsdRepository;
	
	@Override
	protected Double calcularPrediccion(Robot bot) {
		

		Double prediccion = 0.0;
		
		List<HistXauUsd> historico = histXauUsdRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());
		
		List<Datos> datosForecast = toDatosList(historico);
		darylNormalizer.setDatos(datosForecast, Mode.CLOSE);
		
		List<Double> datos = darylNormalizer.getDatos();
		try {
			
			ARIMA arima=new ARIMA(datos.stream().mapToDouble(Double::new).toArray());
			
			int []model=arima.getARIMAmodel();

			double forecast = (double)arima.aftDeal(arima.predictValue(model[0],model[1]));
			logger.info("Robot -> " + bot.getRobot() + " PREDICCIÓN -> " + forecast + " ANTERIOR -> " + datos.get(datos.size()-1));
			if(forecast > datos.get(datos.size()-1)) {
				prediccion = 1.0;
			}else if(forecast < datos.get(datos.size()-1)) {
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
