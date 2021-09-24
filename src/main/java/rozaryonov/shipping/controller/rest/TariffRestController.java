package rozaryonov.shipping.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.service.LocalityService;
import rozaryonov.shipping.service.TariffService;

@RestController
@RequiredArgsConstructor
public class TariffRestController {
	
	private final TariffService tariffService;
	
	@GetMapping ("/tariffJsonList")
	public Iterable<Tariff> transferTariffList () {
		return tariffService.findAll();
	}
}
