package rozaryonov.shipping.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.service.InvoiceService;
import rozaryonov.shipping.service.PersonService;

@RestController
@RequiredArgsConstructor
public class InvoiceRestController {
	
	private final InvoiceService invoiceService;
	
	@GetMapping ("/invoiceJsonList")
	public Iterable<Invoice> transferPersonList () {
		return invoiceService.findAll();
	}
}
