package daryl.system.web.services;

import daryl.system.model.Orden;


public interface IOrdenService {
	Orden findByfBajaAndEstrategia(Long fBaja, String robot);
}
