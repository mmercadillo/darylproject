package daryl.system.robots.ordenes.all.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.RobotsCuenta;
import daryl.system.robots.ordenes.all.repo.IRobotsCuentaRepository;

@Service
public class RobotsCuentaServiceImpl implements IRobotsCuentaService {

	@Autowired
	IRobotsCuentaRepository repository;
	

	@Transactional
	public List<RobotsCuenta> findRobotsCuentaByCuenta(String cuenta) {
		
		return repository.findRobotsCuentaByCuenta(cuenta);
	}

	
}
