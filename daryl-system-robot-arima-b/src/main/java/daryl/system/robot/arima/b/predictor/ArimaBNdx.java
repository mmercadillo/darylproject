package daryl.system.robot.arima.b.predictor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.arima.gen.ARIMA;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.model.Orden;
import daryl.system.model.Robot;
import daryl.system.model.historicos.HistNdx;
import daryl.system.robot.arima.b.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.b.repository.IHistNdxRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaBNdx  extends ArimaPredictor{
	

	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistNdxRepository histNdxRepository;
	

	static Integer prediccionArimaAnterior = 0;
	@Override
	protected Double calcularPrediccion(Robot bot) {

		Double prediccion = 0.0;

		List<HistNdx> historico = histNdxRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());
		
		List<Datos> datosForecast = toDatosList(historico);
		darylNormalizer.setDatos(datosForecast, Mode.CLOSE);
		
		List<Double> datos = darylNormalizer.getDatos();
		try {
			
			ARIMA arima=new ARIMA(datos.stream().mapToDouble(Double::new).toArray());
			
			int []model=arima.getARIMAmodel();

			if(prediccionArimaAnterior == 0) {
				
				if(arima.aftDeal(arima.predictValue(model[0],model[1])) > datos.get(datos.size()-1)) {
					prediccion = 1.0;
				}else if(arima.aftDeal(arima.predictValue(model[0],model[1])) < datos.get(datos.size()-1)) {
					prediccion = -1.0;
				}else {
					prediccion = 0.0;
				}

			}else {
				
				if(arima.aftDeal(arima.predictValue(model[0],model[1])) > prediccionArimaAnterior) {
					prediccion = 1.0;
				}else if(arima.aftDeal(arima.predictValue(model[0],model[1])) < prediccionArimaAnterior) {
					prediccion = -1.0;
				}else {
					prediccion = 0.0;
				}

			}
			prediccionArimaAnterior = arima.aftDeal(arima.predictValue(model[0],model[1]));
			
		}catch (Exception e) {
			logger.error("No se ha podido calcular la prediccion para el robot: {}", bot.getRobot(), e);
		}

		return prediccion;
	
	}

	
	private List<Datos> toDatosList(List<HistNdx> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (HistNdx hist : historico) {
			
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
