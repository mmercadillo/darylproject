package org.espy.arima;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import daryl.arima.gen.ARIMA;

@Ignore
public class DarylArimaForecasterTest {
	
	List<Double> data;
	List<Double> data2;
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
	
	@Before
	public void loadData() {
		
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
		
		//Cargamos la primera lista
		File ficherodatos = new File("C:\\Users\\Admin\\Desktop\\DarylWorkspace\\Historicos\\Junit\\XAUUSD_240.csv");
    	try(BufferedReader reader = new BufferedReader(new FileReader(ficherodatos))){
    		
    		data = new ArrayList<Double>();
    		String leido;
    		while( (leido = reader.readLine()) != null  ) {
    			
    			String[] partes = leido.split(",");
    			data.add(new Double(partes[5]));
    			
    		}

    		
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//Cargamos la segunda lista
		File ficherodatos2 = new File("C:\\Users\\Admin\\Desktop\\DarylWorkspace\\Historicos\\Junit\\XAUUSD_240_2.csv");
    	try(BufferedReader reader = new BufferedReader(new FileReader(ficherodatos2))){
    		
    		data2 = new ArrayList<Double>();
    		String leido;
    		while( (leido = reader.readLine()) != null  ) {
    			
    			String[] partes = leido.split(",");
    			data2.add(new Double(partes[5]));
    			
    		}

    		
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
    @Test
    public void test() {
    	
    	for(int k = 3; k <= 25; k++) {
    		
    		System.out.println("K = " + k);
    		
	        double maxRachaGanadoraAux = 0.0;
	        double maxRachaPerdedoraAux = 0.0;
	        for(int i = 0; i < data2.size(); i++) {
	        	
	        	double[] observations = getArrayDatos();
	        	
				ARIMA arima = new ARIMA(observations,k);
				
				int[] model = arima.getARIMAmodel();
				//System.out.println("Best model is [p,q]="+"["+model[0]+" "+model[1]+"]");
				//System.out.println("Predict value="+arima.aftDeal(arima.predictValue(model[0],model[1])));
				//System.out.println("Predict error="+(arima.aftDeal(arima.predictValue(model[0],model[1]))-datos.get(datos.size()-1))/datos.get(datos.size()-1)*100+"%");
			
				//prediccion = arima.aftDeal(arima.predictValue(model[0],model[1])) - datos.get(datos.size()-1);
				double forecast = (double)arima.aftDeal(arima.predictValue(model[0],model[1]));
	
		        //System.out.println("Forecast: " + forecast);
		    
		        //Chequeamos el resultado
		        double ultimoDato = data.get(data.size()-1);
		        double prediccion = forecast;
		        
		        double res = 0.0;
		        if(ultimoDato > prediccion) {
		        	//Bajista
		        	res = (ultimoDato - data2.get(i));
		        }
		        if(ultimoDato < prediccion) {
		        	//Alcista
		        	res = (data2.get(i) - ultimoDato);
		        }
		        //System.out.println("Forecast: " + res);
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
		        
		        
		        data.add(data2.get(i));
		    
	        }
	        
	        mediaOpsLoss = (double)opLoss / operaciones;
	        mediaOpsWin = (double)opWin / operaciones;
	        gananciaMediaPorOpWin = resWin / (double)opWin;
	        perdidaMediaPorOpLoss = resLoss / (double)opLoss;
	        try {
	        
	        	FileWriter fw = new FileWriter("C:\\Users\\Admin\\Desktop\\DarylWorkspace\\Historicos\\Junit\\XAUUSD_240_RES.csv", true);
	        	PrintWriter pw = new PrintWriter(fw);
	        
	        	pw.println(k);
	        
	        	System.out.println("Resultado -> " + resultado);
	        	pw.println("Resultado -> " + resultado);
	        
		        System.out.println("Operaciones -> " + operaciones);
		        pw.println("Operaciones -> " + operaciones);
		        
		        System.out.println("Operaciones W -> " + opWin);
		        pw.println("Operaciones W -> " + opWin);
		        System.out.println("Operaciones L -> " + opLoss);
		        pw.println("Operaciones L -> " + opLoss);
		        System.out.println("Media pts por Op -> " + (double)resultado/operaciones);
		        pw.println("Media pts por Op -> " + (double)resultado/operaciones);
		        System.out.println("Media de ops W -> " + mediaOpsWin);
		        pw.println("Media de ops W -> " + mediaOpsWin);
		        System.out.println("Media de ops L -> " + mediaOpsLoss);
		        pw.println("Media de ops L -> " + mediaOpsLoss);
		        System.out.println("W Media por op W -> " + gananciaMediaPorOpWin);
		        pw.println("W Media por op W -> " + gananciaMediaPorOpWin);
		        System.out.println("L Media por op L -> " + perdidaMediaPorOpLoss);
		        pw.println("L Media por op L -> " + perdidaMediaPorOpLoss);
		        System.out.println("Max G en una Op -> " + maxGananciaEnUnaOp);
		        pw.println("Max G en una Op -> " + maxGananciaEnUnaOp);
		        System.out.println("Max L en una Op -> " + maxPerdidaEnUnaOp);
		        pw.println("Max L en una Op -> " + maxPerdidaEnUnaOp);
		        System.out.println("Max Racha G -> " + maxRachaGanadora);
		        pw.println("Max Racha G -> " + maxRachaGanadora);
		        System.out.println("Max Racha L -> " + maxRachaPerdedora);
		        pw.println("Max Racha L -> " + maxRachaPerdedora);
		        System.out.println("-----------------------------------------");
		        pw.println("-----------------------------------------");
		        
		        pw.flush();
		        pw.close();
	        
	        }catch (Exception e) {
				e.printStackTrace();
			}
	        loadData();
    	}
        
    }


    private double[] getArrayDatos() {
    	
    	//List<Double> aux = data.subList((data.size()-inicio), data.size());
    	List<Double> aux = data;
    	
    	double[] datos = new double[aux.size()];
    	for(int i = 0; i < aux.size(); i++) {
    		datos[i] = aux.get(i).doubleValue();
    	}
    	return datos;
    }
    
    
    
}
