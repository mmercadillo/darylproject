package daryl.system.robots.client.web.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import daryl.system.model.Orden;
import daryl.system.robots.client.web.repository.IOrdenRepository;

@Service
public class OrdenesServiceImpl implements IOrdenesService {

	@Autowired
	IOrdenRepository repository;
	
	@Transactional
	public List<Orden> findAllByOrderByRobotAsc() {
		
		return repository.findAllByOrderByRobotAsc();
	}

	@Transactional
	public List<Orden> findAllByRobots(List<String> robots) {
		
		return repository.findAllByRobots(robots);
	}	
	
}
