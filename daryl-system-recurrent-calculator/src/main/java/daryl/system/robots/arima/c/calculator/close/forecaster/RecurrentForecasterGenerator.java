package daryl.system.robots.arima.c.calculator.close.forecaster;

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

import daryl.system.comun.configuration.ConfigData;
import daryl.system.comun.enums.Activo;
import daryl.system.comun.enums.Mode;
import daryl.system.comun.enums.Timeframes;
import daryl.system.model.historicos.Historico;
import daryl.system.robots.arima.c.calculator.close.repository.IHistoricoRepository;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RecurrentForecasterGenerator implements Runnable{
	
	@Autowired
	Logger logger;
	@Autowired
	ConfigData config;
	@Autowired
	private IHistoricoRepository historicoRepository;
	
	private String robot;
	private Activo tipoActivo;
	private String estrategia;
	private Timeframes timeframe;
	int pagina;
	private List<Double> dataForForecast;

	public RecurrentForecasterGenerator() {
	}
	
	public void init(String estrategia, String robot, Activo tipoActivo, Timeframes timeframe, int pagina) {
		
		this.robot = robot;
		this.tipoActivo = tipoActivo;
		this.estrategia = estrategia;
		this.timeframe = timeframe;
		this.pagina = pagina;

	}


	public  void run() {

		
		List<Historico> historico = historicoRepository.findAllByTimeframeAndActivoOrderByFechaHoraDesc(this.timeframe, this.tipoActivo, PageRequest.of(0, this.pagina));
		
		Collections.reverse(historico);
		
		BarSeries datosForecast = BarSeriesUtils.generateBarListFromHistorico(historico,  "BarSeries_" + this.timeframe + "_" + this.tipoActivo, this.tipoActivo.getMultiplicador());
		MaxMinNormalizer darylNormalizer = new MaxMinNormalizer(datosForecast, Mode.CLOSE);
		List<Double> cierres = darylNormalizer.getDatos();

				
		
		
	}

    
}
