package rozaryonov.shipping.service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.page.Pageable;

public interface TariffService extends Pageable<Tariff> {
	Tariff findById(Long id);
	Iterable<Tariff> findAll(); 
	List<Tariff> findFilterSort(Timestamp after, Timestamp before, Predicate<Tariff> p, Comparator<Tariff> c);
}
