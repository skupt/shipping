package rozaryonov.shipping.model;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dto.PersonDto;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.repository.RoleRepository;

@Component
@RequiredArgsConstructor
public class Mapper {//todo why it is in a model package? what is 'Mapper'; name correctly

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
		//todo use Enum for ROLES!!!!!!!! (this can fix your problem with security)
		
		return person;
	}
}
