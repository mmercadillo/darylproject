package daryl.system.control.contizaciones.zeromq.control;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import com.google.gson.Gson;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.comun.exceptions.SistemaException;
import daryl.system.control.contizaciones.zeromq.Sender;
import daryl.system.control.contizaciones.zeromq.model.Cotizacion;
import daryl.system.control.contizaciones.zeromq.model.HistoricosUtil;
import daryl.system.control.contizaciones.zeromq.repository.IHistAudCadRepository;
import daryl.system.control.contizaciones.zeromq.repository.IHistEurUsdRepository;
import daryl.system.control.contizaciones.zeromq.repository.IHistGdaxiRepository;
import daryl.system.control.contizaciones.zeromq.repository.IHistNdxRepository;
import daryl.system.control.contizaciones.zeromq.repository.IHistWtiRepository;
import daryl.system.control.contizaciones.zeromq.repository.IHistXauUsdRepository;
import daryl.system.control.contizaciones.zeromq.repository.IRobotsRepository;
import daryl.system.model.Robot;
import daryl.system.model.historicos.HistAudCad;
import daryl.system.model.historicos.HistEurUsd;
import daryl.system.model.historicos.HistGdaxi;
import daryl.system.model.historicos.HistNdx;
import daryl.system.model.historicos.HistWti;
import daryl.system.model.historicos.HistXauUsd;

@Component
public class ControlCotizaciones extends Thread {

	@Autowired
	Logger logger;
	
	@Autowired
	private ConfigData config;
	@Autowired
	private Sender sender;


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

	@Autowired
	private IRobotsRepository robotsRepository;

