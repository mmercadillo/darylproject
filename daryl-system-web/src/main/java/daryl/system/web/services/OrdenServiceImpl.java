package daryl.system.web.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.comun.enums.Activo;
import daryl.system.model.Orden;
import daryl.system.web.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {

	@Autowired
	IOrdenRepository repository;

	@Transactional
	public Orden findByfBajaAndEstrategia(Long fBaja, String robot) {
		return repository.findByfBajaAndEstrategia(fBaja, robot);
	}



	
	
}
