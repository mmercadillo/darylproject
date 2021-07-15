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

	
	//https://www.x-trader.net/articulos/sistemas-de-trading/z-score.html
	private void calcularZScore(ResumenRobot resumen) {
    	
		try {
			
			if(resumen != null) {
				
				//N es el número total de operaciones
				//R es el número total de rachas en la serie. Por ejemplo, si tenemos una secuencia de series de operaciones de la forma +++ - - ++ - ++, 
				//tendríamos que R es igual a 5 ya que tenemos 5 rachas (entendidas como cadena de operaciones ganadoras o perdedoras seguidas) en la serie de resultados.
				//P = 2*W*L, donde W es el número total de operaciones positivas y L, el total de perdedoras.


				long numTotalOperaciones = resumen.getNumOperaciones();
				long numTotalOpsGanadoras = resumen.getNumOpsGanadoras()==0?1:resumen.getNumOpsGanadoras();
				long numTotalOpsPerdedoras = resumen.getNumOpsPerdedoras()==0?1:resumen.getNumOpsPerdedoras();
				long P = 2 * numTotalOpsGanadoras * numTotalOpsPerdedoras;
				
				
				long rachasGanadoras = 0;
				long rachasPerdedoras = 0;
				List<HistoricoOperaciones> lista = historicoOperacionesRepository.findListaByRobot(resumen.getRobot(), "2020.01.01 01:00:00");
				
				if(lista != null && lista.size() > 0) {
					HistoricoOperaciones histAnterior = null;
					for (HistoricoOperaciones hist : lista) {
						if(histAnterior == null || (hist.getProfit() > 0 && histAnterior.getProfit() < 0)) {
							rachasGanadoras++;
						}
						if(histAnterior == null || (hist.getProfit() < 0 && histAnterior.getProfit() > 0)) {
							rachasPerdedoras++;
						}
						histAnterior = hist;
					}
					
				}
				
				long numTotalRachas = rachasGanadoras + rachasPerdedoras;
				if(numTotalOperaciones > 1) {
					double divisor = Math.sqrt(  ((P * (P - numTotalOperaciones)) /  (numTotalOperaciones - 1)))  ;
					if(divisor != 0) {
						double zScore = ((numTotalOperaciones * (numTotalRachas - 0.5)) - P) / divisor;
						resumen.setZScore(zScore);
					}
				}
		    	//resumenRobotRepository.save(resumen);
			}
			
		}catch (Exception e) {
			logger.error("ERROR AL ACTUALIZAR Z-SCORE =============", e);
		}
    		

    }
	
	
	

	//@Scheduled(fixedDelay = 600000, initialDelay = 1000)
    //@Transactional
	private void calcularMaxMinDD(ResumenRobot resumen) {
    	
		try {
			
			if(resumen != null) {
				
				double max = 0.0;
				double min = 0.0;
				double difMaxMin = 0.0;
				double res = 0.0;
				List<HistoricoOperaciones> lista = historicoOperacionesRepository.findListaByRobot(resumen.getRobot(), "2020.01.01 01:00:00");
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
		    	//resumenRobotRepository.save(resumen);
			}
			
		}catch (Exception e) {
			logger.error("ERROR AL ACTUALIZAR MAX DD =============", e);
		}

    	
    }
	
    //@Scheduled(fixedDelay = 600000, initialDelay = 1000)
    //@Transactional
	private void calcularMaximaRachaPerdedora(ResumenRobot resumen) {

		try {

			if(resumen != null) {
				
				double maxRachaPerdedora = 0.0;
				double perdidas = 0.0;
				List<HistoricoOperaciones> lista = historicoOperacionesRepository.findListaByRobot(resumen.getRobot(), "2020.01.01 01:00:00");
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
		    	//resumenRobotRepository.save(resumen);
			}
			
		}catch (Exception e) {
			logger.error("ERROR AL ACTUALIZAR MAXIMA RACHA PERDEDORA ACTUALIZADA=============", e);
		}

    	
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
				String desde = "2020.01.01 01:00:00";
				if(resumen != null) {
					
					desde = resumen.getUltimoTicket();
				}
				else resumen = new ResumenRobot();
				
				Calendar c = Calendar.getInstance();

				//Buscamos su lista de operaciones
		    	List<HistoricoOperaciones> lista = historicoOperacionesRepository.findListaByRobot(robot.getRobot(), desde);
				
		    	if(lista != null && lista.size() > 0) {
		    		Double totalTiempoEnMercado = 0.0;
		    		for (HistoricoOperaciones hops : lista) {
		    			resumen.setFUltimoCierre(hops.getFcierre());
		    			resumen.setFUltimaApertura(hops.getFapertura());
		    			resumen.setUltimoTicket(hops.getFcierre());
						
		    			Long aperturaOp = config.getFechaHoraInMillis(hops.getFapertura());
		    			Long cierreOp = config.getFechaHoraInMillis(hops.getFcierre());
		    			
		    			totalTiempoEnMercado += (cierreOp - aperturaOp);
		    			
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
		    		
		    		resumen.setTotalTiempoEnMercado(totalTiempoEnMercado);
		    		//Media de tiempo en el mercado
		    		Double mediaTiempoEnMercado = totalTiempoEnMercado / resumen.getNumOperaciones();
		    		resumen.setMediaTiempoEnMercado(mediaTiempoEnMercado);
			    	
			    	logger.info("CALCULO DE MAX RACHA PERDEDORA -> " + robot.getRobot());
			    	calcularMaximaRachaPerdedora(resumen);
			    	logger.info("MAX RACHA PERDEDORA CALCULADA -> " + robot.getRobot());
			    	logger.info("CALCULO DE MAX DD -> " + robot.getRobot());
			    	calcularMaxMinDD(resumen);
			    	logger.info("MAX DD PERDEDORA CALCULADA -> " + robot.getRobot());
			    	logger.info("CALCULO DE Z-SCORE -> " + robot.getRobot());
			    	calcularZScore(resumen);
			    	logger.info("Z-SCORE CALCULADO -> " + robot.getRobot());
			    	
		    		
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
