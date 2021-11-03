package rozaryonov.shipping.repository;

import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.repository.page.Pageable;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface CustomInvoiceRepository extends Pageable<Invoice> {
	List<Invoice> findFilterSort(Timestamp after, Timestamp before, Predicate<Invoice> p, Comparator<Invoice> c);
 }
