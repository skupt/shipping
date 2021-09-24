package rozaryonov.shipping.service;

import rozaryonov.shipping.model.Settlements;

public interface SettlementsService {
	Settlements findById(Long id);
	Iterable<Settlements> findAll(); 
}
