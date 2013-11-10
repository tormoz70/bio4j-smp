package bio4j.server.common.handler;

import java.lang.annotation.*;

@Target(value=ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface RequestHandler {
	String mapping();
}