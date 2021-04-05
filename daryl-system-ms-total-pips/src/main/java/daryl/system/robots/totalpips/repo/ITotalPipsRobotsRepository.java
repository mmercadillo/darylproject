package daryl.system.robots.totalpips.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import daryl.system.model.ResumenRobot;

@Repository
public interface ITotalPipsRobotsRepository extends JpaRepository<ResumenRobot, Long> {
	@Query(value="SELECT SUM(r.total) FROM ResumenRobot r")
	Long sumResumenRobots();
}
