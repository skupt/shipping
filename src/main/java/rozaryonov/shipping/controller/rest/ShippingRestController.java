package rozaryonov.shipping.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.service.ShippingService;

@RestController
@RequiredArgsConstructor
public class ShippingRestController {
	
	private final ShippingService shippingService;
	
	@GetMapping ("/shippingJsonList")
	public Iterable<Shipping> transferShippingList () {
		return shippingService.findAll();
	}
}
