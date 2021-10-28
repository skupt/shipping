package rozaryonov.shipping.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.exception.LocalityNotFoundException;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.repository.LocalityRepository;

@Service
@RequiredArgsConstructor
public class LocalityServiceImpl {

	private final LocalityRepository localityRepository;

	public Locality findById(Long id) {
		return localityRepository.findById(id)
				.orElseThrow(() -> new LocalityNotFoundException("No Locality with id:" + id));
	}

	public Iterable<Locality> findAll() {
		return localityRepository.findAll();
	}

}
