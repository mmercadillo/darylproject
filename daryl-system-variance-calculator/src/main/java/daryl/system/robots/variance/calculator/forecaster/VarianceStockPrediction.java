package daryl.system.robots.variance.calculator.forecaster;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.VarianceConfig;
import daryl.system.model.VarianceConfigCalcs;
import daryl.system.model.historicos.Historico;
import daryl.system.robots.variance.calculator.repository.IHistoricoRepository;
import daryl.system.robots.variance.calculator.repository.IVarianceConfigCalcsRepository;
import daryl.variance.StockPredict;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VarianceStockPrediction implements Runnable{

	@Autowired
	Logger logger;
	@Autowired
	private IVarianceConfigCalcsRepository varianceConfigCalcsRepository;
	@Autowired
	ConfigData config;
	@Autowired
	private IHistoricoRepository historicoRepository;

	
	private String robot;
	private Activo activo;
	private String estrategia;
	private Timeframes timeframe;
	private int inicio = 0;
	private List<Double> datos = null;

	public VarianceStockPrediction() {
	}
	
	public void init(Activo activo, Timeframes timeframe, String robot, int inicio) {
		this.activo = activo;
		this.timeframe = timeframe;
		this.robot = robot;
		this.estrategia = robot;
		this.inicio = inicio;
	}
	
	private List<Datos> toDatosList(List<Historico> historico){
		
		List<Datos> datos = new ArrayList<Datos>();
		
		for (Historico hist : historico) {
			
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
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			
	    	//Cargamos las cotizaciones
	    	System.out.println("Cargando cotizaciones para: " + robot);
			if(datos == null) {
		    	/*
				//Cargamos la segunda lista
				File ficherodatos2 = new File(BASE_PATH + DATA);
		    	try(BufferedReader reader = new BufferedReader(new FileReader(ficherodatos2))){
		    		datos = new ArrayList<Double>();
		    		String leido;
		    		boolean encabezado = true;
		    		while( (leido = reader.readLine()) != null  ) {
		    			if(encabezado == true) {
		    				encabezado = false;
		    				continue;
		    			}
		    			String[] partes = leido.split(",");
		    			datos.add(new Double(partes[5]));
		    			
		    		}
		    	} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	*/
				List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(this.timeframe, this.activo);
				List<Datos> datosForecast = toDatosList(historico);
				DarylMaxMinNormalizer darylNormalizer = new DarylMaxMinNormalizer(datosForecast, Mode.CLOSE);
				datos = darylNormalizer.getDatos();
		    	
			}
			System.out.println("Cotizaciones cargadas para: " + robot);
			
			
			runPrediction();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
    private void runPrediction() throws FileNotFoundException, IOException, Exception {
    	

       	int offset = 0;
       	int n = 1;
       	int m = 0;
        int alpha = 1;
        int beta = 1;
        int shift = 0;

        VarianceConfigCalcs varianceConfig = varianceConfigCalcsRepository.findVarianceConfigCalcsByRobot(robot);
        System.out.println("Config para: " + robot + " -> " + varianceConfig);
        
		if(varianceConfig != null && varianceConfig.getLastOffset() != null) offset = varianceConfig.getLastOffset();
		if(varianceConfig != null && varianceConfig.getLastN() != null) n = varianceConfig.getLastN();
		if(varianceConfig != null && varianceConfig.getLastM() != null) m = varianceConfig.getLastM();
		if(varianceConfig != null && varianceConfig.getLastAlpha() != null) alpha = varianceConfig.getLastAlpha();
		if(varianceConfig != null && varianceConfig.getLastBeta() != null) beta = varianceConfig.getLastBeta();
        
		System.out.println("Empezamos en offset -> " + offset + " y n -> " + n + " para: " + robot);
		
		
        for(int indicadorN = n; indicadorN < 20; indicadorN++) {        	
        	for(int indicadorOffset = offset; indicadorOffset < 20; indicadorOffset++) {
        		for(int indicadorM = m; indicadorM < 10; indicadorM++) {
        			for(int indicadorAlpha = alpha; indicadorAlpha < 10; indicadorAlpha++) {
        				for(int indicadorBeta = beta; indicadorBeta < 200; indicadorBeta++) {
        					
        					int valorN = indicadorN;
        					int valorOffset = indicadorOffset;
        					int valorM = indicadorM;
        					double valorAlpha = 0.0 + (0.001 * indicadorAlpha);
        					double valorBeta = 0.0 + (0.1 * indicadorBeta);
        					
			        		
			                double lastPrice = 0.0;
			                double expectedPrice = 0.0;
			                double lastExpectedPrice = 0.0;
			                double predictedPrice = 0.0;
			                double lastPredictedPrice = 0.0;
			              
			                double ganancias = 0.0;
			                double perdidas = 0.0;
			                double resultado = 0.0;

					        try {
					        	System.out.println(
								        			"Probando " + robot + 
								        			" con -> n: " + valorN + 
								        			" - offset: " + valorOffset + 
								        			" - m: " + valorM + 
								        			" - Alpha: " + valorAlpha + 
								        			" - Beta: " + valorBeta);

					        	
					        	int contador = 0;
						        while(true){
						        	
						        	
						        	int desde = contador;
						        	int hasta = contador + (valorN + valorOffset + shift);
						        	
						        	if(hasta == datos.size()) break;
						        	
						        	List<Double> datosForecast = datos.subList(desde, hasta);
						        	
						        	//System.out.println("Lista de datos: " + datosForecast.toString());
						        	lastPrice = datos.get(desde);
						            expectedPrice = datos.get(hasta);
						            //System.out.println("Last price -> " + lastPrice);
						            //System.out.println("Expected price -> " + expectedPrice);
						            
						            StockPredict stock = new StockPredict(datosForecast, valorOffset, valorN, valorAlpha, valorBeta, valorM);
						            double[] priceVariance = stock.getPriceVariance();
						
						            predictedPrice = priceVariance[0];
						        	
						            
						            contador++;

						            if(predictedPrice > lastPredictedPrice && lastPredictedPrice > 0) {
						            	//BUY
						            	if(expectedPrice > lastPrice) {
						            		ganancias += (expectedPrice - lastPrice);
						            	}else {
						            		perdidas += (lastPrice - expectedPrice);
						            	}
						            }
						            if(predictedPrice < lastPredictedPrice && lastPredictedPrice > 0) {
						            	//SELL
						            	if(expectedPrice < lastPrice) {
						            		ganancias += (lastPrice - expectedPrice);
						            	}else {
						            		perdidas += (expectedPrice - lastPrice);
						            	}
						            }
						            resultado = ganancias - perdidas;

						            lastExpectedPrice = expectedPrice;
						            lastPredictedPrice = predictedPrice;
						            
						        }
						        						        
					        }catch (Exception e) {
								e.printStackTrace();
							}finally {
								
					            System.out.println("RESULTADO " + robot + " -> " + resultado);
					            //Comprobamos el resultado
					            varianceConfig = checkResultado(varianceConfig, resultado, valorN, valorOffset, valorM, valorAlpha, valorBeta, indicadorAlpha, indicadorBeta);
					            
							}
        				}
        			}
        		}
        	}
        }
        			
    }

    
	private VarianceConfigCalcs checkResultado(VarianceConfigCalcs varianceConfig, Double resultado, int n, int offset, int m, double alpha, double beta, int lastAlpha, int lastBeta) {
		
		if(varianceConfig == null) {
			
			varianceConfig = new VarianceConfigCalcs();
			varianceConfig.setRobot(robot);
			varianceConfig.setEstrategia(estrategia);
			varianceConfig.setTipoActivo(activo);
			
		}
		
		Long fechaHoraMillis = System.currentTimeMillis();
		if(varianceConfig.getResultado() == null || varianceConfig.getResultado() < resultado) {
			
			varianceConfig.setFecha(config.getFechaInString(fechaHoraMillis));
			varianceConfig.setFModificacion(fechaHoraMillis);
			varianceConfig.setHora(config.getHoraInString(fechaHoraMillis));
			varianceConfig.setResultado(resultado);
			varianceConfig.setOffset(offset);
			varianceConfig.setN(n);
			varianceConfig.setM(m);
			varianceConfig.setAlpha(alpha);
			varianceConfig.setBeta(beta);
			
			
			varianceConfig.setLastN(n);
			varianceConfig.setLastOffset(offset);
			varianceConfig.setLastM(m);
			varianceConfig.setLastAlpha(lastAlpha);
			varianceConfig.setLastBeta(lastBeta);
			
			
			varianceConfigCalcsRepository.save(varianceConfig);
		}else {
			
			if(varianceConfig != null) {
				varianceConfig.setLastN(n);
				varianceConfig.setLastOffset(offset);
				varianceConfig.setLastM(m);
				varianceConfig.setLastAlpha(lastAlpha);
				varianceConfig.setLastBeta(lastBeta);
				varianceConfigCalcsRepository.save(varianceConfig);
			}
			
        }
		return varianceConfig;
		
	}
    

}