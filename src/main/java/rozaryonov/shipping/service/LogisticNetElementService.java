package rozaryonov.shipping.service;

import rozaryonov.shipping.model.LogisticNetElement;

public interface LogisticNetElementService {
	LogisticNetElement findById(Long id);
	Iterable<LogisticNetElement> findAll(); 
	Iterable<LogisticNetElement> findByNetConfig(Long netConfigId);
}
