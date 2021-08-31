package daryl.system.databaseloader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.BackupHistorico;
import daryl.system.model.historicos.HistAudCad;
import daryl.system.model.historicos.Historico;

@Repository
public interface IBackupHistoricoRepository extends JpaRepository<BackupHistorico, Long> {

	BackupHistorico findFirstByTimeframeAndActivoOrderByFechaHoraDesc(Timeframes tf, Activo activo);
	
}
