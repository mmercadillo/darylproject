package daryl.system.robot.full.node.predictor.arima;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Orden;
import daryl.system.model.Robot;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class ArimaC2Forecaster  extends ArimaCForecaster{

	
	@Override
	protected Orden calcularOperacion(Robot robot, Double prediccion, Boolean inv) {
		
		long millis = System.currentTimeMillis();
		Orden orden = new Orden();
			orden.setFAlta(millis);
			orden.setFBaja(null);
			orden.setEstrategia(robot.getEstrategia());
			orden.setTipoActivo(robot.getActivo());
			orden.setTipoOrden(TipoOrden.CLOSE);
			orden.setRobot(robot.getRobot());
			orden.setFecha(config.getFechaInString(millis));
			orden.setHora(config.getHoraInString(millis));
		
		//recuperamos la orden existente en TF 10080
		String estrategia = "ARIMA_C_" + robot.getActivo() + "_10080";
		Orden orden10080 = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, robot.getActivo(), estrategia);
			
		if(orden10080 != null) {
			if(orden10080.getTipoOrden() == TipoOrden.SELL) {
				if(prediccion <= 0.0 && inv == Boolean.FALSE) {
					orden.setTipoOrden(TipoOrden.SELL);
				}else {
					
				}
				
				if(prediccion >= 0.0 && inv == Boolean.TRUE) {
					orden.setTipoOrden(TipoOrden.SELL);
				}else {
					//orden.setTipoOrden(TipoOrden.CLOSE);
				}
			}else if(orden10080.getTipoOrden() == TipoOrden.BUY) {
				if(prediccion >= 0.0 && inv == Boolean.FALSE) {
					orden.setTipoOrden(TipoOrden.BUY);
				}else {
					//orden.setTipoOrden(TipoOrden.CLOSE);
				}
				
				if(prediccion <= 0.0 && inv == Boolean.TRUE) {
					orden.setTipoOrden(TipoOrden.BUY);
				}else {
					//orden.setTipoOrden(TipoOrden.CLOSE);
				}
			}
		}
		
		return orden;
	}

}
