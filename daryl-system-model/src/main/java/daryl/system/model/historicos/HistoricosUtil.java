package daryl.system.model.historicos;

public abstract class HistoricosUtil {

	public static final Boolean compararContizacionNuevaEurUsd(HistEurUsd hist, String nuevaCotizacion) {
		
		Boolean esIgual = Boolean.FALSE;
		
		String tokens[] = nuevaCotizacion.split(",");
			
		if(hist.getFecha().equalsIgnoreCase(tokens[0]) == Boolean.TRUE && hist.getHora().equals(tokens[1]) == Boolean.TRUE) {
			esIgual = Boolean.TRUE;
		}
		
		double apertura = Double.parseDouble(tokens[2]);
		double maximo = Double.parseDouble(tokens[3]);
		double minimo = Double.parseDouble(tokens[4]);
		double cierre = Double.parseDouble(tokens[5]);
		if(hist.getApertura() == apertura && 
				hist.getMaximo() == maximo && 
				hist.getMinimo() == minimo && hist.getCierre() == cierre) {
			esIgual = Boolean.TRUE;
		}
		
		return esIgual;
		
	}
	
	public static final Boolean compararContizacionNuevaNdx(HistNdx hist, String nuevaCotizacion) {
		
		Boolean esIgual = Boolean.FALSE;
		
		String tokens[] = nuevaCotizacion.split(",");
			
		if(hist.getFecha().equalsIgnoreCase(tokens[0]) == Boolean.TRUE && hist.getHora().equals(tokens[1]) == Boolean.TRUE) {
			esIgual = Boolean.TRUE;
		}
		
		double apertura = Double.parseDouble(tokens[2]);
		double maximo = Double.parseDouble(tokens[3]);
		double minimo = Double.parseDouble(tokens[4]);
		double cierre = Double.parseDouble(tokens[5]);
		if(hist.getApertura() == apertura && 
				hist.getMaximo() == maximo && 
				hist.getMinimo() == minimo && hist.getCierre() == cierre) {
			esIgual = Boolean.TRUE;
		}
		
		return esIgual;
		
	}
	
	public static final Boolean compararContizacionNuevaXauUsd(HistXauUsd hist, String nuevaCotizacion) {
		
		Boolean esIgual = Boolean.FALSE;
		
		String tokens[] = nuevaCotizacion.split(",");
			
		if(hist.getFecha().equalsIgnoreCase(tokens[0]) == Boolean.TRUE && hist.getHora().equals(tokens[1]) == Boolean.TRUE) {
			esIgual = Boolean.TRUE;
		}
		
		double apertura = Double.parseDouble(tokens[2]);
		double maximo = Double.parseDouble(tokens[3]);
		double minimo = Double.parseDouble(tokens[4]);
		double cierre = Double.parseDouble(tokens[5]);
		if(hist.getApertura() == apertura && 
				hist.getMaximo() == maximo && 
				hist.getMinimo() == minimo && hist.getCierre() == cierre) {
			esIgual = Boolean.TRUE;
		}
		
		return esIgual;
		
	}
	
	public static final Boolean compararContizacionNuevaGdaxi(HistGdaxi hist, String nuevaCotizacion) {
		
		Boolean esIgual = Boolean.FALSE;
		
		String tokens[] = nuevaCotizacion.split(",");
			
		if(hist.getFecha().equalsIgnoreCase(tokens[0]) == Boolean.TRUE && hist.getHora().equals(tokens[1]) == Boolean.TRUE) {
			esIgual = Boolean.TRUE;
		}
		
		double apertura = Double.parseDouble(tokens[2]);
		double maximo = Double.parseDouble(tokens[3]);
		double minimo = Double.parseDouble(tokens[4]);
		double cierre = Double.parseDouble(tokens[5]);
		if(hist.getApertura() == apertura && 
				hist.getMaximo() == maximo && 
				hist.getMinimo() == minimo && hist.getCierre() == cierre) {
			esIgual = Boolean.TRUE;
		}
		
		return esIgual;
		
	}
	
	public static final Boolean compararContizacionNuevaAudCad(HistAudCad hist, String nuevaCotizacion) {
		
		Boolean esIgual = Boolean.FALSE;
		
		String tokens[] = nuevaCotizacion.split(",");
			
		if(hist.getFecha().equalsIgnoreCase(tokens[0]) == Boolean.TRUE && hist.getHora().equals(tokens[1]) == Boolean.TRUE) {
			esIgual = Boolean.TRUE;
		}
		
		double apertura = Double.parseDouble(tokens[2]);
		double maximo = Double.parseDouble(tokens[3]);
		double minimo = Double.parseDouble(tokens[4]);
		double cierre = Double.parseDouble(tokens[5]);
		if(hist.getApertura() == apertura && 
				hist.getMaximo() == maximo && 
				hist.getMinimo() == minimo && hist.getCierre() == cierre) {
			esIgual = Boolean.TRUE;
		}
		
		return esIgual;
		
		
	}
	
	public static final Boolean compararContizacionNuevaWti(HistWti hist, String nuevaCotizacion) {
		
		Boolean esIgual = Boolean.FALSE;
		
		String tokens[] = nuevaCotizacion.split(",");
			
		if(hist.getFecha().equalsIgnoreCase(tokens[0]) == Boolean.TRUE && hist.getHora().equals(tokens[1]) == Boolean.TRUE) {
			esIgual = Boolean.TRUE;
		}
		
		double apertura = Double.parseDouble(tokens[2]);
		double maximo = Double.parseDouble(tokens[3]);
		double minimo = Double.parseDouble(tokens[4]);
		double cierre = Double.parseDouble(tokens[5]);
		if(hist.getApertura() == apertura && 
				hist.getMaximo() == maximo && 
				hist.getMinimo() == minimo && hist.getCierre() == cierre) {
			esIgual = Boolean.TRUE;
		}
		
		return esIgual;
		
	}
	
	
}
