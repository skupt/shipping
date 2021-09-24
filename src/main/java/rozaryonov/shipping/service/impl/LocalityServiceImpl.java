package rozaryonov.shipping.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.repository.LocalityRepository;
import rozaryonov.shipping.repository.SettlementsRepository;
import rozaryonov.shipping.repository.ShippingRepository;
import rozaryonov.shipping.service.LocalityService;
import rozaryonov.shipping.service.SettlementsService;
import rozaryonov.shipping.service.ShippingService;

@Service
@RequiredArgsConstructor
public class LocalityServiceImpl implements LocalityService {

	private final LocalityRepository localityRepository;
	
	@Override
	public Locality findById(Long id) {
		return localityRepository.findById(id).orElseThrow(()-> new DaoException("No Locality with id:" + id));
	}

	@Override
	public Iterable<Locality> findAll() {
		return localityRepository.findAll();
	}

}
