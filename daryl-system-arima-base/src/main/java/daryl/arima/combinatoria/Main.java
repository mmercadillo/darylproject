package daryl.arima.combinatoria;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		
		String[] valores = {
				"10.0", "9.0", "8.0", "7.0", "6.0", "5.0", "4.0", "3.0", "2.0", "1.0", 
				"-10.0", "-9.0", "-8.0", "-7.0", "-6.0", "-5.0", "-4.0", "-3.0", "-2.0", "-1.0",
				"0.9", "0.95", "-0.9", "-0.95",
				"0.8", "0.85", "-0.8", "-0.85",
				"0.7", "0.75", "-0.7", "-0.75",
				"0.6", "0.65", "-0.6", "-0.65",
				"0.5", "0.55", "-0.5", "-0.55",
				"0.4", "0.45", "-0.4", "-0.45",
				"0.3", "0.35", "-0.3", "-0.35",
				"0.2", "0.25", "-0.2", "-0.25",
				"0.1", "0.15", "-0.1", "-0.15",
				"0.05", "-0.05", "0.025", "-0.025",
				"0.01", "0.015", "-0.01", "-0.015",
				"0.001", "0.0015", "-0.001", "-0.0015"
			};
		List<String> lista = Arrays.asList(valores);
		
		for(int i = 1; i < lista.size(); i++) {
			
			List<String> sublista = lista.subList(0, i);
			Logica log = new Logica();
			log.start(sublista.stream().toArray(String[] :: new));
			//break;
		}
		

	}
}
