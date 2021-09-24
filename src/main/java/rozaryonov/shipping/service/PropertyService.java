package rozaryonov.shipping.service;

import rozaryonov.shipping.model.Property;

public interface PropertyService {
	Property findById(Long id);
	Iterable<Property> findAll(); 
}
