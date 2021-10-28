package rozaryonov.shipping.repository.impl;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import rozaryonov.shipping.exception.PageableListFindingException;
import rozaryonov.shipping.exception.PersonBalanceCalculationException;
import rozaryonov.shipping.exception.PersonNotFoundException;
import rozaryonov.shipping.exception.SettlementsTypeNotFoundException;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.repository.CustomSettlementsRepository;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.repository.SettlementsTypeRepository;
@Slf4j
public class CustomSettlementsRepositoryImpl implements CustomSettlementsRepository {
	private static final String BALANCE = "select sum(amount * vector) as balance " +
			"from settlements, settlement_type " + 
			"where settlment_type_id = settlement_type.id " + 
			"and person_id = ?; ";
	private final SettlementsTypeRepository settlementsTypeRepository;
	private final PersonRepository personRepository;

	private Connection connection;
	
	CustomSettlementsRepositoryImpl(SettlementsTypeRepository settlementsTypeRepository, PersonRepository personRepository, DataSource dataSource) {
		this.settlementsTypeRepository = settlementsTypeRepository;
		this.personRepository = personRepository;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			log.error(e.getMessage());
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
				log.error("SQLException while PersonDAo.calcAndSetBalane(Person_id)" + e.getMessage());
				throw new PersonBalanceCalculationException(e.getMessage());
			}
			if (b==null) b=BigDecimal.ZERO;
			return b;
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
			log.error("SQLException while Settlements findAllInPeriod. ", e.getMessage());
			throw new PageableListFindingException(e.getMessage());
		}

		return settlements;
	}

}
