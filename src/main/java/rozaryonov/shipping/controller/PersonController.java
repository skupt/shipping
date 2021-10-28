package rozaryonov.shipping.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rozaryonov.shipping.dto.PersonDto;
import rozaryonov.shipping.service.PersonService;

import javax.validation.Valid;

@Controller
@RequestMapping("/persons/")
@Slf4j
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping("/form")
    public String getNewPersonForm(@ModelAttribute("personDto") PersonDto personDto) {
        return "/person/form";
    }

    @PostMapping("/")
    public String createPerson(@ModelAttribute ("personDto") @Valid PersonDto personDto, BindingResult bindingResult) {
        personService.createUser(personDto, bindingResult);
        return "redirect:/";
    }
}
