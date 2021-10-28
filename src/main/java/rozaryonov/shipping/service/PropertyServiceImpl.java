package rozaryonov.shipping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rozaryonov.shipping.model.Property;
import rozaryonov.shipping.repository.PropertyRepository;

import javax.el.PropertyNotFoundException;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl {

	private final PropertyRepository propertyRepository;

	public Property findById(String id) {
		return propertyRepository.findById(id)
				.orElseThrow(() -> new PropertyNotFoundException("No Property with id:" + id));
	}
}
