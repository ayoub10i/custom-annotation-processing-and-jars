# custom annotation processing and jars
 Java core concepts for Java begginers - annotation processing and jar files
 ## a custom compile time annotation processor
 Creating and processing Java annotations at compile time involves a few steps. First, you need to define the annotation, then create an annotation processor, and finally configure the build system to use your processor during compilation.
In this example, I'll create a simple annotation @ToString that generates a toString method for a class at compile time. We'll then create an annotation processor that processes this annotation to generate the toString method.
```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)  // Source-level annotation
@Target(ElementType.TYPE)  // Can be applied to classes
public @interface ToString {
}

