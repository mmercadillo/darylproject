package daryl.system.robot.arima.a.inv.predictor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import daryl.arima.gen.ARIMA;
import daryl.system.comun.dataset.DataSetLoader;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.loader.DatosLoader;
import daryl.system.comun.dataset.loader.DatosLoaderOHLC;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Orden;
import daryl.system.model.historicos.HistGdaxi;
import daryl.system.robot.arima.a.inv.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.a.inv.predictor.config.ConfiguracionArimaGdaxi1440;
import daryl.system.robot.arima.a.inv.repository.IHistGdaxiRepository;
import lombok.ToString;

@Component(value = "arimaInvGdaxi1440")
@ToString
public class ArimaInvGdaxi1440  extends ArimaPredictor{
	
	@Autowired
	Logger logger;
	
	@Autowired(required = true)
	ConfiguracionArimaGdaxi1440 configuracion;
	@Autowired
	private DataSetLoader dataSetLoader;
	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistGdaxiRepository histGdaxiRepository;
	
	private List<HistGdaxi> historico;
	private List<Datos> datosTotal;

	private final String robot = "ARIMA_I_GDAXI_1440";
	private final Boolean inv = Boolean.TRUE;
	
	
	@PostConstruct
	public void load() {
		
		DatosLoader loader = DatosLoaderOHLC.getInstance();
		datosTotal = loader.loadDatos(configuracion.getFHistoricoLearn());

	}

	@Override
	public void calculate(Activo activo, String estrategia) {
		
		//Calcular la predicción		//Calcular la predicción
		//System.out.println("-----------------------------------------------------------------------------------------------------------------");
		//System.out.println("PREDICCION ANTERIOR -> " + prediccionAnterior);		
		Double prediccion = calcularPrediccion();
		//logger.info("Nueva predicción para el GDAXI D1 : {} a las: {}" , prediccion, config.getActualDateFormattedInString());
				
		//actualizamos el fichero de ordenes
		Orden orden = calcularOperacion(activo, estrategia, prediccion, robot, inv);


		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//Cerramos la operacion anterior en caso q hubiera
		Long fechaHoraMillis = System.currentTimeMillis();
		
		//Actualizamos la tabla con la predicción
		super.actualizarPrediccionBDs(activo, estrategia, orden.getTipoOrden(), prediccion, fechaHoraMillis);
		super.actualizarUltimaOrden(activo, estrategia, orden, fechaHoraMillis);
		super.guardarNuevaOrden(orden, fechaHoraMillis);
		///// 
		
	}

	@Override
	protected Double calcularPrediccion() {

		Double prediccion = 0.0;
		
		historico = histGdaxiRepository.findAllByTimeframeOrderByFechaHoraAsc(Timeframes.PERIOD_D1);

		List<Datos> datosForecast = toDatosList(historico);
		
		datosTotal.addAll(datosForecast);
		darylNormalizer.setDatos(datosTotal, Mode.valueOf(configuracion.getMode()));
		
		List<Double> datos = darylNormalizer.getDatos();
		try {
			
			ARIMA arima=new ARIMA(datos.stream().mapToDouble(Double::new).toArray());
			
			int []model=arima.getARIMAmodel();

			//Double media = media(7, datos);
			prediccion = (double)arima.aftDeal(arima.predictValue(model[0],model[1]));

			
			if(prediccion > datos.get(datos.size()-1) /*&& datos.get(datos.size()-1) > media && media > 0*/) {
				prediccion = 1.0;
			}else if(prediccion < datos.get(datos.size()-1) /*&& datos.get(datos.size()-1) < media && media > 0*/) {
				prediccion = -1.0;
			}else {
				prediccion = 0.0;
			}
			
		}catch (Exception e) {
			
		}

        
		return prediccion;
	}

	private List<Datos> toDatosList(List<HistGdaxi> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (HistGdaxi hist : historico) {
			
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
