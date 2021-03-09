package daryl.system.robot.arima.d.inv.predictor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.espy.arima.ArimaForecaster;
import org.espy.arima.ArimaProcess;
import org.espy.arima.DefaultArimaForecaster;
import org.espy.arima.DefaultArimaProcess;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import daryl.system.comun.dataset.DataSetLoader;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.loader.DatosLoader;
import daryl.system.comun.dataset.loader.DatosLoaderOHLC;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.ArimaConfig;
import daryl.system.model.Orden;
import daryl.system.model.Robot;
import daryl.system.model.historicos.HistGdaxi;
import daryl.system.robot.arima.d.inv.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.d.inv.predictor.config.ConfiguracionArimaGdaxi;
import daryl.system.robot.arima.d.inv.repository.IArimaConfigRepository;
import daryl.system.robot.arima.d.inv.repository.IHistGdaxiRepository;
import lombok.ToString;

@Component(value = "arimaDInvGdaxi60")
@ToString
public class ArimaDInvGdaxi60  extends ArimaPredictor{
	
	@Autowired
	Logger logger;
	
	@Autowired
	IArimaConfigRepository arimaConfigRepository;
	
	@Autowired(required = true)
	ConfiguracionArimaGdaxi configuracion;
	@Autowired
	private DataSetLoader dataSetLoader;
	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistGdaxiRepository histGdaxiRepository;
	
	private List<HistGdaxi> historico;
	private List<Datos> datosTotal;
	private Integer inicio;


	private final String robot= "ARIMA_I_D_GDAXI_60";
	private final String robot_config= "ARIMA_D_GDAXI_60";
	private final Boolean inv = Boolean.TRUE;
	private final Timeframes timeframe = Timeframes.PERIOD_H1;
	
	@PostConstruct
	public void load() {
		
		DatosLoader loader = DatosLoaderOHLC.getInstance();
		datosTotal = loader.loadDatos(configuracion.getFHistoricoLearn());
		//List<Datos> datosTotal = loader.loadDatos(configuracion.getFHistoricoLearn());
	}
	

	@Override
	public void calculate(Robot bot) {
		
		//Calcular la predicción		//Calcular la predicción
		System.out.println("-----------------------------------------------------------------------------------------------------------------");
		//System.out.println("PREDICCION ANTERIOR -> " + prediccionAnterior);		
		Double prediccion = calcularPrediccion();
		//logger.info("Nueva predicción para el ROBOT " + robot + " : {} a las: {}" , prediccion, config.getActualDateFormattedInString());
				
		//actualizamos el fichero de ordenes
		Orden orden = calcularOperacion(bot.getActivo(), bot.getEstrategia(), prediccion, robot, inv);
		logger.info("ORDEN GENERADA " + orden.getTipoOrden().name() + " ROBOT -> " + bot);
		//Enviamos al controlador para q esté disponible lo antes posible
		//ArimaBGdaxiController.orden = orden.getTipoOrden();

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//Cerramos la operacion anterior en caso q hubiera
		Long fechaHoraMillis = System.currentTimeMillis();
		
		//Actualizamos la tabla con la predicción
		super.actualizarPrediccionBDs(bot.getActivo(), bot.getEstrategia(), orden.getTipoOrden(), prediccion, fechaHoraMillis);
		super.actualizarUltimaOrden(bot.getActivo(), bot.getEstrategia(), orden, fechaHoraMillis);
		super.guardarNuevaOrden(orden, fechaHoraMillis);
		///// 
		
	}
	static Double prediccionArimaAnterior = 0.0;
	@Override
	protected Double calcularPrediccion() {
		
		Double prediccion = 0.0;
		
		historico = histGdaxiRepository.findAllByTimeframeOrderByFechaHoraAsc(timeframe);
		
		List<Datos> datosForecast = toDatosList(historico);
		//List<Datos> datosT = loader.loadDatos(configuracion.getFHistoricoLearn());
		
		datosTotal.addAll(datosForecast);
		darylNormalizer.setDatos(datosTotal, Mode.valueOf(configuracion.getMode()));
		
		List<Double> datos = darylNormalizer.getDatos();
		
		datos.stream().forEach(dato -> {
			int pos = datos.indexOf(dato);
			datos.set(pos, dato * 10000);
		});
		
		try {


			ArimaConfig arimaConfig = arimaConfigRepository.findArimaConfigByRobot(robot);
			this.inicio = arimaConfig.getInicio();
			DefaultArimaProcess arimaProcess = (DefaultArimaProcess)getArimaProcess(arimaConfig);

	        
	    	List<Double> aux = datos;
	    	if(datos.size() > this.inicio) {
	    		aux = datos.subList((datos.size()-this.inicio), datos.size());
	    	}else {
	    		
	    	}
	    	
	    	//List<Double> aux = data.subList((data.size()-inicio), data.size())
	    	double[] observations = new double[aux.size()];
	    	for(int i = 0; i < aux.size(); i++) {
	    		observations[i] = aux.get(i).doubleValue();
	    	}

	    	ArimaForecaster arimaForecaster = null;
        	try {
        		arimaForecaster = new DefaultArimaForecaster(arimaProcess, observations);
        		
        		double forecast = arimaForecaster.next();			
    	        double ultimoDato = datos.get(datos.size()-1);
    	        
    	        if(prediccionArimaAnterior != 0.0) {
    	        	ultimoDato = prediccionArimaAnterior;
    	        }
    	        if(forecast > ultimoDato) {
    	        	prediccion = 1.0;
    	        }
    	        if(forecast < ultimoDato) {
    	        	prediccion = -1.0;
    	        }
    	        prediccionArimaAnterior = forecast;
        		
        	}catch (Exception e) {
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
