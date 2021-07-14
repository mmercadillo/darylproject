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

	//@Scheduled(fixedDelay = 300000, initialDelay = 1000)
    //@Transactional
	private void calcularMaxMinDD(ResumenRobotDemolab resumen) {

    		try {
    			

    			if(resumen != null) {
    				
    				double max = 0.0;
    				double min = 0.0;
    				double difMaxMin = 0.0;
    				double res = 0.0;
    				List<DemolabOps> lista = historicoOperacionesDemolabRepository.findListaByRobot(resumen.getRobot(), "2020.01.01 01:00:00");
    				if(lista != null && lista.size() > 0) {
    		    		for (DemolabOps hops : lista) {
		    				
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
    			}
    			
    		}catch (Exception e) {
    			
			}
    		
    	
    }
	
	
    //@Scheduled(fixedDelay = 300000, initialDelay = 1000)
    //@Transactional
	private void calcularMaximaRachaPerdedora(ResumenRobotDemolab resumen) {

    		try {
    			
    			
    			if(resumen != null) {
    				
    				double maxRachaPerdedora = 0.0;
    				double perdidas = 0.0;
    				List<DemolabOps> lista = historicoOperacionesDemolabRepository.findListaByRobot(resumen.getRobot(), "2020.01.01 01:00:00");
    				if(lista != null && lista.size() > 0) {
    		    		for (DemolabOps hops : lista) {

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

    			}
    			
    		}catch (Exception e) {
    			logger.error("ERROR AL ACTUALIZAR MAXIMA RACHA PERDEDORA ACTUALIZADA DEMOLAB=============", e);
			}

    	
	}
	
		
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
				String desde = "2020.01.01 01:00:00";
				if(resumen != null) {
					
					desde = resumen.getUltimoTicket();
				}
				else resumen = new ResumenRobotDemolab();
				
				Calendar c = Calendar.getInstance();

				//Buscamos su lista de operaciones
		    	final List<DemolabOps> lista = historicoOperacionesDemolabRepository.findListaByRobot(robot.name(), desde);
				
		    	if(lista != null && lista.size() > 0) {
		    		for (DemolabOps hops : lista) {
		    			resumen.setFUltimoCierre(hops.getFcierre());
						resumen.setUltimoTicket(hops.getFcierre());
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
								resumen.setGananciaMediaPorOpGanadora(resumen.getTotalGanancias() / (double)resumen.getNumOpsGanadoras());
							}
							if(resumen.getNumOpsPerdedoras() != 0) {
								resumen.setPerdidaMediaPorOpPerdedora(resumen.getTotalPerdidas() / (double)resumen.getNumOpsPerdedoras());
							}
						}catch (Exception e) {
							// TODO: handle exception
						}
						
						//Calculo de la esperanza matem�tica
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
		    		
			    	logger.info("CALCULO DE MAX RACHA PERDEDORA -> " + robot.name());
			    	calcularMaximaRachaPerdedora(resumen);
			    	logger.info("MAX RACHA PERDEDORA CALCULADA -> " + robot.name());
			    	logger.info("CALCULO DE MAX DD -> " + robot.name());
			    	calcularMaxMinDD(resumen);
			    	logger.info("MAX DD PERDEDORA CALCULADA -> " + robot.name());

		    		
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
