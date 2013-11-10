package bio4j.common.utils;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.testng.annotations.Test;

public class DateUtlTest {

	@Test
	public void parse() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2012, (12-1), 20, 15, 11, 50);
		System.out.println("calendar.getTime(): "+calendar.getTime());
		Date testDate = DateUtl.parse("2012.12.20-15:11:50", "yyyy.MM.dd-HH:mm:ss");
		Assert.assertEquals(calendar.getTime(), testDate);
	}
}
