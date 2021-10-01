package rozaryonov.shipping.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.service.PersonService;
import rozaryonov.shipping.service.RoleService;

@Controller
@RequiredArgsConstructor
public class PersonController {
	
	private final PersonService personService;
	
	@GetMapping ("/persons")
	public String showPersonList (Model model) {
		List<Person> personList = new ArrayList<>();
		personService.findAll().forEach(personList::add);
		System.out.println(personList);
		model.addAttribute("personList", personList);

		return "persons";
	}
}
