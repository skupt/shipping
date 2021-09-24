package rozaryonov.shipping.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.service.SettlementsService;

@RestController
@RequiredArgsConstructor
public class SettlementsRestController {
	
	private final SettlementsService settlementsService;
	
	@GetMapping ("/settlementsJsonList")
	public Iterable<Settlements> transferSettlementsList () {
		return settlementsService.findAll();
	}
}
