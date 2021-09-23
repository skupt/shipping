package rozaryonov.shipping.service;

import rozaryonov.shipping.model.Person;

public interface PersonService {
	Person findById(Long id);
	Iterable<Person> findAll(); 
}
