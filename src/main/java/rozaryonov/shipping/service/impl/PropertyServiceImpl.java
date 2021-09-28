package rozaryonov.shipping.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Property;
import rozaryonov.shipping.repository.PropertyRepository;
import rozaryonov.shipping.service.PropertyService;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

	private final PropertyRepository propertyRepository;
	
	@Override
	public Property findById(String id) {
		return propertyRepository.findById(id).orElseThrow(()-> new DaoException("No Property with id:" + id));
	}

	@Override
	public Iterable<Property> findAll() {
		return propertyRepository.findAll();
	}

}
