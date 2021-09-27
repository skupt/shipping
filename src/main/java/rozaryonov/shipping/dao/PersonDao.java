package rozaryonov.shipping.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Person;

public class PersonDao extends AbstractDao <Person, Long>{
	private static final String UPDATE = "update person set login=?, password=?, email=?, name=?, surname=?, patronomic=?, title=?, role_id=? where id=?";
	private static final String INSERT = "insert into person (login, password, email, name, surname, patronomic, title, role_id) values (?,?,?,?,?,?,?,?)";
	private static final String FIND_BY_ID = "select login, password, email, name, surname, patronomic, title, role_id  from person where id=?";
	private static final String FIND_ALL = "select id, login, password, email, name, surname, patronomic, title, role_id from person";
	private static final String DELETE = "delete from person where id=?";
	private static final String EXIST = "select id from person where id=?";
	private static final String FIND_BY_LOGIN = "select login, password, email, name, surname, patronomic, title, role_id, id  from person where login=?";
	private static final String BALANCE = "select sum(amount * vector) as balance " + 
			"from settlements, settlement_type " + 
			"where settlment_type_id = settlement_type.id " + 
			"and person_id = ?; ";
	private static final String REPLACE_BALANCE = "replace into user_details (person_id, balance) values (?,?)";
	
	private static Logger logger = LogManager.getLogger(PersonDao.class.getName());

	
	public PersonDao(Connection connection) {
		super(connection);
	}

	@Override
	public Person save(Person person) {
		if (person.getId() == 0) {
			try (PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);) {
				ps.setString(1, person.getLogin());
				ps.setString(2, person.getPassword());
				ps.setString(3, person.getEmail());
				ps.setString(4, person.getName());
				ps.setString(5, person.getSurname());
				ps.setString(6, person.getPatronomic());
				ps.setString(7, person.getTitle());
				ps.setLong(8, person.getRole().getId());
				
				if (ps.executeUpdate() > 0) {
					ResultSet rs = ps.getGeneratedKeys();
					if (rs.next()) {
						person.setId(rs.getLong(1));
					}
				}
			} catch (SQLException e) {
				logger.error("SQLException while Role save. ", e.getMessage());
				throw new DaoException("SQLException while Person save.", e.getCause());
			}
			
		} else {
			try (PreparedStatement ps = connection.prepareStatement(UPDATE);) {
				ps.setString(1, person.getLogin());
				ps.setString(2, person.getPassword());
				ps.setString(3, person.getEmail());
				ps.setString(4, person.getName());
				ps.setString(5, person.getSurname());
				ps.setString(6, person.getPatronomic());
				ps.setString(7, person.getTitle());
				ps.setLong(8, person.getRole().getId());
				ps.setLong(9, person.getId());
			} catch (SQLException e) {
				logger.error("SQLException while Role save. ", e.getMessage());
				throw new DaoException("SQLException while Person save.", e);
			}
			
		}
		return person;
	}

	@Override
	public Optional<Person> findById(Long id) {
		Person person = null;
		long role_id = 0;
		try (PreparedStatement ps = connection.prepareStatement(FIND_BY_ID);) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					person = new Person();
					if (id != 0) {
						person.setId(id);
						person.setLogin(rs.getString(1));
						person.setPassword(rs.getString(2));
						person.setEmail(rs.getString(3));
						person.setName(rs.getString(4));
						person.setSurname(rs.getString(5));
						person.setPatronomic(rs.getString(6));
						person.setTitle(rs.getString(7));
						role_id = rs.getLong(8);
					}
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException while Role save. ", e.getMessage());
			throw new DaoException("SQLException while Role find by id.", e);
		}
		RoleDao rd;
		rd = new RoleDao(super.connection);
		person.setRole(rd.findById(role_id).orElseThrow(()-> new DaoException("Can't find Role for Person while Person findById")));
		//rd.close();
		return Optional.ofNullable(person);
	}

	@Override
	public boolean existsById(Long id) {
		try (PreparedStatement ps = connection.prepareStatement(EXIST);) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return true;
		} catch (SQLException e) {
			logger.error("SQLException while Person existById. ", e.getMessage());
			throw new DaoException("SQLException while Role find by id.", e);
		}
		return false;
	}

	@Override
	public Iterable<Person> findAll() {
		ArrayList<Person> rpersons = new ArrayList<>();
		RoleDao rd = new RoleDao(super.connection);
		try (PreparedStatement ps = connection.prepareStatement(FIND_ALL);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Person person = new Person();
				person.setId(rs.getLong(1));
				person.setLogin(rs.getString(2));
				person.setPassword(rs.getString(3));
				person.setEmail(rs.getString(4));
				person.setName(rs.getString(5));
				person.setSurname(rs.getString(6));
				person.setPatronomic(rs.getString(7));
				person.setTitle(rs.getString(8));
				person.setRole(rd.findById(rs.getLong(9)).orElseThrow(()-> new DaoException("Can't find Role for Person while Person findById")));
				rpersons.add(person);
			}
		} catch (SQLException e) {
			logger.error("SQLException while Role findAll. ", e.getMessage());
			throw new DaoException("SQLException while Role deleteById.", e);
		}
		return rpersons;
	}

	@Override
	public void deleteById(Long id) {
		try (PreparedStatement ps = connection.prepareStatement(DELETE);) {
			ps.setLong(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("SQLException while Person delete. ", e.getMessage());
			throw new DaoException("SQLException while Person deleteById.", e);
		}
	}
	
	public Optional<Person> findByLogin(String login) {
		Person person = null;
		long role_id = 0;
		try (PreparedStatement ps = connection.prepareStatement(FIND_BY_LOGIN);) {
			ps.setString(1, login);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					person = new Person();
						person.setLogin(rs.getString(1));
						person.setPassword(rs.getString(2));
						person.setEmail(rs.getString(3));
						person.setName(rs.getString(4));
						person.setSurname(rs.getString(5));
						person.setPatronomic(rs.getString(6));
						person.setTitle(rs.getString(7));
						role_id = rs.getLong(8);
						person.setId(rs.getLong(9));
						
					}
				}
		} catch (SQLException e) {
			logger.error("SQLException while Role save. ", e.getMessage());
			throw new DaoException("SQLException while Role find by login.", e);
		}
		if (person==null) return Optional.ofNullable(person);
		RoleDao rd;
		rd = new RoleDao(super.connection);
		person.setRole(rd.findById(role_id).orElseThrow(()-> new DaoException("Can't find Role for Person while Person findByLogin")));
		return Optional.ofNullable(person);
	}
	
	public BigDecimal calcAndReplaceBalance(Long person_id) {
		BigDecimal b = null;
		try(PreparedStatement ps = connection.prepareStatement(BALANCE)) {
			ps.setLong(1, person_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				b = rs.getBigDecimal(1);
			}
		} catch (SQLException e) {
			logger.error("SQLException while PersonDAo.calcAndSetBalane(Person_id)" + e.getMessage());
		}
		if (b==null) b=BigDecimal.ZERO;
		try(PreparedStatement ps = connection.prepareStatement(REPLACE_BALANCE)) {
			ps.setLong(1, person_id);
			ps.setBigDecimal(2, b);
			if (ps.executeUpdate() == 0) throw new DaoException("Balance isn't replaced for person_id:" + person_id);
		} catch (SQLException e) {
			logger.error("SQLException while PersonDAo.calcAndSetBalane(Person_id)" + e.getMessage());
		}
		return b;
	}
	

	
	
	
	
	@Override
	public void close() {
		super.close();
	}


}
