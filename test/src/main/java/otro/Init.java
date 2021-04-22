package otro;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Init {

	public static void main(String[] args) {
		
		String[] activos = {"AUDCAD_60","EURUSD_60","NDX_60","GDAXI_60","XAUUSD_60",
							"AUDCAD_240","EURUSD_240","NDX_240"/*,"GDAXI_240"*/,"XAUUSD_240",
							"AUDCAD_1440","EURUSD_1440","NDX_1440","GDAXI_1440","XAUUSD_1440"
							};
		
		ExecutorService servicio = Executors.newFixedThreadPool(15);
		
		for (String activo : activos) {
			servicio.submit(new MyDemoStockPrediction(activo));
		}
		

		servicio.shutdown();
	}

}
