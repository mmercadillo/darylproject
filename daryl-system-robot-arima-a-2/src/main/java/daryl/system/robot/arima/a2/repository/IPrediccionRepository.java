package daryl.system.robot.arima.a2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.Prediccion;

@Repository
public interface IPrediccionRepository extends JpaRepository<Prediccion, Long> {

	
}
