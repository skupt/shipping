package rozaryonov.shipping.service.impl;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.repository.SettlementsRepository;
import rozaryonov.shipping.service.PersonService;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
	
	private static Logger logger = LogManager.getLogger(PersonServiceImpl.class.getName());
	private final SettlementsRepository settlementsRepository;
	private final PersonRepository personRepository;
	

	@Override
	public Person findByLogin(String login) {
		return personRepository.findByLogin(login).orElse(null);
	}

	
	@Override
	@Transactional
	public BigDecimal calcAndReplaceBalance(long personId) {
		BigDecimal balance = settlementsRepository.calcPersonBalance(personId);
		Person person = personRepository.findById(personId).orElse(null);
		person.setBalance(balance);
		personRepository.saveAndFlush(person);
		
		
		return balance; 
	}

}
