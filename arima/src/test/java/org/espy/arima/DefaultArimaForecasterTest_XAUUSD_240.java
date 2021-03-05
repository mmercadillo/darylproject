package org.espy.arima;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class DefaultArimaForecasterTest_XAUUSD_240 {
	
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
        DefaultArimaProcess arimaProcess = new DefaultArimaProcess();
        //arimaProcess.setMaCoefficients(0.1);
        arimaProcess.setMaCoefficients(1.0, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1);
        arimaProcess.setArCoefficients(-1.0, -0.9, -0.8, -0.7, 0.5, 0.4, 0.01, 0.001);
        arimaProcess.setIntegrationOrder(1);
        //arimaProcess.setShockExpectation(-100);
        //arimaProcess.setConstant(10.0);
        //arimaProcess.setShockVariation(10.0);

        /*double[] observations =
                new double[]{ -0.262502, -1.18863, -0.874338, 0.587444, 1.37453, 1.8149, 1.87319, 1.32972, 2.13976,
                              4.93046, 5.24314, 3.38584, 3.1152, 2.84794, 1.04411, 1.5423, 1.85116, 0.968863, -0.173814,
                              -3.43235 };*/
        
        double prediccionAnterior = 0.0;
        double maxRachaGanadoraAux = 0.0;
        double maxRachaPerdedoraAux = 0.0;
        for(int i = 0; i < data2.size(); i++) {
        	
        	if(i < inicio) {
        		data.add(data2.get(i));
        		continue;
        	}

        	double[] observations = getArrayDatos();
	        //System.out.println("Forecast: " + Arrays.toString(observations));
	       
	        ArimaForecaster arimaForecaster = new DefaultArimaForecaster(arimaProcess, observations);
	        double forecast = arimaForecaster.next();
	        
	        System.out.println("Forecast: " + forecast);
	    
	        //Chequeamos el resultado
	        double ultimoDato = data.get(data.size()-1);
	        double prediccion = forecast;
	        
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
	        //System.out.println("Forecast: " + res);
	        //if(prediccionAnterior > 0.0) {
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
	        //}
	        //System.out.println("-----------------------------------------");
	        prediccionAnterior = prediccion;
	        data.add(data2.get(i));
	    
        }
        
        mediaOpsLoss = (double)opLoss / operaciones;
        mediaOpsWin = (double)opWin / operaciones;
        gananciaMediaPorOpWin = resWin / (double)opWin;
        perdidaMediaPorOpLoss = resLoss / (double)opLoss;
        
        System.out.println(arimaProcess.toString());
        System.out.println("Resultado -> " + resultado);
        System.out.println("Operaciones -> " + operaciones);
        System.out.println("Operaciones W -> " + opWin);
        System.out.println("Operaciones L -> " + opLoss);
        System.out.println("Media pts por Op -> " + (double)resultado/operaciones);
        System.out.println("Media de ops W -> " + mediaOpsWin);
        System.out.println("Media de ops L -> " + mediaOpsLoss);
        System.out.println("W Media por op W -> " + gananciaMediaPorOpWin);
        System.out.println("L Media por op L -> " + perdidaMediaPorOpLoss);
        System.out.println("Max G en una Op -> " + maxGananciaEnUnaOp);
        System.out.println("Max L en una Op -> " + maxPerdidaEnUnaOp);
        System.out.println("Max Racha G -> " + maxRachaGanadora);
        System.out.println("Max Racha L -> " + maxRachaPerdedora);
        
    }

    int inicio = 45;
    private double[] getArrayDatos() {
    	
    	List<Double> aux = data.subList((data.size()-inicio), data.size());
    	//List<Double> aux = data;
    	
    	double[] datos = new double[aux.size()];
    	for(int i = 0; i < aux.size(); i++) {
    		datos[i] = aux.get(i).doubleValue();
    	}
    	return datos;
    }
    
    
    
}
