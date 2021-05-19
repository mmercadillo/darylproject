package daryl.system.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import daryl.system.model.DemolabOps;


@Repository
public interface IDemolabDataRepository extends JpaRepository<DemolabOps, Long> {

	@Query("SELECT ho FROM DemolabOps ho WHERE ho.comentario LIKE ?1% ORDER BY ho.id asc")
	List<DemolabOps> findListaParaChartDemolabByRobot(String robot);
	
	List<DemolabOps> findAllByOrderByFcierreAsc();
	
}
