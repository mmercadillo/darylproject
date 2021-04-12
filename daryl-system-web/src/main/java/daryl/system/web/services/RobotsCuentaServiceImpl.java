package daryl.system.web.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.RobotsCuenta;
import daryl.system.web.repository.IRobotsCuentaRepository;

@Service
public class RobotsCuentaServiceImpl implements IRobotsCuentaService {

	@Autowired
	IRobotsCuentaRepository repository;
	
	@Transactional
	public List<RobotsCuenta> findRobotsCuentaByCuenta(String cuenta) {		
		return repository.findRobotsCuentaByCuenta(cuenta);
	}

	public void saveAll(List<RobotsCuenta> robotsCuenta) {
		repository.saveAll(robotsCuenta);
	}

	public void deleteAll(List<RobotsCuenta> robotsCuenta) {
		repository.deleteAll(robotsCuenta);
		
	}

	
}
