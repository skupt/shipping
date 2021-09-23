package rozaryonov.shipping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.service.PersonService;

@RestController
@RequiredArgsConstructor
public class PersonRestController {
	
	private final PersonService personService;
	
	@GetMapping ("/personJsonList")
	public Iterable<Person> transferPersonList () {
		return personService.findAll();
	}
}
