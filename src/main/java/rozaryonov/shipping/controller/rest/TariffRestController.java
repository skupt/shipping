package rozaryonov.shipping.controller.rest;

import java.sql.Timestamp;
import java.util.Comparator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.service.TariffService;

@RestController
@RequiredArgsConstructor
public class TariffRestController {
	private final TariffService tariffService;
	
	@GetMapping ("/tariffJsonList")
	public Iterable<Tariff> transferTariffList () {
		return tariffService.findAll();
	}
	
	@GetMapping ("/tariffFFS")
	public Iterable<Tariff> findFilterSort () {
		Timestamp after = Timestamp.valueOf("1970-12-12 12:00:00");
		Timestamp before = Timestamp.valueOf("2100-12-12 12:00:00");
		return tariffService.findFilterSort(after, before, p->true, Comparator.comparing((Tariff t) -> t.getCreationTimestamp()));
	}

}
