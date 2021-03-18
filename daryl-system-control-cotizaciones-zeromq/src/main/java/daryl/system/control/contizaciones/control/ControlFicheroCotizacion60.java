package daryl.system.control.contizaciones.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.comun.exceptions.SistemaException;
import daryl.system.control.contizaciones.control.apachemq.Sender;
import daryl.system.control.contizaciones.control.repository.IRobotsRepository;
import daryl.system.model.Robot;
import daryl.system.model.historicos.HistAudCad;
import daryl.system.model.historicos.HistEurUsd;
import daryl.system.model.historicos.HistGdaxi;
import daryl.system.model.historicos.HistNdx;
import daryl.system.model.historicos.HistWti;
import daryl.system.model.historicos.HistXauUsd;

@Component
@EnableAsync
public class ControlFicheroCotizacion60 extends Control{

	@Autowired
	Logger logger;
	
	@Autowired
	private ConfigData config;
	@Autowired
	private Sender sender;

	@Autowired
	private IRobotsRepository robotsRepository;

	
	private final Timeframes timeframe = Timeframes.PERIOD_H1;
	
	Boolean prueba = Boolean.TRUE;
    @Scheduled(fixedDelay = 10000, initialDelay = 1000)
	public void run() {
    	//System.out.println("---------------------------------");
    	for (Activo activo : Activo.values()) {
    		//System.out.println("ACTUALIZANDO COTIZACIÓN " + activo + " TF= " + timeframe.name());
			//logger.info("Control de cotizaciones para {} INCIADO a las: {} ", activo.name(), config.getActualDateFormattedInString());
			try {
				String rutaFicheroMt4 = config.getRutaFicheroMt4(activo.name(), timeframe);
				//logger.info("Ruta MT4 para {} es {} ", activo.name(), rutaFicheroMt4);

				if(super.inTime()) {
					Boolean noExiste = getNuevaCotizacionFromMetatrader(activo, rutaFicheroMt4); 
					if(noExiste == Boolean.TRUE) {
						
						//logger.info("Nueva cotizaciÃ³n {} - {} : SE ENVÃ?A A LA COLA", activo.name(), timeframe);
						try {
							//sender.send(config.getCanalAmq(activo, timeframe).name(), "Hay nueva cotización");
							List<Robot> robots = robotsRepository.findRobotsByActivoAndTimeframe(activo, timeframe);
							for(Robot robot : robots) {
								System.out.println("SE ENVIA SEÑAL AL ROBOT " + robot.getRobot() + " TF= " + timeframe.name());
								//sender.send(robot.getCanal().name(), robot);
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else {
					//logger.info("Sin nueva cotizaciÃ³n {} - {} : FUERA DE TIEMPO", activo.name(), timeframe);
				}

			} catch (SistemaException e) {
				//logger.error("Se ha produccido un error inesperado: {}", activo.name(), e);
			} catch (Exception e) {
				//logger.error("Se ha produccido un error inesperado: {}", activo.name(), e);
			}
		}
    	
	}
    
    @Async
	public Boolean getNuevaCotizacionFromMetatrader(Activo activo, String rutaFicheroMt4) throws SistemaException{
    	//logger.info("CHECKEANDO NUEVA COTIZACION PARA EL ACTIVO: {} en {}", activo.name(), timeframe);
    	Boolean noExiste = Boolean.FALSE;
    	try {

			File ficheroMt4 = new File(rutaFicheroMt4);

			//Leemos la linea
			try(FileReader fr = new FileReader(ficheroMt4)) {
				
				BufferedReader reader = new BufferedReader(fr);
				String nuevaCotizacion;
				if( (nuevaCotizacion = reader.readLine()) != null ) {
					
					noExiste = this.checkNuevaCotizacion(activo, nuevaCotizacion, timeframe);
					if(noExiste) {
						//logger.info("Cotizacion nueva para el activo {} detectada: {} en {}", activo.name(), nuevaCotizacion, timeframe);
						System.out.println("NUEVA COTIZACIÓN " + activo.name() + " TF= " + timeframe.name() + " VALOR= " + nuevaCotizacion);
						//Guardamos en la tabla correspondiente la nueva cotizaion
						try {
							String[] tokens = nuevaCotizacion.split(",");
							if(activo == Activo.XAUUSD) {
								HistXauUsd historico = new HistXauUsd();
									historico.setFecha(tokens[0]);
									historico.setHora(tokens[1]);
									historico.setApertura(new Double(tokens[2]));
									historico.setMaximo(new Double(tokens[3]));
									historico.setMinimo(new Double(tokens[4]));
									historico.setCierre(new Double(tokens[5]));
									historico.setVolumen(new Double(tokens[6]));
									historico.setFechaHora(config.getFechaHoraInMillis(tokens[0], tokens[1]));
									historico.setTimeframe(timeframe);
									
								histXauUsdRepository.save(historico);
								//logger.info("Cotizacion para el activo {} guardada: {}", activo.name(), nuevaCotizacion);
							}							
							if(activo == Activo.AUDCAD) {
								HistAudCad historico = new HistAudCad();
									historico.setFecha(tokens[0]);
									historico.setHora(tokens[1]);
									historico.setApertura(new Double(tokens[2]));
									historico.setMaximo(new Double(tokens[3]));
									historico.setMinimo(new Double(tokens[4]));
									historico.setCierre(new Double(tokens[5]));
									historico.setVolumen(new Double(tokens[6]));
									historico.setFechaHora(config.getFechaHoraInMillis(tokens[0], tokens[1]));
									historico.setTimeframe(timeframe);
								
								histAudCadRepository.save(historico);
								//logger.info("Cotizacion para el activo {} guardada: {}", activo.name(), nuevaCotizacion);
							}
							if(activo == Activo.XTIUSD) {
								HistWti historico = new HistWti();
									historico.setFecha(tokens[0]);
									historico.setHora(tokens[1]);
									historico.setApertura(new Double(tokens[2]));
									historico.setMaximo(new Double(tokens[3]));
									historico.setMinimo(new Double(tokens[4]));
									historico.setCierre(new Double(tokens[5]));
									historico.setVolumen(new Double(tokens[6]));
									historico.setFechaHora(config.getFechaHoraInMillis(tokens[0], tokens[1]));
									historico.setTimeframe(timeframe);
								
								histWtiRepository.save(historico);
								//logger.info("Cotizacion para el activo {} guardada: {}", activo.name(), nuevaCotizacion);
							}
							if(activo == Activo.GDAXI) {
								HistGdaxi historico = new HistGdaxi();
									historico.setFecha(tokens[0]);
									historico.setHora(tokens[1]);
									historico.setApertura(new Double(tokens[2]));
									historico.setMaximo(new Double(tokens[3]));
									historico.setMinimo(new Double(tokens[4]));
									historico.setCierre(new Double(tokens[5]));
									historico.setVolumen(new Double(tokens[6]));
									historico.setFechaHora(config.getFechaHoraInMillis(tokens[0], tokens[1]));
									historico.setTimeframe(timeframe);
									
								histGdaxiRepository.save(historico);
								//logger.info("Cotizacion para el activo {} guardada: {}", activo.name(), nuevaCotizacion);
							}
							if(activo == Activo.NDX) {
								HistNdx historico = new HistNdx();
									historico.setFecha(tokens[0]);
									historico.setHora(tokens[1]);
									historico.setApertura(new Double(tokens[2]));
									historico.setMaximo(new Double(tokens[3]));
									historico.setMinimo(new Double(tokens[4]));
									historico.setCierre(new Double(tokens[5]));
									historico.setVolumen(new Double(tokens[6]));
									historico.setFechaHora(config.getFechaHoraInMillis(tokens[0], tokens[1]));
									historico.setTimeframe(timeframe);
									
								histNdxRepository.save(historico);
								//logger.info("Cotizacion para el activo {} guardada: {}", activo.name(), nuevaCotizacion);
							}
							if(activo == Activo.EURUSD) {
								HistEurUsd historico = new HistEurUsd();
									historico.setFecha(tokens[0]);
									historico.setHora(tokens[1]);
									historico.setApertura(new Double(tokens[2]));
									historico.setMaximo(new Double(tokens[3]));
									historico.setMinimo(new Double(tokens[4]));
									historico.setCierre(new Double(tokens[5]));
									historico.setVolumen(new Double(tokens[6]));
									historico.setFechaHora(config.getFechaHoraInMillis(tokens[0], tokens[1]));
									historico.setTimeframe(timeframe);
									
								histEurUsdRepository.save(historico);
								//logger.info("Cotizacion para el activo {} guardada: {}", activo.name(), nuevaCotizacion);
							}
						}catch (Exception e) {
							//logger.error("\"No se ha podido actualizar el historico del activo: {}", activo.name(), e);
							throw new SistemaException("No se ha podido actualizar el historico del activo " + activo.name());
						}
						
					}else {
						//logger.info("No hay cotizacion nueva para el activo {} ", activo.name());
					}
					
				}else {
					//logger.error("No se ha podido recuperar la ultima cotizacion de MT4 del activo: {}", activo.name());
					throw new SistemaException("No se ha podido recuperar la ï¿½ltima cotizaciï¿½n de MT4 del activo: " + activo.name());
				}
				
			}catch (Exception e) {
				//logger.error("No se ha podido recuperar la ultima cotizacion de MT4 del activo: {}", activo.name(), e);
				throw new SistemaException("No se ha podido recuperar la ultima cotizacion de MT4 del activo: " + activo.name(), e);
			}
			
			
		}catch (Exception e) {
			//logger.error("No se ha podido recuperar la ultima cotizacion de MT4 del activo: {}", activo.name(), e);
			throw new SistemaException("No se ha podido recuperar la ultima cotizacion de MT4 del activo " + activo.name(), e);
		}
    	return noExiste;
		
	}

}
