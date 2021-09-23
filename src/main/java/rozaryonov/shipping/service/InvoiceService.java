package rozaryonov.shipping.service;

import rozaryonov.shipping.model.Invoice;

public interface InvoiceService {
	Invoice findById(Long id);
	Iterable<Invoice> findAll(); 
}
