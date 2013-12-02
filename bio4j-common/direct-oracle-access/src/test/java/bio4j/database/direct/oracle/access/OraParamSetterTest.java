package bio4j.database.direct.oracle.access;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ayrat
 * Date: 02.12.13
 * Time: 21:09
 * To change this template use File | Settings | File Templates.
 */
public class OraParamSetterTest {
    @Test
    public void testExtractParamNamesFromSQL() throws Exception {
        List<String> params = OraParamSetter.extractParamNamesFromSQL("select user as curuser, :dummy as dummy_param, ':wer' as mak from dual");
        Assert.assertEquals(params.size(), 1);
        Assert.assertEquals(params.get(0), "dummy");
    }
}
