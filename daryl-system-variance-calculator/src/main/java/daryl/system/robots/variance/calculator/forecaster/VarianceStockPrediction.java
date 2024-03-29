package daryl.system.robots.variance.calculator.forecaster;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.utils.BarSeriesUtils;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
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
	private int pagina = 500;

	public VarianceStockPrediction() {
	}
	
	public void init(Activo activo, Timeframes timeframe, String robot, int inicio, int pagina) {
		this.activo = activo;
		this.timeframe = timeframe;
		this.robot = robot;
		this.estrategia = robot;
		this.inicio = inicio;
		this.pagina = pagina;
	}
	
	
	/*private static BarSeries  generateBarList(List<Historico> historico, String name, int multiplicador){
		
		BarSeries series = new BaseBarSeriesBuilder().withName(name).build();
		for (Historico hist : historico) {
			
			Long millis = hist.getFechaHora();
			
			Instant instant = Instant.ofEpochMilli(millis);
			ZonedDateTime barDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
			
			series.addBar(	barDateTime, 
							hist.getApertura() * multiplicador, 
							hist.getMaximo() * multiplicador, 
							hist.getMinimo() * multiplicador, 
							hist.getCierre() * multiplicador, 
							hist.getVolumen() * multiplicador);
			
		}
		
		return series;
		
		
	}*/
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			
	    	//Cargamos las cotizaciones
	    	System.out.println("Cargando cotizaciones para: " + robot);
			if(datos == null) {
				List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraDesc(this.timeframe, this.activo, PageRequest.of(0, this.pagina));
				Collections.reverse(historico);
				BarSeries serieParaCalculo = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + this.timeframe + "_" + this.activo, 1);
				this.datos = serieParaCalculo.getBarData().stream().map(bar -> bar.getClosePrice().doubleValue()).map(Double::new).collect(Collectors.toList());

		    	
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
    	
       	int lastOffset = 0;
       	int lastN = 1;
       	int lastM = 0;
        int lastAlpha = 1;
        int lastBeta = 1;
        int lastShift = 0;

        VarianceConfigCalcs varianceConfig = varianceConfigCalcsRepository.findVarianceConfigCalcsByRobot(robot);
        System.out.println("Config para: " + robot + " -> " + varianceConfig);
        
		if(varianceConfig != null && varianceConfig.getLastOffset() != null) lastOffset = varianceConfig.getLastOffset();
		if(varianceConfig != null && varianceConfig.getLastN() != null) lastN = varianceConfig.getLastN();
		if(varianceConfig != null && varianceConfig.getLastM() != null) lastM = varianceConfig.getLastM();
		if(varianceConfig != null && varianceConfig.getLastAlpha() != null) lastAlpha = varianceConfig.getLastAlpha();
		if(varianceConfig != null && varianceConfig.getLastBeta() != null) lastBeta = varianceConfig.getLastBeta();
		if(varianceConfig != null && varianceConfig.getLastShift() != null) lastShift = varianceConfig.getLastShift();
        
		System.out.println("Empezamos en offset -> " + lastOffset + " y n -> " + lastN + " para: " + robot);
		
        for(int indicadorN = lastN; indicadorN < 20; indicadorN++) {        	
        	for(int indicadorOffset = lastOffset; indicadorOffset < 20; indicadorOffset++) {
        		for(int indicadorM = lastM; indicadorM < 10; indicadorM++) {
        			for(int indicadorAlpha = lastAlpha; indicadorAlpha < 10; indicadorAlpha++) {
        				for(int indicadorBeta = lastBeta; indicadorBeta < 200; indicadorBeta++) {
        					//for(int indicadorShift = lastShift; indicadorShift < 25; indicadorShift++) {
        					
	        					int valorN = indicadorN;
	        					int valorOffset = indicadorOffset;
	        					int valorM = indicadorM;
	        					double valorAlpha = 0.0 + (0.001 * indicadorAlpha);
	        					double valorBeta = 0.0 + (0.1 * indicadorBeta);
	        					//int valorShift = indicadorShift;
	        					int valorShift = 0;
				        		
				                double lastPrice = 0.0;
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
									        			" - Beta: " + valorBeta + 
									        			" - Shift: " + valorShift);
	
						        	int contador = 0;
							        while(true){
							        	
							        	int desde = contador;
							        	int hasta = contador + (valorN + valorOffset + valorShift);
							        	
							        	if(hasta == datos.size()) break;
							        	
							        	List<Double> datosForecast = datos.subList(desde, hasta);
							        	
							        	//System.out.println("Lista de datos: " + datosForecast.toString());
							        	//lastPrice = datos.get(desde);
							            double expectedPrice = datos.get(hasta);
							            double lastExpectedPrice = datos.get(hasta - 1);
							            //System.out.println("Last price -> " + lastPrice);
							            //System.out.println("Expected price -> " + expectedPrice);
							            
							            StockPredict stock = new StockPredict(datosForecast, valorOffset, valorN, valorAlpha, valorBeta, valorM);
							            double[] priceVariance = stock.getPriceVariance();
							
							            predictedPrice = priceVariance[0];
							        	
							            
							            contador++;
	
							            if(predictedPrice > lastPredictedPrice && lastPredictedPrice > 0 && lastExpectedPrice > 0) {
							            	//BUY
							            	if(expectedPrice > lastExpectedPrice) {
							            		ganancias += (expectedPrice - lastExpectedPrice);
							            	}else {
							            		perdidas += (lastExpectedPrice - expectedPrice);
							            	}
							            }
							            if(predictedPrice < lastPredictedPrice && lastPredictedPrice > 0 && lastExpectedPrice > 0) {
							            	//SELL
							            	if(expectedPrice < lastExpectedPrice) {
							            		ganancias += (lastExpectedPrice - expectedPrice);
							            	}else {
							            		perdidas += (expectedPrice - lastExpectedPrice);
							            	}
							            }
							            resultado = ganancias - perdidas;

							            lastPredictedPrice = predictedPrice;
							            
							        }
							        						        
						        }catch (Exception e) {
									e.printStackTrace();
								}finally {
									
						            System.out.println("RESULTADO " + robot + " -> " + resultado);
						            //Comprobamos el resultado
						            varianceConfig = checkResultado(varianceConfig, resultado, valorN, valorOffset, valorM, valorAlpha, valorBeta, indicadorAlpha, indicadorBeta, valorShift);
						            
								}
        					//}
        					//lastShift = 0;
        				}
        				lastBeta = 1;
        			}
        			lastAlpha = 1;
        		}
        		lastM = 0;
        	}
        	lastOffset = 0;
        }
        lastN = 1;	
    }

    
	private VarianceConfigCalcs checkResultado(VarianceConfigCalcs varianceConfig, Double resultado, int n, int offset, int m, double alpha, double beta, int lastAlpha, int lastBeta, int lastShift) {
		
		if(varianceConfig == null) {
			
			varianceConfig = new VarianceConfigCalcs();
			varianceConfig.setRobot(robot);
			varianceConfig.setEstrategia(estrategia);
			varianceConfig.setTipoActivo(activo);
			
		}

		varianceConfig.setLastN(n);
		varianceConfig.setLastOffset(offset);
		varianceConfig.setLastM(m);
		varianceConfig.setLastAlpha(lastAlpha);
		varianceConfig.setLastBeta(lastBeta);
		varianceConfig.setLastShift(lastShift);
		
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
			
		}
		
		varianceConfigCalcsRepository.save(varianceConfig);
		return varianceConfig;
		
	}
    

}