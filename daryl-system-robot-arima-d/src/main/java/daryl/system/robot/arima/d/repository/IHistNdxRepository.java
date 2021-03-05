package daryl.system.robot.arima.d.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.HistNdx;

@Repository
public interface IHistNdxRepository extends JpaRepository<HistNdx, Long> {

	HistNdx findFirstByOrderByFechaHoraDesc();
	//HistNdx findFirstByOrderByFechaHoraDesc();
	HistNdx findFirstByTimeframeOrderByFechaHoraDesc(Timeframes tf);
	List<HistNdx> findAllByTimeframeOrderByFechaHoraAsc(Timeframes tf);

	
}
