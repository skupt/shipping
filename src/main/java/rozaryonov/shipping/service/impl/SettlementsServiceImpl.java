package rozaryonov.shipping.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.repository.SettlementsRepository;
import rozaryonov.shipping.repository.ShippingRepository;
import rozaryonov.shipping.service.SettlementsService;
import rozaryonov.shipping.service.ShippingService;

@Service
@RequiredArgsConstructor
public class SettlementsServiceImpl implements SettlementsService {

	private final SettlementsRepository settlementsRepository;
	
	@Override
	public Settlements findById(Long id) {
		return settlementsRepository.findById(id).orElseThrow(()-> new DaoException("No Settlements with id:" + id));
	}

	@Override
	public Iterable<Settlements> findAll() {
		return settlementsRepository.findAll();
	}

}
