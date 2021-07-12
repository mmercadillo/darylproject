package daryl.system.control.contizaciones.zeromq.model;

import daryl.system.model.historicos.Historico;

public abstract class HistoricosUtil {

	public static final Boolean compararContizacionNueva(Historico hist, Cotizacion nuevaCotizacion) {
		
		Boolean esIgual = Boolean.FALSE;
		
		if(hist.getFecha().equalsIgnoreCase(nuevaCotizacion.getFecha()) == Boolean.TRUE 
				&& hist.getHora().equals(nuevaCotizacion.getHora()) == Boolean.TRUE) {
			esIgual = Boolean.TRUE;
		}
		
		if(esIgual == Boolean.FALSE) {
			double apertura = nuevaCotizacion.getApertura();
			double maximo = nuevaCotizacion.getMaximo();
			double minimo = nuevaCotizacion.getMinimo();
			double cierre = nuevaCotizacion.getCierre();
			if(hist.getApertura() == apertura && 
					hist.getMaximo() == maximo && 
					hist.getMinimo() == minimo && hist.getCierre() == cierre) {
				esIgual = Boolean.TRUE;
			}
		}
		
		return esIgual;
		
	}

}
