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
import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.repository.InvoiceStatusRepository;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {
	private static Logger logger = LogManager.getLogger();

	private final DataSource dataSource;
	private final PersonRepository personRepository;
	private final InvoiceStatusRepository invoiceStatusRepository;

	private Connection connection;

	public InvoiceServiceImpl(
			DataSource dataSource, 
			PersonRepository personRepository, 
			InvoiceStatusRepository invoiceStatusRepository) {
		this.dataSource = dataSource;
		this.personRepository = personRepository;
		this.invoiceStatusRepository = invoiceStatusRepository;
		try {
			this.connection = dataSource.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public List<Invoice> findFilterSort(Timestamp after, Timestamp before, Predicate<Invoice> p,
			Comparator<Invoice> c) {
		List <Invoice> invoices = findAllInPeriod(after, before);
		
		return invoices.stream().filter(p).sorted(c).collect(Collectors.toList());
	}

	private static final String PERIOD_PAGE = "select "
			+ "id, person_id, creation_datetime, sum, invoice_status_id "
			+ "from invoice "
			+ "where creation_datetime>=? and creation_datetime<? "
			+ "order by creation_datetime ;";
	
	private List<Invoice> findAllInPeriod(Timestamp after, Timestamp before) {
		ArrayList<Invoice> invoiceList = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(PERIOD_PAGE);) {
			ps.setTimestamp(1, after);
			ps.setTimestamp(2, before);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Invoice i = new Invoice();
				i.setId(rs.getLong(1));
				i.setPerson(personRepository.findById(rs.getLong(2)).orElseThrow(()-> new DaoException("No Person while InvoiceDao.findAll")));
				i.setCreationDateTime(rs.getTimestamp(3));
				i.setSum(rs.getBigDecimal(4));
				i.setInvoiceStatus(invoiceStatusRepository.findById(rs.getLong(5)).orElseThrow(()-> new DaoException("No load Locality while InvoiceDao.findAllinPeriod")));
				
				invoiceList.add(i);
			}
		} catch (SQLException e) {
			logger.error("SQLException while Invoice findAllInPeriod. ", e.getMessage());
			throw new DaoException("SQLException while Invoice findAllInPeriod .", e);
		}
		
		return invoiceList;
	}



	
}
