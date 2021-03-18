package daryl.system.control.contizaciones.control.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.HistEurUsd;

@Repository
public interface IHistEurUsdRepository extends JpaRepository<HistEurUsd, Long> {

	HistEurUsd findFirstByTimeframeOrderByFechaHoraDesc(Timeframes tf);

	
}
