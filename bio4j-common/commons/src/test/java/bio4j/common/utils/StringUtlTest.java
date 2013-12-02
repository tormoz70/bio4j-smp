package bio4j.common.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StringUtlTest {

	@Test
	public void appendStr() {
		String line = null;
		line = StringUtl.append(line, "qwe1", "|");
		Assert.assertEquals(line, "qwe1");
		line = StringUtl.append(line, "qwe2", "|");
		Assert.assertEquals(line, "qwe1|qwe2");
		line = StringUtl.append(line, "", "|");
		Assert.assertEquals(line, "qwe1|qwe2|");
		line = StringUtl.append(line, null, "|");
		Assert.assertEquals(line, "qwe1|qwe2||");
		line = StringUtl.append(line, "asd", "|");
		Assert.assertEquals(line, "qwe1|qwe2|||asd");
	}

	@Test
	public void compareStrings() {
		Assert.assertEquals(StringUtl.compare(null, null, false), true);
		Assert.assertEquals(StringUtl.compare("", null, false), false);
		Assert.assertEquals(StringUtl.compare(null, "", false), false);
		Assert.assertEquals(StringUtl.compare("asd", "asd", false), true);
		Assert.assertEquals(StringUtl.compare("asd", "ASD", false), false);
		Assert.assertEquals(StringUtl.compare("asd", "ASD", true), true);
	}

	@Test
	public void split() {
		//System.out.println(String.format("UPPER(%%%s%%)", "FIELD1"));
		//System.out.println(String.format("UPPER(%s) LIKE UPPER('\u0025'||:%s||'\u0025')", "FIELD1", "test"));
		//System.out.println(String.format("%s IS NULL", "fff1", "fff2"));
		String[] strs = StringUtl.split("qwe,asd,zxc", ",");
		Assert.assertEquals(strs[0], "qwe");
		Assert.assertEquals(strs[1], "asd");
		Assert.assertEquals(strs[2], "zxc");
	}

	@Test
	public void isNullOrEmpty() {
		Assert.assertEquals(StringUtl.isNullOrEmpty(null), true);
		Assert.assertEquals(StringUtl.isNullOrEmpty(""), true);
		Assert.assertEquals(StringUtl.isNullOrEmpty("qwe"), false);
	}
}
