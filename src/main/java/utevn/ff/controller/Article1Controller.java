package utevn.ff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import utevn.ff.commom.CommomDataService;
import utevn.ff.entities.User;



@Controller
public class Article1Controller extends CommomController {

	@Autowired
	CommomDataService commomDataService;

	@GetMapping(value = "/article1")
	public String contact(Model model, User user) {

		commomDataService.commonData(model, user);
		return "web/article1";
	}
}
