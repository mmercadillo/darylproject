package daryl.system.model.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public class DarwinDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Long id;
	
	@Getter @Setter
	private String darwin;
	
}
