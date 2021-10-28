package rozaryonov.shipping.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rozaryonov.shipping.repository.impl.CustomLogisticNetElementRepositoryImpl;
import rozaryonov.shipping.exception.ConnectionGettingException;
import rozaryonov.shipping.exception.LogisticNetNotFoundException;
import rozaryonov.shipping.model.LogisticNetElement;
import rozaryonov.shipping.repository.LogisticNetElementRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
@Slf4j
@Service
@RequiredArgsConstructor
public class LogisticNetElementServiceImpl {

	private final LogisticNetElementRepository logisticNetElementRepository;
//	private final LocalityServiceImpl localityService;
//	private final DataSource dataSource;

	public LogisticNetElement findById(Long id) {
		return logisticNetElementRepository.findById(id)
				.orElseThrow(() -> new LogisticNetNotFoundException("No LogisticNetElement with id:" + id));
	}

	public Iterable<LogisticNetElement> findByNetConfig(Long netConfigId) {
		return logisticNetElementRepository.findByNetConfig(netConfigId);
	}

//	public Iterable<LogisticNetElement> findByNetConfig(Long netConfigId) {
//		Connection connection = null;
//		try {
//			connection = dataSource.getConnection();
//		} catch (SQLException e) {
//			log.error(e.getMessage());
//			throw new ConnectionGettingException(e.getMessage());
//		}
//		CustomLogisticNetElementRepositoryImpl lDao = new CustomLogisticNetElementRepositoryImpl(connection, localityService);
//
//		return lDao.findByNetConfig(netConfigId);
//
//	}

}
