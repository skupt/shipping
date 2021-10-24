package rozaryonov.shipping.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.model.ShippingStatus;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long>{
	
	Page<Shipping> findAllByShippingStatusOrderByCreationTimestamp(ShippingStatus shippingStatus, Pageable pageable);

}



	
