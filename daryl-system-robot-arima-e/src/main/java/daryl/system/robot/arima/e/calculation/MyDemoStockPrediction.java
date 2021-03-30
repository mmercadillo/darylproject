package otro;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Hon on 3/5/2015.
 */
public class MyDemoStockPrediction {

    public void runPrediction() throws FileNotFoundException, IOException {
    	
     	List<String> cotizsTotal = new ArrayList<String>();
    	
    	String symbol = "GDAXI_60";
    	String fileName = "StockData/" + symbol + ".csv";
    	//Leemos el fichero con las cotizaciones
    	BufferedReader reader = new BufferedReader(new FileReader (new File(fileName)));
    	
    	String leido;
    	while( (leido = reader.readLine()) != null ) {
    		cotizsTotal.add(leido);
    	}
    	
        System.out.printf("\n-------------------- DEMO MODE --------------------\n");

        Scanner sc = new Scanner(System.in);
       	List<String> cotizs = new ArrayList<String>();
       	int contador = 0;
       	int offset = 15;
       	int n = 1;
       	
        String cont = "yes"; // this variable is used to judge whether the user want to continue
        double lastPrice = 0.0;
        double expectedPrice = 0.0;
        double lastExpectedPrice = 0.0;
        double predictedPrice = 0.0;
        double lastPredictedPrice = 0.0;
        
        
        double ganancias = 0.0;
        double perdidas = 0.0;
        double resultado = 0.0;
        try {
	        while(cont.equals("yes")){
	
	        	cotizs.add(0,cotizsTotal.get(contador));
	
	        	if(contador < offset + n + 10 ) {
	                contador++;
	        		continue;
	            }
	
	            StockPredict stock = new StockPredict(cotizs, symbol, offset, n);
	            double[] priceVariance = stock.getPriceVariance();
	
	            lastPrice = Double.parseDouble(cotizsTotal.get(contador).split(",")[5]);
	            expectedPrice = Double.parseDouble(cotizsTotal.get(contador+1).split(",")[5]);
	            predictedPrice = priceVariance[0];
	            
	            System.out.printf("\n-------------- The prediction is: --------------\n\n");
	            System.out.println("Line: " + contador + " fecha: " + cotizsTotal.get(contador).split(",")[0] + " " + cotizsTotal.get(contador).split(",")[1]);
	            System.out.printf("The last price is: %f\n", lastPrice);
	            System.out.printf("The expeted price is: %f\n", expectedPrice);
	            System.out.printf("The predicted price is: %f\n", predictedPrice);
	            System.out.printf("The predicted variance is: %f\n", priceVariance[1]);
	            System.out.printf("The price would be likely in this range: %f ~ %f\n", priceVariance[0] - 3 * priceVariance[1], priceVariance[0] + 3 * priceVariance[1]);
	            System.out.printf("\n---------------------- END ----------------------\n");
	
	            
	            contador++;
	            System.out.println(lastPredictedPrice + " - " + predictedPrice);
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
	            System.out.println("RESULTADO -> " + resultado);
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
		}

    }

    
    public static void main(String[] args) {
    	try {
			new MyDemoStockPrediction().runPrediction();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}