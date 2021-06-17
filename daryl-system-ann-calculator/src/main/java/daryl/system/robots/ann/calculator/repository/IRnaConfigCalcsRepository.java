package daryl.system.robots.ann.calculator.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.ArimaConfig;
import daryl.system.model.RnaConfig;
import daryl.system.model.RnaConfigCalcs;
import daryl.system.model.Robot;


@Repository
public interface IRnaConfigCalcsRepository extends JpaRepository<RnaConfigCalcs, Long> {

	RnaConfigCalcs findRnaConfigCalcsByRobot(String robot);

}
