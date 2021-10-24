package rozaryonov.shipping.repository.page;

import java.util.Comparator;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.reportable.DayReport;
import rozaryonov.shipping.repository.reportable.DirectionReport;
import rozaryonov.shipping.service.InvoiceService;
import rozaryonov.shipping.service.SettlementsService;
import rozaryonov.shipping.service.TariffService;

@Component
@RequiredArgsConstructor
public class PageableFactory {
	private static Logger logger = LogManager.getLogger(PageableFactory.class.getName());
	private final TariffService tariffService;
	private final InvoiceService invoiceService;
	private final SettlementsService settlementsService;
	private final DayReportRepo dayReportRepo;
	private final DirectionReportRepo directionReportRepo;
	
	public Page<Settlements, SettlementsService> getPageableForManagerPaymentsPage (int rowsOnPage) {
		 Page<Settlements, SettlementsService> page = new Page<>(
				 settlementsService, 
				 Comparator.comparing((Settlements s) -> s.getCreationDatetime()));
		 page.setPredicat(e->e.getSettlementType().getName().equals("payment"));
		 page.setRowsOnPage(rowsOnPage);
		 page.init();

		 return page;
	}

	public Page<Invoice, InvoiceService> getPageableForUserSpendingPage (int rowsOnPage, Person person) {
		 Page<Invoice, InvoiceService> page = new Page<>(
				 invoiceService, 
				 Comparator.comparing((Invoice s) -> s.getCreationDateTime()).reversed());
		 page.setPredicat((Invoice e)-> (e.getInvoiceStatus().getId()==1)&&(e.getPerson().getId()==person.getId()));
		 page.setRowsOnPage(rowsOnPage);
		 page.init();

		 return page;
	}
	/**
	 * 
	 * @param rowsOnPage - number of rows per each page
	 * @param c = Comparator for Tariff
	 * @param p = Predicate for Tariff
	 * @return Page <T, R extends Pageable<T>>
	 */
	public Page<Tariff, TariffService> getPageableForTariffArchive (int rowsOnPage, Comparator<Tariff> c, Predicate<Tariff> p) {
		 Page<Tariff, TariffService> page = null;
		 if (c == null) {
			 page = new Page<>(tariffService, Comparator.comparing((Tariff s) -> s.getCreationTimestamp()));
		 } else {
			 page = new Page<>(tariffService, c);
		 }
		 if (p == null) {
			 page.setPredicat((Tariff e)-> true);
		 } else {
			 page.setPredicat(p);
		 }
		 page.init();

		 return page;
	}
	
	public Page<DayReport, DayReportRepo> getPageableForManagerDayReport (int rowsOnPage) {
		 Page<DayReport, DayReportRepo> page = new Page<>(dayReportRepo, Comparator.comparing((DayReport r) -> r.getIndex()));
		 page.setRowsOnPage(rowsOnPage);
		 page.init();

		 return page;
	}

	public Page<DirectionReport, DirectionReportRepo> getPageableForManagerDirectionReport (int rowsOnPage) {
		 Page<DirectionReport, DirectionReportRepo> page = new Page<>(directionReportRepo, Comparator.comparing((DirectionReport r) -> r.getIndex()));
		 page.setRowsOnPage(rowsOnPage);
		 page.init();

		 return page;
	}

}
