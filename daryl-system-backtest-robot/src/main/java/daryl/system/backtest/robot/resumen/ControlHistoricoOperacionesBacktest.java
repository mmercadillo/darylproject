package daryl.system.backtest.robot.resumen;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import daryl.system.backtest.robot.repository.IHistoricoOperacionesBacktestRepository;
import daryl.system.backtest.robot.repository.IResumenRobotBacktestRepository;
import daryl.system.model.Robot;
import daryl.system.model.backtest.HistoricoOperacionesBacktest;
import daryl.system.model.backtest.ResumenRobotBacktest;

@Component
public class ControlHistoricoOperacionesBacktest {

	@Autowired
	Logger logger;

	@Autowired
	IHistoricoOperacionesBacktestRepository historicoOperacionesBacktestRepository;
	@Autowired
	IResumenRobotBacktestRepository resumenRobotBacktestRepository;

	private Robot robot;
	
	public void init(Robot robot) {
		this.robot = robot;
	}

	public void calcularMaxMinDD() {

		
		try {
			
			ResumenRobotBacktest resumen = resumenRobotBacktestRepository.findResumenRobotByRobot(this.robot.getRobot());
			if(resumen != null) {
				
				double max = 0.0;
				double min = 0.0;
				double difMaxMin = 0.0;
				double res = 0.0;
				List<HistoricoOperacionesBacktest> lista = historicoOperacionesBacktestRepository.findListaByRobot(this.robot.getRobot(), 0L);
				if(lista != null && lista.size() > 0) {
		    		for (HistoricoOperacionesBacktest hops : lista) {
	    				
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
				resumenRobotBacktestRepository.save(resumen);
			}
			
		}catch (Exception e) {
			
		}
    		
    	
    }
	
	public void calcularMaximaRachaPerdedora() {

		try {
			
			ResumenRobotBacktest resumen = resumenRobotBacktestRepository.findResumenRobotByRobot(this.robot.getRobot());
			if(resumen != null) {
				
				double maxRachaPerdedora = 0.0;
				double perdidas = 0.0;
				List<HistoricoOperacionesBacktest> lista = historicoOperacionesBacktestRepository.findListaByRobot(this.robot.getRobot(), 0L);
				if(lista != null && lista.size() > 0) {
		    		for (HistoricoOperacionesBacktest hops : lista) {
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
		    	resumenRobotBacktestRepository.save(resumen);
		    	logger.info("MAXIMA RACHA PERDEDORA ROBOT GUARDADAS-> " + robot.getRobot());
			}
			
		}catch (Exception e) {
			logger.error("ERROR AL ACTUALIZAR MAXIMA RACHA PERDEDORA ACTUALIZADA=============", e);
		}
		

    	
	}
	

	public void run() {
    	
		try {
			//Recuperamos la info del robot de la BD,s
			ResumenRobotBacktest resumen = resumenRobotBacktestRepository.findResumenRobotByRobot(this.robot.getRobot());
			Long desde = 0L;
			if(resumen == null) {
				resumen = new ResumenRobotBacktest();
			}
			
			Calendar c = Calendar.getInstance();

			//Buscamos su lista de operaciones
	    	List<HistoricoOperacionesBacktest> lista = historicoOperacionesBacktestRepository.findListaByRobot(this.robot.getRobot(), desde);
			
	    	if(lista != null && lista.size() > 0) {
	    		for (HistoricoOperacionesBacktest hops : lista) {
					resumen.setRobot(robot.getRobot());
					resumen.setNumOperaciones(resumen.getNumOperaciones()+1);
					resumen.setTipoActivo(robot.getActivo());
					resumen.setEstrategia(robot.getEstrategia());
					
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
		    	resumenRobotBacktestRepository.save(resumen);
		    	logger.info("OPERACIONES ROBOT GUARDADAS-> " + robot.getRobot());
		    	
	    	}
		
		}catch (Exception e) {
			logger.error("ERROR AL ACTUALZIAR EL RESUMEN DE OPERACIONES=============", e);
		}

	}

}
