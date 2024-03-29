package daryl.system.robots.ann.calculator.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.RnaConfigCalcs;


@Repository
public interface IRnaConfigCalcsRepository extends JpaRepository<RnaConfigCalcs, Long> {

	RnaConfigCalcs findRnaConfigCalcsByRobot(String robot);

}
