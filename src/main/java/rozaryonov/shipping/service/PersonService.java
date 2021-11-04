package rozaryonov.shipping.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import rozaryonov.shipping.dto.PersonDto;
import rozaryonov.shipping.dto.mapper.PersonMapper;
import rozaryonov.shipping.exception.PersonNotFoundException;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Role;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.repository.SettlementsRepository;

import javax.validation.Valid;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private final SettlementsRepository settlementsRepository;
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PersonMapper mapper;

    @Transactional
    public BigDecimal calcAndReplaceBalance(long personId) {
        BigDecimal balance = settlementsRepository.calcPersonBalance(personId);
        Person person = personRepository.findById(personId).orElseThrow(() ->
                new PersonNotFoundException(("Not found by id=" + personId)));
        person.setBalance(balance);
        personRepository.saveAndFlush(person);
        return balance;
    }

    @Transactional
    public String createUser(@ModelAttribute("personDto") @Valid PersonDto personDto) {
        personDto.setRoleId(Role.ROLE_USER.toString());
        String passEncoded = passwordEncoder.encode(personDto.getPassword());
        personDto.setPassword(passEncoded);
        Person person = mapper.toPerson(personDto);
        person.setBalance(BigDecimal.ZERO);
        personRepository.save(person);
        return "redirect:/";
    }

    public BindingResult checkUserCreationForm (@ModelAttribute("personDto") @Valid PersonDto personDto, BindingResult bindingResult) {
        try {
            if (personRepository.findByLogin(personDto.getLogin()).orElseThrow(()-> new PersonNotFoundException()) != null) {
                bindingResult.addError(new FieldError("personDto", "login", "Please, choose other login."));
            }
        } catch(PersonNotFoundException e) {
            // do nothing, chosen login is free
        }
        return bindingResult;
    }


}