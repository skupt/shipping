package rozaryonov.shipping.service;

import rozaryonov.shipping.model.Locality;

public interface LocalityService {
	Locality findById(Long id);
	Iterable<Locality> findAll(); 
}
