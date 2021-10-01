package rozaryonov.shipping.model;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dto.PersonDto;
import rozaryonov.shipping.service.RoleService;

@Component
@RequiredArgsConstructor
public class Mapper {

	private final RoleService roleService;
	
	
	public Person toPerson(PersonDto personDto) {
		Person person = new Person();
		person.setId(personDto.getId());
		person.setLogin(personDto.getLogin());
		person.setName(personDto.getName());
		person.setPassword(personDto.getPassword());
		person.setPatronomic(personDto.getPatronomic());
		person.setSurname(personDto.getSurname());
		person.setTitle(personDto.getTitle());
		person.setRole(roleService.findById(personDto.getRoleId()));
		
		return person;
	}
}
