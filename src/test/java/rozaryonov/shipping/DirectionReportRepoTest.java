package rozaryonov.shipping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import rozaryonov.shipping.repository.page.DayReportRepo;
import rozaryonov.shipping.repository.page.DirectionReportRepo;
import rozaryonov.shipping.repository.page.Page;
import rozaryonov.shipping.repository.page.PageableFactory;
import rozaryonov.shipping.repository.reportable.DayReport;
import rozaryonov.shipping.repository.reportable.DirectionReport;

@SpringBootTest
class DirectionReportRepoTest {
	@Autowired
	private PageableFactory pageableFactory;
	
	
	@Test
	void testNextPage() {
		Page<DirectionReport, DirectionReportRepo> page = pageableFactory.getPageableForManagerDirectionReport(100);
		List<DirectionReport> lr = page.nextPage();
		
		StringBuilder sb = new StringBuilder();
		for (DirectionReport r : lr) {
			sb.append(r.toString()).append("\n");
		}
		
		String expected = "Киев -> Киев\t466.1\n" +
				"Луцк -> Запорожье\t400.0\n" +
				"Луцк -> Киев\t2131.2\n" +
				"Львов -> Житомир\t2691.92\n" +
				"Львов -> Кропивницкий\t462.0\n" +
				"Ровно -> Днепр\t150.2\n" +
				"Ровно -> Луцк\t43.62\n" +
				"Ровно -> Ровно\t1078.7\n" +
				"Ужгород -> Киев\t1840.14\n" +
				"Ужгород -> Хмельницкий\t3796.8\n" +
				"Ужгород -> Черкассы\t378.0\n";
		
		assertEquals(expected, sb.toString());
		
	}

	@Test
	void testOther() {
		Page<DayReport, DayReportRepo> page = pageableFactory.getPageableForManagerDayReport(100);
		page.nextPage();
		int curPage = page.getCurPageNum();
		int pageTotal = page.getTotalPages();
		
		assertEquals(1, curPage);
		assertEquals(1, pageTotal);
		
		
	}
	
	
	


}
