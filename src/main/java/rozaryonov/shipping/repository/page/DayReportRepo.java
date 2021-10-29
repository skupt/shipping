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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import rozaryonov.shipping.exception.ConnectionGettingException;
import rozaryonov.shipping.exception.PageableListFindingException;
import rozaryonov.shipping.repository.reportable.DayReport;

@Slf4j
@Component
public class DayReportRepo implements Pageable<DayReport> {
	private Connection connection;
	
	public DayReportRepo(DataSource dataSource) {
		try {
			this.connection = dataSource.getConnection();
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new ConnectionGettingException(e.getMessage());
		}

	}
	
	private static final String FITER_BY_PERIOD  = "select DATE(download_datetime) AS Date, sum(fare) as Turnover" + 
			" from shipping" + 
			" group by Date;";

	
	@Override
	public List<DayReport> findFilterSort(Timestamp after, Timestamp before, Predicate<DayReport> p,
			Comparator<DayReport> c) {
		
		List <DayReport> tariffs = findAllInPeriod(after, before);
		
		return tariffs.stream().filter(p).sorted(c).collect(Collectors.toList());
	}
	
	private List<DayReport> findAllInPeriod(Timestamp after, Timestamp before) {
		ArrayList<DayReport> reportRows = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(FITER_BY_PERIOD);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DayReport r = new DayReport();
				r.setIndex(rs.getString(1));
				r.setValue(Double.parseDouble(rs.getString(2)));
				reportRows.add(r);
			}
		} catch (SQLException e) {
			log.error("SQLException while Shipping findAllInPeriod. ", e.getMessage());
			throw new PageableListFindingException(e.getMessage());
		}
		return reportRows;
	}

}
