package rozaryonov.shipping.service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.repository.page.Pageable;

public interface InvoiceService extends Pageable<Invoice>{
	List<Invoice> findFilterSort(Timestamp after, Timestamp before, Predicate<Invoice> p, Comparator<Invoice> c);
}
