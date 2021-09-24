package rozaryonov.shipping.service;

import rozaryonov.shipping.model.Shipping;

public interface ShippingService {
	Shipping findById(Long id);
	Iterable<Shipping> findAll(); 
}
