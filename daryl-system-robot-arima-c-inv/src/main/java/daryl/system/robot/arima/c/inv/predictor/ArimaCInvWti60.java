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
import daryl.system.model.historicos.HistWti;
import daryl.system.robot.arima.c.inv.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.c.inv.predictor.config.ConfiguracionArimaWti60;
import daryl.system.robot.arima.c.inv.repository.IArimaConfigRepository;
import daryl.system.robot.arima.c.inv.repository.IHistWtiRepository;
import lombok.ToString;

@Component(value = "arimaCInvWti60")
@ToString
public class ArimaCInvWti60  extends ArimaPredictor{
	
	@Autowired
	Logger logger;
	
	@Autowired
	IArimaConfigRepository arimaConfigRepository;
	
	@Autowired(required = true)
	ConfiguracionArimaWti60 configuracion;
	@Autowired
	private DataSetLoader dataSetLoader;
	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistWtiRepository histWtiRepository;
	
	private List<HistWti> historico;
	private List<Datos> datosTotal;
	private Integer inicio;
	
	private final String robot= "ARIMA_I_C_WTI_60";
	private final String robot_config= "ARIMA_C_WTI_60";
	private final Boolean inv = Boolean.TRUE;
	
	@PostConstruct
	public void load() {
		
		DatosLoader loader = DatosLoaderOHLC.getInstance();
		datosTotal = loader.loadDatos(configuracion.getFHistoricoLearn());
		//List<Datos> datosTotal = loader.loadDatos(configuracion.getFHistoricoLearn());
	}
	
	private ArimaProcess getArimaProcess() {
		ArimaConfig arimaConfig = arimaConfigRepository.findArimaConfigByRobot(robot_config);
		this.inicio = arimaConfig.getInicio();
		double[] coefficentsAr = null;
		try {
			
			String arTxt = arimaConfig.getArCoefficients();
			arTxt = arTxt.substring(1, arTxt.length()-1);
			
			String[] optionsAr = arTxt.trim().split(",");
			coefficentsAr = new double[optionsAr.length];
			for(int j = 0; j < optionsAr.length; j++) {
				coefficentsAr[j] = Double.parseDouble(optionsAr[j]);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		double[] coefficentsMa = null;
		try {
			
			String maTxt = arimaConfig.getMaCoefficients();
			maTxt = maTxt.substring(1, maTxt.length()-1);
			
			String[] optionsMa = maTxt.trim().split(",");
			coefficentsMa = new double[optionsMa.length];
			for(int j = 0; j < optionsMa.length; j++) {
				coefficentsMa[j] = Double.parseDouble(optionsMa[j]);
			}				
		}catch (Exception e) {
			e.printStackTrace();
		}

		
    	DefaultArimaProcess arimaProcess = new DefaultArimaProcess();
        if(coefficentsMa != null) arimaProcess.setMaCoefficients(coefficentsMa);
        if(coefficentsAr != null) arimaProcess.setArCoefficients(coefficentsAr);
        arimaProcess.setIntegrationOrder(arimaConfig.getIntegrationOrder());
        arimaProcess.setStd(arimaConfig.getStandarDeviation());
        arimaProcess.setShockExpectation(arimaConfig.getShockExpectation());
        arimaProcess.setConstant(arimaConfig.getConstant());
        arimaProcess.setShockVariation(arimaConfig.getShockVariation());
        
        System.out.println(arimaProcess);
        return arimaProcess;
		
		
	}

	@Override
	public void calculate(Activo activo, String estrategia) {
		//Calcular la predicción
		System.out.println("-----------------------------------------------------------------------------------------------------------------");
		Double prediccion = calcularPrediccion();
		//logger.info("Nueva predicción para el ROBOT " + robot + " : {} a las: {}" , prediccion, config.getActualDateFormattedInString());
		
				
		//actualizamos el fichero de ordenes
		Orden orden = calcularOperacion(activo, estrategia, prediccion, robot, inv);
		
		//Enviamos al controlador para q esté disponible lo antes posible
		//ArimaBAudCadController.orden = orden.getTipoOrden();

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//Cerramos la operacion anterior en caso q hubiera
		Long fechaHoraMillis = System.currentTimeMillis();
		
		//Actualizamos la tabla con la predicción
		super.actualizarPrediccionBDs(activo, estrategia, orden.getTipoOrden(), prediccion, fechaHoraMillis);
		super.actualizarUltimaOrden(activo, estrategia, orden, fechaHoraMillis);
		super.guardarNuevaOrden(orden, fechaHoraMillis);
		///// 
		
	}
	static Double prediccionArimaAnterior = 0.0;
	@Override
	protected Double calcularPrediccion() {
		Double prediccion = 0.0;
		try {
			
			
			historico = histWtiRepository.findAllByTimeframeOrderByFechaHoraAsc(Timeframes.PERIOD_H1);
			
			List<Datos> datosForecast = toDatosList(historico);
			//List<Datos> datosT = loader.loadDatos(configuracion.getFHistoricoLearn());
			
			datosTotal.addAll(datosForecast);
			darylNormalizer.setDatos(datosTotal, Mode.valueOf(configuracion.getMode()));
			
			List<Double> datos = darylNormalizer.getDatos();
			

			
			try {

				DefaultArimaProcess arimaProcess = (DefaultArimaProcess)getArimaProcess();
	
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

	
	private List<Datos> toDatosList(List<HistWti> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (HistWti hist : historico) {
			
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
