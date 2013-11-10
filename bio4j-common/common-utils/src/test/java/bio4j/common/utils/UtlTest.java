package bio4j.common.utils;

import java.util.ArrayList;

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
}
