package rozaryonov.shipping.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.repository.InvoiceRepository;
import rozaryonov.shipping.service.InvoiceService;

@RestController
@RequiredArgsConstructor
public class InvoiceRestController {
	
	private final InvoiceRepository invoiceRepository;
	
	@GetMapping ("/invoiceJsonList")
	public Iterable<Invoice> transferInvoiceList () {
		return invoiceRepository.findAll();
	}
}
