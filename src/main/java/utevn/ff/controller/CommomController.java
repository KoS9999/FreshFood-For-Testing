package utevn.ff.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import utevn.ff.entities.Category;
import utevn.ff.entities.User;
import utevn.ff.repository.CategoryRepository;
import utevn.ff.repository.ProductRepository;
import utevn.ff.repository.UserRepository;

@Controller
public class CommomController {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;

	@ModelAttribute(value = "user")
	public User user(Model model, Principal principal, User user) {

		if (principal != null) {
			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}

		return user;
	}

	@ModelAttribute("categoryList")
	public List<Category> showCategory(Model model) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);

		return categoryList;
	}

}
