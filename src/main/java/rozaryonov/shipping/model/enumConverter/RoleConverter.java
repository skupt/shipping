package rozaryonov.shipping.model.enumConverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import rozaryonov.shipping.model.Role;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Long> {

	@Override
	public Long convertToDatabaseColumn(Role role) {
		if (role == null) return null;
		return role.getId();
	}

	@Override
	public Role convertToEntityAttribute(Long id) {
		if (id == null) return null;
		
		return Role.findById(id);
	}

}
