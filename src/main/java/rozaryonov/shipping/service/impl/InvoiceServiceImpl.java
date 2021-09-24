package rozaryonov.shipping.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.repository.InvoiceRepository;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.service.InvoiceService;
import rozaryonov.shipping.service.PersonService;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

	private final InvoiceRepository invoiceRepository;
	
	@Override
	public Invoice findById(Long id) {
		return invoiceRepository.findById(id).orElseThrow(()-> new DaoException("No Invoice with id:" + id));
	}

	@Override
	public Iterable<Invoice> findAll() {
		return invoiceRepository.findAll();
	}

}
