package rozaryonov.shipping.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.exception.DaoException;
import rozaryonov.shipping.model.Role;
import rozaryonov.shipping.repository.RoleRepository;
import rozaryonov.shipping.service.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;
	
	@Override
	public Role findById(Long id) {
		return roleRepository.findById(id).orElseThrow(()-> new DaoException("No Role with id:" + id));
	}

	@Override
	public Iterable<Role> findAll() {
		return roleRepository.findAll();
	}

}
