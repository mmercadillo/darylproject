package daryl.system.robot.arima.b2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.HistAudCad;

@Repository
public interface IHistAudCadRepository extends JpaRepository<HistAudCad, Long> {

	HistAudCad findFirstByOrderByFechaHoraDesc();
	//HistAudCad findFirstByOrderByFechaHoraDesc();
	HistAudCad findFirstByTimeframeOrderByFechaHoraDesc(Timeframes tf);
	List<HistAudCad> findAllByTimeframeOrderByFechaHoraAsc(Timeframes tf);

	
}
