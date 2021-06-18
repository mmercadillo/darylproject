package daryl.system.robots.ann.calculator.forecaster;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.MaxMinNormalizer;
import org.ta4j.core.utils.BarSeriesUtils;

import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Mode;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.AnnConfig;
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;
import daryl.system.robots.ann.calculator.repository.IAnnConfigRepository;
import daryl.system.robots.ann.calculator.repository.IHistoricoRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class AnnForecasterTester  {

	
	@Autowired
	Logger logger;

	@Autowired
	private IAnnConfigRepository annConfigRepository;
	@Autowired
	private IHistoricoRepository historicoRepository;

	public Double calcularPrediccion(String bot, Timeframes timeframe, Activo activo){

		Double prediccion = 0.0;
		AnnConfig annConfig = getAnnConfig(bot);

		if(annConfig != null) {
			
			ANN ann  = null;
			try {
				ann = annFromByteArray(annConfig.getAnn());
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
			
			if(ann != null) {		
				
				List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(timeframe, activo, PageRequest.of(0,  annConfig.getNeuronasEntrada()));
				BarSeries serieParaCalculo = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + timeframe + "_" + activo, activo.getMultiplicador());
				MaxMinNormalizer darylNormalizer =  new MaxMinNormalizer(serieParaCalculo, Mode.CLOSE);
				List<Double> datos = darylNormalizer.getDatos();
				

				prediccion = prediccionANN(ann, datos);
				
				
			}
			
		}
		
		
        return prediccion;
	
	}
	
    private double prediccionANN(ANN net, List<Double> datos){
    	
    	double prediccion = 0.0;
    	
    	try {
	    	double[] input = datos.stream().mapToDouble(dato -> dato.doubleValue()).toArray();
	    	
	        FischerTransform ft_ann = new FischerTransform();
	        MovingAverages ma = new MovingAverages();
	        
	        input = ft_ann.convert(input);
	        input = ma.SMA(input, 5);
	        
	        double[] annSignalTemp = net.run(input);
	        logger.info("SIGNAL -> " +annSignalTemp[0]);
	        long annSignal = Math.round(annSignalTemp[0]);
	        
	        if (annSignal == 0.0) {
	        	//Vendemos
	        	prediccion = -1.0;
	        }
	        else if (annSignal == 1.0){
	        	//Compramos
	        	prediccion = 1.0;
	        }
	        
    	}catch (Exception e) {
    		e.printStackTrace();
		}
        return prediccion;
    }
	
	
	private AnnConfig getAnnConfig(String robot) {
		
		return annConfigRepository.findAnnConfigByRobot(robot);
		
	}
	
	public ANN annFromByteArray(byte[] byteArray) throws IOException, ClassNotFoundException {
		
		ANN ann = (ANN)SerializationUtils.deserialize(byteArray);
		return ann;
	}


}
