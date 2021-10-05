package rozaryonov.shipping.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Role;
import rozaryonov.shipping.model.SettlementType;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.model.Shipping;

@Repository
public interface SettlementsRepository extends CrudRepository<Settlements, Long>, CustomSettlementsRepository {
	List<Settlements> findBySettlementType(SettlementType settlementType);
	

}
