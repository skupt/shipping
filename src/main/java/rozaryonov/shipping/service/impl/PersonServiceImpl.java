package rozaryonov.shipping.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	
	private static Logger logger = LogManager.getLogger(PersonServiceImpl.class.getName());

	private final DataSource dataSource;
	


	
	@Override
	public Person findByLogin(String login) {
		PersonDao personDao = null;;
		try {
			personDao = new PersonDao(dataSource.getConnection());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return personDao.findByLogin(login).orElse(null);
	}
	
	@Override
	public BigDecimal calcAndReplaceBalance(long personId) {
		PersonDao personDao = null;;
		try {
			personDao = new PersonDao(dataSource.getConnection());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		
		System.out.println("PersonServiceImpl.calcAndReplaceBalance, personDao: " + personDao);
		BigDecimal balance = personDao.calcAndReplaceBalance(personId);
		return balance;
	}

}
