package daryl.system.robots.control.historico.operaciones.control;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.DemolabRobot;
import daryl.system.model.DemolabOps;
import daryl.system.model.ResumenRobotDemolab;
import daryl.system.robots.control.historico.operaciones.repository.IHistoricoOperacionesDemolabRepository;
import daryl.system.robots.control.historico.operaciones.repository.IResumenRobotDemolabRepository;

@Component
public class ControlHistoricoOperacionesDemolab {

	@Autowired
	Logger logger;

	@Autowired
	IHistoricoOperacionesDemolabRepository historicoOperacionesDemolabRepository;
	@Autowired
	IResumenRobotDemolabRepository resumenRobotDemolabRepository;

		
    @Scheduled(fixedDelay = 300000, initialDelay = 1000)
    @Transactional
	public void run() {
    	
		
    	logger.info("ACTUALIZANDO RESUMEN DE OPERACIONES DEMOLAB=============");
    	List<DemolabRobot> robots = Arrays.asList(DemolabRobot.values());
		for (DemolabRobot robot : robots) {
			logger.info("ACTUALIZANDO OPERACIONES DEMOLAB ROBOT -> " + robot);
			//System.out.println("RESUMEN ROBOT -> " + robot.name() + " =============");
			try {
				//Recuperamos la info del robot de la BD,s
				ResumenRobotDemolab resumen = resumenRobotDemolabRepository.findResumenRobotDemolabByRobot(robot.name());
				Long desde = 0L;
				if(resumen != null) {
					
					desde = resumen.getUltimoTicket();
				}
				else resumen = new ResumenRobotDemolab();
				
				Calendar c = Calendar.getInstance();

				//Buscamos su lista de operaciones
		    	List<DemolabOps> lista = historicoOperacionesDemolabRepository.findListaByRobot(robot.name(), desde);
				
		    	if(lista != null && lista.size() > 0) {
		    		for (DemolabOps hops : lista) {
		    			resumen.setFUltimoCierre(hops.getFcierre());
						resumen.setUltimoTicket(hops.getId());
						resumen.setRobot(robot.name());
						resumen.setNumOperaciones(resumen.getNumOperaciones()+1);
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
		    		logger.info("OPERACIONES DEMOLAB ROBOT ACTUALIZADAS-> " + robot.name());
			    	resumenRobotDemolabRepository.save(resumen);
			    	logger.info("OPERACIONES ROBOT GUARDADAS-> " + robot.name());
			    	
		    	}
			
			}catch (Exception e) {
				logger.error("RESUMEN DE OPERACIONES DEMOLAB ACTUALIZADO=============", e);
			}
		}
    	logger.info("RESUMEN DE OPERACIONES DEMOLAB ACTUALIZADO=============");

	}

}
