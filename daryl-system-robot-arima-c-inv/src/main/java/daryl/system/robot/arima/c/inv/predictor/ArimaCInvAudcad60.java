package daryl.system.robot.arima.c.inv.predictor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.espy.arima.ArimaForecaster;
import org.espy.arima.ArimaProcess;
import org.espy.arima.DefaultArimaForecaster;
import org.espy.arima.DefaultArimaProcess;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
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
import daryl.system.model.historicos.HistAudCad;
import daryl.system.model.historicos.HistNdx;
import daryl.system.robot.arima.c.inv.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.c.inv.predictor.config.ConfiguracionArimaAudCad60;
import daryl.system.robot.arima.c.inv.repository.IArimaConfigRepository;
import daryl.system.robot.arima.c.inv.repository.IHistAudCadRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaCInvAudcad60  extends ArimaPredictor{
	

	
	@Autowired
	IArimaConfigRepository arimaConfigRepository;
	
	@Autowired(required = true)
	ConfiguracionArimaAudCad60 configuracion;

	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistAudCadRepository histAudCadRepository;
	

	private Integer inicio;
	

	private final String robot_config= "ARIMA_C_AUDCAD_60";

	/*
	private List<Datos> datosTotal;
	
	@PostConstruct
	public void load() {
		
		DatosLoader loader = DatosLoaderOHLC.getInstance();
		datosTotal = loader.loadDatos(configuracion.getFHistoricoLearn());
	}
	*/
	

	@Override
	public void calculate(Robot bot) {
		//Calcular la predicción
		System.out.println("-----------------------------------------------------------------------------------------------------------------");
		Double prediccion = calcularPrediccion(bot);
		//logger.info("Nueva predicción para el ROBOT " + robot + " : {} a las: {}" , prediccion, config.getActualDateFormattedInString());
		
				
		//actualizamos el fichero de ordenes
		Orden orden = calcularOperacion(bot.getActivo(), bot.getEstrategia(), prediccion, bot.getRobot(), bot.getInverso());
		//logger.info("ORDEN GENERADA " + orden.getTipoOrden().name() + " ROBOT -> " + bot);
		//Enviamos al controlador para q esté disponible lo antes posible
		//ArimaBAudCadController.orden = orden.getTipoOrden();

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//Cerramos la operacion anterior en caso q hubiera
		Long fechaHoraMillis = System.currentTimeMillis();
		
		//Actualizamos la tabla con la predicción
		super.actualizarPrediccionBDs(bot.getActivo(), bot.getEstrategia(), bot.getRobot(), orden.getTipoOrden(), prediccion, fechaHoraMillis);
		super.actualizarUltimaOrden(bot.getActivo(), bot.getEstrategia(), orden, fechaHoraMillis);
		super.guardarNuevaOrden(orden, fechaHoraMillis);
		///// 
		
	}
	static Double prediccionArimaAnterior = 0.0;
	@Override
	protected Double calcularPrediccion(Robot bot) {
		Double prediccion = 0.0;
		try {
			
			
			List<HistAudCad> historico = histAudCadRepository.findAllByTimeframeOrderByFechaHoraAsc(bot.getTimeframe());
			
			List<Datos> datosForecast = toDatosList(historico);
			//List<Datos> datosT = loader.loadDatos(configuracion.getFHistoricoLearn());
			List<Datos> datosTotal = new ArrayList<Datos>();
			datosTotal.addAll(datosForecast);
			darylNormalizer.setDatos(datosTotal, Mode.valueOf(configuracion.getMode()));
			
			List<Double> datos = darylNormalizer.getDatos();
	
			
			try {


				ArimaConfig arimaConfig = arimaConfigRepository.findArimaConfigByRobot(robot_config);
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
			        if(forecast > ultimoDato) {
			        	prediccion = 1.0;
			        }
			        if(forecast < ultimoDato) {
			        	prediccion = -1.0;
			        }
	        	}catch (Exception e) {
	        	}
	
			}catch (Exception e) {
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
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
	
	protected void verInputs(List<Double> inputs) {
		StringBuffer buffer = new StringBuffer();
		for (Double input : inputs) {
			buffer.append(darylNormalizer.denormData(input)).append("-");
		}
		System.out.println(buffer.toString());
	}
	
	
}
