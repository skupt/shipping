package rozaryonov.shipping.dto.mapper;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dto.PersonDto;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Role;

@Component
@RequiredArgsConstructor
public class PersonMapper {
	
	public Person toPerson(PersonDto personDto) {
		Person person = new Person();
		person.setId(personDto.getId());
		person.setLogin(personDto.getLogin());
		person.setName(personDto.getName());
		person.setPassword(personDto.getPassword());
		person.setPatronomic(personDto.getPatronomic());
		person.setSurname(personDto.getSurname());
		person.setTitle(personDto.getTitle());
		person.setRole(Role.valueOf(personDto.getRoleId()));
		
		return person;
	}
}
