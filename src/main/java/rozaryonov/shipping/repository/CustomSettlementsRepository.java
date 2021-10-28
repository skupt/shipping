package rozaryonov.shipping.repository;

import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.repository.page.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface CustomSettlementsRepository extends Pageable<Settlements> {
	BigDecimal calcPersonBalance(long personId);
	List<Settlements> findFilterSort(Timestamp after, Timestamp before, Predicate<Settlements> p,
											Comparator<Settlements> c);
}
