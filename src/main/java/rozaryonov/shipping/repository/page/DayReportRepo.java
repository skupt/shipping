package rozaryonov.shipping.repository.page;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.repository.reportable.DayReport;

@Component
public class DayReportRepo implements Pageable<DayReport> {
	private final DataSource dataSource;
	private Connection connection;
	
	public DayReportRepo(DataSource dataSource) {
		this.dataSource = dataSource;
		try {
			this.connection = dataSource.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}

	}
	
	private static final String FITER_BY_PERIOD  = "select DATE(download_datetime) AS Date, sum(fare) as Turnover" + 
			" from shipping" + 
			" group by Date;";

	
	private static Logger logger = LogManager.getLogger(DayReportRepo.class.getName());

	
	@Override
	public List<DayReport> findFilterSort(Timestamp after, Timestamp before, Predicate<DayReport> p,
			Comparator<DayReport> c) {
		
		List <DayReport> tariffs = findAllInPeriod(after, before);
		
		return tariffs.stream().filter(p).sorted(c).collect(Collectors.toList());
	}
	
	private List<DayReport> findAllInPeriod(Timestamp after, Timestamp before) {
		ArrayList<DayReport> reportRows = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(FITER_BY_PERIOD);) {
			//ps.setTimestamp(1, after);
			//ps.setTimestamp(2, before);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DayReport r = new DayReport();
				r.setIndex(rs.getString(1));
				r.setValue(Double.parseDouble(rs.getString(2)));
				reportRows.add(r);
			}
		} catch (SQLException e) {
			logger.error("SQLException while Shipping findAllInPeriod. ", e.getMessage());
			throw new DaoException("SQLException while findAllInPeriod deleteById.", e);
		}
		return reportRows;
	}

}
