package bio4j.common.types;

import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ParamsTest {

//	private final Params testParams = new Params();

	@BeforeClass
	private void setUp() {
//		testParams.add("param1", 11);
//		testParams.add("param2", 22);
	}

	@Test(enabled = true)
	public void add() {
		Params testParams = new Params();
		Param newParam = testParams.add("param1", 111);
		Assert.assertNotNull(newParam);
		newParam = testParams.add("param1", 111, true);
		Assert.assertNotNull(newParam);
		Assert.assertEquals(111, testParams.getValueByName("param1", false));
		newParam = testParams.add("param3", 33);
		Assert.assertNotNull(newParam);
		Assert.assertNotNull(testParams.getParam("param3"));
		Assert.assertEquals(newParam, testParams.getParam("param3"));
		Assert.assertTrue(testParams.paramExists("param3"));
		Assert.assertFalse(testParams.paramExists("Param3"));
		Assert.assertTrue(testParams.paramExists("Param3", true));
	}

	@Test(enabled = false)
	public void addListStringObjectchar() {
//		Params prms = new Params();
//		prms.addList("p1,p2", new Object[]{11, 22}, ",");
	}

	@Test(enabled = false)
	public void addListStringObject() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void addListStringStringchar() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void addListStringString() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void buildUrlParams() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void buildUrlParamsString() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void containsStringObject() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void containsParam() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void containsParams() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void encode() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void first() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void getIndexOf() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void getInnerObjectByName() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void getNamesList() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void getParamStringBooleanBoolean() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void getParamStringBoolean() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void getParamString() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void getValsList() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void getValueAsStringByName() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void getValueByName() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void merge() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void paramExistsString() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void paramExistsStringBoolean() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void process() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void removeParam() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void removeString() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void removeListStringchar() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void removeListString() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void setListStringObjectchar() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void setListStringStringchar() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void setListStringString() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void setValue() {
		throw new RuntimeException("Test not implemented");
	}

	@Test(enabled = false)
	public void toMap() {
		throw new RuntimeException("Test not implemented");
	}
}
