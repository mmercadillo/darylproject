package daryl.system.control.contizaciones.zeromq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.HistAudCad;

@Repository
public interface IHistAudCadRepository extends JpaRepository<HistAudCad, Long> {

	HistAudCad findFirstByTimeframeOrderByFechaHoraDesc(Timeframes tf);
	
}
