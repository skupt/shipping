package rozaryonov.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rozaryonov.shipping.model.SettlementType;

@Repository
public interface SettlementsTypeRepository extends JpaRepository<SettlementType, Long>{

}
