package rozaryonov.shipping.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Role;



public class RoleDao extends AbstractDao<Role, Long> {
	private static final String UPDATE = "update role set name=? where id = ?";
	private static final String INSERT = "insert into role (name) values (?)";
	private static final String FIND_BY_ID = "select id, name from role where id=?";
	private static final String FIND_BY_NAME = "select id, name from role where name=?";
	private static final String FIND_ALL = "select id, name from role";
	private static final String DELETE = "delete from role where id=?";
	private static final String EXIST = "select id from role where id=?";
	private static Logger logger = LogManager.getLogger(RoleDao.class.getName());

	public RoleDao(Connection connection) {
		super(connection);
	}

	@Override
	public Role save(Role role) {
		if (role.getId() == 0) {
			try (PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);) {
				ps.setString(1, role.getName());
				if (ps.executeUpdate() > 0) {
					ResultSet rs = ps.getGeneratedKeys();
					if (rs.next()) {
						role.setId(rs.getLong(1));
					}
				}
			} catch (SQLException e) {
				logger.error("SQLException while Role save. ", e.getMessage());
				throw new DaoException("SQLException while Role save.", e.getCause());
			}

		} else {
			try (PreparedStatement ps = connection.prepareStatement(UPDATE);) {
				ps.setString(1, role.getName());
				ps.setLong(2, role.getId());
			} catch (SQLException e) {
				logger.error("SQLException while Role save. ", e.getMessage());
				throw new DaoException("SQLException while Role save.", e);
			}

		}
		return role;
	}

	@Override
	public Optional<Role> findById(Long id) {
		Role role = null;
		try (PreparedStatement ps = connection.prepareStatement(FIND_BY_ID);) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					long id2 = rs.getLong(1);
					String name = rs.getString(2);
					if (id != 0) {
						role = new Role();
						role.setId(id2);
						role.setName(name);
					}
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException while Role findById. ", e.getMessage());
			throw new DaoException("SQLException while Role find by id.", e);
		}

		return Optional.ofNullable(role);
	}

	@Override
	public boolean existsById(Long id) {
		try (PreparedStatement ps = connection.prepareStatement(EXIST);) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return true;
		} catch (SQLException e) {
			logger.error("SQLException while Role save. ", e.getMessage());
			throw new DaoException("SQLException while Role find by id.", e);
		}
		return false;
	}

	@Override
	public Iterable<Role> findAll() {
		ArrayList<Role> roles = new ArrayList<>();
		;
		try (PreparedStatement ps = connection.prepareStatement(FIND_ALL);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Role role = new Role();
				role.setId(rs.getLong(1));
				role.setName(rs.getString(2));
				roles.add(role);
			}
		} catch (SQLException e) {
			logger.error("SQLException while Role findAll. ", e.getMessage());
			throw new DaoException("SQLException while Role deleteById.", e);
		}
		return roles;
	}

	@Override
	public void deleteById(Long id) {
		try (PreparedStatement ps = connection.prepareStatement(DELETE);) {
			ps.setLong(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("SQLException while Role deleteById. ", e.getMessage());
			throw new DaoException("SQLException while Role deleteById.", e);
		}
	}

	@Override
	public void close() {
		super.close();
	}

	public Optional<Role> findByName(String roleName) {
		Role role = null;
		try (PreparedStatement ps = connection.prepareStatement(FIND_BY_NAME);) {
			ps.setString(1, roleName);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					long id2 = rs.getLong(1);
					String name = rs.getString(2);
					role = new Role();
					role.setId(id2);
					role.setName(name);
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException while Role findByName. ", e.getMessage());
			throw new DaoException("SQLException while Role find by id.", e);
		}

		return Optional.ofNullable(role);
	}

}
