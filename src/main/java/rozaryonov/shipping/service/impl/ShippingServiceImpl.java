package rozaryonov.shipping.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.exception.ShippingNotFoundException;
import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.repository.ShippingRepository;
import rozaryonov.shipping.service.ShippingService;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

	private final ShippingRepository shippingRepository;
	
	@Override
	public Shipping findById(Long id) {
		return shippingRepository.findById(id).orElseThrow(()-> new ShippingNotFoundException("No Shipping with id:" + id));
	}

	@Override
	public Iterable<Shipping> findAll() {
		return shippingRepository.findAll();
	}//todo is it okay return Iterable??? Vitaly: "Every lector on summer course said if you could use super type or interface you should have used it".

}
