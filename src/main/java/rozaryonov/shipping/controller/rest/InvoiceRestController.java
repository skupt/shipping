package rozaryonov.shipping.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.service.InvoiceService;

@RestController
@RequiredArgsConstructor
public class InvoiceRestController {
	
	private final InvoiceService invoiceService;
	
	@GetMapping ("/invoiceJsonList")
	public Iterable<Invoice> transferInvoiceList () {
		return invoiceService.findAll();
	}
}
