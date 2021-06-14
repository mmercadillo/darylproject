package daryl.system.robots.control.historico.operaciones.control;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.model.HistoricoOperaciones;
import daryl.system.model.ResumenRobot;
import daryl.system.model.Robot;
import daryl.system.robots.control.historico.operaciones.repository.IHistoricoOperacionesRepository;
import daryl.system.robots.control.historico.operaciones.repository.IHistoricoResumenRobotRepository;
import daryl.system.robots.control.historico.operaciones.repository.IResumenRobotRepository;
import daryl.system.robots.control.historico.operaciones.repository.IRobotsRepository;

@Component
public class ControlHistoricoOperaciones {

	@Autowired
	Logger logger;

	@Autowired
	ConfigData config;
	
	@Autowired
	IHistoricoOperacionesRepository historicoOperacionesRepository;
	@Autowired
	IResumenRobotRepository resumenRobotRepository;
	@Autowired
	IHistoricoResumenRobotRepository historicoResumenRobotRepository;
	@Autowired
	IRobotsRepository robotRepository;

    @Scheduled(fixedDelay = 600000, initialDelay = 1000)
    @Transactional
	public void calcularMaxMinDD() {
    	
    	List<Robot> robots = robotRepository.findAll();
    	for (Robot robot : robots) {
    		
    		try {
    			
    			ResumenRobot resumen = resumenRobotRepository.findResumenRobotByRobot(robot.getRobot());
    			if(resumen != null) {
    				
    				double max = 0.0;
    				double min = 0.0;
    				double difMaxMin = 0.0;
    				double res = 0.0;
    				List<HistoricoOperaciones> lista = historicoOperacionesRepository.findListaByRobot(robot.getRobot(), 0L);
    				if(lista != null && lista.size() > 0) {
    		    		for (HistoricoOperaciones hops : lista) {
		    				
    		    			res += hops.getProfit();
    		    			if(res < min) {
    		    				min = res;
    		    			}
    		    			if(res > max) {
    		    				max = res;
    		    			}

    		    			
    		    		}
    				}

    				difMaxMin = max - min;
    				
    				resumen.setMaximo(max);
    				resumen.setMinimo(min);
    				resumen.setDifMaxMin(difMaxMin);
			    	resumenRobotRepository.save(resumen);
    			}
    			
    		}catch (Exception e) {
    			
			}
    		
    	}
    	
    }
	
