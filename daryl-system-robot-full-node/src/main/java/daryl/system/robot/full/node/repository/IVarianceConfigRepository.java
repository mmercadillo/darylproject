package daryl.system.robot.full.node.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.VarianceConfig;

@Repository
public interface IVarianceConfigRepository extends JpaRepository<VarianceConfig, Long> {

	VarianceConfig findVarianceConfigByRobot(String robot);

}
