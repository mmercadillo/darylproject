package daryl.system.backtest.robot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.Historico;

@Repository
public interface IHistoricoRepository extends JpaRepository<Historico, Long> {

	List<Historico> findAllByTimeframeAndActivoOrderByFechaHoraAsc(Timeframes tf, Activo activo);
	
}
