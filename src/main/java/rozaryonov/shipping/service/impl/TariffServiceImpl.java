package rozaryonov.shipping.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.LogisticConfigRepository;
import rozaryonov.shipping.repository.TariffRepository;
import rozaryonov.shipping.service.TariffService;

@Service
public class TariffServiceImpl implements TariffService {
	private static Logger logger = LogManager.getLogger();

	private final TariffRepository tariffRepository;
	private final DataSource dataSource;
	private final LogisticConfigRepository logisticConfigRepository;

	private Connection connection;

	public TariffServiceImpl(TariffRepository tariffRepository, DataSource dataSource, 
			LogisticConfigRepository logisticConfigRepository) {
		this.tariffRepository = tariffRepository;
		this.dataSource = dataSource;
		this.logisticConfigRepository = logisticConfigRepository;
		try {
			this.connection = dataSource.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	
	
	private static final String FITER_BY_PERIOD  = "select"
			+ " id, creation_timestamp, logistic_config_id, truck_velocity, density, paperwork,"
			+ " targeted_receipt, targeted_delivery, shipping_rate, insurance_worth, insurance_rate"
			+ " from tariff"
			+ " where creation_timestamp>=? and creation_timestamp<? ";

	public List<Tariff> findFilterSort(Timestamp after, Timestamp before, Predicate<Tariff> p,
			Comparator<Tariff> c) {
		
		List <Tariff> tariffs = findAllInPeriod(after, before);
		
		return tariffs.stream().filter(p).sorted(c).collect(Collectors.toList());
	}
	
	private List<Tariff> findAllInPeriod(Timestamp after, Timestamp before) {
		ArrayList<Tariff> tariffs = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(FITER_BY_PERIOD);) {
			ps.setTimestamp(1, after);
			ps.setTimestamp(2, before);
			
			// " id, creation_timestamp, logistic_config_id, truck_velocity, density, paperwork,"
			// " targeted_receipt, targeted_delivery, shipping_rate, insurance_worth, insurance_rate"

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Tariff t = new Tariff();
				t.setId(rs.getLong(1));
				t.setCreationTimestamp(rs.getTimestamp(2));
				t.setLogisticConfig(logisticConfigRepository. findById(rs.getLong(3)).orElseThrow(
						()-> new DaoException("No LogisticConfig while TariffRepositoryImpl.findAllByPeriod()")));
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
			logger.error("SQLException while Shipping findAllInPeriod. ", e.getMessage());
			throw new DaoException("SQLException while findAllInPeriod deleteById.", e);
		}
		return tariffs;
	}
	

	
	@Override
	public Tariff findById(Long id) {
		return tariffRepository.findById(id).orElseThrow(()-> new DaoException("No Tariff with id:" + id));
	}

	@Override
	public Iterable<Tariff> findAll() {
		return tariffRepository.findAll();
	}

}
