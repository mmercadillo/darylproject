package daryl.system.robots.client.web.mvc.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import daryl.system.robots.client.web.mvc.dto.OrdenDto;

@RestController
public class OrdenesController {

	@Autowired
	RestTemplate restClient;
 
	@GetMapping("/mt/ordenes/all/{cta}")
    public List<OrdenDto> main( @PathVariable String cta) {
		
		System.out.println("FULL: " + cta + " " + new Date());
		List<OrdenDto> ordenes = Arrays.asList(restClient.getForObject("http://servicio-zuul-server:8888/api/daryl/ordenes/all", OrdenDto[].class));
        return ordenes;
    }
    
	@GetMapping("/mt/ordenes/{num}/{cta}")
    public List<OrdenDto> main(@PathVariable Integer num, @PathVariable String cta) {
		System.out.println("TOP: " + cta + " NUM: " + num + " - " + new Date());
		List<OrdenDto> ordenes = Arrays.asList(restClient.getForObject("http://servicio-zuul-server:8888/api/daryl/ordenes/top/"+num, OrdenDto[].class));
        return ordenes;
    }
	
	@GetMapping("/mt/ordenes/cuenta/{cta}")
    public List<OrdenDto> ordenesCuenta(@PathVariable String cta) {
		System.out.println("ROBOTS CTA: " + cta + new Date());
		List<OrdenDto> ordenes = Arrays.asList(restClient.getForObject("http://servicio-zuul-server:8888/api/daryl/ordenes/cuenta/"+cta, OrdenDto[].class));
        
		System.out.println(ordenes);
		return ordenes;
    }
	
}
