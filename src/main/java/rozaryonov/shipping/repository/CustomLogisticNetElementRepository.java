package rozaryonov.shipping.repository;


import rozaryonov.shipping.model.LogisticNetElement;

public interface CustomLogisticNetElementRepository {
	public Iterable<LogisticNetElement> findByNetConfig(long netConfigId);
}
