package daryl.system.robot.ann.b.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.Historico;

@Repository
public interface IHistoricoRepository extends JpaRepository<Historico, Long> {

	Historico findFirstByTimeframeAndActivoOrderByFechaHoraDesc(Timeframes tf, Activo activo);
	List<Historico> findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes tf, Activo activo);
	List<Historico> findAllByTimeframeAndActivoOrderByFechaHoraDesc(Timeframes tf, Activo activo, Pageable page);
	
}
