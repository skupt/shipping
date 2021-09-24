package rozaryonov.shipping.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Property;
import rozaryonov.shipping.service.PersonService;
import rozaryonov.shipping.service.PropertyService;

@RestController
@RequiredArgsConstructor
public class PropertyRestController {
	
	private final PropertyService propertyService;
	
	@GetMapping ("/propertyJsonList")
	public Iterable<Property> transferPersonList () {
		return propertyService.findAll();
	}
}
