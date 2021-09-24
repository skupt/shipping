package rozaryonov.shipping.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.model.Shipping;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.LocalityRepository;
import rozaryonov.shipping.repository.SettlementsRepository;
import rozaryonov.shipping.repository.ShippingRepository;
import rozaryonov.shipping.repository.TariffRepository;
import rozaryonov.shipping.service.LocalityService;
import rozaryonov.shipping.service.SettlementsService;
import rozaryonov.shipping.service.ShippingService;
import rozaryonov.shipping.service.TariffService;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

	private final TariffRepository tariffRepository;
	
	@Override
	public Tariff findById(Long id) {
		return tariffRepository.findById(id).orElseThrow(()-> new DaoException("No Tariff with id:" + id));
	}

	@Override
	public Iterable<Tariff> findAll() {
		return tariffRepository.findAll();
	}

}
