package daryl.system.robot.full.node.control;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Orden;
import daryl.system.model.Robot;
import daryl.system.robot.full.node.repository.IOrdenRepository;
import daryl.system.robot.full.node.repository.IRobotsRepository;

@Component
public class ControlAutoCierreOperaciones {

	@Autowired
	Logger logger;
	@Autowired
	ConfigData config;
	@Autowired
	private IOrdenRepository ordenRepository;
	@Autowired
	private IRobotsRepository robotsRepository;

	//Ejecutamos cada 10 mins
    @Scheduled(fixedDelay = 360000, initialDelay = 1000)
    @Transactional
	public void cerrarOperacionesAutomaticamente() {
    	logger.info("CERRANDO OPERACIONES AUTOMÁTICAMENTE=============");
    	
    	List<Robot> robots = robotsRepository.findAll();
    	for(Robot robot : robots) {
			//Cerramos todas las operaciones de cada robot
			//en caso de estar fuera de hora
			if(config.checkFechaHoraOperaciones() == Boolean.FALSE || robot.getRobotActivo() == Boolean.FALSE) {
				try {
					long millis = System.currentTimeMillis();
					
					List<Orden> ordenes = ordenRepository.findByfBajaAndTipoActivoAndEstrategia(null, robot.getActivo(), robot.getEstrategia());
					
					if(ordenes != null && ordenes.size() > 0) {
						
						for(Orden orden : ordenes) {
							orden.setFecha(config.getFechaInString(millis));
							orden.setHora(config.getHoraInString(millis));
							orden.setTipoOrden(TipoOrden.CLOSE);
							ordenRepository.save(orden);
							logger.info("ORDEN CERRADA POR CIERRE DE MERCADO -> " + orden);
						}
						
					}else {
						logger.info("NO EXISTE ORDEN PARA EL ROBOT -> " + robot);
					}
					
				}catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
    	}
		
    	logger.info("OPERACIONES CERRADAS AUTOMÁTICAMENTE=============");
    	
	}

}
