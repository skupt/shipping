package rozaryonov.shipping.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.dto.SettlementsDto;
import rozaryonov.shipping.dto.ShippingToFinishDto;
import rozaryonov.shipping.repository.page.DayReportRepo;
import rozaryonov.shipping.repository.page.DirectionReportRepo;
import rozaryonov.shipping.repository.reportable.DayReport;
import rozaryonov.shipping.repository.reportable.DirectionReport;
import rozaryonov.shipping.service.ManagerService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/")

//todo in parallel learn Spring and its basics
public class ManagerController {
	private static Logger logger = LogManager.getLogger();
	
	private final ManagerService managerService;
	private final rozaryonov.shipping.repository.page.PageableFactory pageableFactory;

	@GetMapping("/cabinet")
	public String cabinet (HttpSession session) {
		//if (!managerService.isManager(session)) return "redirect:/";
		return "/manager/cabinet"; 
	}
	
	@GetMapping("/prg")
	public String getPrg() {
		return "/manager/prg"; 
	}

	
	@GetMapping("/payments")
	public String paymentsShow (@ModelAttribute("settlements") SettlementsDto settlements, HttpSession session, HttpServletRequest request) {
		//if (!managerService.isManager(session)) return "redirect:/";
		return managerService.paymentsShow(settlements, session, request);
	}
	
	@PostMapping("/payments")
	public String paymentsCreate (@ModelAttribute ("settlements") @Valid SettlementsDto settlements, 
			BindingResult bindingResult, HttpServletRequest request, HttpSession session) {
		//if (!managerService.isManager(session)) return "redirect:/";
		return managerService.paymentsCreate(settlements, bindingResult, request, session);
	}

	@GetMapping("/create_invoices")
	public String showCreateInvoicesForm (HttpSession session, HttpServletRequest request) {
		//if (!managerService.isManager(session)) return "redirect:/";
		return managerService.showCreateInvoicesForm (session, request);
	}
	
	@PostMapping("/create_invoices")
	public String createInvoices(HttpServletRequest request) {
		return managerService.createInvoices(request);
	}
	
	@GetMapping("/finish_shippings")
	public String showFinishShippingsForm (@ModelAttribute("shippingDto") ShippingToFinishDto shippingDto, HttpSession session, HttpServletRequest request) {
		//if (!managerService.isManager(session)) return "redirect:/";
		return managerService.showFinishShippingsForm (shippingDto, session, request);
	}
	
	@PostMapping("/finish_shippings")
	public String finishShippings(@ModelAttribute("shippingDto") @Valid ShippingToFinishDto shippingDto, BindingResult bindingResult, HttpServletRequest request, HttpSession session) {
		//if (!managerService.isManager(session)) return "redirect:/";
		return managerService.finishShippings(shippingDto, bindingResult, request, session);
	}
	
	@GetMapping("/report_day")
	public String reportDay(HttpSession session, HttpServletRequest request) {
		//if (!managerService.isManager(session)) return "redirect:/";
		rozaryonov.shipping.repository.page.Page<DayReport, DayReportRepo> pageDayReport = null;
		List<DayReport> reportDayList = null;
		String cmd = request.getParameter("cmd");
		if (cmd != null) {
			switch (cmd) {
			case "prevPage":
				pageDayReport = (rozaryonov.shipping.repository.page.Page<DayReport, DayReportRepo>) session.getAttribute("pageDayReport");
				reportDayList  = pageDayReport.prevPage();
				session.setAttribute("pageDayReport", pageDayReport);
				session.setAttribute("reportDayList", reportDayList);
				break;
			case "nextPage":
				pageDayReport = (rozaryonov.shipping.repository.page.Page<DayReport, DayReportRepo>) session.getAttribute("pageDayReport");
				reportDayList  = pageDayReport.nextPage();
				session.setAttribute("pageDayReport", pageDayReport);
				session.setAttribute("reportDayList", reportDayList);
				break;
			}
		} else {
			pageDayReport = pageableFactory.getPageableForManagerDayReport(3);
			reportDayList = pageDayReport.nextPage();
			session.setAttribute("pageDayReport", pageDayReport);
			session.setAttribute("reportDayList", reportDayList);
		}
		
		return "/manager/report_day";
	}
	
	@GetMapping("/report_direction")
	public String reportDirection(HttpSession session, HttpServletRequest request) {
		//if (!managerService.isManager(session)) return "redirect:/";
		rozaryonov.shipping.repository.page.Page<DirectionReport, DirectionReportRepo> pageDirectionReport = null;
		List<DirectionReport> reportDirectionList = null;
		String cmd = request.getParameter("cmd");
		if (cmd != null) {
			switch (cmd) {
			case "prevPage":
				pageDirectionReport = (rozaryonov.shipping.repository.page.Page<DirectionReport, DirectionReportRepo>) session.getAttribute("pageDirectionReport");
				reportDirectionList  = pageDirectionReport.prevPage();
				session.setAttribute("pageDirectionReport", pageDirectionReport);
				session.setAttribute("reportDirectionList", reportDirectionList);
				break;
			case "nextPage":
				pageDirectionReport = (rozaryonov.shipping.repository.page.Page<DirectionReport, DirectionReportRepo>) session.getAttribute("pageDirectionReport");
				reportDirectionList  = pageDirectionReport.nextPage();
				session.setAttribute("pageDirectionReport", pageDirectionReport);
				session.setAttribute("reportDirectionList", reportDirectionList);
				break;
			}
		} else {
			pageDirectionReport = pageableFactory.getPageableForManagerDirectionReport(3);
			reportDirectionList = pageDirectionReport.nextPage();
			session.setAttribute("pageDirectionReport", pageDirectionReport);
			session.setAttribute("reportDirectionList", reportDirectionList);
		}
		
		return "/manager/report_direction";
	}
	
	
}
