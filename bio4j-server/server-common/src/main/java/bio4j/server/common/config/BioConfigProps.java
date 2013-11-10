package bio4j.server.common.config;

import java.lang.annotation.*;

@Target(value=ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface BioConfigProps {
     String path();
}