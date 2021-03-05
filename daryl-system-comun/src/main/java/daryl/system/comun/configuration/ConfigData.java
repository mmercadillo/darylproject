package daryl.system.comun.configuration;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import daryl.system.comun.enums.Timeframes;
import daryl.system.comun.exceptions.SistemaException;

@Component
public class ConfigData {

	public static final String CADENA_VACIA = "";
	public static final String FILE_SEPARATOR = "\\";

	public static final String ZEROMQ_SERVER_URL_FROM_PYTHON = "tcp://127.0.0.1:5555";
	public static final String ZEROMQ_SERVER_URL_FROM_JAVA = "tcp://127.0.0.1:5556";
	
	//@Autowired
	//private ConfigFicheros ficheros;
	//@Autowired
	//private ConfigModo modo;
	@Autowired
	private ConfigMetratrader metatrader;
	
	//private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	private SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	private SimpleDateFormat sdfFechaSolo = new SimpleDateFormat("yyyy.MM.dd");
	private SimpleDateFormat sdfHoraSolo = new SimpleDateFormat("HH:mm:ss");

	public String getActualDateFormattedInString() {
		return sdfFull.format(new Date());
	}	
	
	public Long getFechaHoraInMillis(String fecha, String hora) throws ParseException {	
		return sdfFull.parse(fecha + " " + hora).getTime();
	}
	
	public Date getFechaHoraFromMillis(Long millis) throws ParseException {
		return new Date(millis);
	}
	
	public String getFechaInString(Long millis) {
		return getFechaInString(new Date(millis));
	}	
	
	public String getHoraInString(Long millis) {
		return getHoraInString(new Date(millis));
	}	
	
	public String getFechaInString(Date fecha) {
		return sdfFechaSolo.format(fecha);
	}	
	
	public String getHoraInString(Date fecha) {
		return sdfHoraSolo.format(fecha);
	}
	
