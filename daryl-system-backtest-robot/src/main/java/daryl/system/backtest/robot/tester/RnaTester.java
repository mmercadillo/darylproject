package daryl.system.backtest.robot.tester;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.neuroph.core.NeuralNetwork;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import daryl.system.backtest.robot.repository.IArimaConfigRepository;
import daryl.system.backtest.robot.repository.IOperacionBacktestRepository;
import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.dataset.Datos;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.dataset.normalizer.DarylMaxMinNormalizer;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.Robot;
import daryl.system.model.backtest.OperacionBacktest;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RnaTester extends Tester implements Runnable{
	
	@Autowired
	Logger logger;
	@Autowired
	private IOperacionBacktestRepository operacionBacktestRepository;
	@Autowired
	IArimaConfigRepository arimaConfigRepository;
	@Autowired
	ConfigData config;
	@Autowired
	ApplicationContext ctx;

	private Robot robot;
	private List<Datos> datosParaTest;
	private List<Datos> cierres;
	private NeuralNetwork neuralNetwork;

	public RnaTester() {
	}
	
	public void init(Robot robot, List<Datos> datosParaTest, int inicio) {

		this.robot = robot;
		this.datosParaTest = datosParaTest;
		this.cierres =  this.datosParaTest.subList(0, inicio).stream().map(d -> d).collect(Collectors.toList());
		//Dejamos los datos excepto los quitados anteriormente
		this.datosParaTest = this.datosParaTest.subList(inicio, this.datosParaTest.size());
		
		File rna = null;
		System.out.println("SE CARGA EL FICHERO: " + this.robot.getFicheroRna());
		try {
			System.out.println("SE CARGA DESDE EL CLASSPATH");
			rna = ctx.getResource("classpath:/rnas/" + this.robot.getFicheroRna()).getFile();
			
		}catch (Exception e) {
			System.out.println("SE HACE LA FORMA TRADICIONAL");
			String fileName = "F:\\DarylSystem\\rnas\\"+this.robot.getFicheroRna();
			rna = new File(fileName);
	        e.printStackTrace();
		}
		this.neuralNetwork = NeuralNetwork.createFromFile(rna);
	}
	
	
	private Double getPrediccionAnterior(List<Datos> cierres) {
		
		DarylMaxMinNormalizer darylNormalizer = new DarylMaxMinNormalizer(cierres, Mode.CLOSE);
		List<Double> inputs = new ArrayList<Double>();
			
		int index = 0;
		do {
			index++;
			if(this.robot.getMode() == Mode.CLOSE) {
				inputs.add(darylNormalizer.normData(cierres.get(cierres.size()-index).getCierre()));
			}
			if(this.robot.getMode() == Mode.HIGH) {
				inputs.add(darylNormalizer.normData(cierres.get(cierres.size()-index).getMaximo()));
			}
			if(this.robot.getMode() == Mode.LOW) {
				inputs.add(darylNormalizer.normData(cierres.get(cierres.size()-index).getMinimo()));
			}
			if(this.robot.getMode() == Mode.OPEN) {
				inputs.add(darylNormalizer.normData(cierres.get(cierres.size()-index).getApertura()));
			}			
		}while(index < this.robot.getNeuronasEntrada());
		
		Collections.reverse(inputs);
		neuralNetwork.setInput(inputs.stream().mapToDouble(Double::doubleValue).toArray());
		neuralNetwork.calculate();
		
        // get network output
        double[] networkOutput = this.neuralNetwork.getOutput();
        //double predicted = interpretOutput(networkOutput);
        double prediccionAnterior =  darylNormalizer.denormData(networkOutput[0]);

        return prediccionAnterior;
	}

	
	
	public  void run() {

		//Recorremos los datos 
		for (int i = 0; i < datosParaTest.size()-1; i++) {
			
			cierres.add(datosParaTest.get(i));
			
			try {
				
				OperacionBacktest opBt = new OperacionBacktest();
				opBt.setRobot(this.robot.getRobot());
				

				Double prediccionAnterior = getPrediccionAnterior(cierres.subList(0, cierres.size()-1));
				
				DarylMaxMinNormalizer darylNormalizer = new DarylMaxMinNormalizer(cierres, Mode.CLOSE);
				
				List<Double> inputs = new ArrayList<Double>();
				int index = 0;
				do {
					index++;
					if(this.robot.getMode() == Mode.CLOSE) {
						inputs.add(darylNormalizer.normData(cierres.get(cierres.size()-index).getCierre()));
					}
					if(this.robot.getMode() == Mode.HIGH) {
						inputs.add(darylNormalizer.normData(cierres.get(cierres.size()-index).getMaximo()));
					}
					if(this.robot.getMode() == Mode.LOW) {
						inputs.add(darylNormalizer.normData(cierres.get(cierres.size()-index).getMinimo()));
					}
					if(this.robot.getMode() == Mode.OPEN) {
						inputs.add(darylNormalizer.normData(cierres.get(cierres.size()-index).getApertura()));
					}			
				}while(index < this.robot.getNeuronasEntrada());

				Collections.reverse(inputs);

				neuralNetwork.setInput(inputs.stream().mapToDouble(Double::doubleValue).toArray());
				neuralNetwork.calculate();
				
		        // get network output
		        double[] networkOutput = this.neuralNetwork.getOutput();
		        //double predicted = interpretOutput(networkOutput);
		        double forecast = darylNormalizer.denormData(networkOutput[0]);
		        		        
				Double apertura = datosParaTest.get(i).getCierre();
				Double cierre = datosParaTest.get(i+1).getCierre(); 
				opBt.setApertura(apertura);
				opBt.setCierre(cierre);
				
				String fechaHoraApertura = datosParaTest.get(i).getFecha() + " " + datosParaTest.get(i).getHora();
				String fechaHoraCierre = datosParaTest.get(i+1).getFecha() + " " + datosParaTest.get(i+1).getHora();
				opBt.setFaperturaTxt(fechaHoraApertura);
				opBt.setFcierreTxt(fechaHoraCierre);
				
				opBt.setFapertura(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").parse(fechaHoraApertura).getTime());
				opBt.setFcierre(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").parse(fechaHoraCierre).getTime());
		        
		        
		        if(forecast > prediccionAnterior) {
		        	opBt.setTipo(TipoOrden.BUY);
					opBt.setProfit(cierre - apertura);
		        }else if(forecast < prediccionAnterior) {
		        	opBt.setTipo(TipoOrden.SELL);
					opBt.setProfit(apertura - cierre);
		        }
		        
		        
				opBt.setSwap(0.0);
				opBt.setComision(0.0);

				//Guardamos la operación de backtest
				operacionBacktestRepository.save(opBt);
				System.out.println("Operación guardada: " + opBt.toString());

			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	
	}
	

}
