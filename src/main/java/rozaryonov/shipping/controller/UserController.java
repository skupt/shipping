package rozaryonov.shipping.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.service.PersonService;
import rozaryonov.shipping.service.RoleService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth_user")
public class UserController {
	
	@GetMapping("/cabinet")
	public String cabinet() {
		return "/auth_user/cabinet"; 
	}

}