	/*
	public Estrategia getEstrategiaByRobot(TipoRobot robot) {
		
		if(robot == TipoRobot.ARIMA_AUDCAD_10080) return Estrategia.ARIMA_AUDCAD_10080;
		if(robot == TipoRobot.ARIMA_AUDCAD_1440) return Estrategia.ARIMA_AUDCAD_1440;
		if(robot == TipoRobot.ARIMA_AUDCAD_240) return Estrategia.ARIMA_AUDCAD_240;
		if(robot == TipoRobot.ARIMA_AUDCAD_60) return Estrategia.ARIMA_AUDCAD_60;

		if(robot == TipoRobot.ARIMA_I_AUDCAD_10080) return Estrategia.ARIMA_I_AUDCAD_10080;
		if(robot == TipoRobot.ARIMA_I_AUDCAD_1440) return Estrategia.ARIMA_I_AUDCAD_1440;
		if(robot == TipoRobot.ARIMA_I_AUDCAD_240) return Estrategia.ARIMA_I_AUDCAD_240;
		if(robot == TipoRobot.ARIMA_I_AUDCAD_60) return Estrategia.ARIMA_I_AUDCAD_60;
		
		if(robot == TipoRobot.ARIMA_B_AUDCAD_10080) return Estrategia.ARIMA_B_AUDCAD_10080;
		if(robot == TipoRobot.ARIMA_B_AUDCAD_1440) return Estrategia.ARIMA_B_AUDCAD_1440;
		if(robot == TipoRobot.ARIMA_B_AUDCAD_240) return Estrategia.ARIMA_B_AUDCAD_240;
		if(robot == TipoRobot.ARIMA_B_AUDCAD_60) return Estrategia.ARIMA_B_AUDCAD_60;
		
		if(robot == TipoRobot.ARIMA_I_B_AUDCAD_10080) return Estrategia.ARIMA_I_B_AUDCAD_10080;
		if(robot == TipoRobot.ARIMA_I_B_AUDCAD_1440) return Estrategia.ARIMA_I_B_AUDCAD_1440;
		if(robot == TipoRobot.ARIMA_I_B_AUDCAD_240) return Estrategia.ARIMA_I_B_AUDCAD_240;
		if(robot == TipoRobot.ARIMA_I_B_AUDCAD_60) return Estrategia.ARIMA_I_B_AUDCAD_60;
		
		if(robot == TipoRobot.ARIMA_C_AUDCAD_10080) return Estrategia.ARIMA_C_AUDCAD_10080;
		if(robot == TipoRobot.ARIMA_C_AUDCAD_1440) return Estrategia.ARIMA_C_AUDCAD_1440;
		if(robot == TipoRobot.ARIMA_C_AUDCAD_240) return Estrategia.ARIMA_C_AUDCAD_240;
		if(robot == TipoRobot.ARIMA_C_AUDCAD_60) return Estrategia.ARIMA_C_AUDCAD_60;

		if(robot == TipoRobot.ARIMA_I_C_AUDCAD_10080) return Estrategia.ARIMA_I_C_AUDCAD_10080;
		if(robot == TipoRobot.ARIMA_I_C_AUDCAD_1440) return Estrategia.ARIMA_I_C_AUDCAD_1440;
		if(robot == TipoRobot.ARIMA_I_C_AUDCAD_240) return Estrategia.ARIMA_I_C_AUDCAD_240;
		if(robot == TipoRobot.ARIMA_I_C_AUDCAD_60) return Estrategia.ARIMA_I_C_AUDCAD_60;
		
		if(robot == TipoRobot.ARIMA_D_AUDCAD_10080) return Estrategia.ARIMA_D_AUDCAD_10080;
		if(robot == TipoRobot.ARIMA_D_AUDCAD_1440) return Estrategia.ARIMA_D_AUDCAD_1440;
		if(robot == TipoRobot.ARIMA_D_AUDCAD_240) return Estrategia.ARIMA_D_AUDCAD_240;
		if(robot == TipoRobot.ARIMA_D_AUDCAD_60) return Estrategia.ARIMA_D_AUDCAD_60;
		
		if(robot == TipoRobot.ARIMA_EURUSD_10080) return Estrategia.ARIMA_EURUSD_10080;
		if(robot == TipoRobot.ARIMA_EURUSD_1440) return Estrategia.ARIMA_EURUSD_1440;
		if(robot == TipoRobot.ARIMA_EURUSD_240) return Estrategia.ARIMA_EURUSD_240;
		if(robot == TipoRobot.ARIMA_EURUSD_60) return Estrategia.ARIMA_EURUSD_60;

		if(robot == TipoRobot.ARIMA_I_EURUSD_10080) return Estrategia.ARIMA_I_EURUSD_10080;
		if(robot == TipoRobot.ARIMA_I_EURUSD_1440) return Estrategia.ARIMA_I_EURUSD_1440;
		if(robot == TipoRobot.ARIMA_I_EURUSD_240) return Estrategia.ARIMA_I_EURUSD_240;
		if(robot == TipoRobot.ARIMA_I_EURUSD_60) return Estrategia.ARIMA_I_EURUSD_60;
		
		if(robot == TipoRobot.ARIMA_B_EURUSD_10080) return Estrategia.ARIMA_B_EURUSD_10080;
		if(robot == TipoRobot.ARIMA_B_EURUSD_1440) return Estrategia.ARIMA_B_EURUSD_1440;
		if(robot == TipoRobot.ARIMA_B_EURUSD_240) return Estrategia.ARIMA_B_EURUSD_240;
		if(robot == TipoRobot.ARIMA_B_EURUSD_60) return Estrategia.ARIMA_B_EURUSD_60;

		if(robot == TipoRobot.ARIMA_I_B_EURUSD_10080) return Estrategia.ARIMA_I_B_EURUSD_10080;
		if(robot == TipoRobot.ARIMA_I_B_EURUSD_1440) return Estrategia.ARIMA_I_B_EURUSD_1440;
		if(robot == TipoRobot.ARIMA_I_B_EURUSD_240) return Estrategia.ARIMA_I_B_EURUSD_240;
		if(robot == TipoRobot.ARIMA_I_B_EURUSD_60) return Estrategia.ARIMA_I_B_EURUSD_60;
		
		if(robot == TipoRobot.ARIMA_C_EURUSD_10080) return Estrategia.ARIMA_C_EURUSD_10080;
		if(robot == TipoRobot.ARIMA_C_EURUSD_1440) return Estrategia.ARIMA_C_EURUSD_1440;
		if(robot == TipoRobot.ARIMA_C_EURUSD_240) return Estrategia.ARIMA_C_EURUSD_240;
		if(robot == TipoRobot.ARIMA_C_EURUSD_60) return Estrategia.ARIMA_C_EURUSD_60;

		if(robot == TipoRobot.ARIMA_I_C_EURUSD_10080) return Estrategia.ARIMA_I_C_EURUSD_10080;
		if(robot == TipoRobot.ARIMA_I_C_EURUSD_1440) return Estrategia.ARIMA_I_C_EURUSD_1440;
		if(robot == TipoRobot.ARIMA_I_C_EURUSD_240) return Estrategia.ARIMA_I_C_EURUSD_240;
		if(robot == TipoRobot.ARIMA_I_C_EURUSD_60) return Estrategia.ARIMA_I_C_EURUSD_60;
		
		if(robot == TipoRobot.ARIMA_D_EURUSD_10080) return Estrategia.ARIMA_D_EURUSD_10080;
		if(robot == TipoRobot.ARIMA_D_EURUSD_1440) return Estrategia.ARIMA_D_EURUSD_1440;
		if(robot == TipoRobot.ARIMA_D_EURUSD_240) return Estrategia.ARIMA_D_EURUSD_240;
		if(robot == TipoRobot.ARIMA_D_EURUSD_60) return Estrategia.ARIMA_D_EURUSD_60;
		
		if(robot == TipoRobot.ARIMA_GDAXI_10080) return Estrategia.ARIMA_GDAXI_10080;
		if(robot == TipoRobot.ARIMA_GDAXI_1440) return Estrategia.ARIMA_GDAXI_1440;
		if(robot == TipoRobot.ARIMA_GDAXI_240) return Estrategia.ARIMA_GDAXI_240;
		if(robot == TipoRobot.ARIMA_GDAXI_60) return Estrategia.ARIMA_GDAXI_60;

		if(robot == TipoRobot.ARIMA_I_GDAXI_10080) return Estrategia.ARIMA_I_GDAXI_10080;
		if(robot == TipoRobot.ARIMA_I_GDAXI_1440) return Estrategia.ARIMA_I_GDAXI_1440;
		if(robot == TipoRobot.ARIMA_I_GDAXI_240) return Estrategia.ARIMA_I_GDAXI_240;
		if(robot == TipoRobot.ARIMA_I_GDAXI_60) return Estrategia.ARIMA_I_GDAXI_60;
		
		if(robot == TipoRobot.ARIMA_B_GDAXI_10080) return Estrategia.ARIMA_B_GDAXI_10080;
		if(robot == TipoRobot.ARIMA_B_GDAXI_1440) return Estrategia.ARIMA_B_GDAXI_1440;
		if(robot == TipoRobot.ARIMA_B_GDAXI_240) return Estrategia.ARIMA_B_GDAXI_240;
		if(robot == TipoRobot.ARIMA_B_GDAXI_60) return Estrategia.ARIMA_B_GDAXI_60;

		if(robot == TipoRobot.ARIMA_I_B_GDAXI_10080) return Estrategia.ARIMA_I_B_GDAXI_10080;
		if(robot == TipoRobot.ARIMA_I_B_GDAXI_1440) return Estrategia.ARIMA_I_B_GDAXI_1440;
		if(robot == TipoRobot.ARIMA_I_B_GDAXI_240) return Estrategia.ARIMA_I_B_GDAXI_240;
		if(robot == TipoRobot.ARIMA_I_B_GDAXI_60) return Estrategia.ARIMA_I_B_GDAXI_60;
		
		if(robot == TipoRobot.ARIMA_C_GDAXI_10080) return Estrategia.ARIMA_C_GDAXI_10080;
		if(robot == TipoRobot.ARIMA_C_GDAXI_1440) return Estrategia.ARIMA_C_GDAXI_1440;
		if(robot == TipoRobot.ARIMA_C_GDAXI_240) return Estrategia.ARIMA_C_GDAXI_240;
		if(robot == TipoRobot.ARIMA_C_GDAXI_60) return Estrategia.ARIMA_C_GDAXI_60;

		if(robot == TipoRobot.ARIMA_I_C_GDAXI_10080) return Estrategia.ARIMA_I_C_GDAXI_10080;
		if(robot == TipoRobot.ARIMA_I_C_GDAXI_1440) return Estrategia.ARIMA_I_C_GDAXI_1440;
		if(robot == TipoRobot.ARIMA_I_C_GDAXI_240) return Estrategia.ARIMA_I_C_GDAXI_240;
		if(robot == TipoRobot.ARIMA_I_C_GDAXI_60) return Estrategia.ARIMA_I_C_GDAXI_60;
		
		if(robot == TipoRobot.ARIMA_D_GDAXI_10080) return Estrategia.ARIMA_D_GDAXI_10080;
		if(robot == TipoRobot.ARIMA_D_GDAXI_1440) return Estrategia.ARIMA_D_GDAXI_1440;
		if(robot == TipoRobot.ARIMA_D_GDAXI_240) return Estrategia.ARIMA_D_GDAXI_240;
		if(robot == TipoRobot.ARIMA_D_GDAXI_60) return Estrategia.ARIMA_D_GDAXI_60;
		
		if(robot == TipoRobot.ARIMA_NDX_10080) return Estrategia.ARIMA_NDX_10080;
		if(robot == TipoRobot.ARIMA_NDX_1440) return Estrategia.ARIMA_NDX_1440;
		if(robot == TipoRobot.ARIMA_NDX_240) return Estrategia.ARIMA_NDX_240;
		if(robot == TipoRobot.ARIMA_NDX_60) return Estrategia.ARIMA_NDX_60;

		if(robot == TipoRobot.ARIMA_I_NDX_10080) return Estrategia.ARIMA_I_NDX_10080;
		if(robot == TipoRobot.ARIMA_I_NDX_1440) return Estrategia.ARIMA_I_NDX_1440;
		if(robot == TipoRobot.ARIMA_I_NDX_240) return Estrategia.ARIMA_I_NDX_240;
		if(robot == TipoRobot.ARIMA_I_NDX_60) return Estrategia.ARIMA_I_NDX_60;
		
		if(robot == TipoRobot.ARIMA_B_NDX_10080) return Estrategia.ARIMA_B_NDX_10080;
		if(robot == TipoRobot.ARIMA_B_NDX_1440) return Estrategia.ARIMA_B_NDX_1440;
		if(robot == TipoRobot.ARIMA_B_NDX_240) return Estrategia.ARIMA_B_NDX_240;
		if(robot == TipoRobot.ARIMA_B_NDX_60) return Estrategia.ARIMA_B_NDX_60;

		if(robot == TipoRobot.ARIMA_I_B_NDX_10080) return Estrategia.ARIMA_I_B_NDX_10080;
		if(robot == TipoRobot.ARIMA_I_B_NDX_1440) return Estrategia.ARIMA_I_B_NDX_1440;
		if(robot == TipoRobot.ARIMA_I_B_NDX_240) return Estrategia.ARIMA_I_B_NDX_240;
		if(robot == TipoRobot.ARIMA_I_B_NDX_60) return Estrategia.ARIMA_I_B_NDX_60;
		
		if(robot == TipoRobot.ARIMA_C_NDX_10080) return Estrategia.ARIMA_C_NDX_10080;
		if(robot == TipoRobot.ARIMA_C_NDX_1440) return Estrategia.ARIMA_C_NDX_1440;
		if(robot == TipoRobot.ARIMA_C_NDX_240) return Estrategia.ARIMA_C_NDX_240;
		if(robot == TipoRobot.ARIMA_C_NDX_60) return Estrategia.ARIMA_C_NDX_60;

		if(robot == TipoRobot.ARIMA_I_C_NDX_10080) return Estrategia.ARIMA_I_C_NDX_10080;
		if(robot == TipoRobot.ARIMA_I_C_NDX_1440) return Estrategia.ARIMA_I_C_NDX_1440;
		if(robot == TipoRobot.ARIMA_I_C_NDX_240) return Estrategia.ARIMA_I_C_NDX_240;
		if(robot == TipoRobot.ARIMA_I_C_NDX_60) return Estrategia.ARIMA_I_C_NDX_60;
		
		if(robot == TipoRobot.ARIMA_D_NDX_10080) return Estrategia.ARIMA_D_NDX_10080;
		if(robot == TipoRobot.ARIMA_D_NDX_1440) return Estrategia.ARIMA_D_NDX_1440;
		if(robot == TipoRobot.ARIMA_D_NDX_240) return Estrategia.ARIMA_D_NDX_240;
		if(robot == TipoRobot.ARIMA_D_NDX_60) return Estrategia.ARIMA_D_NDX_60;
		
		if(robot == TipoRobot.ARIMA_XAUUSD_10080) return Estrategia.ARIMA_XAUUSD_10080;
		if(robot == TipoRobot.ARIMA_XAUUSD_1440) return Estrategia.ARIMA_XAUUSD_1440;
		if(robot == TipoRobot.ARIMA_XAUUSD_240) return Estrategia.ARIMA_XAUUSD_240;
		if(robot == TipoRobot.ARIMA_XAUUSD_60) return Estrategia.ARIMA_XAUUSD_60;

		if(robot == TipoRobot.ARIMA_I_XAUUSD_10080) return Estrategia.ARIMA_I_XAUUSD_10080;
		if(robot == TipoRobot.ARIMA_I_XAUUSD_1440) return Estrategia.ARIMA_I_XAUUSD_1440;
		if(robot == TipoRobot.ARIMA_I_XAUUSD_240) return Estrategia.ARIMA_I_XAUUSD_240;
		if(robot == TipoRobot.ARIMA_I_XAUUSD_60) return Estrategia.ARIMA_I_XAUUSD_60;
		
		if(robot == TipoRobot.ARIMA_B_XAUUSD_10080) return Estrategia.ARIMA_B_XAUUSD_10080;
		if(robot == TipoRobot.ARIMA_B_XAUUSD_1440) return Estrategia.ARIMA_B_XAUUSD_1440;
		if(robot == TipoRobot.ARIMA_B_XAUUSD_240) return Estrategia.ARIMA_B_XAUUSD_240;
		if(robot == TipoRobot.ARIMA_B_XAUUSD_60) return Estrategia.ARIMA_B_XAUUSD_60;
		
		if(robot == TipoRobot.ARIMA_I_B_XAUUSD_10080) return Estrategia.ARIMA_I_B_XAUUSD_10080;
		if(robot == TipoRobot.ARIMA_I_B_XAUUSD_1440) return Estrategia.ARIMA_I_B_XAUUSD_1440;
		if(robot == TipoRobot.ARIMA_I_B_XAUUSD_240) return Estrategia.ARIMA_I_B_XAUUSD_240;
		if(robot == TipoRobot.ARIMA_I_B_XAUUSD_60) return Estrategia.ARIMA_I_B_XAUUSD_60;
		
		if(robot == TipoRobot.ARIMA_C_XAUUSD_10080) return Estrategia.ARIMA_C_XAUUSD_10080;
		if(robot == TipoRobot.ARIMA_C_XAUUSD_1440) return Estrategia.ARIMA_C_XAUUSD_1440;
		if(robot == TipoRobot.ARIMA_C_XAUUSD_240) return Estrategia.ARIMA_C_XAUUSD_240;
		if(robot == TipoRobot.ARIMA_C_XAUUSD_60) return Estrategia.ARIMA_C_XAUUSD_60;
		
		if(robot == TipoRobot.ARIMA_D_XAUUSD_10080) return Estrategia.ARIMA_D_XAUUSD_10080;
		if(robot == TipoRobot.ARIMA_D_XAUUSD_1440) return Estrategia.ARIMA_D_XAUUSD_1440;
		if(robot == TipoRobot.ARIMA_D_XAUUSD_240) return Estrategia.ARIMA_D_XAUUSD_240;
		if(robot == TipoRobot.ARIMA_D_XAUUSD_60) return Estrategia.ARIMA_D_XAUUSD_60;
		
		if(robot == TipoRobot.ARIMA_I_C_XAUUSD_10080) return Estrategia.ARIMA_I_C_XAUUSD_10080;
		if(robot == TipoRobot.ARIMA_I_C_XAUUSD_1440) return Estrategia.ARIMA_I_C_XAUUSD_1440;
		if(robot == TipoRobot.ARIMA_I_C_XAUUSD_240) return Estrategia.ARIMA_I_C_XAUUSD_240;
		if(robot == TipoRobot.ARIMA_I_C_XAUUSD_60) return Estrategia.ARIMA_I_C_XAUUSD_60;
		
		if(robot == TipoRobot.ARIMA_C_WTI_1440) return Estrategia.ARIMA_C_WTI_1440;
		if(robot == TipoRobot.ARIMA_C_WTI_240) return Estrategia.ARIMA_C_WTI_240;
		if(robot == TipoRobot.ARIMA_C_WTI_60) return Estrategia.ARIMA_C_WTI_60;
		
		if(robot == TipoRobot.ARIMA_D_WTI_1440) return Estrategia.ARIMA_D_WTI_1440;
		if(robot == TipoRobot.ARIMA_D_WTI_240) return Estrategia.ARIMA_D_WTI_240;
		if(robot == TipoRobot.ARIMA_D_WTI_60) return Estrategia.ARIMA_D_WTI_60;
		
		if(robot == TipoRobot.ARIMA_I_C_WTI_1440) return Estrategia.ARIMA_I_C_WTI_1440;
		if(robot == TipoRobot.ARIMA_I_C_WTI_240) return Estrategia.ARIMA_I_C_WTI_240;
		if(robot == TipoRobot.ARIMA_I_C_WTI_60) return Estrategia.ARIMA_I_C_WTI_60;
		
		if(robot == TipoRobot.ARIMA_D_WTI_1440) return Estrategia.ARIMA_D_WTI_1440;
		if(robot == TipoRobot.ARIMA_D_WTI_240) return Estrategia.ARIMA_D_WTI_240;
		if(robot == TipoRobot.ARIMA_D_WTI_60) return Estrategia.ARIMA_D_WTI_60;
		
		if(robot == TipoRobot.RNA_AUDCAD_10080) return Estrategia.RNA_AUDCAD_10080;
		if(robot == TipoRobot.RNA_AUDCAD_1440) return Estrategia.RNA_AUDCAD_1440;
		if(robot == TipoRobot.RNA_AUDCAD_240) return Estrategia.RNA_AUDCAD_240;
		if(robot == TipoRobot.RNA_AUDCAD_60) return Estrategia.RNA_AUDCAD_60;
		
		if(robot == TipoRobot.RNA_I_AUDCAD_10080) return Estrategia.RNA_I_AUDCAD_10080;
		if(robot == TipoRobot.RNA_I_AUDCAD_1440) return Estrategia.RNA_I_AUDCAD_1440;
		if(robot == TipoRobot.RNA_I_AUDCAD_240) return Estrategia.RNA_I_AUDCAD_240;
		if(robot == TipoRobot.RNA_I_AUDCAD_60) return Estrategia.RNA_I_AUDCAD_60;
		
		if(robot == TipoRobot.RNA_GDAXI_10080) return Estrategia.RNA_GDAXI_10080;
		if(robot == TipoRobot.RNA_GDAXI_1440) return Estrategia.RNA_GDAXI_1440;
		if(robot == TipoRobot.RNA_GDAXI_240) return Estrategia.RNA_GDAXI_240;
		if(robot == TipoRobot.RNA_GDAXI_60) return Estrategia.RNA_GDAXI_60;
		
		if(robot == TipoRobot.RNA_I_GDAXI_10080) return Estrategia.RNA_I_GDAXI_10080;
		if(robot == TipoRobot.RNA_I_GDAXI_1440) return Estrategia.RNA_I_GDAXI_1440;
		if(robot == TipoRobot.RNA_I_GDAXI_240) return Estrategia.RNA_I_GDAXI_240;
		if(robot == TipoRobot.RNA_I_GDAXI_60) return Estrategia.RNA_I_GDAXI_60;
		
		if(robot == TipoRobot.RNA_NDX_10080) return Estrategia.RNA_NDX_10080;
		if(robot == TipoRobot.RNA_NDX_1440) return Estrategia.RNA_NDX_1440;
		if(robot == TipoRobot.RNA_NDX_240) return Estrategia.RNA_NDX_240;
		if(robot == TipoRobot.RNA_NDX_60) return Estrategia.RNA_NDX_60;
		
		if(robot == TipoRobot.RNA_I_NDX_10080) return Estrategia.RNA_I_NDX_10080;
		if(robot == TipoRobot.RNA_I_NDX_1440) return Estrategia.RNA_I_NDX_1440;
		if(robot == TipoRobot.RNA_I_NDX_240) return Estrategia.RNA_I_NDX_240;
		if(robot == TipoRobot.RNA_I_NDX_60) return Estrategia.RNA_I_NDX_60;
		
		if(robot == TipoRobot.RNA_XAUUSD_10080) return Estrategia.RNA_XAUUSD_10080;
		if(robot == TipoRobot.RNA_XAUUSD_1440) return Estrategia.RNA_XAUUSD_1440;
		if(robot == TipoRobot.RNA_XAUUSD_240) return Estrategia.RNA_XAUUSD_240;
		if(robot == TipoRobot.RNA_XAUUSD_60) return Estrategia.RNA_XAUUSD_60;
		
		if(robot == TipoRobot.RNA_I_XAUUSD_10080) return Estrategia.RNA_I_XAUUSD_10080;
		if(robot == TipoRobot.RNA_I_XAUUSD_1440) return Estrategia.RNA_I_XAUUSD_1440;
		if(robot == TipoRobot.RNA_I_XAUUSD_240) return Estrategia.RNA_I_XAUUSD_240;
		if(robot == TipoRobot.RNA_I_XAUUSD_60) return Estrategia.RNA_I_XAUUSD_60;
		
		return null;
		
		
	}
	
	
	public TipoActivo getTipoActivoByRobot(TipoRobot robot) {
		
		for (TipoActivo ta : TipoActivo.values()) {
			if(robot.name().contains(ta.name())) return ta;
		}
		
		return null;
	}
	
	public CanalAmq getCanalAmq(TipoActivo activo, Timeframes timeframe) {
		if(activo == TipoActivo.XAUUSD && timeframe == Timeframes.PERIOD_H1) {
			return CanalAmq.CHNL_XAUUSD_60;
		}if(activo == TipoActivo.XAUUSD && timeframe == Timeframes.PERIOD_H4) {
			return CanalAmq.CHNL_XAUUSD_240;
		}if(activo == TipoActivo.XAUUSD && timeframe == Timeframes.PERIOD_D1) {
			return CanalAmq.CHNL_XAUUSD_1440;
		}if(activo == TipoActivo.XAUUSD && timeframe == Timeframes.PERIOD_W1) {
			return CanalAmq.CHNL_XAUUSD_10080;
		}else if(activo == TipoActivo.AUDCAD && timeframe == Timeframes.PERIOD_H1) {
			return CanalAmq.CHNL_AUDCAD_60;
		}else if(activo == TipoActivo.AUDCAD && timeframe == Timeframes.PERIOD_H4) {
			return CanalAmq.CHNL_AUDCAD_240;
		}else if(activo == TipoActivo.AUDCAD && timeframe == Timeframes.PERIOD_D1) {
			return CanalAmq.CHNL_AUDCAD_1440;
		}else if(activo == TipoActivo.AUDCAD && timeframe == Timeframes.PERIOD_W1) {
			return CanalAmq.CHNL_AUDCAD_10080;
		}else if(activo == TipoActivo.EURUSD && timeframe == Timeframes.PERIOD_H1) {
			return CanalAmq.CHNL_EURUSD_60;
		}else if(activo == TipoActivo.EURUSD && timeframe == Timeframes.PERIOD_H4) {
			return CanalAmq.CHNL_EURUSD_240;
		}else if(activo == TipoActivo.EURUSD && timeframe == Timeframes.PERIOD_D1) {
			return CanalAmq.CHNL_EURUSD_1440;
		}else if(activo == TipoActivo.EURUSD && timeframe == Timeframes.PERIOD_W1) {
			return CanalAmq.CHNL_EURUSD_10080;
		}else if(activo == TipoActivo.GDAXI && timeframe == Timeframes.PERIOD_H1) {
			return CanalAmq.CHNL_GDAXI_60;
		}else if(activo == TipoActivo.GDAXI && timeframe == Timeframes.PERIOD_H4) {
			return CanalAmq.CHNL_GDAXI_240;
		}else if(activo == TipoActivo.GDAXI && timeframe == Timeframes.PERIOD_D1) {
			return CanalAmq.CHNL_GDAXI_1440;
		}else if(activo == TipoActivo.GDAXI && timeframe == Timeframes.PERIOD_W1) {
			return CanalAmq.CHNL_GDAXI_10080;
		}else if(activo == TipoActivo.NDX && timeframe == Timeframes.PERIOD_H1) {
			return CanalAmq.CHNL_NDX_60;
		}else if(activo == TipoActivo.NDX && timeframe == Timeframes.PERIOD_H4) {
			return CanalAmq.CHNL_NDX_240;
		}else if(activo == TipoActivo.NDX && timeframe == Timeframes.PERIOD_D1) {
			return CanalAmq.CHNL_NDX_1440;
		}else if(activo == TipoActivo.NDX && timeframe == Timeframes.PERIOD_W1) {
			return CanalAmq.CHNL_NDX_10080;
		}else if(activo == TipoActivo.XTIUSD && timeframe == Timeframes.PERIOD_H1) {
			return CanalAmq.CHNL_WTI_60;
		}else if(activo == TipoActivo.XTIUSD && timeframe == Timeframes.PERIOD_H4) {
			return CanalAmq.CHNL_WTI_240;
		}else if(activo == TipoActivo.XTIUSD && timeframe == Timeframes.PERIOD_D1) {
			return CanalAmq.CHNL_WTI_1440;
		}else if(activo == TipoActivo.XTIUSD && timeframe == Timeframes.PERIOD_W1) {
			return CanalAmq.CHNL_WTI_10080;
		}else{
			return null;
		}
	}
	*/
	
