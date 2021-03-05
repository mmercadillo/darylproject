package daryl.system.control.contizaciones.control.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.HistXauUsd;

@Repository
public interface IHistXauUsdRepository extends JpaRepository<HistXauUsd, Long> {

	HistXauUsd findFirstByTimeframeOrderByFechaHoraDesc(Timeframes tf);

}
