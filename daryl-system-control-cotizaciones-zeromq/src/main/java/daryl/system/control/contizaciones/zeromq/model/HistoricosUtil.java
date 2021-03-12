package daryl.system.control.contizaciones.zeromq.model;

import daryl.system.model.historicos.HistAudCad;
import daryl.system.model.historicos.HistEurUsd;
import daryl.system.model.historicos.HistGdaxi;
import daryl.system.model.historicos.HistNdx;
import daryl.system.model.historicos.HistWti;
import daryl.system.model.historicos.HistXauUsd;

public abstract class HistoricosUtil {

	public static final Boolean compararContizacionNuevaEurUsd(HistEurUsd hist, Cotizacion nuevaCotizacion) {
		
		Boolean esIgual = Boolean.FALSE;
		
		if(hist.getFecha().equalsIgnoreCase(nuevaCotizacion.getFecha()) == Boolean.TRUE 
				&& hist.getHora().equals(nuevaCotizacion.getHora()) == Boolean.TRUE) {
			esIgual = Boolean.TRUE;
		}
		
		double apertura = nuevaCotizacion.getApertura();
		double maximo = nuevaCotizacion.getMaximo();
		double minimo = nuevaCotizacion.getMinimo();
		double cierre = nuevaCotizacion.getCierre();
		if(hist.getApertura() == apertura && 
				hist.getMaximo() == maximo && 
				hist.getMinimo() == minimo && hist.getCierre() == cierre) {
			esIgual = Boolean.TRUE;
		}
		
		return esIgual;
		
	}
	
	public static final Boolean compararContizacionNuevaNdx(HistNdx hist, Cotizacion nuevaCotizacion) {
		
		Boolean esIgual = Boolean.FALSE;
		
		if(hist.getFecha().equalsIgnoreCase(nuevaCotizacion.getFecha()) == Boolean.TRUE 
				&& hist.getHora().equals(nuevaCotizacion.getHora()) == Boolean.TRUE) {
			esIgual = Boolean.TRUE;
		}
		
		double apertura = nuevaCotizacion.getApertura();
		double maximo = nuevaCotizacion.getMaximo();
		double minimo = nuevaCotizacion.getMinimo();
		double cierre = nuevaCotizacion.getCierre();
		if(hist.getApertura() == apertura && 
				hist.getMaximo() == maximo && 
				hist.getMinimo() == minimo && hist.getCierre() == cierre) {
			esIgual = Boolean.TRUE;
		}
		
		return esIgual;
		
	}
	
	public static final Boolean compararContizacionNuevaXauUsd(HistXauUsd hist, Cotizacion nuevaCotizacion) {
		
		Boolean esIgual = Boolean.FALSE;
		
		if(hist.getFecha().equalsIgnoreCase(nuevaCotizacion.getFecha()) == Boolean.TRUE 
				&& hist.getHora().equals(nuevaCotizacion.getHora()) == Boolean.TRUE) {
			esIgual = Boolean.TRUE;
		}
		
		double apertura = nuevaCotizacion.getApertura();
		double maximo = nuevaCotizacion.getMaximo();
		double minimo = nuevaCotizacion.getMinimo();
		double cierre = nuevaCotizacion.getCierre();
		if(hist.getApertura() == apertura && 
				hist.getMaximo() == maximo && 
				hist.getMinimo() == minimo && hist.getCierre() == cierre) {
			esIgual = Boolean.TRUE;
		}
		
		return esIgual;
		
	}
	
	public static final Boolean compararContizacionNuevaGdaxi(HistGdaxi hist, Cotizacion nuevaCotizacion) {
		
		Boolean esIgual = Boolean.FALSE;
		
		if(hist.getFecha().equalsIgnoreCase(nuevaCotizacion.getFecha()) == Boolean.TRUE 
				&& hist.getHora().equals(nuevaCotizacion.getHora()) == Boolean.TRUE) {
			esIgual = Boolean.TRUE;
		}
		
		double apertura = nuevaCotizacion.getApertura();
		double maximo = nuevaCotizacion.getMaximo();
		double minimo = nuevaCotizacion.getMinimo();
		double cierre = nuevaCotizacion.getCierre();
		if(hist.getApertura() == apertura && 
				hist.getMaximo() == maximo && 
				hist.getMinimo() == minimo && hist.getCierre() == cierre) {
			esIgual = Boolean.TRUE;
		}
		
		return esIgual;
		
	}
	
	public static final Boolean compararContizacionNuevaAudCad(HistAudCad hist, Cotizacion nuevaCotizacion) {
		
		Boolean esIgual = Boolean.FALSE;
		
		if(hist.getFecha().equalsIgnoreCase(nuevaCotizacion.getFecha()) == Boolean.TRUE 
				&& hist.getHora().equals(nuevaCotizacion.getHora()) == Boolean.TRUE) {
			esIgual = Boolean.TRUE;
		}
		
		double apertura = nuevaCotizacion.getApertura();
		double maximo = nuevaCotizacion.getMaximo();
		double minimo = nuevaCotizacion.getMinimo();
		double cierre = nuevaCotizacion.getCierre();
		if(hist.getApertura() == apertura && 
				hist.getMaximo() == maximo && 
				hist.getMinimo() == minimo && hist.getCierre() == cierre) {
			esIgual = Boolean.TRUE;
		}
		
		return esIgual;
		
		
	}
	
	public static final Boolean compararContizacionNuevaWti(HistWti hist, Cotizacion nuevaCotizacion) {
		
		Boolean esIgual = Boolean.FALSE;
		
		if(hist.getFecha().equalsIgnoreCase(nuevaCotizacion.getFecha()) == Boolean.TRUE 
				&& hist.getHora().equals(nuevaCotizacion.getHora()) == Boolean.TRUE) {
			esIgual = Boolean.TRUE;
		}
		
		double apertura = nuevaCotizacion.getApertura();
		double maximo = nuevaCotizacion.getMaximo();
		double minimo = nuevaCotizacion.getMinimo();
		double cierre = nuevaCotizacion.getCierre();
		if(hist.getApertura() == apertura && 
				hist.getMaximo() == maximo && 
				hist.getMinimo() == minimo && hist.getCierre() == cierre) {
			esIgual = Boolean.TRUE;
		}
		
		return esIgual;
		
	}
	
	
}
