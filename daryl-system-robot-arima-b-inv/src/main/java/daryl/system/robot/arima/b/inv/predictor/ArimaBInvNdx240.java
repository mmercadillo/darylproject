package daryl.system.robot.arima.b.inv.predictor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

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
import daryl.system.model.historicos.HistNdx;
import daryl.system.robot.arima.b.inv.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.b.inv.predictor.config.ConfiguracionArimaNdx240;
import daryl.system.robot.arima.b.inv.repository.IHistNdxRepository;
import lombok.ToString;

@Component(value = "arimaBInvNdx240")
@ToString
public class ArimaBInvNdx240  extends ArimaPredictor{
	
	@Autowired(required = true)
	ConfiguracionArimaNdx240 configuracion;
	@Autowired
	private DataSetLoader dataSetLoader;
	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistNdxRepository histNdxRepository;
	
	private List<HistNdx> historico;
	private List<Datos> datosTotal;
	
	private final String robot = "ARIMA_I_B_NDX_240";
	private final Boolean inv = Boolean.TRUE;
	
	@PostConstruct
	public void load() {
		
		DatosLoader loader = DatosLoaderOHLC.getInstance();
		datosTotal = loader.loadDatos(configuracion.getFHistoricoLearn());
		//List<Datos> datosTotal = loader.loadDatos(configuracion.getFHistoricoLearn());
	}

	@Override
	public void calculate(Activo activo, String estrategia) {
		//Calcular la predicción
		System.out.println("-----------------------------------------------------------------------------------------------------------------");
		Double prediccion = calcularPrediccion();
		//logger.info("Nueva predicción para el NDX H4 : {} a las: {}" , prediccion, config.getActualDateFormattedInString());
				
		//actualizamos el fichero de ordenes
		Orden orden = calcularOperacion(activo, estrategia, prediccion, robot, inv);
		
		//Enviamos al controlador para q esté disponible lo antes posible
		//ArimaBNdxH4Controller.orden = orden.getTipoOrden();

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//Cerramos la operacion anterior en caso q hubiera
		Long fechaHoraMillis = System.currentTimeMillis();
		
		//Actualizamos la tabla con la predicción
		super.actualizarPrediccionBDs(activo, estrategia, orden.getTipoOrden(), prediccion, fechaHoraMillis);
		super.actualizarUltimaOrden(activo, estrategia, orden, fechaHoraMillis);
		super.guardarNuevaOrden(orden, fechaHoraMillis);
		///// 
		
	}
	static Integer prediccionArimaAnterior = 0;
	@Override
	protected Double calcularPrediccion() {
		Double prediccionAnterior = null;
		Double prediccion = 0.0;

		historico = histNdxRepository.findAllByTimeframeOrderByFechaHoraAsc(Timeframes.PERIOD_H4);
		
		List<Datos> datosForecast = toDatosList(historico);
		//List<Datos> datosT = loader.loadDatos(configuracion.getFHistoricoLearn());
		
		datosTotal.addAll(datosForecast);
		darylNormalizer.setDatos(datosTotal, Mode.valueOf(configuracion.getMode()));
		
		List<Double> datos = darylNormalizer.getDatos();
		try {
			
			ARIMA arima=new ARIMA(datos.stream().mapToDouble(Double::new).toArray());
			
			int []model=arima.getARIMAmodel();
			System.out.println("Best model is [p,q]="+"["+model[0]+" "+model[1]+"]");
			System.out.println("Predict value="+arima.aftDeal(arima.predictValue(model[0],model[1])));
			System.out.println("Predict error="+(arima.aftDeal(arima.predictValue(model[0],model[1]))-datos.get(datos.size()-1))/datos.get(datos.size()-1)*100+"%");
		
			if(prediccionArimaAnterior == 0) {
				prediccion = arima.aftDeal(arima.predictValue(model[0],model[1])) - datos.get(datos.size()-1);
			}else {
				prediccion = (double)(arima.aftDeal(arima.predictValue(model[0],model[1])) - prediccionArimaAnterior);
			}
			prediccionArimaAnterior = arima.aftDeal(arima.predictValue(model[0],model[1]));
			
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
