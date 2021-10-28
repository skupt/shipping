package rozaryonov.shipping.repository;

import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.page.Pageable;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface CustomTariffRepository extends Pageable<Tariff> {
	List<Tariff> findFilterSort(Timestamp after, Timestamp before, Predicate<Tariff> p, Comparator<Tariff> c);
}
