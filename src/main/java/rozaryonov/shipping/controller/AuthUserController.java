package rozaryonov.shipping.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.service.PersonService;
import rozaryonov.shipping.service.RoleService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth_user")
public class AuthUserController {
	
	@GetMapping("/cabinet")
	public String cabinet() {
		return "/auth_user/cabinet"; 
	}
	
	@GetMapping("/shippings_new")
	public String newShipping(HttpSession session) {
		System.out.println("* inside newShipping");
		Person person = (Person) session.getAttribute("person");
		if (person == null) return "redirect:/new";
		return "/auth_user/shippings_new";
	}
	
	@PostMapping("/auth_user/shippings")
	public String createShipping() {
		System.out.println("* inside createShipping");
		return "redirect:/auth_user";
	}
}
