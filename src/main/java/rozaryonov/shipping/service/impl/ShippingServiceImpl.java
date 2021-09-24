package rozaryonov.shipping.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.repository.ShippingRepository;
import rozaryonov.shipping.service.ShippingService;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

	private final ShippingRepository shippingRepository;
	
	@Override
	public Shipping findById(Long id) {
		return shippingRepository.findById(id).orElseThrow(()-> new DaoException("No Shipping with id:" + id));
	}

	@Override
	public Iterable<Shipping> findAll() {
		return shippingRepository.findAll();
	}

}
