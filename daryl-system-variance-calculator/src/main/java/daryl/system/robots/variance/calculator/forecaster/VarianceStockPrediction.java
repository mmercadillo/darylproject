package daryl.system.robots.variance.calculator.forecaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.ArimaConfig;
import daryl.system.model.VarianceConfig;
import daryl.system.robots.variance.calculator.repository.IVarianceConfigRepository;
import daryl.variance.StockPredict;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VarianceStockPrediction implements Runnable{

	@Autowired
	Logger logger;
	@Autowired
	private IVarianceConfigRepository varianceConfigRepository;
	@Autowired
	ConfigData config;
	
	final String BASE_PATH = "C:\\Users\\Admin\\Desktop\\DarylWorkspace\\Historicos\\h\\";
	
	private String robot;
	private Activo activo;
	private String estrategia;
	private Timeframes timeframe;
	private String DATA = "";
	private int inicio = 0;

	public VarianceStockPrediction() {
	}
	
	public void init(Activo activo, Timeframes timeframe, String robot, int inicio) {
		this.activo = activo;
		this.timeframe = timeframe;
		this.DATA = activo.name() + "_" + timeframe.valor + ".csv"; //EURUSD_60.csv
		this.robot = robot;
		this.estrategia = robot;
		this.inicio = inicio;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			runPrediction();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	List<Double> datos = null;
	
    private void runPrediction() throws FileNotFoundException, IOException, Exception {
    	
    	//Cargamos las cotizaciones
    	System.out.println("Cargando cotizaciones para: " + robot);
		if(datos == null) {
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
		}
		System.out.println("Cotizaciones cargadas para: " + robot);

       	int offset = 1;
       	int n = 1;
        double alpha = 0.005;
        double beta = 11.1;

        VarianceConfig varianceConfig = varianceConfigRepository.findVarianceConfigByRobot(robot);
        System.out.println("Config para: " + robot + " -> " + varianceConfig);
        
		if(varianceConfig != null && varianceConfig.getLastOffset() != null) offset = varianceConfig.getLastOffset();
		if(varianceConfig != null && varianceConfig.getLastN() != null) n = varianceConfig.getLastN();
        
		System.out.println("Empezamos en offset -> " + offset + " y n -> " + n + " para: " + robot);
		
		
        for(int i = n; i < 20; i++) {
        	
        	for(int j = offset; j < 20; j++) {
        		
        		int contador = 0;
        		
                double lastPrice = 0.0;
                double expectedPrice = 0.0;
                double lastExpectedPrice = 0.0;
                double predictedPrice = 0.0;
                double lastPredictedPrice = 0.0;
              
                double ganancias = 0.0;
                double perdidas = 0.0;
                double resultado = 0.0;

                List<Double> datos2 = new ArrayList<Double>();
                
		        try {
		        	System.out.println("Probando " + robot + " con -> n: " + i + " - offset: " + j);
		        	
			        while(true){
			
			        	if(contador == datos.size()-1) break;
			        	
			        	datos2.add(0,datos.get(contador));
			
			        	if(contador < (inicio + (i + j) + 1) ) {
			                contador++;
			        		continue;
			            }
			
			            StockPredict stock = new StockPredict(datos2, activo, j, i, alpha, beta);
			            double[] priceVariance = stock.getPriceVariance();
			
			            lastPrice = datos.get(contador);
			            expectedPrice = datos.get(contador+1);
			            predictedPrice = priceVariance[0];

			            
			            contador++;
			            //System.out.println(lastPredictedPrice + " - " + predictedPrice);
			            //////////////////////////////////////////////////////////////////////////////////////
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
			            //System.out.println("Line: " + contador + " fecha: " + cotizsTotal.get(contador).split(",")[0] + " " + cotizsTotal.get(contador).split(",")[1] + " " + resultado);
			            ////////////////////////////////////////////////////////////////////////////////////
			            
			            lastExpectedPrice = expectedPrice;
			            lastPredictedPrice = predictedPrice;
			            
			        }
			        
			        
		        }catch (Exception e) {
					e.printStackTrace();
				}finally {
		            String lineaGenerada = j + "," + i + "," + resultado;
		            System.out.println("RESULTADO " + robot + " -> " + lineaGenerada);

		            //Comprobamos el resultado
		            varianceConfig = checkResultado(varianceConfig, resultado, i, j);
		            
		            
				}
        	}
        }

    }

    
	private VarianceConfig checkResultado(VarianceConfig varianceConfig, Double resultado, int n, int offset) {
		
		if(varianceConfig == null) {
			
			varianceConfig = new VarianceConfig();
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
			varianceConfig.setLastN(n);
			varianceConfig.setLastOffset(offset);
			varianceConfigRepository.save(varianceConfig);
		}else {
			
			if(varianceConfig != null) {
				varianceConfig.setLastN(n);
				varianceConfig.setLastOffset(offset);
				varianceConfigRepository.save(varianceConfig);
			}
			
        }
		return varianceConfig;
		
	}
    

}