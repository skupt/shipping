package rozaryonov.shipping.service;

import rozaryonov.shipping.model.Role;

public interface RoleService {
	Role findById(Long id);
	Iterable<Role> findAll(); 
}
