package org.ta4j.core;

import java.util.ArrayList;
import java.util.List;

import daryl.system.comun.enums.Mode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MaxMinNormalizer{

	@Getter private List<Double> datos;
	private Double maximo;
	private Double minimo;
	
	public MaxMinNormalizer(BarSeries series, Mode mode) {
		
		this.datos = new ArrayList<Double>();
		for (Bar bar : series.getBarData()) {
			if(mode == Mode.CLOSE) this.datos.add(bar.getClosePrice().doubleValue());
			if(mode == Mode.HIGH) this.datos.add(bar.getHighPrice().doubleValue());
			if(mode == Mode.LOW) this.datos.add(bar.getLowPrice().doubleValue());
			if(mode == Mode.OPEN) this.datos.add(bar.getOpenPrice().doubleValue());
		}
		
		this.maximo = getMaximo();
		this.minimo = getMinimo();
		
	}

	public List<Double> getNormalizedList(){
		
		List<Double> normalizedList = new ArrayList<Double>();
		for (Double d : this.datos) {
			normalizedList.add(normData(d));
		}
		
		return normalizedList;
		
	}

	public List<Double> getDenormalizedList(){
		
		List<Double> denormalizedList = new ArrayList<Double>();
		for (Double d : this.datos) {
			denormalizedList.add(denormData(d));
		}
		
		return denormalizedList;
		
	}
	
	
	public Double getMaximo() {
    	Double maximo = null;
    	
    	for (Double dato : datos) {
			if(maximo == null || maximo < dato) maximo = dato;
		}
    	
    	return maximo;
    }
    
    public Double getMinimo() {
    	Double minimo = null;
    	
    	for (Double dato : datos) {
			if(minimo ==  null || minimo > dato) minimo = dato;
		}
    	
    	return minimo;
    }
    
    public static Double getMaximo(List<Double> cierres) {
    	Double maximo = null;
    	
    	for (Double cierre : cierres) {
			if(maximo == null || maximo < cierre) maximo = cierre;
		}
    	
    	return maximo;
    }
    
    public static Double getMinimo(List<Double> cierres) {
    	Double minimo = null;
    	
    	for (Double cierre : cierres) {
			if(minimo == null || minimo > cierre) minimo = cierre;
		}
    	
    	return minimo;
    }
    
	
    public static Double denormData(Double max, Double min, Double normData) {
    	
    	Double denormData = 1.0;
    	    	
    	denormData = (normData*(max-min))+min;
    	
    	return denormData;
    	
    }
    
    public static Double normData(Double max, Double min, Double denormData) {
    	
    	Double normData = 1.0;    	    	
    	normData = (denormData-min) /(max-min);
    	return normData;
    	
    }
    
    
	
    public Double denormData(Double normData) {
    	
    	Double denormData = 1.0;
    	Double max = this.maximo;
    	Double min = this.minimo; 
    	    	
    	denormData = (normData*(max-min))+min;
    	
    	return denormData;
    	
    }
    
    public Double normData(Double denormData) {
    	
    	Double normData = 1.0;
    	Double max = this.maximo;
    	Double min = this.minimo;
    	
    	normData = (denormData-min) /(max-min);
    	
    	return normData;
    	
    }


	
	
}
