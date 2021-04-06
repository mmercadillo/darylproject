package daryl.system.robots.client.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.RobotsCuenta;

@Repository
public interface IRobotsCuentaRepository extends JpaRepository<RobotsCuenta, Long> {
	List<RobotsCuenta> findRobotsCuentaByCuenta(String cuenta);
}
