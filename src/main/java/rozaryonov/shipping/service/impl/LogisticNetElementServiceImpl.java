package rozaryonov.shipping.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.model.LogisticNetElement;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.repository.LocalityRepository;
import rozaryonov.shipping.repository.LogisticNetElementRepository;
import rozaryonov.shipping.repository.SettlementsRepository;
import rozaryonov.shipping.repository.ShippingRepository;
import rozaryonov.shipping.service.LocalityService;
import rozaryonov.shipping.service.LogisticNetElementService;
import rozaryonov.shipping.service.SettlementsService;
import rozaryonov.shipping.service.ShippingService;

@Service
@RequiredArgsConstructor
public class LogisticNetElementServiceImpl implements LogisticNetElementService {

	private final LogisticNetElementRepository logisticNetElementRepository;
	
	@Override
	public LogisticNetElement findById(Long id) {
		return logisticNetElementRepository.findById(id).orElseThrow(()-> new DaoException("No LogisticNetElement with id:" + id));
	}

	@Override
	public Iterable<LogisticNetElement> findAll() {
		return logisticNetElementRepository.findAll();
	}

}
