package rozaryonov.shipping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rozaryonov.shipping.exception.PersonNotFoundException;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.repository.PersonRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	private final PersonRepository personRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Person person = personRepository.findByLogin(username).orElseThrow(()-> new PersonNotFoundException(username));
		if (person != null) {
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
			grantedAuthorities.add(new SimpleGrantedAuthority(person.getRole().toString()));
			return (UserDetails) new User(person.getLogin(), person.getPassword(), grantedAuthorities);
		}
		throw new UsernameNotFoundException("User with login=" + username + " hasn't been found");
	}
	
	

}
