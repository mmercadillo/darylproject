package daryl.system.robots.ordenes.all.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import daryl.system.model.Orden;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Long> {

	List<Orden> findAllByOrderByRobotAsc();
	
	@Query("SELECT orden FROM Orden orden WHERE orden.robot in (?1)")
	List<Orden> findAllByRobots(List<String> robots);
}
