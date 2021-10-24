package rozaryonov.shipping.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.exception.LocalityNotFoundException;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.repository.LocalityRepository;
import rozaryonov.shipping.service.LocalityService;

@Service
@RequiredArgsConstructor
public class LocalityServiceImpl implements LocalityService {

	private final LocalityRepository localityRepository;

	@Override
	public Locality findById(Long id) {
		return localityRepository.findById(id)
				.orElseThrow(() -> new LocalityNotFoundException("No Locality with id:" + id));
	}

	@Override
	public Iterable<Locality> findAll() {
		return localityRepository.findAll();
	}

}
