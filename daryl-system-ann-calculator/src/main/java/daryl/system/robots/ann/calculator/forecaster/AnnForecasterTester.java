package daryl.system.robots.ann.calculator.forecaster;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
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
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Mode;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.AnnConfig;
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

	public void calcularPrediccion(String bot, Timeframes timeframe, Activo activo, Integer pagina){

		Double res = 0.0;
		AnnConfig annConfig = getAnnConfig(bot);

		if(annConfig != null) {
			
			ANN ann  = null;
			try {
				ann = annFromByteArray(annConfig.getAnn());
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
			
			if(ann != null) {		
				
				List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraDesc(timeframe, activo, PageRequest.of(0, pagina));
				
				Collections.reverse(historico);
				
				BarSeries serieParaCalculo = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + timeframe + "_" + activo, activo.getMultiplicador());
				MaxMinNormalizer darylNormalizer =  new MaxMinNormalizer(serieParaCalculo, Mode.CLOSE);
				List<Double> datos = darylNormalizer.getDatos();
				datos = datos.subList(0, 120);
				
				
				for(int i = 0; i < (datos.size() - (annConfig.getNeuronasEntrada() + 1)); i++) {
					
					List<Double> datosAux = datos.subList(i, i+annConfig.getNeuronasEntrada()+1);
					
					res += prediccionANN(ann, datosAux, annConfig.getNeuronasEntrada());
					System.out.println("RESULTADO -> " + res);					
				}
				
				System.out.println("TOTAL -> " + res);
			}
			
		}
		
	
	}
	
    private double prediccionANN(ANN net, List<Double> datos, int neuronasEntrada){
    	
    	double[] input = datos.subList(0, datos.size()-1).stream().mapToDouble(dato -> dato.doubleValue()).toArray();
    	
    	FischerTransform ft_ann = net.getFt_ann();
        MovingAverages ma = new MovingAverages();
    	
        double[] ann_window = new double[neuronasEntrada];
        
        double resultado = 0.0;
        System.out.println("NEURONAS ENTRADA -> " + neuronasEntrada);
        for (int i=neuronasEntrada; i<=input.length;i++){
            try {
	            //Place past 20 values in ann_window
	            System.arraycopy(input, 0, ann_window, 0, neuronasEntrada);
	            
	            System.out.println("DATOS -> " + Arrays.toString(ann_window) + " - ESPERADO -> " + datos.get(datos.size()-1));
	            
	            ann_window = ft_ann.convert(ann_window);
	            ann_window = ma.SMA(ann_window, neuronasEntrada);
	            double[] annSignalTemp = net.run(ann_window);
	            long annSignal = Math.round(annSignalTemp[0]);
	            
	            System.out.println("SIGNAL -> " + annSignalTemp[0]);
	            
	            if (annSignal == 0.0) {
	            	//Vendemos
	            	resultado += datos.get(datos.size()-2) - datos.get(datos.size()-1);
	            	System.out.println("VENTA -> " + resultado);
	            }
	            else if (annSignal == 1.0){
	            	//Compramos
	            	resultado += datos.get(datos.size()-1) - datos.get(datos.size() - 2);
	            	System.out.println("COMPRA -> " + resultado);
	            }
            }catch (Exception e) {
				e.printStackTrace();
			}
        }
        net.setFt_ann(ft_ann);
        
        return resultado;
    }
	
	
	private AnnConfig getAnnConfig(String robot) {
		
		return annConfigRepository.findAnnConfigByRobot(robot);
		
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
