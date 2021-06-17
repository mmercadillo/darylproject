package daryl.system.robots.control.historico.operaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import daryl.system.model.HistoricoOperaciones;

@Repository
public interface IHistoricoOperacionesRepository extends JpaRepository<HistoricoOperaciones, Long> {

	@Query("SELECT ho FROM HistoricoOperaciones ho WHERE ho.comentario LIKE ?1% and ho.fapertura > ?2 ORDER BY ho.fapertura asc")
	List<HistoricoOperaciones> findListaByRobot(String comentario, String fapertura);
	
	
}
