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

import rozaryonov.shipping.exception.ConnectionGettingException;
import rozaryonov.shipping.exception.PageableListFindingException;
import rozaryonov.shipping.exception.PersonNotFoundException;
import rozaryonov.shipping.exception.SettlementsTypeNotFoundException;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.repository.SettlementsTypeRepository;
import rozaryonov.shipping.service.SettlementsService;

@Service
public class SettlementsServiceImpl implements SettlementsService {
	private static Logger logger = LogManager.getLogger();

	private final SettlementsTypeRepository settlementsTypeRepository;
	private final PersonRepository personRepository;
	private Connection connection;

	public SettlementsServiceImpl(DataSource dataSource, SettlementsTypeRepository settlementsTypeRepository,
			PersonRepository personRepository) {

		this.settlementsTypeRepository = settlementsTypeRepository;
		this.personRepository = personRepository;
		try {
			this.connection = dataSource.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new ConnectionGettingException(e.getMessage());
		}
	}

	private static final String PERIOD_PAGE = "select " + "id, creation_datetime, person_id, settlment_type_id, amount "
			+ "from settlements " + "where creation_datetime>=? and creation_datetime<? "
			+ "order by creation_datetime ";

	@Override
	public List<Settlements> findFilterSort(Timestamp after, Timestamp before, Predicate<Settlements> p,
			Comparator<Settlements> c) {

		return findAllInPeriod(after, before).stream().filter(p).sorted(c).collect(Collectors.toList());
	}

	private List<Settlements> findAllInPeriod(Timestamp after, Timestamp before) {
		ArrayList<Settlements> settlements = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(PERIOD_PAGE);) {
			ps.setTimestamp(1, after);
			ps.setTimestamp(2, before);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Settlements s = new Settlements();
				s.setId(rs.getLong(1));
				s.setCreationDatetime(rs.getTimestamp(2).toLocalDateTime());
				s.setPerson(personRepository.findById(rs.getLong(3)).orElseThrow(
						() -> new PersonNotFoundException("No Person found in SettlementsService.findAllinPeriod()")));
				s.setSettlementType(settlementsTypeRepository.findById(rs.getLong(4))
						.orElseThrow(() -> new SettlementsTypeNotFoundException(
								"No Locality found in SettlementsService.findAllinPeriod()")));
				s.setAmount(rs.getBigDecimal(5));

				settlements.add(s);
			}
		} catch (SQLException e) {
			logger.error("SQLException while Settlements findAllInPeriod. ", e.getMessage());
			throw new PageableListFindingException(e.getMessage());
		}

		return settlements;
	}

}
