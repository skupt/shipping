package rozaryonov.shipping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.service.RoleService;

@Controller
@RequiredArgsConstructor
public class PersonController {
	
	private final RoleService roleService;
	
	@GetMapping ("/personList")
	public String showPersonList (Model model) {
		model.addAttribute("roleList", roleService.findAll());

		return "personList";
	}
}
