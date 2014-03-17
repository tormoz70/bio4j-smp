package ru.bio4j.smp.common.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Matcher;

public class UtlTest {

    @Test
    public void getClassNamesFromPackageTest() {
    //	  ArrayList<String> clss = getClassNamesFromPackage("");
    //	  Assert.
    }

    @Test
    public void findAnnotationTest() {
        AnnotationTest annot = Utl.findAnnotation(AnnotationTest.class, AnnotetedClass.class);
        if(annot != null)
            Assert.assertEquals(annot.path(), "/test_path");
        else
            Assert.fail();
    }

    @Test
    public void typesIsSameTest() {
        Assert.assertTrue(Utl.typesIsSame(DateTimeParser.class, DateTimeParser.class));
    }

    @Test
    public void buildBeanStateInfoTest() {
        TBox box = new TBox();
        String rslt = "  ru.bio4j.smp.common.utils.TBox {\n" +
                "   - name : null;\n" +
                "   - created : null;\n" +
                "   - volume : null;\n" +
                "   - packets : null;\n" +
                "   - ex : null;\n" +
                "  }";
        String info = Utl.buildBeanStateInfo(box, null, "  ");
        System.out.println(info);
        Assert.assertEquals(info, rslt);
    }

    @Test(enabled = false)
    public void regexFindTest() {
        String txt = "ORA-20001: Не верное имя или пароль пользователя!\n" +
                "ORA-06512: на  \"GIVCAPI.GACC\", line 316\n" +
                "ORA-06512: на  \"GIVCAPI.GACC\", line 331\n" +
                "ORA-06512: на  line 1";
        Matcher m = RegexUtl.match(txt, "(?<=ORA-2\\d{4}:).+(?=\\nORA-\\d{5}:)", true, true, true);
        String fnd = m.group();
        System.out.println(fnd);
    }

}
