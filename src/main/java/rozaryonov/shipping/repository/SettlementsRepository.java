package rozaryonov.shipping.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import rozaryonov.shipping.model.SettlementType;
import rozaryonov.shipping.model.Settlements;

@Repository
public interface SettlementsRepository extends CrudRepository<Settlements, Long>, CustomSettlementsRepository {
	List<Settlements> findBySettlementType(SettlementType settlementType);
	

}
