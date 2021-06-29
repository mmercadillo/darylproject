package daryl.system.robots.arima.c.calculator.close.forecaster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.espy.arima.ArimaForecaster;
import org.espy.arima.DefaultArimaForecaster;
import org.espy.arima.DefaultArimaProcess;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.ArimaConfigCalcs;
import daryl.system.model.CombinacionArimaC;
import daryl.system.model.historicos.Historico;
import daryl.system.robots.arima.c.calculator.close.repository.IArimaConfigCalcsRepository;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ArimaForecasterGenerator implements Runnable{
	
	@Autowired
	Logger logger;
	@Autowired
	private IArimaConfigCalcsRepository arimaConfigRepository;
	@Autowired
	ConfigData config;

	
	private String robot;
	private Activo tipoActivo;
	private String estrategia;
	private Timeframes timeframe;
	private int inicio = 0;
	private int desviaciones = 0;

	
	private List<CombinacionArimaC> combinacionesFile;
	private List<Double> data2;

	public ArimaForecasterGenerator() {
	}
	
	public void init(String estrategia, String robot, Activo tipoActivo, Timeframes timeframe, int std, int inicio, List<Double> data2, List<CombinacionArimaC> combinacionesFile) {
		this.robot = robot;
		
		this.desviaciones = std;
		this.inicio = inicio;
		this.tipoActivo = tipoActivo;
		this.estrategia = estrategia;
		this.timeframe = timeframe;
		this.combinacionesFile = combinacionesFile;
		this.data2 = data2;
		loadData();
	}

	
	List<Double> data;
	
	Double resultado = 0.0;
	Integer operaciones = 0;
	Integer opWin = 0;
	Integer opLoss = 0;
	Double resWin = 0.0;
	Double resLoss = 0.0;
	Double mediaOpsWin = 0.0;
	Double mediaOpsLoss = 0.0;
	Double gananciaMediaPorOpWin = 0.0;
	Double perdidaMediaPorOpLoss = 0.0;
	Double maxPerdidaEnUnaOp = 0.0;
	Double maxGananciaEnUnaOp = 0.0;
	Double maxRachaGanadora = 0.0;
	Double maxRachaPerdedora = 0.0;
	

	
	public  void loadData() {

		
		resultado = 0.0;
		operaciones = 0;
		opWin = 0;
		opLoss = 0;
		resWin = 0.0;
		resLoss = 0.0;
		mediaOpsWin = 0.0;
		mediaOpsLoss = 0.0;
		gananciaMediaPorOpWin = 0.0;
		perdidaMediaPorOpLoss = 0.0;
		maxPerdidaEnUnaOp = 0.0;
		maxGananciaEnUnaOp = 0.0;
		maxRachaGanadora = 0.0;
		maxRachaPerdedora = 0.0;

		data = new ArrayList<Double>();
		
		
	}



	public  void run() {

		
		ArimaConfigCalcs arimaConfig = arimaConfigRepository.findArimaConfigCalcsByRobot(robot);
		
		int iAux = 0;
		int iiAux = 0;
		int lastStd = 0;
		int lastPosInicio = 0;
		
		if(arimaConfig != null && arimaConfig.getIndexA() != null) iAux = arimaConfig.getIndexA();
		if(arimaConfig != null && arimaConfig.getIndexB() != null) iiAux = arimaConfig.getIndexB();
		if(arimaConfig != null && arimaConfig.getLastStd() != null) lastStd = arimaConfig.getLastStd();
		if(arimaConfig != null && arimaConfig.getLastPosInicio() != null) lastPosInicio = arimaConfig.getLastPosInicio();

		for(int k = iiAux; k < combinacionesFile.size(); k++) {
			
			String combinacionMa = combinacionesFile.get(k).getCombinacion();
			String[] optionsMa = combinacionMa.split(",");
			double[] coefficentsMa = new double[optionsMa.length];
			for(int j = 0; j < optionsMa.length; j++) {
				coefficentsMa[j] = Double.parseDouble(optionsMa[j]);
			}
			
			for(int i = iAux; i < combinacionesFile.size(); i++) {
				
				for(int std = lastStd; std <= desviaciones; std++) {		
					
					String combinacion = combinacionesFile.get(i).getCombinacion();

					String[] optionsAr = combinacion.split(",");
					double[] coefficentsAr = new double[optionsAr.length];
					for(int j = 0; j < optionsAr.length; j++) {
						coefficentsAr[j] = Double.parseDouble(optionsAr[j]);
					}
					//numCombinacion++;
			        
			        try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			        for(int posInicio = lastPosInicio; posInicio <= this.inicio; posInicio++) {
						loadData();
			        	calcular(coefficentsAr, coefficentsMa, std, 1, 0.0, 0.0, 1.0, i, k, posInicio);
			        }
					lastPosInicio = 0;
				}
				lastStd = 0;
			}
			iAux = 0;
		}

	}
	
	boolean seguir = true;
	double resAnt = 0.0;
    public  void calcular(double[] coefficentsAr, double[] coefficentsMa, int std, int integrationOrder,
    		double constant, double shockExpectation, double shockVariation, int indexA, int indexB, int posInicio) {
        
    	DefaultArimaProcess arimaProcess = new DefaultArimaProcess();
        //arimaProcess.setMaCoefficients(0.1);
        if(coefficentsMa != null) arimaProcess.setMaCoefficients(coefficentsMa);
        if(coefficentsAr != null) arimaProcess.setArCoefficients(coefficentsAr);
        arimaProcess.setIntegrationOrder(integrationOrder);
        arimaProcess.setStd(std);
        arimaProcess.setShockExpectation(shockExpectation);
        arimaProcess.setConstant(constant);
        arimaProcess.setShockVariation(shockVariation);

        
        double prediccionAnterior = 0.0;
        double maxRachaGanadoraAux = 0.0;
        double maxRachaPerdedoraAux = 0.0;
    
        for(int i = 0; i < data2.size(); i++) {
        	if(i < posInicio) {
        		data.add(data2.get(i));
        		continue;
        	}

        	double[] observations = getArrayDatos(posInicio);
	        //System.out.println("Forecast: " + Arrays.toString(observations));
        	ArimaForecaster arimaForecaster = null;
        	try {
        		//System.out.println(observations.length);
        		arimaForecaster = new DefaultArimaForecaster(arimaProcess, observations);
        	}catch (Exception e) {
	    	   data.add(data2.get(i));
       			continue;
	       }
        	double forecast = arimaForecaster.next();
       	    
	        //Chequeamos el resultado
	        double ultimoDato = data.get(data.size()-1);
	        double prediccion = forecast;
		        
		    //System.out.println("FORECAST: " + forecast + " ULTIMO DATO: " + ultimoDato);
		                
	        double res = 0.0;
	        //if(prediccionAnterior > 0.0 && prediccionAnterior > prediccion) {
	        if(ultimoDato > prediccion) {
	        	//Bajista
	        	res = (ultimoDato - data2.get(i));
	        }
	        //if(prediccionAnterior > 0.0 && prediccionAnterior < prediccion) {
	        if(ultimoDato < prediccion) {
	        	//Alcista
	        	res = (data2.get(i) - ultimoDato);
	        }

	        resultado += res;
	        if(res > 0) {
	        	resWin += res;
	        	opWin++;
	        	if(maxGananciaEnUnaOp < res) maxGananciaEnUnaOp = res;
	        	maxRachaGanadoraAux += res;
	        	
	        	if(maxRachaPerdedora > maxRachaPerdedoraAux) {
	        		maxRachaPerdedora = maxRachaPerdedoraAux;
	        	}
	        	maxRachaPerdedoraAux = 0.0;
	        	
	        }else {
	        	resLoss += res;
	        	opLoss++;
	        	if(maxPerdidaEnUnaOp > res) maxPerdidaEnUnaOp = res;
	        	maxRachaPerdedoraAux += res;
	        	
	        	if(maxRachaGanadora < maxRachaGanadoraAux) {
	        		maxRachaGanadora = maxRachaGanadoraAux;
	        	}
	        	maxRachaGanadoraAux = 0.0;
	        }
	        operaciones++;

	        prediccionAnterior = prediccion;
	        data.add(data2.get(i));
    
        }

	    mediaOpsLoss = (double)opLoss / operaciones;
	    mediaOpsWin = (double)opWin / operaciones;
	    gananciaMediaPorOpWin = resWin / (double)opWin;
	    perdidaMediaPorOpLoss = resLoss / (double)opLoss;

        
        //System.out.println(numCombinacion + " de " + totalCombinaciones +  " - Resultado " + this.tipoActivo.name() + "_" + this.timeframe.valor + " -> " + resultado + " - " + arimaProcess.toString());
    	//Recuperamos el anterior
    	ArimaConfigCalcs arimaConfig = arimaConfigRepository.findArimaConfigCalcsByRobot(robot);
    	
        logger.info("=================================");
        logger.info("Robot -> " + robot + " Resultado -> " + resultado + " A -> " + indexA + " B -> " + indexB + " std -> " + std + " inicio -> " + posInicio);

        
    	if(arimaConfig == null) {
    		
    		arimaConfig = new ArimaConfigCalcs();
    		arimaConfig.setRobot(robot);
    		arimaConfig.setEstrategia(estrategia);
    		arimaConfig.setTipoActivo(tipoActivo);
    		
    	}

    	
    	arimaConfig.setIndexA(indexA);
    	arimaConfig.setIndexB(indexB);
    	arimaConfig.setLastStd(std);
    	arimaConfig.setLastPosInicio(posInicio);
    	
        if(/*resultado > 0 &&*/ resAnt == 0 || resAnt < resultado) {
        	resAnt = resultado;
        	
        	Long fechaHoraMillis = System.currentTimeMillis();

    		if(arimaConfig.getResultado() == null || arimaConfig.getResultado() < resultado) {

    			arimaConfig.setArCoefficients(Arrays.toString(arimaProcess.getArCoefficients()));
        		arimaConfig.setConstant(arimaProcess.getConstant());
        		arimaConfig.setFecha(config.getFechaInString(fechaHoraMillis));
        		arimaConfig.setFModificacion(fechaHoraMillis);
        		arimaConfig.setHora(config.getHoraInString(fechaHoraMillis));
        		arimaConfig.setIntegrationOrder(arimaProcess.getIntegrationOrder());
        		arimaConfig.setMaCoefficients(Arrays.toString(arimaProcess.getMaCoefficients()));
        		arimaConfig.setShockExpectation(arimaProcess.getShockExpectation());
        		arimaConfig.setShockVariation(arimaProcess.getShockVariation());
        		arimaConfig.setStandarDeviation(arimaProcess.getStd());
        		arimaConfig.setResultado(resultado);
        		arimaConfig.setInicio(posInicio);

    		}
    		
            logger.info(arimaProcess.toString());
            
        }
        arimaConfigRepository.save(arimaConfig);
        logger.info("================================");

    }


    private double[] getArrayDatos(int posInicio) {
    	
    	List<Double> aux = data;
    	if(data.size() > posInicio) {
    		aux = data.subList((data.size()-posInicio), data.size());
    	}else {
    		
    	}
    	//List<Double> aux = data.subList((data.size()-inicio), data.size());
    	
    	
    	double[] datos = new double[aux.size()];
    	for(int i = 0; i < aux.size(); i++) {
    		datos[i] = aux.get(i).doubleValue();
    	}
    	return datos;
    }
    
    
    
}
