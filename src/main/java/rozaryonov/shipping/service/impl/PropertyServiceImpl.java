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

	private final PropertyRepository roleRepository;
	
	@Override
	public Property findById(Long id) {
		return roleRepository.findById(id).orElseThrow(()-> new DaoException("No Property with id:" + id));
	}

	@Override
	public Iterable<Property> findAll() {
		return roleRepository.findAll();
	}

}
