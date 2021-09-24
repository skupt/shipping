package rozaryonov.shipping.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.service.LocalityService;

@RestController
@RequiredArgsConstructor
public class LocalityRestController {
	
	private final LocalityService localityService;
	
	@GetMapping ("/localityJsonList")
	public Iterable<Locality> transferLocalityList () {
		return localityService.findAll();
	}
}
