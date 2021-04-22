package otro;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Hon on 3/5/2015.
 */
public class MyDemoStockPrediction implements Runnable{

	private String symbol = null;
	private String fileName = null;
	
	public MyDemoStockPrediction(String symbol) {
		this.symbol = symbol;
		this.fileName =  "StockData/" + symbol + ".csv";
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			runPrediction();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void escribirNuevoResultado(String fileName, String linea) {
			
		try {
			
			FileWriter fw = new FileWriter(new File(fileName), Boolean.FALSE);
			PrintWriter pw = new PrintWriter(fw);
			
			pw.println(linea);
			pw.flush();
			pw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	
	private String leerResultadoAnterior(String fileName) {
		
		String resultado = null;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader (new File(fileName)));
	    	resultado = reader.readLine();
	    	reader.close();
			
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			System.out.println("Fichero no encontrado, se crea uno: " + e.getMessage());
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Fichero no encontrado, se crea uno: " + e.getMessage());
		}
		
		
		
		return resultado;
		
	}
	
	private void checkResultado(String lineaNueva, String fileName) {
		

		//Recuperamos el anterior
		String resAnterior = leerResultadoAnterior(fileName);
		if(resAnterior != null && !resAnterior.equals("")) {
			String[] datosAnteriores = resAnterior.split(",");
			
			//Nuevos datos
			String[] datosNuevos = lineaNueva.split(",");
			
			//Comparamos los resultados
			if(Double.parseDouble(datosNuevos[2]) > Double.parseDouble(datosAnteriores[2])) {
				//Como el nuevo es mayor almacenamos el nuevo
				System.out.println("Nueva linea guardada -> " + lineaNueva);
				escribirNuevoResultado(fileName, lineaNueva);
			}
		}else {
			//Como el nuevo es mayor almacenamos el nuevo
			System.out.println("Nueva linea guardada -> " + lineaNueva);
			escribirNuevoResultado(fileName, lineaNueva);
		}
		
	}

    private void runPrediction() throws FileNotFoundException, IOException {
    	
     	List<String> cotizsTotal = new ArrayList<String>();
    	
    	
    	//Leemos el fichero con las cotizaciones
    	BufferedReader reader = new BufferedReader(new FileReader (new File(fileName)));
    	
    	String leido;
    	Boolean encabezado = true;
    	while( (leido = reader.readLine()) != null ) {
    		if(encabezado == true) {
    			encabezado = false;
    			continue;
    		}
    		cotizsTotal.add(leido);
    	}
    	
        System.out.printf("\n-------------------- DEMO MODE --------------------\n");

       	
       	
       	int offset = 1;
       	int n = 1;
       	
        String cont = "yes"; // this variable is used to judge whether the user want to continue

        
        for(n = 1; n < 20; n++) {
        	
        	for(offset = 1; offset < 20; offset++) {
        		
        		int contador = 0;
        		
                double lastPrice = 0.0;
                double expectedPrice = 0.0;
                double lastExpectedPrice = 0.0;
                double predictedPrice = 0.0;
                double lastPredictedPrice = 0.0;
              
                double ganancias = 0.0;
                double perdidas = 0.0;
                double resultado = 0.0;

                List<String> cotizs = new ArrayList<String>();
		        try {
		        	System.out.println("Probando " + symbol + " con -> n: " + n + " - offset: " + offset);
		        	
			        while(true){
			
			        	if(contador == cotizsTotal.size()-1) break;
			        	
			        	cotizs.add(0,cotizsTotal.get(contador));
			
			        	if(contador < ((offset + n) + 1) ) {
			                contador++;
			        		continue;
			            }
			
			            StockPredict stock = new StockPredict(cotizs, symbol, offset, n);
			            double[] priceVariance = stock.getPriceVariance();
			
			            lastPrice = Double.parseDouble(cotizsTotal.get(contador).split(",")[5]);
			            expectedPrice = Double.parseDouble(cotizsTotal.get(contador+1).split(",")[5]);
			            predictedPrice = priceVariance[0];
			            
			            /*
			            System.out.printf("\n-------------- The prediction is: --------------\n\n");
			            System.out.println("Line: " + contador + " fecha: " + cotizsTotal.get(contador).split(",")[0] + " " + cotizsTotal.get(contador).split(",")[1]);
			            System.out.printf("The last price is: %f\n", lastPrice);
			            System.out.printf("The expeted price is: %f\n", expectedPrice);
			            System.out.printf("The predicted price is: %f\n", predictedPrice);
			            System.out.printf("The predicted variance is: %f\n", priceVariance[1]);
			            System.out.printf("The price would be likely in this range: %f ~ %f\n", priceVariance[0] - 3 * priceVariance[1], priceVariance[0] + 3 * priceVariance[1]);
			            System.out.printf("\n---------------------- END ----------------------\n");
						*/
			            
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
			            
			            
			
			            //System.out.println("Do you want to continue? (yes/anything)");
			            //cont = sc.nextLine();
			            //System.out.printf("\n");
			            //Thread.sleep(10);
			            
			            //if(cotizsTotal.get(contador).split(",")[0].equals("20191231")) break;
			        }
			        
			        
		        }catch (Exception e) {
					e.printStackTrace();
				}finally {
		            String lineaGenerada = offset + "," + n + "," + resultado;
		            System.out.println("RESULTADO " + symbol + " -> " + lineaGenerada);
		            checkResultado(lineaGenerada, "StockData/" + symbol + "_RES.csv");
				}
        	}
        }

    }

    
    public static void main(String[] args) {
    	try {
			new MyDemoStockPrediction("GDAXI_240").runPrediction();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}