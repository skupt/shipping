package rozaryonov.shipping.service;

import rozaryonov.shipping.model.Tariff;

public interface TariffService {
	Tariff findById(Long id);
	Iterable<Tariff> findAll(); 
}
