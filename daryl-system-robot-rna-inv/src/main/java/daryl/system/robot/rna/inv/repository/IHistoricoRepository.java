package daryl.system.robot.rna.inv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.HistAudCad;
import daryl.system.model.historicos.Historico;

@Repository
public interface IHistoricoRepository extends JpaRepository<Historico, Long> {

	Historico findFirstByTimeframeAndActivoOrderByFechaHoraDesc(Timeframes tf, Activo activo);
	
}
