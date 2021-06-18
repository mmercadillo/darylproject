package daryl.system.robots.control.configuraciones.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.AnnConfigCalcs;


@Repository
public interface IAnnConfigCalcsRepository extends JpaRepository<AnnConfigCalcs, Long> {

	AnnConfigCalcs findAnnConfigCalcsByRobot(String robot);

}
