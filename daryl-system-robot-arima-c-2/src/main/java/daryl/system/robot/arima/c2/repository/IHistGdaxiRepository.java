package daryl.system.robot.arima.c2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.HistGdaxi;

@Repository
public interface IHistGdaxiRepository extends JpaRepository<HistGdaxi, Long> {

	HistGdaxi findFirstByOrderByFechaHoraDesc();
	//HistGdaxi findFirstByOrderByFechaHoraDesc();
	HistGdaxi findFirstByTimeframeOrderByFechaHoraDesc(Timeframes tf);
	List<HistGdaxi> findAllByTimeframeOrderByFechaHoraAsc(Timeframes tf);

	
}