	public void run() {
    	
    	
		try (ZContext context = new ZContext()) {
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.bind("tcp://*:5559");
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("ESPERANDO DATOS DE COTIZACIONES ...");
            	// Block until a message is received
                byte[] reply = socket.recv(0);
                // Print the message
                String cotizacionesRecibidas = new String(reply, ZMQ.CHARSET);
                logger.info("DATOS RECIBIDOS -> " + cotizacionesRecibidas);
               
                // Enviamos la respuesta al cliente python
                String response = "Datos recibidos, gracias";
                socket.send(response.getBytes(ZMQ.CHARSET), 0);


                checkCotizacion(cotizacionesRecibidas);
                
                
            }
        }catch (Exception e) {
        	logger.error(e.getMessage(), e);
		}finally {
		}
    	
	}	
    
	
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
						
						if(robot.getRobotActivo() == Boolean.TRUE) {
							logger.info("SE ENVIA SEÑAL AL ROBOT " + robot.getRobot() + " TF= " + ctzcn.getTimeframe().name());
							sender.send(robot.getCanal().name(), new Gson().toJson(robot));
							logger.info("SEÑAL ENVIADA AL ROBOT " + robot.getRobot() + " TF= " + ctzcn.getTimeframe().name());
						}
						
					}
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

			if(ctzcn.getActivo() == Activo.XAUUSD) {
				HistXauUsd historico = new HistXauUsd();
					historico.setFecha(ctzcn.getFecha());
					historico.setHora(ctzcn.getHora());
					historico.setApertura(new Double(ctzcn.getApertura()));
					historico.setMaximo(new Double(ctzcn.getMaximo()));
					historico.setMinimo(new Double(ctzcn.getMinimo()));
					historico.setCierre(new Double(ctzcn.getCierre()));
					historico.setVolumen(new Double(ctzcn.getVolumen()));
					historico.setFechaHora(config.getFechaHoraInMillis(ctzcn.getFecha(), ctzcn.getHora()));
					historico.setTimeframe(ctzcn.getTimeframe());
					
				histXauUsdRepository.save(historico);
				logger.info("COTIZACIÓN PARA EL ACTIVO {} GUARDADA: {}", ctzcn.getActivo(), ctzcn.toString());
			}							
			if(ctzcn.getActivo() == Activo.AUDCAD) {
				HistAudCad historico = new HistAudCad();
					historico.setFecha(ctzcn.getFecha());
					historico.setHora(ctzcn.getHora());
					historico.setApertura(new Double(ctzcn.getApertura()));
					historico.setMaximo(new Double(ctzcn.getMaximo()));
					historico.setMinimo(new Double(ctzcn.getMinimo()));
					historico.setCierre(new Double(ctzcn.getCierre()));
					historico.setVolumen(new Double(ctzcn.getVolumen()));
					historico.setFechaHora(config.getFechaHoraInMillis(ctzcn.getFecha(), ctzcn.getHora()));
					historico.setTimeframe(ctzcn.getTimeframe());
				
				histAudCadRepository.save(historico);
				logger.info("COTIZACIÓN PARA EL ACTIVO {} GUARDADA: {}", ctzcn.getActivo(), ctzcn.toString());
			}
			if(ctzcn.getActivo() == Activo.XTIUSD) {
				HistWti historico = new HistWti();
					historico.setFecha(ctzcn.getFecha());
					historico.setHora(ctzcn.getHora());
					historico.setApertura(new Double(ctzcn.getApertura()));
					historico.setMaximo(new Double(ctzcn.getMaximo()));
					historico.setMinimo(new Double(ctzcn.getMinimo()));
					historico.setCierre(new Double(ctzcn.getCierre()));
					historico.setVolumen(new Double(ctzcn.getVolumen()));
					historico.setFechaHora(config.getFechaHoraInMillis(ctzcn.getFecha(), ctzcn.getHora()));
					historico.setTimeframe(ctzcn.getTimeframe());
				
				histWtiRepository.save(historico);
				logger.info("COTIZACIÓN PARA EL ACTIVO {} GUARDADA: {}", ctzcn.getActivo(), ctzcn.toString());
			}
			if(ctzcn.getActivo() == Activo.GDAXI) {
				HistGdaxi historico = new HistGdaxi();
					historico.setFecha(ctzcn.getFecha());
					historico.setHora(ctzcn.getHora());
					historico.setApertura(new Double(ctzcn.getApertura()));
					historico.setMaximo(new Double(ctzcn.getMaximo()));
					historico.setMinimo(new Double(ctzcn.getMinimo()));
					historico.setCierre(new Double(ctzcn.getCierre()));
					historico.setVolumen(new Double(ctzcn.getVolumen()));
					historico.setFechaHora(config.getFechaHoraInMillis(ctzcn.getFecha(), ctzcn.getHora()));
					historico.setTimeframe(ctzcn.getTimeframe());
					
				histGdaxiRepository.save(historico);
				logger.info("COTIZACIÓN PARA EL ACTIVO {} GUARDADA: {}", ctzcn.getActivo(), ctzcn.toString());
			}
			if(ctzcn.getActivo() == Activo.NDX) {
				HistNdx historico = new HistNdx();
					historico.setFecha(ctzcn.getFecha());
					historico.setHora(ctzcn.getHora());
					historico.setApertura(new Double(ctzcn.getApertura()));
					historico.setMaximo(new Double(ctzcn.getMaximo()));
					historico.setMinimo(new Double(ctzcn.getMinimo()));
					historico.setCierre(new Double(ctzcn.getCierre()));
					historico.setVolumen(new Double(ctzcn.getVolumen()));
					historico.setFechaHora(config.getFechaHoraInMillis(ctzcn.getFecha(), ctzcn.getHora()));
					historico.setTimeframe(ctzcn.getTimeframe());
					
				histNdxRepository.save(historico);
				logger.info("COTIZACIÓN PARA EL ACTIVO {} GUARDADA: {}", ctzcn.getActivo(), ctzcn.toString());
			}
			if(ctzcn.getActivo() == Activo.EURUSD) {
				HistEurUsd historico = new HistEurUsd();
					historico.setFecha(ctzcn.getFecha());
					historico.setHora(ctzcn.getHora());
					historico.setApertura(new Double(ctzcn.getApertura()));
					historico.setMaximo(new Double(ctzcn.getMaximo()));
					historico.setMinimo(new Double(ctzcn.getMinimo()));
					historico.setCierre(new Double(ctzcn.getCierre()));
					historico.setVolumen(new Double(ctzcn.getVolumen()));
					historico.setFechaHora(config.getFechaHoraInMillis(ctzcn.getFecha(), ctzcn.getHora()));
					historico.setTimeframe(ctzcn.getTimeframe());
					
				histEurUsdRepository.save(historico);
				logger.info("COTIZACIÓN PARA EL ACTIVO {} GUARDADA: {}", ctzcn.getActivo(), ctzcn.toString());
			}
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
			
			if(activo == Activo.XAUUSD) {
				HistXauUsd ultimaCotizacionAlmacenada = histXauUsdRepository.findFirstByTimeframeOrderByFechaHoraDesc(timeframe);
				//Comparamos las cotizaciones 
				if(ultimaCotizacionAlmacenada != null && HistoricosUtil.compararContizacionNuevaXauUsd(ultimaCotizacionAlmacenada, cotizacion) == Boolean.TRUE) {
					noExiste = Boolean.FALSE;
				}
				
			}
			if(activo == Activo.GDAXI) {
				HistGdaxi ultimaCotizacionAlmacenada = histGdaxiRepository.findFirstByTimeframeOrderByFechaHoraDesc(timeframe);
				//Comparamos las cotizaciones 
				if(ultimaCotizacionAlmacenada != null && HistoricosUtil.compararContizacionNuevaGdaxi(ultimaCotizacionAlmacenada, cotizacion) == Boolean.TRUE) {
					noExiste = Boolean.FALSE;
				}
				
			}
			if(activo == Activo.AUDCAD) {
				HistAudCad ultimaCotizacionAlmacenada = histAudCadRepository.findFirstByTimeframeOrderByFechaHoraDesc(timeframe);
				//Comparamos las cotizaciones 
				if(ultimaCotizacionAlmacenada != null && HistoricosUtil.compararContizacionNuevaAudCad(ultimaCotizacionAlmacenada, cotizacion) == Boolean.TRUE) {
					noExiste = Boolean.FALSE;
				}
				
			}
			if(activo == Activo.XTIUSD) {
				HistWti ultimaCotizacionAlmacenada = histWtiRepository.findFirstByTimeframeOrderByFechaHoraDesc(timeframe);
				//Comparamos las cotizaciones 
				if(ultimaCotizacionAlmacenada != null && HistoricosUtil.compararContizacionNuevaWti(ultimaCotizacionAlmacenada, cotizacion) == Boolean.TRUE) {
					noExiste = Boolean.FALSE;
				}
				
			}
			if(activo == Activo.NDX) {
				HistNdx ultimaCotizacionAlmacenada = histNdxRepository.findFirstByTimeframeOrderByFechaHoraDesc(timeframe);
				//Comparamos las cotizaciones 
				if(ultimaCotizacionAlmacenada != null && HistoricosUtil.compararContizacionNuevaNdx(ultimaCotizacionAlmacenada, cotizacion) == Boolean.TRUE) {
					noExiste = Boolean.FALSE;
				}
				
			}	
			if(activo == Activo.EURUSD) {
				HistEurUsd ultimaCotizacionAlmacenada = histEurUsdRepository.findFirstByTimeframeOrderByFechaHoraDesc(timeframe);
				//Comparamos las cotizaciones 
				if(ultimaCotizacionAlmacenada != null && HistoricosUtil.compararContizacionNuevaEurUsd(ultimaCotizacionAlmacenada, cotizacion) == Boolean.TRUE) {
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
