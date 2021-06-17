package daryl.system.robots.control.historico.operaciones.control;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.HistoricoOperaciones;
import daryl.system.model.HistoricoResumenRobot;
import daryl.system.model.ResumenRobot;
import daryl.system.model.Robot;
import daryl.system.robots.control.historico.operaciones.repository.IHistoricoOperacionesRepository;
import daryl.system.robots.control.historico.operaciones.repository.IHistoricoResumenRobotRepository;
import daryl.system.robots.control.historico.operaciones.repository.IResumenRobotRepository;
import daryl.system.robots.control.historico.operaciones.repository.IRobotsRepository;

@Component
public class ControlHistoricoResumenes {

	@Autowired
	Logger logger;

	@Autowired
	IResumenRobotRepository resumenRobotRepository;
	@Autowired
	IHistoricoResumenRobotRepository historicoResumenRobotRepository;
	@Autowired
	IRobotsRepository robotRepository;
	
	private HistoricoResumenRobot getHistorico(ResumenRobot resumen) {
		Calendar now = Calendar.getInstance();
		HistoricoResumenRobot hrr = new HistoricoResumenRobot();
			hrr.setIdResumen(resumen.getId());
			hrr.setEspmat(resumen.getEspmat());
			hrr.setEspmatAnterior(resumen.getEspmatAnterior());
			hrr.setEstrategia(resumen.getEstrategia());
			hrr.setFAlta(now.getTimeInMillis());
			hrr.setGananciaMediaPorOpGanadora(resumen.getGananciaMediaPorOpGanadora());
			hrr.setNumOperaciones(resumen.getNumOperaciones());
			hrr.setNumOpsGanadoras(resumen.getNumOpsGanadoras());
			hrr.setNumOpsPerdedoras(resumen.getNumOpsPerdedoras());
			hrr.setPctOpsGanadoras(resumen.getPctOpsGanadoras());
			hrr.setPctOpsPerdedoras(resumen.getPctOpsPerdedoras());
			hrr.setPerdidaMediaPorOpPerdedora(resumen.getPerdidaMediaPorOpPerdedora());
			hrr.setRobot(resumen.getRobot());
			hrr.setTipoActivo(resumen.getTipoActivo());
			hrr.setTotal(resumen.getTotal());
			hrr.setTotalAnterior(resumen.getTotalAnterior());
			hrr.setTotalGanancias(resumen.getTotalGanancias());
			hrr.setTotalPerdidas(resumen.getTotalPerdidas());
			hrr.setVersion(resumen.getVersion());
			
			
		return hrr;	
			
		
		
	}
	
	//Cada hora
    //@Scheduled(fixedDelay = 600000, initialDelay = 1000)
    @Transactional
	public void run60() {
    	
		
    	logger.info("ACTUALIZANDO HISTORICO RESUMENES =============");
    	List<Robot> robots = robotRepository.findAll();
		for (Robot robot : robots) {
			if(robot.getTimeframe() == Timeframes.PERIOD_H1) {
				logger.info("ACTUALIZANDO HISTORICO RESUMEN ROBOT -> " + robot.getRobot());
				//System.out.println("RESUMEN ROBOT -> " + robot.name() + " =============");
				try {
					//Recuperamos la info del robot de la BD,s
					ResumenRobot resumen = resumenRobotRepository.findResumenRobotByRobot(robot.getRobot());
					String desde = "2020.01.01 01:00:00";
					if(resumen != null) {
						
						//Creamos el Historico y lo guardamos
						
						try {
							
							//Buscamos el historico
							//si existe pasamos
							HistoricoResumenRobot hrr = historicoResumenRobotRepository.findHistoricoResumenRobotByIdResumen(resumen.getId());
							if(hrr == null) {
								hrr = getHistorico(resumen);
								historicoResumenRobotRepository.save(hrr);
							}
							
						}catch (Exception e) {
							e.printStackTrace();
						}
						
						desde = resumen.getUltimoTicket();
					}
				}catch (Exception e) {
					logger.error("HISTORICO RESUMEN DE OPERACIONES ACTUALIZADO=============", e);
				}
			}
		}
    	logger.info("HISTORICO RESUMEN DE OPERACIONES ACTUALIZADO=============");

	}
    
