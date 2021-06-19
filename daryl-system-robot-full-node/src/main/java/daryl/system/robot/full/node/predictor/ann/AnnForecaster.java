package daryl.system.robot.full.node.predictor.ann;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.MaxMinNormalizer;
import org.ta4j.core.utils.BarSeriesUtils;

import daryl.ann.ANN;
import daryl.ann.FischerTransform;
import daryl.ann.MovingAverages;
import daryl.system.comun.enums.Mode;
import daryl.system.model.AnnConfig;
import daryl.system.model.Robot;
import daryl.system.model.historicos.Historico;
import daryl.system.robot.full.node.predictor.base.Forecaster;
import daryl.system.robot.full.node.repository.IAnnConfigRepository;
import lombok.ToString;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ToString
public class AnnForecaster  extends Forecaster{

	@Autowired
	private IAnnConfigRepository annConfigRepository;

	@Override
	public Double calcularPrediccion(Robot bot){

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
				
				List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraAsc(bot.getTimeframe(), bot.getActivo(), PageRequest.of(0,  20));
				BarSeries serieParaCalculo = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + bot.getTimeframe() + "_" + bot.getActivo(), bot.getActivo().getMultiplicador());
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
	
	
	private AnnConfig getAnnConfig(Robot robot) {
		
		return annConfigRepository.findAnnConfigByRobot(robot.getAnnConfig());
		
	}
	public ANN annFromByteArray(byte[] byteArray) throws IOException, ClassNotFoundException {
		
		ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object obj2 = ois.readObject();
		
		//Object obj = SerializationUtils.deserialize(byteArray);
		ANN ann = (ANN)obj2;
		return ann;
	}


}
