package daryl.system.robot.arima.d.inv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.HistWti;

@Repository
public interface IHistWtiRepository extends JpaRepository<HistWti, Long> {

	HistWti findFirstByOrderByFechaHoraDesc();
	//HistNdx findFirstByOrderByFechaHoraDesc();
	HistWti findFirstByTimeframeOrderByFechaHoraDesc(Timeframes tf);
	List<HistWti> findAllByTimeframeOrderByFechaHoraAsc(Timeframes tf);

	
}
