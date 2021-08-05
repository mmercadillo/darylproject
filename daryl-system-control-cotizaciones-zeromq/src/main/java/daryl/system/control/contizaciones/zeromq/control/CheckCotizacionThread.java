package daryl.system.control.contizaciones.zeromq.control;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.CanalAmq;
import daryl.system.comun.enums.Timeframes;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.control.contizaciones.zeromq.Sender;
import daryl.system.control.contizaciones.zeromq.model.Cotizacion;
import daryl.system.control.contizaciones.zeromq.model.HistoricosUtil;
import daryl.system.control.contizaciones.zeromq.repository.IHistoricoRepository;
import daryl.system.control.contizaciones.zeromq.repository.IOrdenRepository;
import daryl.system.control.contizaciones.zeromq.repository.IRobotsRepository;
import daryl.system.model.Orden;
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CheckCotizacionThread extends Thread {

	@Autowired
	Logger logger;
	
	@Autowired
	private ConfigData config;
	@Autowired
	private Sender sender;

	@Autowired
	private IHistoricoRepository histRepository;	
	@Autowired
	private IRobotsRepository robotsRepository;
	@Autowired
	private IOrdenRepository ordenRepository;
	
	private String cotizacionRecibida;
	
	public void init(String cotizacionRecibida) {
		this.cotizacionRecibida = cotizacionRecibida;
	}
	
	public void run() {
    	
		try{
			logger.info("PROCESO CHECK COTIZACIÓN INICIADO -> " + this.cotizacionRecibida);
			checkCotizacion(this.cotizacionRecibida);
			logger.info("PROCESO CHECK COTIZACIÓN FINALIZADO -> " + this.cotizacionRecibida);
        }catch (Exception e) {
        	logger.error(e.getMessage(), e);
		}finally {
		}
    	
	}	
	
	private void checkCotizacion(String linea) throws Exception{
		try {
			Cotizacion ctzcn = Cotizacion.getCotizacionFromZeroMQ(linea);
			Boolean noExiste = checkNuevaCotizacion(ctzcn);
			if(noExiste == Boolean.TRUE) {
				logger.info("GUARDAMOS NUEVA COTIZACIÓN -> " + linea);
				guardarCotizacion(ctzcn);
				logger.info("NUEVA COTIZACIÓN GUARDADA");
				
				
				if(config.checkFechaHoraOperaciones() == Boolean.TRUE) {
					try {
	
						List<CanalAmq> canales = Arrays.asList(CanalAmq.values());
						for(CanalAmq canal : canales) {
							
							List<Robot> robots = robotsRepository.findRobotsByActivoAndTimeframeAndCanal(ctzcn.getActivo(), ctzcn.getTimeframe(), canal);	
							
							//Enviamos el mensaje al nodo principal de control de los robots
							logger.info("SE ENVIA SEÑAL AL CANAL= " + canal + " TF= " + ctzcn.getTimeframe().name() + " ROBOTS= " + robots);
							sender.send(canal.name(), new Gson().toJson(robots));
							logger.info("SEÑAL ENVIADA AL CANAL= " + canal + " TF= " + ctzcn.getTimeframe().name() + " ROBOTS= " + robots);
							////////////////////////////////////////////////////////////////////////////////
							
						}
					}catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}

			}else {
				logger.info("LA COTIZACIÓN YA EXISTE-> " + linea);
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
    	
	}
	
	
	/*
	private void checkCotizacion(String linea) throws Exception{
		try {
			Cotizacion ctzcn = Cotizacion.getCotizacionFromZeroMQ(linea);
			Boolean noExiste = checkNuevaCotizacion(ctzcn);
			if(noExiste == Boolean.TRUE) {
				logger.info("GUARDAMOS NUEVA COTIZACIÓN -> " + linea);
				guardarCotizacion(ctzcn);
				logger.info("NUEVA COTIZACIÓN GUARDADA");
				try {

					List<Robot> robots = robotsRepository.findRobotsByActivoAndTimeframe(ctzcn.getActivo(), ctzcn.getTimeframe());
					
					for(Robot robot : robots) {
						
						if(robot.getRobotActivo() == Boolean.TRUE && config.checkFechaHoraOperaciones() == Boolean.TRUE) {
							logger.info("SE ENVIA SEÑAL AL ROBOT " + robot.getRobot() + " TF= " + ctzcn.getTimeframe().name());
							sender.send(robot.getCanal().name(), new Gson().toJson(robot));
							logger.info("SEÑAL ENVIADA AL ROBOT " + robot.getRobot() + " TF= " + ctzcn.getTimeframe().name());
						}
						
						//Cerramos todas las operaciones de cada robot
						//en caso de estar fuera de hora
						if(config.checkFechaHoraOperaciones() == Boolean.FALSE || robot.getRobotActivo() == Boolean.FALSE) {
							try {
								long millis = System.currentTimeMillis();
								
								Orden orden = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, robot.getActivo(), robot.getEstrategia());
								if(orden != null) {
									orden.setFecha(config.getFechaInString(millis));
									orden.setHora(config.getHoraInString(millis));
									orden.setTipoOrden(TipoOrden.CLOSE);
									ordenRepository.save(orden);
									logger.info("ORDEN CERRADA POR CIERRE DE MERCADO -> " + orden);
								}else {
									logger.info("NO EXISTE ORDEN PARA EL ROBOT -> " + robot);
								}
								
							}catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
						}
					}
					
					//Enviamos el mensaje al nodo principal de control de los robots
					//logger.info("SE ENVIA SEÑAL AL CANAL CHNL_FULL_NODE " + robots + " TF= " + ctzcn.getTimeframe().name());
					//sender.send("CHNL_FULL_NODE", new Gson().toJson(robots));
					//logger.info("SEÑAL ENVIADA AL CANAL CHNL_FULL_NODE TF= " + ctzcn.getTimeframe().name());
					////////////////////////////////////////////////////////////////////////////////
					
				}catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}else {
				logger.info("LA COTIZACIÓN YA EXISTE-> " + linea);
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
    	
	}
	*/
	
	private void guardarCotizacion(Cotizacion ctzcn) throws Exception {
		//Guardamos en la tabla correspondiente la nueva cotizaion
		try {

			//Guardamos en la tabla Historico
			try {
				
				Historico historico = new Historico();
					historico.setFecha(ctzcn.getFecha());
					historico.setHora(ctzcn.getHora());
					historico.setApertura(new Double(ctzcn.getApertura()));
					historico.setMaximo(new Double(ctzcn.getMaximo()));
					historico.setMinimo(new Double(ctzcn.getMinimo()));
					historico.setCierre(new Double(ctzcn.getCierre()));
					historico.setVolumen(new Double(ctzcn.getVolumen()));
					historico.setFechaHora(config.getFechaHoraInMillis(ctzcn.getFecha(), ctzcn.getHora()));
					historico.setTimeframe(ctzcn.getTimeframe());
					historico.setActivo(ctzcn.getActivo());
				
				histRepository.save(historico);
				
			}catch (Exception e) {
				throw e;
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////
			

		}catch (Exception e) {
			logger.error("NO SE HA PODIDO ACTUALIZAR EL HISTORICO DEL ACTIVO: {}", ctzcn.getActivo(), e);
			throw new Exception("No se ha podido actualizar el historico del activo " + ctzcn.getActivo());
		}
	}
	
	private Boolean checkNuevaCotizacion(Cotizacion cotizacion) throws Exception{
		Boolean noExiste = Boolean.TRUE;
		
		Activo activo = cotizacion.getActivo();
		Timeframes timeframe = cotizacion.getTimeframe();
		
		try {
			
			Historico ultimaCotizacionAlmacenada = histRepository.findFirstByTimeframeAndActivoOrderByFechaHoraDesc(timeframe, activo);
			if(ultimaCotizacionAlmacenada != null && HistoricosUtil.compararContizacionNueva(ultimaCotizacionAlmacenada, cotizacion) == Boolean.TRUE) {
				noExiste = Boolean.FALSE;
			}
	
		}catch (Exception e) {
			logger.error("ERROR AL COMPROBAR LA ÚLTIMA COTIZACIÓN ALMACENADA: {}", activo.name(), e);
			throw new Exception("No se ha podido recuperar la cotizacion de Checking del activo " + activo.name(), e);
		}		
		
		return noExiste;
	}

}
