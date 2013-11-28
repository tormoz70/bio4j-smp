package bio4j.common.utils;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

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
        String rslt = "  bio4j.common.utils.TBox {\n" +
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
}
