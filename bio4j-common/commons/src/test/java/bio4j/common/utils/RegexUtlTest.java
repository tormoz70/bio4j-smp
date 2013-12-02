package bio4j.common.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Matcher;

/**
 * Created with IntelliJ IDEA.
 * User: ayrat
 * Date: 29.11.13
 * Time: 16:26
 * To change this template use File | Settings | File Templates.
 */
public class RegexUtlTest {
    @Test
    public void testMatch() throws Exception {
        Matcher m = RegexUtl.match("select :w1, :w2, :w3 from dual", "(?<=:)\\b[\\w\\#\\$]+", true);
        StringBuilder paramsList = new StringBuilder();
        while(m.find())
            paramsList.append(m.group()+";");
        Assert.assertEquals(paramsList.toString(), "w1;w2;w3;");

    }

    @Test
    public void testFind() throws Exception {

    }

    @Test
    public void testPos() throws Exception {

    }

    @Test
    public void testReplace() throws Exception {
        String sql = "select 'sdf' as f1 from dual";
        sql = RegexUtl.replace(sql, "(['])(.*?)\\1", "", true);
        System.out.println(sql);
        Assert.assertEquals(sql, "select  as f1 from dual");
    }
}