    @Scheduled(fixedDelay = 600000, initialDelay = 1000)
    @Transactional
	public void calcularMaximaRachaPerdedora() {
    	logger.info("ACTUALIZANDO MAXIMA RACHA PERDEDORA=============");
    	List<Robot> robots = robotRepository.findAll();
    	for (Robot robot : robots) {
    		logger.info("ACTUALIZANDO MAXIMA RACHA PERDEDORA ROBOT -> " + robot.getRobot());
    		try {
    			
    			ResumenRobot resumen = resumenRobotRepository.findResumenRobotByRobot(robot.getRobot());
    			if(resumen != null) {
    				
    				double maxRachaPerdedora = 0.0;
    				double perdidas = 0.0;
    				List<HistoricoOperaciones> lista = historicoOperacionesRepository.findListaByRobot(robot.getRobot(), 0L);
    				if(lista != null && lista.size() > 0) {
    		    		for (HistoricoOperaciones hops : lista) {
		    				if(perdidas < maxRachaPerdedora) {
		    					maxRachaPerdedora = perdidas;
		    				}
    		    			if(hops.getProfit() > 0) {
    		    				perdidas = 0.0;
    		    			}else {
    		    				perdidas += hops.getProfit();
    		    			}
    		    			
    		    		}
    				}
    				resumen.setMaximaPerdidaConsecutiva(maxRachaPerdedora);
		    		logger.info("MAXIMA RACHA PERDEDORA ROBOT ACTUALIZADAS-> " + robot.getRobot());
			    	resumenRobotRepository.save(resumen);
			    	logger.info("MAXIMA RACHA PERDEDORA ROBOT GUARDADAS-> " + robot.getRobot());
    			}
    			
    		}catch (Exception e) {
    			logger.error("ERROR AL ACTUALIZAR MAXIMA RACHA PERDEDORA ACTUALIZADA=============", e);
			}
    		
    	}
    	logger.info("MAXIMA RACHA PERDEDORA ACTUALIZADA=============");
    	
	}
	
	
    @Scheduled(fixedDelay = 600000, initialDelay = 1000)
    @Transactional
	public void run() {
    	
		
    	logger.info("ACTUALIZANDO RESUMEN DE OPERACIONES=============");
    	List<Robot> robots = robotRepository.findAll();
		for (Robot robot : robots) {
			logger.info("ACTUALIZANDO OPERACIONES ROBOT -> " + robot.getRobot());
			//System.out.println("RESUMEN ROBOT -> " + robot.name() + " =============");
			try {
				//Recuperamos la info del robot de la BD,s
				ResumenRobot resumen = resumenRobotRepository.findResumenRobotByRobot(robot.getRobot());
				Long desde = 0L;
				if(resumen != null) {
					
					desde = resumen.getUltimoTicket();
				}
				else resumen = new ResumenRobot();
				
				Calendar c = Calendar.getInstance();

				//Buscamos su lista de operaciones
		    	List<HistoricoOperaciones> lista = historicoOperacionesRepository.findListaByRobot(robot.getRobot(), desde);
				Long totalTiempoEnMercado = 0L;
		    	if(lista != null && lista.size() > 0) {
		    		for (HistoricoOperaciones hops : lista) {
		    			resumen.setFUltimoCierre(hops.getFcierre());
		    			resumen.setFUltimoCierre(hops.getFapertura());
						
		    			Long aperturaOp = config.getFechaHoraInMillis(hops.getFapertura());
		    			Long cierreOp = config.getFechaHoraInMillis(hops.getFcierre());
		    			
		    			totalTiempoEnMercado += (cierreOp - aperturaOp);
		    			
		    			resumen.setUltimoTicket(hops.getId());
						resumen.setRobot(robot.getRobot());
						resumen.setNumOperaciones(resumen.getNumOperaciones()+1);
						resumen.setTipoActivo(robot.getActivo());
						resumen.setEstrategia(robot.getEstrategia());
						resumen.setVersion(resumen.getVersion());
						
						//Ponemos el total anterior
						try{resumen.setTotalAnterior(resumen.getTotal());}catch (Exception e) {
							resumen.setTotalAnterior(0.0);
						}
						resumen.setTotal(hops.getProfit()+resumen.getTotal());
						
						if(hops.getProfit()<0) {
							resumen.setTotalPerdidas(resumen.getTotalPerdidas()+hops.getProfit());
							resumen.setNumOpsPerdedoras(resumen.getNumOpsPerdedoras()+1);
						}else {
							resumen.setTotalGanancias(resumen.getTotalGanancias()+hops.getProfit());
							resumen.setNumOpsGanadoras(resumen.getNumOpsGanadoras()+1);
						}
						Calendar now = Calendar.getInstance();
						if(resumen.getFAlta() == null) resumen.setFAlta(now.getTimeInMillis());
						resumen.setFModificacion(now.getTimeInMillis());
						
						try {
							if(resumen.getNumOperaciones() != 0) {
								resumen.setPctOpsGanadoras( (resumen.getNumOpsGanadoras() * 100.0) / (resumen.getNumOperaciones()) );
								resumen.setPctOpsPerdedoras( (resumen.getNumOpsPerdedoras() * 100.0) / (resumen.getNumOperaciones()) );
							}
							if(resumen.getNumOpsGanadoras() != 0) {
								resumen.setGananciaMediaPorOpGanadora(resumen.getTotalGanancias() / resumen.getNumOpsGanadoras());
							}
							if(resumen.getNumOpsPerdedoras() != 0) {
								resumen.setPerdidaMediaPorOpPerdedora(resumen.getTotalPerdidas() / resumen.getNumOpsPerdedoras());
							}
						}catch (Exception e) {
							// TODO: handle exception
						}
						
						//C�lculo de la esperanza matem�tica
						try {
							
							double probWin = (double)resumen.getNumOpsGanadoras() / (double)resumen.getNumOperaciones();
							double probLoss = (double)resumen.getNumOpsPerdedoras() / (double)resumen.getNumOperaciones();
							
							double gananciaMediaPorOpGanadora = resumen.getGananciaMediaPorOpGanadora();
							double perdidaMediaPorOpPerdedora = resumen.getPerdidaMediaPorOpPerdedora();
							
							double espmat = (probWin * gananciaMediaPorOpGanadora) - (probLoss * perdidaMediaPorOpPerdedora * -1);//Multiplicamos por -1 pq perdidaMediaPorOpPerdedora es negativo
							
							//Ponemos la anterior esp mat
							try {resumen.setEspmatAnterior(resumen.getEspmat());}catch (Exception e) {
								resumen.setEspmatAnterior(0.0);
							}
							resumen.setEspmat(espmat);
							
						}catch (Exception e) {
							e.printStackTrace();
						}
						
					}
		    		
		    		//Media de tiempo en el mercado
		    		Long mediaTiempoEnMercado = resumen.getTotalTiempoEnMercado() / resumen.getNumOperaciones();
		    		resumen.setMediaTiempoEnMercado(mediaTiempoEnMercado);
		    		
		    		
		    		logger.info("OPERACIONES ROBOT ACTUALIZADAS-> " + robot.getRobot());
			    	resumenRobotRepository.save(resumen);
			    	logger.info("OPERACIONES ROBOT GUARDADAS-> " + robot.getRobot());
			    	
		    	}
			
			}catch (Exception e) {
				logger.error("ERROR AL ACTUALZIAR EL RESUMEN DE OPERACIONES=============", e);
			}
		}
    	logger.info("RESUMEN DE OPERACIONES ACTUALIZADO=============");

	}

}
