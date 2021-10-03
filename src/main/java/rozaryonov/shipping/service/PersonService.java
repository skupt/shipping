package rozaryonov.shipping.service;

import java.math.BigDecimal;

import rozaryonov.shipping.model.Person;

public interface PersonService {
	Person findByLogin(String login);
	BigDecimal calcAndReplaceBalance(long personId);
	
}
