package daryl.system.robot.full.node.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Activo;
import daryl.system.model.Orden;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Long> {

	Orden findByfBajaAndTipoActivo(Long fBaja, String activo);
	List<Orden> findByfBajaAndTipoActivoAndEstrategia(Long fBaja, Activo activo, String estrategia);
	List<Orden> findAllByOrderByRobotAsc();
	Long deleteByRobot(String robot);
}
