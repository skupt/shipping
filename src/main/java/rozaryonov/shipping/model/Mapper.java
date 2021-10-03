package rozaryonov.shipping.model;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dto.PersonDto;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.repository.RoleRepository;

@Component
@RequiredArgsConstructor
public class Mapper {

	private final RoleRepository roleRepository;
	
	
	public Person toPerson(PersonDto personDto) {
		Person person = new Person();
		person.setId(personDto.getId());
		person.setLogin(personDto.getLogin());
		person.setName(personDto.getName());
		person.setPassword(personDto.getPassword());
		person.setPatronomic(personDto.getPatronomic());
		person.setSurname(personDto.getSurname());
		person.setTitle(personDto.getTitle());
		person.setRole(roleRepository.findById(personDto.getRoleId()).orElseThrow(()-> new DaoException("No Role with id:"+ personDto.getRoleId())));
		
		return person;
	}
}
