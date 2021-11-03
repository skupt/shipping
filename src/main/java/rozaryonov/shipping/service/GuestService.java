package rozaryonov.shipping.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import rozaryonov.shipping.exception.PersonNotFoundException;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Role;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.PersonRepository;
import rozaryonov.shipping.repository.TariffRepository;
import rozaryonov.shipping.repository.page.Page;
import rozaryonov.shipping.repository.page.PageableFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static rozaryonov.shipping.model.Role.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class GuestService {
	private final PageableFactory pageableFactory;
	private final PersonService personService;
	private final PersonRepository personRepository;



	@SuppressWarnings("unchecked")
	public String tariffs(HttpServletRequest request, HttpSession session) {
		Page<Tariff, TariffRepository> pageTariffArchive;
		List<Tariff> tariffArchiveList;
		String cmd = request.getParameter("cmd");
		if (cmd != null) {
			switch (cmd) {
			case "TariffArchivePrev":
				pageTariffArchive = (Page<Tariff, TariffRepository>) session.getAttribute("pageTariffArchive");
				tariffArchiveList = pageTariffArchive.prevPage();
				session.setAttribute("pageNum", pageTariffArchive.getCurPageNum());
				session.setAttribute("tariffArchiveList", tariffArchiveList);
				break;
			case "TariffArchiveNext":
				pageTariffArchive = (Page<Tariff, TariffRepository>) session.getAttribute("pageTariffArchive");
				tariffArchiveList = pageTariffArchive.nextPage();
				session.setAttribute("pageNum", pageTariffArchive.getCurPageNum());
				session.setAttribute("tariffArchiveList", tariffArchiveList);
				break;
			case "TariffArchiveApply":
				String sort = request.getParameter("sorting");
				int filter = Integer.parseInt(request.getParameter("logConf"));
				// comparator creation
				Comparator<Tariff> c;
				switch (sort) {
				case "incr":
					c = Comparator.comparing((Tariff t) -> t.getCreationTimestamp());
					break;
				case "decr":
					c = Comparator.comparing((Tariff t) -> t.getCreationTimestamp()).reversed();
					break;
				default:
					c = Comparator.comparing((Tariff t) -> t.getCreationTimestamp());
					break;
				}
				// Predicate creation
				Predicate<Tariff> p = (Tariff t) -> t.getLogisticConfig().getId() == filter;
				pageTariffArchive = pageableFactory.getPageableForTariffArchive(6, c, p);
				session.setAttribute("pageTariffArchive", pageTariffArchive);
				tariffArchiveList = pageTariffArchive.nextPage();
				session.setAttribute("pageNum", pageTariffArchive.getCurPageNum());
				session.setAttribute("tariffArchiveList", tariffArchiveList);
				break;
			}
		} else {
			pageTariffArchive = pageableFactory.getPageableForTariffArchive(6, null, null);
			session.setAttribute("pageTariffArchive", pageTariffArchive);
			tariffArchiveList = pageTariffArchive.nextPage();
			session.setAttribute("pageNum", pageTariffArchive.getCurPageNum());
			session.setAttribute("tariffArchiveList", tariffArchiveList);
		}

		return "/tariffs";
	}

	public String enterCabinet(Model model, Principal principal, HttpSession session) {
		Person person = personRepository.findByLogin((principal.getName())).orElseThrow(()-> new PersonNotFoundException(principal.getName()));
		String page;
		switch (person.getRole()) {
			case ROLE_USER :
				model.addAttribute("balance", personService.calcAndReplaceBalance(person.getId()));
				model.addAttribute("person", person);
				session.setAttribute("person", person);
				page = "redirect:/auth_user/cabinet";
				break;
			case ROLE_MANAGER :
				model.addAttribute("person", person);
				session.setAttribute("person", person);
				page = "redirect:/manager/cabinet";
				break;
			default:
				page = "/";
				break;
			}
		return page;
	}

	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

}
