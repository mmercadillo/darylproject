package daryl.system.robots.control.configuraciones.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.VarianceConfigCalcs;


@Repository
public interface IVarianceConfigCalcsRepository extends JpaRepository<VarianceConfigCalcs, Long> {

	VarianceConfigCalcs findVarianceConfigCalcsByRobot(String robot);

}
