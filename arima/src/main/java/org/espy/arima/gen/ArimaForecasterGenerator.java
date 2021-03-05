package org.espy.arima.gen;

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

import org.espy.arima.ArimaForecaster;
import org.espy.arima.DefaultArimaForecaster;
import org.espy.arima.DefaultArimaProcess;


public class ArimaForecasterGenerator implements Runnable{
	
	List<Double> dataFile;
	List<Double> data2File;	
	List<String> combinacionesFile;
	
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
	

    
    boolean sinInicio = true;
	
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
		
		if(combinacionesFile == null) {
	    	//Cargamos la segunda lista
			File ficheroCombinaciones = new File(BASE_PATH + COMBINACIONES);
	    	try(BufferedReader reader = new BufferedReader(new FileReader(ficheroCombinaciones))){
	    		
	    		combinacionesFile = new ArrayList<String>();
	    		String leido;
	    		while( (leido = reader.readLine()) != null  ) {
	    			combinacionesFile.add(leido);
	    		}
	    	} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		data = new ArrayList<Double>();
		
		
		if(data2File == null) {
	    	//Cargamos la segunda lista
			File ficherodatos2 = new File(BASE_PATH + DATA);
	    	try(BufferedReader reader = new BufferedReader(new FileReader(ficherodatos2))){
	    		
	    		data2File = new ArrayList<Double>();
	    		String leido;
	    		boolean encabezado = true;
	    		while( (leido = reader.readLine()) != null  ) {
	    			if(encabezado == true) {
	    				encabezado = false;
	    				continue;
	    			}
	    			String[] partes = leido.split(",");
	    			data2File.add(new Double(partes[5]));
	    			
	    		}
	
	    		
	    	} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		data2 = data2File;
		
	}
	
	final String BASE_PATH = "C:\\Users\\Admin\\Desktop\\DarylWorkspace\\Historicos\\"; 
	final String COMBINACIONES = "COMBINACIONES.csv";
	
	
	String DATA = "";
	String RESULTADO = "";
	int inicio = 0;
	int desviaciones = 0;
	String stock = "";
	String period = "";
	
	//int inicio = 480;//60
    //int inicio = 120;//240
    //int inicio = 20;//1440
    //int inicio = 4;//10080
	public ArimaForecasterGenerator(String stock, String period, int std, int inicio) {
		this.DATA = "h\\" + stock + "_" + period + ".csv"; //EURUSD_60.csv
		this.RESULTADO = "res\\" + stock + "_" + period + "_RES.csv";//EURUSD_60_RES.csv
		this.desviaciones = std;
		this.inicio = inicio;
		this.stock = stock;
		this.period = period;
		
		loadData();
		
	}

	long numCombinacion = 0;
	long totalCombinaciones = 0;
	public void run() {
		totalCombinaciones = combinacionesFile.size() * combinacionesFile.size();
		for(int std = 0; std <= desviaciones; std++) {
			for(String combinacion : combinacionesFile) {
				
				if(seguir == false) break;
				
				
				String[] optionsAr = combinacion.split(",");
				 double[] coefficentsAr = new double[optionsAr.length];
				 for(int j = 0; j < optionsAr.length; j++) {
					 coefficentsAr[j] = Double.parseDouble(optionsAr[j]);
				 }
				 //numCombinacion++;
				 //calcular(coefficentsAr, null, std);
				 
				
				for(String comb2: combinacionesFile) {
					numCombinacion++;
					if(seguir == false) break;
					loadData();
					
					String[] optionsMa = comb2.split(",");
					 double[] coefficentsMa = new double[optionsMa.length];
					 for(int j = 0; j < optionsMa.length; j++) {
						 coefficentsMa[j] = Double.parseDouble(optionsMa[j]);
					 }
					
					calcular(coefficentsAr, coefficentsMa, std);
				}
				
			}
		}

	}
	
	boolean seguir = true;
	double resAnt = 0.0;
    public void calcular(double[] coefficentsAr, double[] coefficentsMa, int std) {
        

		
    	DefaultArimaProcess arimaProcess = new DefaultArimaProcess();
        //arimaProcess.setMaCoefficients(0.1);
        if(coefficentsMa != null) arimaProcess.setMaCoefficients(coefficentsMa);
        arimaProcess.setArCoefficients(coefficentsAr);
        arimaProcess.setIntegrationOrder(1);
        arimaProcess.setStd(std);
        //arimaProcess.setShockExpectation(-100);
        //arimaProcess.setConstant(10.0);
        //arimaProcess.setShockVariation(10.0);

        
        double prediccionAnterior = 0.0;
        double maxRachaGanadoraAux = 0.0;
        double maxRachaPerdedoraAux = 0.0;
    
        for(int i = 0; i < data2.size(); i++) {
    	
        	if(i < inicio && sinInicio == false) {
        		data.add(data2.get(i));
        		continue;
        	}

        	double[] observations = getArrayDatos();
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
        
	        //System.out.println("Forecast: " + forecast);
	    
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
        
        System.out.println(numCombinacion + " de " + totalCombinaciones +  " - Resultado " + this.stock + "_" + this.period + " -> " + resultado + " - " + arimaProcess.toString());
        if(resultado > 0 && resAnt < resultado) {
        	resAnt = resultado;
	        try {
		        
	        	FileWriter fw = new FileWriter(BASE_PATH + RESULTADO, true);
	        	PrintWriter pw = new PrintWriter(fw);
	        
	        	pw.println(arimaProcess.toString());
	        
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
        }
        if(resultado > 1500) {
        	//seguir = false;
        }

    }


    private double[] getArrayDatos() {
    	
    	List<Double> aux = data;
    	if(data.size() > inicio) {
    		aux = data.subList((data.size()-inicio), data.size());
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
