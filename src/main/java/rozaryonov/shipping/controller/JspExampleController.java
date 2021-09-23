package rozaryonov.shipping.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import rozaryonov.shipping.model.TestPerson;

@Controller
public class JspExampleController {

	private static List<TestPerson> persons = new ArrayList<>();

    static {
        persons.add(new TestPerson("Bill", "Gates"));
        persons.add(new TestPerson("Steve", "Jobs"));
    }

    @RequestMapping(value = { "/", "/testIndex" }, method = RequestMethod.GET)
    public String index(Model model) {

        String message = "Hello Spring Boot + JSP";

        model.addAttribute("message", message);

        return "testIndex";
    }

    @RequestMapping(value = { "/testPersonList" }, method = RequestMethod.GET)
    public String viewPersonList(Model model) {

        model.addAttribute("persons", persons);

        return "testPersonList";
    }

}
