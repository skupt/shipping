package rozaryonov.shipping.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rozaryonov.shipping.model.Person;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final PersonServiceImpl personService;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Person person = personService.findByLogin(username);
		if (person != null) {
			System.out.println("Found user: " + person);
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
			grantedAuthorities.add(new SimpleGrantedAuthority(person.getRole().getName()));
			System.out.println(person.getLogin() + " - " + person.getRole().getName());
			return (UserDetails) new User(person.getLogin(), person.getPassword(), grantedAuthorities);
		}
		throw new UsernameNotFoundException("User with login=" + username + " hasn't been found");
	}
	
	

}
