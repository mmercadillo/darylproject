package daryl.system.web.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.ResumenRobot;

@Repository
public interface IResumenRobotRepository extends JpaRepository<ResumenRobot, Long> {
	List<ResumenRobot> findResumenRobotsByOrderByTotalDesc(Pageable page);
	ResumenRobot findResumenRobotByRobotOrderByTotalDesc(String robot);
}
