package rozaryonov.shipping.repository;

import java.math.BigDecimal;

public interface CustomSettlementsRepository {
	BigDecimal calcPersonBalance(long personId);
}
