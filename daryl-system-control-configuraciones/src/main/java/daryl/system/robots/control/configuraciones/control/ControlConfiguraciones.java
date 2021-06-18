package daryl.system.robots.control.configuraciones.control;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import daryl.system.comun.configuration.ConfigData;
import daryl.system.model.AnnConfig;
import daryl.system.model.AnnConfigCalcs;
import daryl.system.model.ArimaConfig;
import daryl.system.model.ArimaConfigCalcs;
import daryl.system.model.RnaConfig;
import daryl.system.model.RnaConfigCalcs;
import daryl.system.model.VarianceConfig;
import daryl.system.model.VarianceConfigCalcs;
import daryl.system.robots.control.configuraciones.repository.IAnnConfigCalcsRepository;
import daryl.system.robots.control.configuraciones.repository.IAnnConfigRepository;
import daryl.system.robots.control.configuraciones.repository.IArimaConfigCalcsRepository;
import daryl.system.robots.control.configuraciones.repository.IArimaConfigRepository;
import daryl.system.robots.control.configuraciones.repository.IRnaConfigCalcsRepository;
import daryl.system.robots.control.configuraciones.repository.IRnaConfigRepository;
import daryl.system.robots.control.configuraciones.repository.IVarianceConfigCalcsRepository;
import daryl.system.robots.control.configuraciones.repository.IVarianceConfigRepository;

@Component
public class ControlConfiguraciones {

	@Autowired
	Logger logger;

	@Autowired
	ConfigData config;
	
	@Autowired
	IArimaConfigCalcsRepository arimaConfigCalcsRepository;
	@Autowired
	IArimaConfigRepository arimaConfigRepository;
	@Autowired
	IRnaConfigCalcsRepository rnaConfigCalcsRepository;
	@Autowired
	IRnaConfigRepository rnaConfigRepository;
	@Autowired
	IVarianceConfigCalcsRepository varianceConfigCalcsRepository;
	@Autowired
	IVarianceConfigRepository varianceConfigRepository;
	@Autowired
	IAnnConfigCalcsRepository annConfigCalcsRepository;
	@Autowired
	IAnnConfigRepository annConfigRepository;

    @Scheduled(fixedDelay = 14400000, initialDelay = 1000)//cada 4 horas
    @Transactional
	public void controlArimaCConfiguration() {
    	
    	System.out.println("ACTUALIZANDO CONFIG ARIMA C");
    	List<ArimaConfig> configs = arimaConfigRepository.findAll();
    	for(ArimaConfig arimaConfig : configs) {
    		
    		ArimaConfigCalcs calcs = arimaConfigCalcsRepository.findArimaConfigCalcsByRobot(arimaConfig.getRobot());
    		if(calcs != null) {
    			
    			if(calcs.getResultado() > arimaConfig.getResultado()) {
    				
    				Long fechaHoraMillis = System.currentTimeMillis();
    				//Mapeamos
    				arimaConfig.setArCoefficients(calcs.getArCoefficients());
    				arimaConfig.setConstant(calcs.getConstant());
    				arimaConfig.setEstrategia(calcs.getEstrategia());
    				arimaConfig.setIntegrationOrder(calcs.getIntegrationOrder());
    				arimaConfig.setMaCoefficients(calcs.getMaCoefficients());
    				arimaConfig.setShockExpectation(calcs.getShockExpectation());
    				arimaConfig.setStandarDeviation(calcs.getStandarDeviation());
    				arimaConfig.setResultado(calcs.getResultado());
    				arimaConfig.setInicio(calcs.getInicio());
    				arimaConfig.setIndexA(calcs.getIndexA());
    				arimaConfig.setIndexB(calcs.getIndexB());
    				
    				arimaConfig.setFModificacion(fechaHoraMillis);
    				arimaConfig.setFecha(config.getFechaInString(fechaHoraMillis));
    				arimaConfig.setHora(config.getHoraInString(fechaHoraMillis));
    				
    				arimaConfigRepository.save(arimaConfig);
    				
    			}	
    		}
    	}
    	System.out.println("ACTUALIZADA CONFIG ARIMA C");
    	
    }
    
