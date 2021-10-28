package rozaryonov.shipping.repository.impl;

import lombok.extern.slf4j.Slf4j;
import rozaryonov.shipping.exception.ConnectionGettingException;
import rozaryonov.shipping.exception.LogisticConfigNotFoundException;
import rozaryonov.shipping.exception.PageableListFindingException;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.CustomTariffRepository;
import rozaryonov.shipping.repository.LogisticConfigRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class CustomTariffRepositoryImpl implements CustomTariffRepository {
	private final LogisticConfigRepository logisticConfigRepository;

	private Connection connection;

	public CustomTariffRepositoryImpl(DataSource dataSource, LogisticConfigRepository logisticConfigRepository) {
		this.logisticConfigRepository = logisticConfigRepository;
		try {
			this.connection = dataSource.getConnection();
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new ConnectionGettingException(e.getMessage());
		}
	}

	private static final String FITER_BY_PERIOD = "select"
			+ " id, creation_timestamp, logistic_config_id, truck_velocity, density, paperwork,"
			+ " targeted_receipt, targeted_delivery, shipping_rate, insurance_worth, insurance_rate" + " from tariff"
			+ " where creation_timestamp>=? and creation_timestamp<? ";

	public List<Tariff> findFilterSort(Timestamp after, Timestamp before, Predicate<Tariff> p, Comparator<Tariff> c) {

		List<Tariff> tariffs = findAllInPeriod(after, before);

		return tariffs.stream().filter(p).sorted(c).collect(Collectors.toList());
	}

	private List<Tariff> findAllInPeriod(Timestamp after, Timestamp before) {
		ArrayList<Tariff> tariffs = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(FITER_BY_PERIOD);) {
			ps.setTimestamp(1, after);
			ps.setTimestamp(2, before);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Tariff t = new Tariff();
				t.setId(rs.getLong(1));
				t.setCreationTimestamp(rs.getTimestamp(2));
				t.setLogisticConfig(logisticConfigRepository.findById(rs.getLong(3))
						.orElseThrow(() -> new LogisticConfigNotFoundException(
								"No LogisticConfig found in TariffServiceImpl.findAllByPeriod()")));
				t.setTruckVelocity(rs.getInt(4));
				t.setDensity(rs.getDouble(5));
				t.setPaperwork(rs.getDouble(6));
				t.setTargetedReceipt(rs.getInt(7));
				t.setTargetedDelivery(rs.getInt(8));
				t.setShippingRate(rs.getDouble(9));
				t.setInsuranceWorth(rs.getDouble(10));
				t.setInsuranceRate(rs.getDouble(11));
				tariffs.add(t);
			}
		} catch (SQLException e) {
			log.error("SQLException while Shipping findAllInPeriod. ", e.getMessage());
			throw new PageableListFindingException(e.getMessage());
		}
		return tariffs;
	}

}
