package rozaryonov.shipping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.service.PersonService;

@SpringBootTest
class PersonServiceTest {
	@Autowired
	private PersonService personService;
	@Autowired
	private PersonRepository personRepository;
	
	@Test
	void findByLoginTest() {
		Person admin = personService.findByLogin("admin");
		assertEquals("admin", admin.getLogin());
	}
	
	@Test 
	void calcAndReplaceBalanceTest() {
		Person p1 = personRepository.findById(1L).orElse(null);
		p1.setBalance(BigDecimal.ZERO);

		personRepository.save(p1);
		//after above balance of user 1 should be 0
		assertEquals("0.00", personRepository.findById(1L).orElse(null).getBalance().toString());

		personService.calcAndReplaceBalance(1L);
		//after above balance of user 1 should be 19500.00
		assertEquals("19500.00", personRepository.findById(1L).orElse(null).getBalance().toString());
		
	}

	
	


}
