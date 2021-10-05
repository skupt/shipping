package rozaryonov.shipping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import rozaryonov.shipping.repository.page.DayReportRepo;
import rozaryonov.shipping.repository.page.Page;
import rozaryonov.shipping.repository.page.PageableFactory;
import rozaryonov.shipping.repository.reportable.DayReport;

@SpringBootTest
class DayReportRepoTest {
	@Autowired
	private PageableFactory pageableFactory;
	
	
	@Test
	void testNextPage() {
		Page<DayReport, DayReportRepo> page = pageableFactory.getPageableForManagerDayReport(2);
		List<DayReport> lr = page.nextPage();
		
		StringBuilder sb = new StringBuilder();
		for (DayReport r : lr) {
			sb.append(r.toString()).append("\n");
		}
		
		String expected = "2021-08-23	1345.96\n" + 
				"2021-09-11	378.0\n";
		
		assertEquals(expected, sb.toString());
		
	}

	@Test
	void testNextPage2() {
		Page<DayReport, DayReportRepo> page = pageableFactory.getPageableForManagerDayReport(2);
		List<DayReport> lr = page.nextPage();
		lr = page.nextPage();
		
		StringBuilder sb = new StringBuilder();
		for (DayReport r : lr) {
			sb.append(r.toString()).append("\n");
		}
		
		String expected = "2021-09-16	550.2\n" + 
				"2021-09-21	1807.96\n";
		
		assertEquals(expected, sb.toString());
		
	}
	
	@Test
	void testPrevPage() {
		Page<DayReport, DayReportRepo> page = pageableFactory.getPageableForManagerDayReport(2);
		List<DayReport> lr = page.nextPage();
		lr = page.nextPage();
		lr = page.prevPage();
		
		StringBuilder sb = new StringBuilder();
		for (DayReport r : lr) {
			sb.append(r.toString()).append("\n");
		}
		
		String expected = "2021-08-23	1345.96\n" + 
				"2021-09-11	378.0\n";
		
		assertEquals(expected, sb.toString());
		
	}
	
	@Test
	void testOther() {
		Page<DayReport, DayReportRepo> page = pageableFactory.getPageableForManagerDayReport(2);
		page.nextPage();
		int curPage = page.getCurPageNum();
		int pageTotal = page.getTotalPages();
		
		assertEquals(1, curPage);
		assertEquals(2, pageTotal);
		
		
	}
	
	
	


}
