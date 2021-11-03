package rozaryonov.shipping.repository.impl;

import lombok.extern.slf4j.Slf4j;
import rozaryonov.shipping.exception.ConnectionGettingException;
import rozaryonov.shipping.exception.LocalityNotFoundException;
import rozaryonov.shipping.exception.LogisticNetNotFoundException;
import rozaryonov.shipping.model.LogisticNetElement;
import rozaryonov.shipping.repository.CustomLogisticNetElementRepository;
import rozaryonov.shipping.repository.LocalityRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Slf4j
public class CustomLogisticNetElementRepositoryImpl implements CustomLogisticNetElementRepository {
	private static final String FIND_ALL = "select city_id, neighbor_id, distance, logistic_config_id from logistic_net where logistic_config_id=? order by city_id, neighbor_id;";
	private final LocalityRepository localityRepository;
	private Connection connection;

	public CustomLogisticNetElementRepositoryImpl(DataSource dataSource, LocalityRepository localityRepository) {
		try {
			this.connection = dataSource.getConnection();
		} catch (SQLException e) {
			throw new ConnectionGettingException("SQL Exception while constructor CustomLogisticNetElementRepositoryImpl. " + e.getMessage());
		}
		this.localityRepository = localityRepository;
	}

	public Iterable<LogisticNetElement> findByNetConfig(long netConfigId) {
		ArrayList<LogisticNetElement> localities = new ArrayList<>();
		;
		try (PreparedStatement ps = connection.prepareStatement(FIND_ALL);) {
			ps.setLong(1, netConfigId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				LogisticNetElement netElement = new LogisticNetElement();
				netElement.setCity(localityRepository.findById(rs.getLong(1)).orElseThrow(()-> new LocalityNotFoundException()));
				netElement.setNeighbor(localityRepository.findById(rs.getLong(2)).orElseThrow(()-> new LocalityNotFoundException()));
				netElement.setDistance(rs.getDouble(3));
				localities.add(netElement);
			}
		} catch (SQLException e) {
			log.error("SQLException while LogisticNetElement findAll. ", e.getMessage());
			throw new LogisticNetNotFoundException(
					"No Iterable<LogisticNetElement> found in LogisticNetElementDao.findByNetConfig.");
		}
		return localities;
	}
}
