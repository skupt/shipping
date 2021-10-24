package rozaryonov.shipping.service.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dao.LogisticNetElementDao;
import rozaryonov.shipping.exception.ConnectionGettingException;
import rozaryonov.shipping.exception.LogisticNetNotFoundException;
import rozaryonov.shipping.model.LogisticNetElement;
import rozaryonov.shipping.repository.LogisticNetElementRepository;
import rozaryonov.shipping.service.LocalityService;
import rozaryonov.shipping.service.LogisticNetElementService;

@Service
@RequiredArgsConstructor
public class LogisticNetElementServiceImpl implements LogisticNetElementService {
	private static Logger logger = LogManager.getLogger(LogisticNetElementServiceImpl.class.getName());

	private final LogisticNetElementRepository logisticNetElementRepository;
	private final LocalityService localityService;
	private final DataSource dataSource;

	@Override
	public LogisticNetElement findById(Long id) {
		return logisticNetElementRepository.findById(id)
				.orElseThrow(() -> new LogisticNetNotFoundException("No LogisticNetElement with id:" + id));
	}

	@Override
	public Iterable<LogisticNetElement> findAll() {
		return logisticNetElementRepository.findAll();
	}

	@Override
	public Iterable<LogisticNetElement> findByNetConfig(Long netConfigId) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new ConnectionGettingException(e.getMessage());
		}
		LogisticNetElementDao lDao = new LogisticNetElementDao(connection, localityService);

		return lDao.findByNetConfig(netConfigId);

	}

}
