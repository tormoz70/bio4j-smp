package ru.bio4j.smp.common.utils;

import java.lang.annotation.*;

@Target(value=ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface AnnotationTest {
     String path();
}