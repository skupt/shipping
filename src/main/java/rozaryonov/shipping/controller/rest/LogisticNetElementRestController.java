package rozaryonov.shipping.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.model.LogisticNetElement;
import rozaryonov.shipping.service.LocalityService;
import rozaryonov.shipping.service.LogisticNetElementService;

@RestController
@RequiredArgsConstructor
public class LogisticNetElementRestController {
	
	private final LogisticNetElementService logisticNetElementService;
	
	@GetMapping ("/logisticNetElementJsonList")
	public Iterable<LogisticNetElement> transferLogisticNetElementList () {
		return logisticNetElementService.findAll();
	}
}
