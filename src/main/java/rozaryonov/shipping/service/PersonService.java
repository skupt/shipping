package rozaryonov.shipping.service;

import java.math.BigDecimal;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import rozaryonov.shipping.dto.PersonDto;
import rozaryonov.shipping.model.Person;

import javax.validation.Valid;

public interface PersonService {
	Person findByLogin(String login);
	BigDecimal calcAndReplaceBalance(long personId);
	String createUser(@ModelAttribute("personDto") @Valid PersonDto personDto, BindingResult bindingResult);


}
