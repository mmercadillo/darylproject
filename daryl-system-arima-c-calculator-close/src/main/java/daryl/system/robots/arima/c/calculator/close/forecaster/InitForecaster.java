package daryl.system.robots.arima.c.calculator.close.forecaster;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InitForecaster {
	
	//int inicio = 480;//60
    //int inicio = 120;//240
    //int inicio = 20;//1440
    //int inicio = 4;//10080
	
	//AUDCAD
	//EURUSD
	//XAUUSD
	//USATECHIDXUSD
	//DEUIDXEUR
	public static void main(String[] args) {
		
		ExecutorService servicio = Executors.newFixedThreadPool(20);
		
		//XAUUSD
		/*
		ArimaForecasterGenerator afgXAUUSD_60 = new ArimaForecasterGenerator("XAUUSD", "60", 0, 480);
		servicio.submit(afgXAUUSD_60);
		
		ArimaForecasterGenerator afgXAUUSD_240 = new ArimaForecasterGenerator("XAUUSD", "240", 2, 120);
		servicio.submit(afgXAUUSD_240);		
		
		ArimaForecasterGenerator afgXAUUSD_1440 = new ArimaForecasterGenerator("XAUUSD", "1440", 3, 20);
		servicio.submit(afgXAUUSD_1440);
		
		ArimaForecasterGenerator afgXAUUSD_10080 = new ArimaForecasterGenerator("XAUUSD", "10080", 4, 4);
		servicio.submit(afgXAUUSD_10080);
		
		//NDX
		ArimaForecasterGenerator afgNDX_60 = new ArimaForecasterGenerator("NDX", "60", 0, 480);
		servicio.submit(afgNDX_60);
		
		ArimaForecasterGenerator afgNDX_240 = new ArimaForecasterGenerator("NDX", "240", 2, 120);
		servicio.submit(afgNDX_240);		
		
		ArimaForecasterGenerator afgNDX_1440 = new ArimaForecasterGenerator("NDX", "1440", 3, 20);
		servicio.submit(afgNDX_1440);
		
		ArimaForecasterGenerator afgNDX_10080 = new ArimaForecasterGenerator("NDX", "10080", 4, 4);
		servicio.submit(afgNDX_10080);
		
		//GDAXI
		ArimaForecasterGenerator afgGDAXI_60 = new ArimaForecasterGenerator("GDAXI", "60", 0, 480);
		servicio.submit(afgGDAXI_60);
		
		ArimaForecasterGenerator afgGDAXI_240 = new ArimaForecasterGenerator("GDAXI", "240", 2, 120);
		servicio.submit(afgGDAXI_240);		
		
		ArimaForecasterGenerator afgGDAXI_1440 = new ArimaForecasterGenerator("GDAXI", "1440", 3, 20);
		servicio.submit(afgGDAXI_1440);
		
		ArimaForecasterGenerator afgGDAXI_10080 = new ArimaForecasterGenerator("GDAXI", "10080", 4, 4);
		servicio.submit(afgGDAXI_10080);
		
		//EURUSD
		ArimaForecasterGenerator afgEURUSD_60 = new ArimaForecasterGenerator("EURUSD", "60", 0, 480);
		servicio.submit(afgEURUSD_60);
		
		ArimaForecasterGenerator afgEURUSD_240 = new ArimaForecasterGenerator("EURUSD", "240", 2, 120);
		servicio.submit(afgEURUSD_240);		
		
		ArimaForecasterGenerator afgEURUSD_1440 = new ArimaForecasterGenerator("EURUSD", "1440", 3, 20);
		servicio.submit(afgEURUSD_1440);
		
		ArimaForecasterGenerator afgEURUSD_10080 = new ArimaForecasterGenerator("EURUSD", "10080", 4, 4);
		servicio.submit(afgEURUSD_10080);
		
		//AUDCAD
		ArimaForecasterGenerator afgAUDCAD_60 = new ArimaForecasterGenerator("AUDCAD", "60", 0, 480);
		servicio.submit(afgAUDCAD_60);
		
		ArimaForecasterGenerator afgAUDCAD_240 = new ArimaForecasterGenerator("AUDCAD", "240", 2, 120);
		servicio.submit(afgAUDCAD_240);		
		
		ArimaForecasterGenerator afgAUDCAD_1440 = new ArimaForecasterGenerator("AUDCAD", "1440", 3, 20);
		servicio.submit(afgAUDCAD_1440);
		
		ArimaForecasterGenerator afgAUDCAD_10080 = new ArimaForecasterGenerator("AUDCAD", "10080", 4, 4);
		servicio.submit(afgAUDCAD_10080);
		
		servicio.shutdown();
		*/
		
	}
	
}
