package rozaryonov.shipping.repository.page;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface Pageable <T>{
	List<T> findFilterSort(Timestamp after, Timestamp before, Predicate<T> p, Comparator<T> c);
}
