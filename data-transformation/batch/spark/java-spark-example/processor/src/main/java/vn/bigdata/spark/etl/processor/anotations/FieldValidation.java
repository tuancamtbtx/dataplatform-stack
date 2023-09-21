package vn.bigdata.spark.etl.processor.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldValidation {
    String fieldName();
    boolean isValidate();
    boolean isRequired() default false;

}
