package daryl.system.control.contizaciones.zeromq.control;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

@Component
public class ControlCotizacionesThread extends Thread {

	@Autowired
	Logger logger;

	
    @Autowired
    private ApplicationContext applicationContext;
	
	
	public void run() {
    	
		try (ZContext context = new ZContext()) {
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.bind("tcp://192.168.0.114:5559");
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("ESPERANDO DATOS DE COTIZACIONES ...");
            	// Block until a message is received
                byte[] reply = socket.recv(0);
                // Print the message
                String cotizacionRecibida = new String(reply, ZMQ.CHARSET);
                logger.info("DATOS RECIBIDOS -> " + cotizacionRecibida);
                
                try {
                	CheckCotizacionThread cct = applicationContext.getBean(CheckCotizacionThread.class);
                	cct.init(cotizacionRecibida);
                	cct.start();
                }catch (Exception e) {
                	logger.error("ERROR EN EL PROCESO -> " + cotizacionRecibida);
                	logger.error(e.getMessage(), e);
				}
                // Enviamos la respuesta al cliente python
                String response = "Datos recibidos, gracias";
                socket.send(response.getBytes(ZMQ.CHARSET), 0);
                
            }
        }catch (Exception e) {
        	logger.error(e.getMessage(), e);
		}finally {
		}
    	
	}	
    
	/*
	private void checkCotizacion(String linea) {
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
		}
    	
	}
	
	private void guardarCotizacion(Cotizacion ctzcn) throws SistemaException {
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
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////
			

		}catch (Exception e) {
			logger.error("NO SE HA PODIDO ACTUALIZAR EL HISTORICO DEL ACTIVO: {}", ctzcn.getActivo(), e);
			throw new SistemaException("No se ha podido actualizar el historico del activo " + ctzcn.getActivo());
		}
	}

	
	protected Boolean checkNuevaCotizacion(Cotizacion cotizacion) throws SistemaException{
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
			throw new SistemaException("No se ha podido recuperar la cotizacion de Checking del activo " + activo.name(), e);
		}		
		
		return noExiste;
	}
	*/
	



}
