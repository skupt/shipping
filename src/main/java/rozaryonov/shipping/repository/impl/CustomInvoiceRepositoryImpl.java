package rozaryonov.shipping.repository.impl;

import lombok.extern.slf4j.Slf4j;
import rozaryonov.shipping.exception.ConnectionGettingException;
import rozaryonov.shipping.exception.InvoiceStatusNotFound;
import rozaryonov.shipping.exception.PersonNotFoundException;
import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.repository.CustomInvoiceRepository;
import rozaryonov.shipping.repository.InvoiceStatusRepository;
import rozaryonov.shipping.repository.PersonRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class CustomInvoiceRepositoryImpl implements CustomInvoiceRepository {
	private final PersonRepository personRepository;
	private final InvoiceStatusRepository invoiceStatusRepository;

	private Connection connection;

	public CustomInvoiceRepositoryImpl(DataSource dataSource, PersonRepository personRepository,
                                       InvoiceStatusRepository invoiceStatusRepository) {
		this.personRepository = personRepository;
		this.invoiceStatusRepository = invoiceStatusRepository;
		try {
			this.connection = dataSource.getConnection();
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new ConnectionGettingException(e.getMessage());
		}
	}

	@Override
	public List<Invoice> findFilterSort(Timestamp after, Timestamp before, Predicate<Invoice> p,
			Comparator<Invoice> c) {
		List<Invoice> invoices = findAllInPeriod(after, before);

		return invoices.stream().filter(p).sorted(c).collect(Collectors.toList());
	}

	private static final String PERIOD_PAGE = "select " + "id, person_id, creation_datetime, sum, invoice_status_id "
			+ "from invoice " + "where creation_datetime>=? and creation_datetime<? " + "order by creation_datetime ;";

	private List<Invoice> findAllInPeriod(Timestamp after, Timestamp before) {
		ArrayList<Invoice> invoiceList = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(PERIOD_PAGE);) {
			ps.setTimestamp(1, after);
			ps.setTimestamp(2, before);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Invoice i = new Invoice();
				i.setId(rs.getLong(1));
				i.setPerson(personRepository.findById(rs.getLong(2))
						.orElseThrow(() -> new PersonNotFoundException("No Person while InvoiceDao.findAll")));
				i.setCreationDateTime(rs.getTimestamp(3));
				i.setSum(rs.getBigDecimal(4));
				i.setInvoiceStatus(
						invoiceStatusRepository.findById(rs.getLong(5)).orElseThrow(() -> new InvoiceStatusNotFound(
								"No InvoiceStatus found in InvoiceServiceImpl.findAllInPeriod()")));
				invoiceList.add(i);
			}
		} catch (SQLException e) {
			log.error("SQLException while Invoice findAllInPeriod. ", e.getMessage());
			throw new ConnectionGettingException(e.getMessage());
		}

		return invoiceList;
	}

}
