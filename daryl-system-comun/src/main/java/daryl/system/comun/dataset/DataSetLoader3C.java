package daryl.system.comun.dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.springframework.stereotype.Component;

import daryl.system.comun.dataset.aprendizaje.DatoAprendizaje;
import daryl.system.comun.enums.Mode;


public final class DataSetLoader3C extends DataSetLoader{
	
	private static DataSetLoader3C loadDataSet;
	private DataSetLoader3C() {
	}
	
	
	public static DataSetLoader3C getInstance() {
		
		if(DataSetLoader3C.loadDataSet == null) {
			DataSetLoader3C.loadDataSet = new DataSetLoader3C();
		}
		return DataSetLoader3C.loadDataSet;
		
	}
	@Override
	public synchronized void loadData(DataSet dataSet, List<Datos> datos, Mode mode, int neuronasEntrada) {
		
		try {
			for(int i = neuronasEntrada-1; i < datos.size() - 1; i++) {
				
				List<Double> d = new ArrayList<Double>();
				int indice = neuronasEntrada - 1;
				while(Boolean.TRUE) {
					
					if(mode == Mode.CLOSE) {
						d.add(datos.get(i - indice).getCierre());
					}
					if(mode == Mode.HIGH) {
						d.add(datos.get(i - indice).getMaximo());
					}
					if(mode == Mode.LOW) {
						d.add(datos.get(i - indice).getMinimo());
					}
					if(mode == Mode.OPEN) {
						d.add(datos.get(i - indice).getApertura());
					}
					
					indice--;
					if(indice < 0) break;
				}
				
				//Datos dato = datos.get(i);
				//Datos datoAnteAnterior = datos.get(i-2);
				//Datos datoAnterior = datos.get(i-1);

				Datos datoSalida = datos.get(i+1);
				
				Double [] entradas = DatoAprendizaje.getEntradas(d.stream().toArray(Double[]::new));
				
				Double [] salidas = DatoAprendizaje.getSalidas(datoSalida.getCierre());
				
				dataSet.add (
						new DataSetRow (
							Stream.of(entradas).mapToDouble(Double::doubleValue).toArray(), 
							Stream.of(salidas).mapToDouble(Double::doubleValue).toArray()
						)
				);
	
			}
		}catch (Exception ive) {
			System.out.println("Error al cargar los datos");
			ive.printStackTrace();
		}
		System.out.println("");
		
	}
	@Override
	public synchronized void loadInputDataOnly(DataSet dataSet, List<Datos> datos, Mode mode) {
		
		
		for(int i = 2; i < datos.size(); i++) {
			
			
			Datos datoAnteAnterior = datos.get(i-2);
			Datos datoAnterior = datos.get(i-1);
			Datos dato = datos.get(i);
			
			
			Double anteAnterior = 0.0, anterior = 0.0, ultimo = 0.0;
			if(mode == Mode.CLOSE) {
				anteAnterior = datoAnteAnterior.getCierre();
				anterior = datoAnterior.getCierre();
				ultimo = dato.getCierre();
			}
			if(mode == Mode.HIGH) {
				anteAnterior = datoAnteAnterior.getMaximo();
				anterior = datoAnterior.getMaximo();
				ultimo = dato.getMaximo();
			}
			if(mode == Mode.LOW) {
				anteAnterior = datoAnteAnterior.getMinimo();
				anterior = datoAnterior.getMinimo();
				ultimo = dato.getMinimo();
			}
			if(mode == Mode.OPEN) {
				anteAnterior = datoAnteAnterior.getApertura();
				anterior = datoAnterior.getApertura();
				ultimo = dato.getApertura();
			}
			
			Double [] entradas = DatoAprendizaje.getEntradas(	anteAnterior, 
																anterior,
																ultimo);
			
			
			
			dataSet.add (
					new DataSetRow (
						Stream.of(entradas).mapToDouble(Double::doubleValue).toArray()
					)
			);

		}
		
		
	}


	@Override
	public void loadRawData(DataSet dataSet, List<Double> datos, int neuronasEntrada) {
		try {
			for(int i = neuronasEntrada-1; i < datos.size() - 1; i++) {
				
				List<Double> d = new ArrayList<Double>();
				int indice = neuronasEntrada - 1;
				while(Boolean.TRUE) {
					d.add(datos.get(i - indice));
					indice--;
					if(indice < 0) break;
				}
				
				//Datos dato = datos.get(i);
				//Datos datoAnteAnterior = datos.get(i-2);
				//Datos datoAnterior = datos.get(i-1);

				Double datoSalida = datos.get(i+1);
				
				Double [] entradas = DatoAprendizaje.getEntradas(d.stream().toArray(Double[]::new));
				
				Double [] salidas = DatoAprendizaje.getSalidas(datoSalida);
				
				dataSet.add (
						new DataSetRow (
							Stream.of(entradas).mapToDouble(Double::doubleValue).toArray(), 
							Stream.of(salidas).mapToDouble(Double::doubleValue).toArray()
						)
				);
	
			}
		}catch (Exception ive) {
			System.out.println("Error al cargar los datos");
			ive.printStackTrace();
		}
		System.out.println("");
		
	}
	
}
