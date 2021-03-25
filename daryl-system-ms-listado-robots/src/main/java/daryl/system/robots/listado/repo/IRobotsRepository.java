package daryl.system.robots.listado.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import daryl.system.model.HistoricoOperaciones;
import daryl.system.model.Robot;

@Repository
public interface IRobotsRepository extends JpaRepository<Robot, Long> {


}
