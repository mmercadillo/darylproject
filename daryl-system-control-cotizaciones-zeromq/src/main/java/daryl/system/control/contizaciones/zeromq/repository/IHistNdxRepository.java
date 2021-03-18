package daryl.system.control.contizaciones.zeromq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.HistNdx;

@Repository
public interface IHistNdxRepository extends JpaRepository<HistNdx, Long> {


	HistNdx findFirstByTimeframeOrderByFechaHoraDesc(Timeframes tf);

}
