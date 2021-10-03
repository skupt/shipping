package rozaryonov.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rozaryonov.shipping.model.ShippingStatus;

@Repository
public interface ShippingStatusRepository extends JpaRepository<ShippingStatus, Long>{

}