	//Cada 4 horas
    //@Scheduled(fixedDelay = 2400000, initialDelay = 1000)
    @Transactional
	public void run240() {
    	
		
    	logger.info("ACTUALIZANDO HISTORICO RESUMENES =============");
    	List<Robot> robots = robotRepository.findAll();
		for (Robot robot : robots) {
			if(robot.getTimeframe() == Timeframes.PERIOD_H4) {
				logger.info("ACTUALIZANDO HISTORICO RESUMEN ROBOT -> " + robot.getRobot());
				//System.out.println("RESUMEN ROBOT -> " + robot.name() + " =============");
				try {
					//Recuperamos la info del robot de la BD,s
					ResumenRobot resumen = resumenRobotRepository.findResumenRobotByRobot(robot.getRobot());
					String desde = "2020.01.01 01:00:00";
					if(resumen != null) {
						
						//Creamos el Historico y lo guardamos
						
						try {
							
							//Buscamos el historico
							//si existe pasamos
							HistoricoResumenRobot hrr = historicoResumenRobotRepository.findHistoricoResumenRobotByIdResumen(resumen.getId());
							if(hrr == null) {
								hrr = getHistorico(resumen);
								historicoResumenRobotRepository.save(hrr);
							}
							
						}catch (Exception e) {
							e.printStackTrace();
						}
						
						desde = resumen.getUltimoTicket();
					}
				}catch (Exception e) {
					logger.error("HISTORICO RESUMEN DE OPERACIONES ACTUALIZADO=============", e);
				}
			}
		}
    	logger.info("HISTORICO RESUMEN DE OPERACIONES ACTUALIZADO=============");

	}

    
	//Cada 24 horas
    //@Scheduled(fixedDelay = 14400000, initialDelay = 1000)
    @Transactional
	public void run1440() {
    	
		
    	logger.info("ACTUALIZANDO HISTORICO RESUMENES =============");
    	List<Robot> robots = robotRepository.findAll();
		for (Robot robot : robots) {
			if(robot.getTimeframe() == Timeframes.PERIOD_D1) {
				logger.info("ACTUALIZANDO HISTORICO RESUMEN ROBOT -> " + robot.getRobot());
				//System.out.println("RESUMEN ROBOT -> " + robot.name() + " =============");
				try {
					//Recuperamos la info del robot de la BD,s
					ResumenRobot resumen = resumenRobotRepository.findResumenRobotByRobot(robot.getRobot());
					String desde = "2020.01.01 01:00:00";
					if(resumen != null) {
						
						//Creamos el Historico y lo guardamos
						
						try {
							
							//Buscamos el historico
							//si existe pasamos
							HistoricoResumenRobot hrr = historicoResumenRobotRepository.findHistoricoResumenRobotByIdResumen(resumen.getId());
							if(hrr == null) {
								hrr = getHistorico(resumen);
								historicoResumenRobotRepository.save(hrr);
							}
							
						}catch (Exception e) {
							e.printStackTrace();
						}
						
						desde = resumen.getUltimoTicket();
					}
				}catch (Exception e) {
					logger.error("HISTORICO RESUMEN DE OPERACIONES ACTUALIZADO=============", e);
				}
			}
		}
    	logger.info("HISTORICO RESUMEN DE OPERACIONES ACTUALIZADO=============");

	}
    
	//Cada semana
    //@Scheduled(fixedDelay = 100800000, initialDelay = 1000)
    @Transactional
	public void run10080() {
    	
		
    	logger.info("ACTUALIZANDO HISTORICO RESUMENES =============");
    	List<Robot> robots = robotRepository.findAll();
		for (Robot robot : robots) {
			if(robot.getTimeframe() == Timeframes.PERIOD_W1) {
				logger.info("ACTUALIZANDO HISTORICO RESUMEN ROBOT -> " + robot.getRobot());
				//System.out.println("RESUMEN ROBOT -> " + robot.name() + " =============");
				try {
					//Recuperamos la info del robot de la BD,s
					ResumenRobot resumen = resumenRobotRepository.findResumenRobotByRobot(robot.getRobot());
					String desde = "2020.01.01 01:00:00";
					if(resumen != null) {
						
						//Creamos el Historico y lo guardamos
						
						try {
							
							//Buscamos el historico
							//si existe pasamos
							HistoricoResumenRobot hrr = historicoResumenRobotRepository.findHistoricoResumenRobotByIdResumen(resumen.getId());
							if(hrr == null) {
								hrr = getHistorico(resumen);
								historicoResumenRobotRepository.save(hrr);
							}
							
						}catch (Exception e) {
							e.printStackTrace();
						}
						
						desde = resumen.getUltimoTicket();
					}
				}catch (Exception e) {
					logger.error("HISTORICO RESUMEN DE OPERACIONES ACTUALIZADO=============", e);
				}
			}
		}
    	logger.info("HISTORICO RESUMEN DE OPERACIONES ACTUALIZADO=============");

	}
    
}