    @Scheduled(fixedDelay = 14400000, initialDelay = 1000)//cada 4 horas
    @Transactional
	public void controlVarianceConfiguration() {
    	
    	System.out.println("ACTUALIZANDO CONFIG VARIANCE");
    	List<VarianceConfig> configs = varianceConfigRepository.findAll();
    	for(VarianceConfig varianceConfig : configs) {
    		
    		VarianceConfigCalcs calcs = varianceConfigCalcsRepository.findVarianceConfigCalcsByRobot(varianceConfig.getRobot());
    		if(calcs != null) {
    			
    			if(calcs.getResultado() > varianceConfig.getResultado()) {
    				
    				Long fechaHoraMillis = System.currentTimeMillis();
    				//Mapeamos
    				varianceConfig.setAlpha(calcs.getAlpha());
    				varianceConfig.setBeta(calcs.getBeta());
    				varianceConfig.setLastN(calcs.getLastN());
    				varianceConfig.setLastOffset(calcs.getLastOffset());
    				varianceConfig.setN(calcs.getN());
    				varianceConfig.setOffset(calcs.getOffset());
    				varianceConfig.setResultado(calcs.getResultado());
    				varianceConfig.setLastAlpha(calcs.getLastAlpha());
    				varianceConfig.setLastBeta(calcs.getLastBeta());
    				varianceConfig.setLastM(calcs.getLastM());
    				varianceConfig.setM(calcs.getM());
    				
    				varianceConfig.setFModificacion(fechaHoraMillis);
    				varianceConfig.setFecha(config.getFechaInString(fechaHoraMillis));
    				varianceConfig.setHora(config.getHoraInString(fechaHoraMillis));
    				
    				varianceConfigRepository.save(varianceConfig);
    				
    			}
    			
    		}
    			
    	}
    	System.out.println("ACTUALIZADA CONFIG VARIANCE");
    	
    }

    @Scheduled(fixedDelay = 14400000, initialDelay = 1000)//cada 4 horas
    @Transactional
	public void controlRnaConfiguration() {
    	
    	System.out.println("ACTUALIZANDO CONFIG RNA");
    	List<RnaConfig> configs = rnaConfigRepository.findAll();
    	for(RnaConfig rnaConfig : configs) {
    		
    		RnaConfigCalcs calcs = rnaConfigCalcsRepository.findRnaConfigCalcsByRobot(rnaConfig.getRobot());
    		if(calcs != null) {
    			
    			if(calcs.getResultado() > rnaConfig.getResultado()) {
    				
    				Long fechaHoraMillis = System.currentTimeMillis();
    				//Mapeamos
    				rnaConfig.setFicheroRna(calcs.getFicheroRna());
    				rnaConfig.setLastBias(calcs.getLastBias());
    				rnaConfig.setLastCapasOcultas(calcs.getLastCapasOcultas());
    				rnaConfig.setLastHiddenNeurons(calcs.getLastHiddenNeurons());
    				rnaConfig.setLastNeuronasEntrada(calcs.getLastNeuronasEntrada());
    				rnaConfig.setLastPasoLearnigRate(calcs.getLastPasoLearnigRate());
    				rnaConfig.setLastPasoMomentum(calcs.getLastPasoMomentum());
    				rnaConfig.setLastTransferFunctionType(calcs.getLastTransferFunctionType());
    				rnaConfig.setNeuronasEntrada(calcs.getNeuronasEntrada());
    				rnaConfig.setResultado(calcs.getResultado());
    				rnaConfig.setRna(calcs.getRna());
    				
    				rnaConfig.setFModificacion(fechaHoraMillis);
    				rnaConfig.setFecha(config.getFechaInString(fechaHoraMillis));
    				rnaConfig.setHora(config.getHoraInString(fechaHoraMillis));
    				
    				rnaConfigRepository.save(rnaConfig);
    				
    			}
    			
    		}
    			
    	}
    	System.out.println("ACTUALIZADA CONFIG RNA");
    	
    }
    

    @Scheduled(fixedDelay = 14400000, initialDelay = 1000)//cada 4 horas
    @Transactional
	public void controlAnnConfiguration() {
    	
    	System.out.println("ACTUALIZANDO CONFIG ANN");
    	
    	
    	List<AnnConfigCalcs> calcs = annConfigCalcsRepository.findAll();
    	for(AnnConfigCalcs configCalc : calcs) {
    		
    		AnnConfig configAnn = annConfigRepository.findAnnConfigByRobot(configCalc.getRobot());
    		if(configAnn == null) configAnn = new AnnConfig();
    		
    		Long fechaHoraMillis = System.currentTimeMillis();
    		if(configAnn.getResultado() == null || configCalc.getResultado() > configAnn.getResultado()) {
				

				//Mapeamos
    			configAnn.setFicheroAnn(configCalc.getFicheroAnn());
    			configAnn.setLastHiddenNeurons(configCalc.getLastHiddenNeurons());
    			configAnn.setLastNeuronasEntrada(configCalc.getLastNeuronasEntrada());
    			configAnn.setLastPasoLearnigRate(configCalc.getLastPasoLearnigRate());
    			configAnn.setLastPasoMomentum(configCalc.getLastPasoMomentum());
    			configAnn.setLastTransferFunctionType(configCalc.getLastTransferFunctionType());
    			configAnn.setNeuronasEntrada(configCalc.getNeuronasEntrada());
    			configAnn.setResultado(configCalc.getResultado());
    			configAnn.setAnn(configCalc.getAnn());
				
    			configAnn.setFModificacion(fechaHoraMillis);
				configAnn.setFecha(config.getFechaInString(fechaHoraMillis));
				configAnn.setHora(config.getHoraInString(fechaHoraMillis));
				
				annConfigRepository.save(configAnn);
				
			}
    		
    		
    	}
    	
    	System.out.println("ACTUALIZADA CONFIG RNA");
    	
    }

}
