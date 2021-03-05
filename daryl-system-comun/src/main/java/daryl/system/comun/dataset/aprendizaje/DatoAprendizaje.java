package daryl.system.comun.dataset.aprendizaje;

import java.util.ArrayList;
import java.util.List;

public abstract class DatoAprendizaje {
	
	
	public static Double[] getEntradas(String... datosEntrada) {
		
		List<String> entradas = new ArrayList<String>();
		for (String entrada : datosEntrada) {
			entradas.add(entrada);
		}
		
		return entradas.stream().map( d -> Double.parseDouble(d)).toArray(Double[] :: new);
		
		//return Arrays.stream(datosEntrada).toArray(Double[]::parseDouble);
		
	}
	
	public static Double[] getSalidas(String... datosSalida) {
		
		List<String> salidas = new ArrayList<String>();
		for (String salida : datosSalida) {
			salidas.add(salida);
		}
		return salidas.stream().map( d -> Double.parseDouble(d)).toArray(Double[] :: new);
		
		//return Arrays.stream(datosSalida).toArray(Double[]::new);
		
	}
	
	public static Double[] getEntradas(Double... datosEntrada) {
		
		List<Double> entradas = new ArrayList<Double>();
		for (Double entrada : datosEntrada) {
			entradas.add(entrada);
		}
		
		return entradas.stream().toArray(Double[] :: new);
		
		//return Arrays.stream(datosEntrada).toArray(Double[]::parseDouble);
		
	}
	
	public static Double[] getSalidas(Double... datosSalida) {
		
		List<Double> salidas = new ArrayList<Double>();
		for (Double salida : datosSalida) {
			salidas.add(salida);
		}
		return salidas.stream().toArray(Double[] :: new);
		
		//return Arrays.stream(datosSalida).toArray(Double[]::new);
		
	}
	
}