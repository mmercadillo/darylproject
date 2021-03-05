package daryl.system.control.contizaciones.control;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.comun.exceptions.SistemaException;
import daryl.system.control.contizaciones.control.repository.IHistAudCadRepository;
import daryl.system.control.contizaciones.control.repository.IHistEurUsdRepository;
import daryl.system.control.contizaciones.control.repository.IHistGdaxiRepository;
import daryl.system.control.contizaciones.control.repository.IHistNdxRepository;
import daryl.system.control.contizaciones.control.repository.IHistWtiRepository;
import daryl.system.control.contizaciones.control.repository.IHistXauUsdRepository;
import daryl.system.model.historicos.HistAudCad;
import daryl.system.model.historicos.HistEurUsd;
import daryl.system.model.historicos.HistGdaxi;
import daryl.system.model.historicos.HistNdx;
import daryl.system.model.historicos.HistWti;
import daryl.system.model.historicos.HistXauUsd;
import daryl.system.model.historicos.HistoricosUtil;

public class Control {


	@Autowired
	protected IHistXauUsdRepository histXauUsdRepository;
	@Autowired
	protected IHistGdaxiRepository histGdaxiRepository;
	@Autowired
	protected IHistAudCadRepository histAudCadRepository;
	@Autowired
	protected IHistNdxRepository histNdxRepository;
	@Autowired
	protected IHistEurUsdRepository histEurUsdRepository;
	@Autowired
	protected IHistWtiRepository histWtiRepository;	
	
	protected Boolean checkNuevaCotizacion(Activo activo, String nuevaCotizacion, Timeframes timeframe) throws SistemaException{
		
		Boolean noExiste = Boolean.TRUE;
		
		try {
			
			if(activo == Activo.XAUUSD) {
				HistXauUsd ultimaCotizacionAlmacenada = histXauUsdRepository.findFirstByTimeframeOrderByFechaHoraDesc(timeframe);
				//Comparamos las cotizaciones 
				if(ultimaCotizacionAlmacenada != null && HistoricosUtil.compararContizacionNuevaXauUsd(ultimaCotizacionAlmacenada, nuevaCotizacion) == Boolean.TRUE) {
					noExiste = Boolean.FALSE;
				}
				
			}
			if(activo == Activo.GDAXI) {
				HistGdaxi ultimaCotizacionAlmacenada = histGdaxiRepository.findFirstByTimeframeOrderByFechaHoraDesc(timeframe);
				//Comparamos las cotizaciones 
				if(ultimaCotizacionAlmacenada != null && HistoricosUtil.compararContizacionNuevaGdaxi(ultimaCotizacionAlmacenada, nuevaCotizacion) == Boolean.TRUE) {
					noExiste = Boolean.FALSE;
				}
				
			}
			if(activo == Activo.AUDCAD) {
				HistAudCad ultimaCotizacionAlmacenada = histAudCadRepository.findFirstByTimeframeOrderByFechaHoraDesc(timeframe);
				//Comparamos las cotizaciones 
				if(ultimaCotizacionAlmacenada != null && HistoricosUtil.compararContizacionNuevaAudCad(ultimaCotizacionAlmacenada, nuevaCotizacion) == Boolean.TRUE) {
					noExiste = Boolean.FALSE;
				}
				
			}
			if(activo == Activo.XTIUSD) {
				HistWti ultimaCotizacionAlmacenada = histWtiRepository.findFirstByTimeframeOrderByFechaHoraDesc(timeframe);
				//Comparamos las cotizaciones 
				if(ultimaCotizacionAlmacenada != null && HistoricosUtil.compararContizacionNuevaWti(ultimaCotizacionAlmacenada, nuevaCotizacion) == Boolean.TRUE) {
					noExiste = Boolean.FALSE;
				}
				
			}
			if(activo == Activo.NDX) {
				HistNdx ultimaCotizacionAlmacenada = histNdxRepository.findFirstByTimeframeOrderByFechaHoraDesc(timeframe);
				//Comparamos las cotizaciones 
				if(ultimaCotizacionAlmacenada != null && HistoricosUtil.compararContizacionNuevaNdx(ultimaCotizacionAlmacenada, nuevaCotizacion) == Boolean.TRUE) {
					noExiste = Boolean.FALSE;
				}
				
			}	
			if(activo == Activo.EURUSD) {
				HistEurUsd ultimaCotizacionAlmacenada = histEurUsdRepository.findFirstByTimeframeOrderByFechaHoraDesc(timeframe);
				//Comparamos las cotizaciones 
				if(ultimaCotizacionAlmacenada != null && HistoricosUtil.compararContizacionNuevaEurUsd(ultimaCotizacionAlmacenada, nuevaCotizacion) == Boolean.TRUE) {
					noExiste = Boolean.FALSE;
				}
				
			}	
		}catch (Exception e) {
			//logger.error("No se ha podido recuperar la cotizacion de Checking del activo: {}", activo.name(), e);
			throw new SistemaException("No se ha podido recuperar la cotizacion de Checking del activo " + activo.name(), e);
		}
		

		return noExiste;
		
		
	}
	
	protected Boolean inTime() {
		
		Boolean inTime = Boolean.TRUE;
		
		Calendar c = Calendar.getInstance();
		if(c.get(Calendar.DAY_OF_WEEK)  == Calendar.FRIDAY && c.get(Calendar.HOUR_OF_DAY) > 22) {
			inTime = Boolean.FALSE;
		}
		if(c.get(Calendar.DAY_OF_WEEK)  == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK)  == Calendar.SUNDAY) {
			inTime = Boolean.FALSE;
		}
		if(c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			if(c.get(Calendar.HOUR_OF_DAY) <= 3 ) {
				inTime = Boolean.FALSE;
			}
		}
		
		return inTime;
	}
	
}
