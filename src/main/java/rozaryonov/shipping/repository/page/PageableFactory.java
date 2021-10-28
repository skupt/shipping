package rozaryonov.shipping.repository.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rozaryonov.shipping.model.Invoice;
import rozaryonov.shipping.model.Person;
import rozaryonov.shipping.model.Settlements;
import rozaryonov.shipping.model.Tariff;
import rozaryonov.shipping.repository.InvoiceRepository;
import rozaryonov.shipping.repository.SettlementsRepository;
import rozaryonov.shipping.repository.TariffRepository;
import rozaryonov.shipping.repository.reportable.DayReport;
import rozaryonov.shipping.repository.reportable.DirectionReport;

import java.util.Comparator;
import java.util.function.Predicate;
@Slf4j
@Component
@RequiredArgsConstructor
public class PageableFactory {
	private final TariffRepository tariffRepository;
	private final InvoiceRepository invoiceRepository;
	private final SettlementsRepository settlementsRepository;
	private final DayReportRepo dayReportRepo;
	private final DirectionReportRepo directionReportRepo;
	
	public Page<Settlements, SettlementsRepository> getPageableForManagerPaymentsPage (int rowsOnPage) {
		 Page<Settlements, SettlementsRepository> page = new Page<>(
				 settlementsRepository, 
				 Comparator.comparing((Settlements s) -> s.getCreationDatetime()));
		 page.setPredicat(e->e.getSettlementType().getName().equals("payment"));
		 page.setRowsOnPage(rowsOnPage);
		 page.init();

		 return page;
	}

	public Page<Invoice, InvoiceRepository> getPageableForUserSpendingPage (int rowsOnPage, Person person) {
		 Page<Invoice, InvoiceRepository> page = new Page<>(
				 invoiceRepository,
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
	public Page<Tariff, TariffRepository> getPageableForTariffArchive (int rowsOnPage, Comparator<Tariff> c, Predicate<Tariff> p) {
		 Page<Tariff, TariffRepository> page = null;
		 if (c == null) {
			 page = new Page<>(tariffRepository, Comparator.comparing((Tariff s) -> s.getCreationTimestamp()));
		 } else {
			 page = new Page<>(tariffRepository, c);
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
