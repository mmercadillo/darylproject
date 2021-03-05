package daryl.system.comun.dataset.normalizer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import lombok.Getter;

@Component
@Scope("prototype")
public class DarylMaxMinNormalizer{

	@Getter private List<Double> datos;
	private Double maximo;
	private Double minimo;

	public void setDatos(List datos, Mode mode) {
		this.datos = new ArrayList<Double>();
		for (Object object : datos) {
			if(object instanceof Double) {
				this.datos.add((Double)object);
			}
			if(object instanceof Datos) {
				if(mode == Mode.CLOSE) this.datos.add(((Datos)object).getCierre());
				if(mode == Mode.HIGH) this.datos.add(((Datos)object).getMaximo());
				if(mode == Mode.LOW) this.datos.add(((Datos)object).getMinimo());
				if(mode == Mode.OPEN) this.datos.add(((Datos)object).getApertura());
			}
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
