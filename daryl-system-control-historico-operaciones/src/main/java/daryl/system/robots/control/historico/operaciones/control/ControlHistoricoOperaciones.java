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
import daryl.system.robots.control.historico.operaciones.repository.IResumenRobotRepository;
import daryl.system.robots.control.historico.operaciones.repository.IRobotsRepository;

@Component
public class ControlHistoricoOperaciones {

	@Autowired
	Logger logger;

	@Autowired
	IHistoricoOperacionesRepository historicoOperacionesRepository;
	@Autowired
	IResumenRobotRepository resumenRobotRepository;
	@Autowired
	IRobotsRepository robotRepository;
	
	
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
				
		    	if(lista != null && lista.size() > 0) {
		    		for (HistoricoOperaciones hops : lista) {
		    			resumen.setFUltimoCierre(hops.getFcierre());
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
		    		logger.info("OPERACIONES ROBOT ACTUALIZADAS-> " + robot.getRobot());
			    	resumenRobotRepository.save(resumen);
			    	logger.info("OPERACIONES ROBOT GUARDADAS-> " + robot.getRobot());
			    	
		    	}
			
			}catch (Exception e) {
				logger.error("RESUMEN DE OPERACIONES ACTUALIZADO=============", e);
			}
		}
    	logger.info("RESUMEN DE OPERACIONES ACTUALIZADO=============");

	}

}
