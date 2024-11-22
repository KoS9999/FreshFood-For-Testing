package utevn.ff.controller.admin;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import utevn.ff.entities.User;
import utevn.ff.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class IndexAdminController{
	
	@Autowired
	UserRepository userRepository;
	
	@ModelAttribute(value = "user")
	public User user(Model model, Principal principal, User user) {

		if (principal != null) {
			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}

		return user;
	}

	@GetMapping(value = "/home")
	public String index() {
		
		return "admin/index";
	}
}
