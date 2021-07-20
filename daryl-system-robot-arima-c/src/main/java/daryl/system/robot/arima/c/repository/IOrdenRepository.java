package daryl.system.robot.arima.c.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Activo;
import daryl.system.model.Orden;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Long> {

	Orden findByfBajaAndTipoActivo(Long fBaja, String tipoActivo);
	Orden findByfBajaAndTipoActivoAndEstrategia(Long fBaja, Activo tipoActivo, String estrategia);
	Orden findBytipoActivoAndEstrategia(Activo tipoActivo, String estrategia);
	List<Orden> findAllByOrderByRobotAsc();
	List<Orden> findAllBytipoActivoAndEstrategia(Activo tipoActivo, String estrategia);
}
