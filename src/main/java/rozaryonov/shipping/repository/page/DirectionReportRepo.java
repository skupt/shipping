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
import org.springframework.stereotype.Component;

import rozaryonov.shipping.exception.ConnectionGettingException;
import rozaryonov.shipping.exception.PageableListFindingException;
import rozaryonov.shipping.repository.reportable.DirectionReport;

@Component
public class DirectionReportRepo implements Pageable<DirectionReport> {
	private static Logger logger = LogManager.getLogger(DayReportRepo.class.getName());
	private static final String FITER_BY_PERIOD  = 
			"select concat_ws(\" -> \", dep.name, arr.name) as Direction, sum(a.fare) as Turnover\n" + 
			"from shipping a,\n" + 
			"	locality dep,\n" + 
			"    locality arr\n" + 
			"where a.load_locality_id = dep.id\n" + 
			"and a.unload_locality_id = arr.id\n" + 
			"group by Direction;";


	private Connection connection;
	
	public DirectionReportRepo(DataSource dataSource) {
		try {
			this.connection = dataSource.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new ConnectionGettingException(e.getMessage());
		}
	}

	@Override
	public List<DirectionReport> findFilterSort(Timestamp after, Timestamp before, Predicate<DirectionReport> p,
			Comparator<DirectionReport> c) {
		
		List <DirectionReport> tariffs = findAllInPeriod(after, before);
		
		return tariffs.stream().filter(p).sorted(c).collect(Collectors.toList());
	}
	
	private List<DirectionReport> findAllInPeriod(Timestamp after, Timestamp before) {
		ArrayList<DirectionReport> reportRows = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(FITER_BY_PERIOD);) {
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DirectionReport r = new DirectionReport();
				r.setIndex(rs.getString(1));
				r.setValue(Double.parseDouble(rs.getString(2)));
				reportRows.add(r);
			}
		} catch (SQLException e) {
			logger.error("SQLException while Shipping findAllInPeriod. ", e.getMessage());
			throw new PageableListFindingException(e.getMessage());
		}
		return reportRows;
	}

}
