package rozaryonov.shipping.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import rozaryonov.shipping.exception.GuestSerivceException;
import rozaryonov.shipping.model.Locality;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.page.Page;
import rozaryonov.shipping.repository.page.PageableFactory;
import rozaryonov.shipping.service.*;
import rozaryonov.shipping.utils.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestServiceImpl{

	private final PropertyService propertyService;
	private final LogisticNetElementService logisticNetElementService;
	private final TariffService tariffService;
	private final LocalityService localityService;
	private final PageableFactory pageableFactory;
	private final PersonServiceImpl personService;


	@SuppressWarnings("unchecked")
	public String tariffs(HttpServletRequest request, HttpSession session) {
		Page<Tariff, TariffService> pageTariffArchive;
		List<Tariff> tariffArchiveList;
		String cmd = request.getParameter("cmd");
		if (cmd != null) {
			switch (cmd) {
			case "TariffArchivePrev":
				pageTariffArchive = (Page<Tariff, TariffService>) session.getAttribute("pageTariffArchive");
				tariffArchiveList = pageTariffArchive.prevPage();
				session.setAttribute("pageNum", pageTariffArchive.getCurPageNum());
				session.setAttribute("tariffArchiveList", tariffArchiveList);
				break;
			case "TariffArchiveNext":
				pageTariffArchive = (Page<Tariff, TariffService>) session.getAttribute("pageTariffArchive");
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
		Person person = personService.findByLogin((principal.getName()));// todo here we get Optional
		String page;// todo don't left gray code
		if (person != null) {// todo use Optional
			switch (person.getRole().getName()) {
			case "ROLE_USER":
				model.addAttribute("balance", personService.calcAndReplaceBalance(person.getId()));
				model.addAttribute("person", person);
				session.setAttribute("person", person);
				page = "redirect:/auth_user/cabinet";
				break;
			case "ROLE_MANAGER":
				model.addAttribute("person", person);
				session.setAttribute("person", person);
				page = "redirect:/manager/cabinet";
				break;
			default:
				page = "/";
				break;
			}
		} else {
			page = "/";
		}
		return page;
	}

	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

}
