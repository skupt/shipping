package rozaryonov.shipping.service.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dao.PersonDao;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.service.PersonService;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

	private final PersonRepository personRepository;
	private final DataSource dataSource;
	
	
	@Override
	public Person findById(Long id) {
		return personRepository.findById(id).orElseThrow(()-> new DaoException("No Person with id:" + id));
	}

	@Override
	public Iterable<Person> findAll() {
		return personRepository.findAll();
	}

	@Override
	public void save(Person person) {
		personRepository.save(person);
		
	}
	
	@Override
	public Person findByLogin(String login) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			//logger.
			e.printStackTrace();
		}
		PersonDao personDao = new PersonDao(connection);
		return personDao.findByLogin(login).orElse(null);
	}


}
