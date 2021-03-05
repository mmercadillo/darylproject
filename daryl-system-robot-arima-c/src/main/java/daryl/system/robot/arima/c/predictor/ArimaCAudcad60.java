package daryl.system.robot.arima.c.predictor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.espy.arima.ArimaForecaster;
import org.espy.arima.ArimaProcess;
import org.espy.arima.DefaultArimaForecaster;
import org.espy.arima.DefaultArimaProcess;
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
import daryl.system.model.historicos.HistAudCad;
import daryl.system.robot.arima.c.predictor.base.ArimaPredictor;
import daryl.system.robot.arima.c.predictor.config.ConfiguracionArimaAudCad60;
import daryl.system.robot.arima.c.repository.IArimaConfigRepository;
import daryl.system.robot.arima.c.repository.IHistAudCadRepository;
import lombok.ToString;

@Component(value = "arimaCAudcad60")
@ToString
public class ArimaCAudcad60  extends ArimaPredictor{
	
	@Autowired
	IArimaConfigRepository arimaConfigRepository;
	
	@Autowired(required = true)
	ConfiguracionArimaAudCad60 configuracion;
	@Autowired
	private DataSetLoader dataSetLoader;
	@Autowired
	private DarylMaxMinNormalizer darylNormalizer;
	@Autowired
	private IHistAudCadRepository histAudCadRepository;
	
	private List<HistAudCad> historico;
	private List<Datos> datosTotal;
	private Integer inicio;
	
	private final String robot= "ARIMA_C_AUDCAD_60";
	
	@PostConstruct
	public void load() {
		
		DatosLoader loader = DatosLoaderOHLC.getInstance();
		datosTotal = loader.loadDatos(configuracion.getFHistoricoLearn());
		//List<Datos> datosTotal = loader.loadDatos(configuracion.getFHistoricoLearn());
	}
	
	private ArimaProcess getArimaProcess() {
		ArimaConfig arimaConfig = arimaConfigRepository.findArimaConfigByRobot(robot);
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
		Orden orden = calcularOperacion(activo, estrategia, prediccion, robot);
		
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
	static Integer prediccionArimaAnterior = 0;
	@Override
	protected Double calcularPrediccion() {
		Double prediccion = 0.0;
		try {
			
			
			historico = histAudCadRepository.findAllByTimeframeOrderByFechaHoraAsc(Timeframes.PERIOD_H1);
			
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

				DefaultArimaProcess arimaProcess = (DefaultArimaProcess)getArimaProcess();
		    	/*
				DefaultArimaProcess arimaProcess = new DefaultArimaProcess();
		        if(coefficentsMa != null) arimaProcess.setMaCoefficients(coefficentsMa);
		        if(coefficentsAr != null) arimaProcess.setArCoefficients(coefficentsAr);
		        arimaProcess.setIntegrationOrder(1);
		        arimaProcess.setStd(configuracion.getStd());
		        //arimaProcess.setShockExpectation(-100);
		        //arimaProcess.setConstant(10.0);
		        //arimaProcess.setShockVariation(10.0);
		         */
		        
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
	        	}catch (Exception e) {
	        	}
		        double forecast = arimaForecaster.next();			
				
		        //Chequeamos el resultado
		        double ultimoDato = datos.get(datos.size()-1);
		        prediccion = forecast;
		        
		        System.out.println("PREDICCION: " + prediccion + " ULT DATO: " + ultimoDato);
		        System.out.println(arimaProcess.toString());
		        
		        if(ultimoDato > prediccion) {
		        	//Bajista
		        	prediccion = -1.0;
		        }
		        //if(prediccionAnterior > 0.0 && prediccionAnterior < prediccion) {
		        if(ultimoDato < prediccion) {
		        	//Alcista
		        	prediccion = 1.0;
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
