package daryl.system.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import daryl.system.model.Usuario;


@Repository
public interface IUserRepository extends JpaRepository<Usuario, Long> {
	
	Usuario findByUsername(String username);
	
}
