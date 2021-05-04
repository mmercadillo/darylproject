package daryl.system.robots.arima.c.calculator.close.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.ArimaConfig;


@Repository
public interface IArimaConfigRepository extends JpaRepository<ArimaConfig, Long> {

	ArimaConfig findArimaConfigByRobot(String robot);

}
