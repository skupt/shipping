package rozaryonov.shipping.repository.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import rozaryonov.shipping.exception.PersonBalanceCalculationException;
import rozaryonov.shipping.repository.CustomSettlementsRepository;

public class CustomSettlementsRepositoryImpl implements CustomSettlementsRepository {
	private static Logger logger = LogManager.getLogger();
	private static final String BALANCE = "select sum(amount * vector) as balance " + 
			"from settlements, settlement_type " + 
			"where settlment_type_id = settlement_type.id " + 
			"and person_id = ?; ";
	
	private Connection connection;
	
	CustomSettlementsRepositoryImpl(DataSource dataSource) {
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new PersonBalanceCalculationException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public BigDecimal calcPersonBalance(long personId) {
			BigDecimal b = null;
			try(PreparedStatement ps = connection.prepareStatement(BALANCE)) {
				ps.setLong(1, personId);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					b = rs.getBigDecimal(1);
				}
			} catch (SQLException e) {
				logger.error("SQLException while PersonDAo.calcAndSetBalane(Person_id)" + e.getMessage());
				throw new PersonBalanceCalculationException(e.getMessage());
			}
			if (b==null) b=BigDecimal.ZERO;
			return b;
	}

}
