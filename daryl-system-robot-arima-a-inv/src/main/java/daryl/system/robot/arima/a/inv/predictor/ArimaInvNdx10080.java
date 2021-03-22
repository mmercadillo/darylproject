package daryl.system.robot.arima.a.inv.predictor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
//logger;
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
import daryl.system.robot.arima.a.inv.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.a.inv.repository.IHistNdxRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaInvNdx10080  extends ArimaPredictor{
	

	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistNdxRepository histNdxRepository;



	@Override
	protected Double calcularPrediccion(Robot bot) {

		Double prediccion = 0.0;

		List<HistNdx> historico = histNdxRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());
		
		List<Datos> datosForecast = toDatosList(historico);
		
		List<Datos> datosTotal = new ArrayList<Datos>();
		datosTotal.addAll(datosForecast);
		darylNormalizer.setDatos(datosTotal, Mode.CLOSE);
		
		List<Double> datos = darylNormalizer.getDatos();
		try {
			
			ARIMA arima=new ARIMA(datos.stream().mapToDouble(Double::new).toArray());
			
			int []model=arima.getARIMAmodel();

			prediccion = (double)arima.aftDeal(arima.predictValue(model[0],model[1]));
			if(prediccion > datos.get(datos.size()-1)) {
				prediccion = 1.0;
			}else if(prediccion < datos.get(datos.size()-1)) {
				prediccion = -1.0;
			}else {
				prediccion = 0.0;
			}
			
		}catch (Exception e) {
			
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
	
	protected void verInputs(List<Double> inputs) {
		StringBuffer buffer = new StringBuffer();
		for (Double input : inputs) {
			buffer.append(darylNormalizer.denormData(input)).append("-");
		}
		System.out.println(buffer.toString());
	}
	
}
