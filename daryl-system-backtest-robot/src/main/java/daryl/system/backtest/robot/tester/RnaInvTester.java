package daryl.system.backtest.robot.tester;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.neuroph.core.NeuralNetwork;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.MaxMinNormalizer;

import daryl.system.backtest.robot.repository.IHistoricoOperacionesBacktestRepository;
import daryl.system.backtest.robot.repository.IRnaConfigRepository;
import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.dataset.enums.Mode;
import daryl.system.comun.enums.TipoOrden;
import daryl.system.model.RnaConfig;
import daryl.system.model.Robot;
import daryl.system.model.backtest.HistoricoOperacionesBacktest;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RnaInvTester extends Tester implements Runnable{
	
	@Autowired
	Logger logger;
	@Autowired
	private IHistoricoOperacionesBacktestRepository operacionBacktestRepository;
	@Autowired
	IRnaConfigRepository rnaConfigRepository;
	@Autowired
	ConfigData config;
	@Autowired
	ApplicationContext ctx;
	
	private Robot robot;
	private BarSeries datosParaTest;
	private BarSeries cierres;
	RnaConfig rnaConfig;
	
	private NeuralNetwork neuralNetwork;

	public RnaInvTester() {
	}
	
	public void init(Robot robot, BarSeries datosParaTest, int inicio) {

		this.robot = robot;
		this.datosParaTest = datosParaTest;
		this.cierres =  this.datosParaTest.getSubSeries(0, inicio);

		//Dejamos los datos excepto los quitados anteriormente
		this.datosParaTest = this.datosParaTest.getSubSeries(inicio, this.datosParaTest.getBarCount());

		rnaConfig = rnaConfigRepository.findRnaConfigByRobot(robot.getRnaConfig());
		try {
			this.neuralNetwork = rnaFromByteArray(rnaConfig.getRna());
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		/*
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
		*/
	}
	
	private Double getPrediccionAnterior(BarSeries cierres) {
		
		MaxMinNormalizer darylNormalizer = new MaxMinNormalizer(cierres, Mode.CLOSE);
		List<Double> inputs = new ArrayList<Double>();
			
		int index = 0;
		do {
			index++;
			if(this.robot.getMode() == Mode.CLOSE) {
				inputs.add(darylNormalizer.normData(cierres.getBar(cierres.getBarCount()-index).getClosePrice().doubleValue()));
			}
			if(this.robot.getMode() == Mode.HIGH) {
				inputs.add(darylNormalizer.normData(cierres.getBar(cierres.getBarCount()-index).getHighPrice().doubleValue()));
			}
			if(this.robot.getMode() == Mode.LOW) {
				inputs.add(darylNormalizer.normData(cierres.getBar(cierres.getBarCount()-index).getLowPrice().doubleValue()));
			}
			if(this.robot.getMode() == Mode.OPEN) {
				inputs.add(darylNormalizer.normData(cierres.getBar(cierres.getBarCount()-index).getOpenPrice().doubleValue()));
			}			
		}while(index < this.rnaConfig.getNeuronasEntrada());
		
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
		for (int i = 0; i < datosParaTest.getBarCount()-1; i++) {
			
			this.cierres.addBar(datosParaTest.getBar(i));
			
			try {
				
				HistoricoOperacionesBacktest opBt = new HistoricoOperacionesBacktest();
				opBt.setRobot(this.robot.getRobot());
				
				Double prediccionAnterior = getPrediccionAnterior(cierres.getSubSeries(0, cierres.getBarCount()-1));
				
				MaxMinNormalizer darylNormalizer = new MaxMinNormalizer(cierres, Mode.CLOSE);
				
				List<Double> inputs = new ArrayList<Double>();
				int index = 0;
				do {
					index++;
					if(this.robot.getMode() == Mode.CLOSE) {
						inputs.add(darylNormalizer.normData(cierres.getBar(cierres.getBarCount()-index).getClosePrice().doubleValue()));
					}
					if(this.robot.getMode() == Mode.HIGH) {
						inputs.add(darylNormalizer.normData(cierres.getBar(cierres.getBarCount()-index).getHighPrice().doubleValue()));
					}
					if(this.robot.getMode() == Mode.LOW) {
						inputs.add(darylNormalizer.normData(cierres.getBar(cierres.getBarCount()-index).getLowPrice().doubleValue()));
					}
					if(this.robot.getMode() == Mode.OPEN) {
						inputs.add(darylNormalizer.normData(cierres.getBar(cierres.getBarCount()-index).getOpenPrice().doubleValue()));
					}			
				}while(index < this.rnaConfig.getNeuronasEntrada());

				Collections.reverse(inputs);

				neuralNetwork.setInput(inputs.stream().mapToDouble(Double::doubleValue).toArray());
				neuralNetwork.calculate();
				
		        // get network output
		        double[] networkOutput = neuralNetwork.getOutput();
		        //double predicted = interpretOutput(networkOutput);
		        double forecast = darylNormalizer.denormData(networkOutput[0]);
		        //logger.info("Robot -> " + this.robot.getRobot() + " PREDICCIÓN -> " + forecast + " ANTERIOR -> " + prediccionAnterior);
		        
				
				Double apertura = datosParaTest.getBar(i).getClosePrice().doubleValue();
				Double cierre = datosParaTest.getBar(i+1).getClosePrice().doubleValue(); 
				opBt.setApertura(apertura);
				opBt.setCierre(cierre);
				
				Long fechaHoraAperturaMillis = datosParaTest.getBar(i).getEndTime().toEpochSecond() * 1000;
				Long fechaHoraCierreMillis = datosParaTest.getBar(i+1).getEndTime().toEpochSecond() * 1000;

				opBt.setFapertura(fechaHoraAperturaMillis);
				opBt.setFcierre(fechaHoraCierreMillis);
		        

				opBt.setFaperturaTxt(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date(fechaHoraAperturaMillis)));
				opBt.setFcierreTxt(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date(fechaHoraCierreMillis)));
				
				opBt.setProfit(0.0);
		        if(forecast > prediccionAnterior) {
		        	opBt.setTipo(TipoOrden.SELL);
					opBt.setProfit(apertura - cierre);
		        }else if(forecast < prediccionAnterior) {
		        	opBt.setTipo(TipoOrden.BUY);
					opBt.setProfit(cierre - apertura);

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
	
	public NeuralNetwork rnaFromByteArray(byte[] byteArray) throws IOException, ClassNotFoundException {
		
		NeuralNetwork rna = (NeuralNetwork)SerializationUtils.deserialize(byteArray);
		return rna;

	}
}
