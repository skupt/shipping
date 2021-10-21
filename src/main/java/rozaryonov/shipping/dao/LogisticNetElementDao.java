package rozaryonov.shipping.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rozaryonov.shipping.model.LogisticNetElement;
import rozaryonov.shipping.service.LocalityService;
import rozaryonov.shipping.exception.DaoException;

public class LogisticNetElementDao {//todo why not extand AbstractDao
	private static final String FIND_ALL = "select city_id, neighbor_id, distance, logistic_config_id from logistic_net where logistic_config_id=? order by city_id, neighbor_id;";
	private static Logger logger = LogManager.getLogger(LogisticNetElementDao.class.getName());
	private Connection connection;
	private LocalityService localityService;
	
	public LogisticNetElementDao (Connection connection, LocalityService localityService) {
		this.connection = connection;
		this.localityService = localityService;
	}
	

	public Iterable<LogisticNetElement> findByNetConfig(long netConfigId) {
		ArrayList<LogisticNetElement> localities = new ArrayList<>();;
		try (PreparedStatement ps = connection.prepareStatement(FIND_ALL);) {
			ps.setLong(1, netConfigId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				LogisticNetElement netElement = new LogisticNetElement();
				netElement.setCity(localityService.findById(rs.getLong(1)));
				netElement.setNeighbor(localityService.findById(rs.getLong(2)));
				netElement.setDistance(rs.getDouble(3));
				localities.add(netElement);
			}
		} catch (SQLException e) {
			logger.error("SQLException while LogisticNetElement findAll. ", e.getMessage());
			throw new DaoException("SQLException while LogisticNetElement deleteById.", e);
		}
		return localities;
	}
}
