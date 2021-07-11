package daryl.system.robots.control.historico.operaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import daryl.system.model.DemolabOps;
import daryl.system.model.HistoricoOperaciones;

@Repository
public interface IHistoricoOperacionesDemolabRepository extends JpaRepository<HistoricoOperaciones, Long> {

	@Query("SELECT ho FROM DemolabOps ho WHERE ho.comentario LIKE ?1% and ho.fcierre > ?2 ORDER BY ho.fcierre asc")
	List<DemolabOps> findListaByRobot(String comentario, String fapertura);
	
	
}
