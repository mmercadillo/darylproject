package daryl.system.robots.control.configuraciones.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.ArimaConfig;
import daryl.system.model.ArimaConfigCalcs;


@Repository
public interface IArimaConfigCalcsRepository extends JpaRepository<ArimaConfigCalcs, Long> {

	ArimaConfigCalcs findArimaConfigCalcsByRobot(String robot);

}
