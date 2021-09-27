package rozaryonov.shipping.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class AbstractDao <T, ID> {
	protected Connection connection;
	protected Logger logger = LogManager.getLogger();
	
	protected AbstractDao(Connection connection) {
		this.connection = connection;
	}
	
    abstract T save(T entity);

    abstract Optional<T> findById(ID id);

    abstract boolean existsById(ID id);

    abstract Iterable<T> findAll();

    abstract void deleteById(ID id);

	
	protected void close() {
		if (connection !=null) {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.warn(e.getMessage());
			}
			connection=null;
		}
	}

}
