package daryl.system.databaseloader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.CombinacionArimaC;

@Repository
public interface ICombinacionesArimaCRepository extends JpaRepository<CombinacionArimaC, Long> {

	
}
