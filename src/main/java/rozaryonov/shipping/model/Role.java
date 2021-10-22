package rozaryonov.shipping.model;

import java.util.stream.Stream;

import rozaryonov.shipping.exception.RoleNotFoundException;

public enum Role {
	ROLE_MANAGER (1, "ROLE_MANAGER"), ROLE_USER (2, "ROLE_USER");
	private Long id;
	private String name;
	
	private Role(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public static Role findById(long role_id) {
		return Stream.of(Role.values())
		.filter(r -> r.getId() == role_id)
		.findFirst()
		.orElseThrow(()-> new RoleNotFoundException("no Role with id = " + role_id));
	}
}
