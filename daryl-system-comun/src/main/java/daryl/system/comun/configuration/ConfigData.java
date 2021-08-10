package daryl.system.comun.configuration;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class ConfigData {

	public static final String CADENA_VACIA = "";
	public static final String FILE_SEPARATOR = "\\";

	public static final String ZEROMQ_SERVER_URL_FROM_PYTHON = "tcp://127.0.0.1:5555";
	public static final String ZEROMQ_SERVER_URL_FROM_JAVA = "tcp://127.0.0.1:5556";
	
	public static final String BASE_PATH_RNAS = "classpath:rnas/";
	
	public static final String HORA_CIERRE_OPS = "21:30:00";
	
	public static final Integer MAX_NUM_OF_THREADS = 5;
	
	
	//private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	private SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	private SimpleDateFormat sdfFechaSolo = new SimpleDateFormat("yyyy.MM.dd");
	private SimpleDateFormat sdfHoraSolo = new SimpleDateFormat("HH:mm:ss");

	public String getActualDateFormattedInString() {
		return sdfFull.format(new Date());
	}	

	public Long getFechaHoraInMillis(String fechaCompleta) throws ParseException {	
		return sdfFull.parse(fechaCompleta).getTime();
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
	public Boolean checkFechaHoraOperaciones() {
		
		Boolean operar = Boolean.TRUE;
		
		Calendar c = Calendar.getInstance();
		
		if(c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
			
			if(c.get(Calendar.HOUR_OF_DAY) >= 21 && c.get(Calendar.MINUTE) >= 30) {
				operar = Boolean.FALSE;
			}
			if(c.get(Calendar.HOUR_OF_DAY) >= 22) {
				operar = Boolean.FALSE;
			}			
			
		}
		if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			operar = Boolean.FALSE;
		}
		
		
		
		return operar;
		
	}

	

	
}
