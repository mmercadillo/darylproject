package daryl.system.robots.rna.calculator.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.ArimaConfig;
import daryl.system.model.RnaConfig;
import daryl.system.model.Robot;


@Repository
public interface IRnaConfigRepository extends JpaRepository<RnaConfig, Long> {

	RnaConfig findRnaConfigByRobot(String robot);

}
