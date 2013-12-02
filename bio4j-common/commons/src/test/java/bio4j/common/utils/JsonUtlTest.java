package bio4j.common.utils;

import junit.framework.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class JsonUtlTest {

	private final TBox testBox = new TBox();

	@BeforeClass
	private void setUp() {
		this.testBox.setName("Test-Box");
		this.testBox.setCreated(DateUtl.parse("2012.12.20-15:11:24", "yyyy.MM.dd-HH:mm:ss"));
		this.testBox.setVolume(123.05);
		this.testBox.setPackets(new TPacket[] { new TPacket() });
		this.testBox.getPackets()[0].setName("packet-0");
		this.testBox.getPackets()[0].setVolume(100.10);
		this.testBox.getPackets()[0].setApples(new TApple[] { new TApple(), new TApple() });
		this.testBox.getPackets()[0].getApples()[0].setName("apple-0-0");
		this.testBox.getPackets()[0].getApples()[0].setWheight(10.100);
		this.testBox.getPackets()[0].getApples()[1].setName("apple-0-1");
		this.testBox.getPackets()[0].getApples()[1].setWheight(10.200);
		this.testBox.setEx(new Exception("FTW TestException"));
	}

	@Test(enabled = false)
	public void a_encode() {
		String expected =
		 "{\"class\":\"bio4j.common.utils.TBox\",\"created\":\"2012.12.20-15:11:24\",\"ex\":{\"class\":\"java.lang.Exception\",\"message\":\"FTW TestException\",\"stackTrace\":[{\"class\":\"java.lang.StackTraceElement\",\"className\":\"bio4j.common.utils.JsonUtlTest\",\"fileName\":\"JsonUtlTest.java\",\"lineNumber\":25,\"methodName\":\"setUp\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"sun.reflect.NativeMethodAccessorImpl\",\"fileName\":\"NativeMethodAccessorImpl.java\",\"lineNumber\":-2,\"methodName\":\"invoke0\",\"nativeMethod\":true},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"sun.reflect.NativeMethodAccessorImpl\",\"fileName\":\"NativeMethodAccessorImpl.java\",\"lineNumber\":57,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"sun.reflect.DelegatingMethodAccessorImpl\",\"fileName\":\"DelegatingMethodAccessorImpl.java\",\"lineNumber\":43,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"java.lang.reflect.Method\",\"fileName\":\"Method.java\",\"lineNumber\":601,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.internal.MethodInvocationHelper\",\"fileName\":\"MethodInvocationHelper.java\",\"lineNumber\":80,\"methodName\":\"invokeMethod\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.internal.Invoker\",\"fileName\":\"Invoker.java\",\"lineNumber\":564,\"methodName\":\"invokeConfigurationMethod\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.internal.Invoker\",\"fileName\":\"Invoker.java\",\"lineNumber\":213,\"methodName\":\"invokeConfigurations\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.internal.Invoker\",\"fileName\":\"Invoker.java\",\"lineNumber\":138,\"methodName\":\"invokeConfigurations\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.internal.TestMethodWorker\",\"fileName\":\"TestMethodWorker.java\",\"lineNumber\":175,\"methodName\":\"invokeBeforeClassMethods\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.internal.TestMethodWorker\",\"fileName\":\"TestMethodWorker.java\",\"lineNumber\":107,\"methodName\":\"run\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.TestRunner\",\"fileName\":\"TestRunner.java\",\"lineNumber\":767,\"methodName\":\"privateRun\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.TestRunner\",\"fileName\":\"TestRunner.java\",\"lineNumber\":617,\"methodName\":\"run\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.SuiteRunner\",\"fileName\":\"SuiteRunner.java\",\"lineNumber\":334,\"methodName\":\"runTest\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.SuiteRunner\",\"fileName\":\"SuiteRunner.java\",\"lineNumber\":329,\"methodName\":\"runSequentially\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.SuiteRunner\",\"fileName\":\"SuiteRunner.java\",\"lineNumber\":291,\"methodName\":\"privateRun\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.SuiteRunner\",\"fileName\":\"SuiteRunner.java\",\"lineNumber\":240,\"methodName\":\"run\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.SuiteRunnerWorker\",\"fileName\":\"SuiteRunnerWorker.java\",\"lineNumber\":52,\"methodName\":\"runSuite\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.SuiteRunnerWorker\",\"fileName\":\"SuiteRunnerWorker.java\",\"lineNumber\":86,\"methodName\":\"run\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.TestNG\",\"fileName\":\"TestNG.java\",\"lineNumber\":1203,\"methodName\":\"runSuitesSequentially\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.TestNG\",\"fileName\":\"TestNG.java\",\"lineNumber\":1128,\"methodName\":\"runSuitesLocally\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.TestNG\",\"fileName\":\"TestNG.java\",\"lineNumber\":1036,\"methodName\":\"run\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.remote.RemoteTestNG\",\"fileName\":\"RemoteTestNG.java\",\"lineNumber\":111,\"methodName\":\"run\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.remote.RemoteTestNG\",\"fileName\":\"RemoteTestNG.java\",\"lineNumber\":204,\"methodName\":\"initAndRun\",\"nativeMethod\":false},{\"class\":\"java.lang.StackTraceElement\",\"className\":\"org.testng.remote.RemoteTestNG\",\"fileName\":\"RemoteTestNG.java\",\"lineNumber\":175,\"methodName\":\"main\",\"nativeMethod\":false}]},\"name\":\"Test-Box\",\"packets\":[{\"apples\":[{\"class\":\"bio4j.common.utils.TApple\",\"name\":\"apple-0-0\",\"wheight\":10.1},{\"class\":\"bio4j.common.utils.TApple\",\"name\":\"apple-0-1\",\"wheight\":10.2}],\"class\":\"bio4j.common.utils.TPacket\",\"name\":\"packet-0\",\"volume\":100.1}],\"volume\":123.05}";
		String testJson = JsonUtl.encode(this.testBox);
		System.out.println(testJson);
		Assert.assertEquals(expected, testJson);
	}

	@Test(enabled = false)
	public void b_decode() {
		String testJson = JsonUtl.encode(this.testBox);
		TBox restored = (TBox) JsonUtl.decode(testJson);
		System.out.println("restored: " + restored);
		Assert.assertEquals(this.testBox.getName(), restored.getName());
		Assert.assertEquals(this.testBox.getCreated(), restored.getCreated());
		Assert.assertEquals(this.testBox.getVolume(), restored.getVolume());
		Assert.assertEquals(this.testBox.getPackets()[0].getName(), restored.getPackets()[0].getName());
		Assert.assertEquals(this.testBox.getPackets()[0].getVolume(), restored.getPackets()[0].getVolume());
		Assert.assertEquals(this.testBox.getPackets()[0].getApples()[0].getName(), restored.getPackets()[0].getApples()[0].getName());
		Assert.assertEquals(this.testBox.getPackets()[0].getApples()[0].getWheight(), restored.getPackets()[0].getApples()[0].getWheight());
		Assert.assertEquals(this.testBox.getPackets()[0].getApples()[1].getName(), restored.getPackets()[0].getApples()[1].getName());
		Assert.assertEquals(this.testBox.getPackets()[0].getApples()[1].getWheight(), restored.getPackets()[0].getApples()[1].getWheight());
	}

}
