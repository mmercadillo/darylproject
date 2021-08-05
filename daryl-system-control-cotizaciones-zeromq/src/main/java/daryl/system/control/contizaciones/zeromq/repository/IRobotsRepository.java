package daryl.system.control.contizaciones.zeromq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.CanalAmq;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.Robot;

@Repository
public interface IRobotsRepository extends JpaRepository<Robot, Long> {

	@Query("SELECT ro FROM Robot ro WHERE ro.activo = ?1 and ro.timeframe = ?2")
	List<Robot> findRobotsByActivoAndTimeframe(Activo activo, Timeframes timeframe);
	
	@Query("SELECT ro FROM Robot ro WHERE ro.activo = ?1 and ro.timeframe = ?2 and ro.canal = ?3")
	List<Robot> findRobotsByActivoAndTimeframeAndCanal(Activo activo, Timeframes timeframe, CanalAmq canal);

}