	public String getRutaFicheroMt4(String activo, Timeframes timeFrame) throws SistemaException{
		String ruta = null;
		
		try {
			StringBuilder rutaFicheroMTraderBuilder = new StringBuilder(this.getRutaBase());
			
			/*
			rutaFicheroMTraderBuilder	.append(ConfigData.FILE_SEPARATOR)
										.append(activo.name());
			
			if(this.getModoActivo().equalsIgnoreCase(modo.getTest())) {
				rutaFicheroMTraderBuilder.append(metatrader.getFolderTest());
			}else if(this.getModoActivo().equalsIgnoreCase(modo.getReal())) {
				rutaFicheroMTraderBuilder.append(metatrader.getFolderReal());
			}
			*/
			rutaFicheroMTraderBuilder.append(ConfigData.FILE_SEPARATOR);
			rutaFicheroMTraderBuilder.append(timeFrame.valor);
			rutaFicheroMTraderBuilder.append("_");
			rutaFicheroMTraderBuilder.append(activo);
			rutaFicheroMTraderBuilder.append(metatrader.getExtension());
			
			ruta = rutaFicheroMTraderBuilder.toString();
			
			if(ruta == null || ruta.equals(ConfigData.CADENA_VACIA)) {
				//logger.error("No se ha podido crear la ruta al fichero MT4 del activo: {}", activo.name());
				throw new SistemaException("No se ha podido crear la ruta al fichero MT4 del activo: " + activo);
			}else {
				//logger.info("Ruta fichero de MT4 crada correctamente: : {}", rutaFicheroMTraderBuilder.toString());
			}
			
		}catch (Exception e) {
			//logger.error("No se ha podido crear la ruta al fichero MT4 del activo: {}", activo.name(), e);
			throw new SistemaException("No se ha podido crear la ruta al fichero MT4 del activo: " + activo, e);
		}
		
		return ruta;
		
	}	
	/*
	public String getModoActivo() throws SistemaException{
		String modoActivo = null;
		try {
			modoActivo = modo.getActivo();
			if(modoActivo == null || modoActivo.equals(ConfigData.CADENA_VACIA)) {
				logger.error("No se ha podido recuperar el modo activo {}.", modoActivo);
				throw new SistemaException("No se ha podido recuperar el modo activo: " + modoActivo);
			}
			//logger.info("Modo activo recuperado: {}", modoActivo);
		}catch (Exception e) {
			logger.error("No se ha podido recuperar el modo activo {}.", modoActivo);
			throw new SistemaException("No se ha podido recuperar el modo activo: " + modoActivo);
		}
		return modoActivo;
	}
	*/
	
	public String getRutaBase() throws SistemaException{
		
		String rutaBase = null;
		try {
			StringBuilder builder = new StringBuilder();
			builder	.append(metatrader.getInstallation())
					.append(metatrader.getTerminal())
					.append(metatrader.getFolderRoot());
				
			rutaBase = builder.toString();
			
			if(rutaBase == null || rutaBase.equals(ConfigData.CADENA_VACIA)) {
				//logger.error("No se ha podido crear la ruta base {}.", rutaBase);
				throw new SistemaException("No se ha podido crear la ruta base: " + rutaBase);
			}else {
				//logger.info("Ruta base creada correctamente: {}", rutaBase);
				//System.out.println("Ruta base creada correctamente: " + rutaBase);
			}
			
		}catch (Exception e) {
			//logger.error("No se ha podido crear la ruta base.", e);
			throw new SistemaException("No se ha podido crear la ruta base", e);
		}
		return rutaBase;
		
	}
	
	
}
