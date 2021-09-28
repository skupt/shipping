package rozaryonov.shipping.service;

import rozaryonov.shipping.model.Property;

public interface PropertyService {
	Property findById(String id);
	Iterable<Property> findAll(); 
}
