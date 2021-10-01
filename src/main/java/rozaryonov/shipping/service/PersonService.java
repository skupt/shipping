package rozaryonov.shipping.service;

import java.math.BigDecimal;

import rozaryonov.shipping.model.Person;

public interface PersonService {
	Person findById(Long id);
	Iterable<Person> findAll();
	Person save (Person person);
	Person findByLogin(String login);
	BigDecimal calcAndReplaceBalance(long personId);
	
}
