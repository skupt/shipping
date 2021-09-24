package rozaryonov.shipping.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.service.PersonService;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

	private final PersonRepository personRepository;
	
	@Override
	public Person findById(Long id) {
		return personRepository.findById(id).orElseThrow(()-> new DaoException("No Person with id:" + id));
	}

	@Override
	public Iterable<Person> findAll() {
		return personRepository.findAll();
	}

}
