package rozaryonov.shipping.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.model.ShippingStatus;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long>{
	
	Page<Shipping> findAllByShippingStatusOrderByCreationTimestamp(ShippingStatus shippingStatus, Pageable pageable);

}

//	@Query(value="select " + 
//			" id, creation_timestamp, person_id, download_datetime, load_locality_id,  " + 
//			" shipper, download_address, consignee, unload_locality_id,  " + 
//			" unload_address, unloading_datetime, distance, weight, volume, " + 
//			" fare, shipping_status_id " + 
//			" from shipping  " + 
//			" where shipping_status_id=? ",
//			nativeQuery = true)
//	List<Shipping> findAllByShippingStatusIdNative(Long shippingStatusId, Pageable pageable);

//@Query(
//		value="select DATE(download_datetime) AS Date, sum(fare) as Turnover from shipping group by Date;",
//		nativeQuery = true)
//Page<Shipping> prepareStatisticByDate(Pageable pageable)


	
