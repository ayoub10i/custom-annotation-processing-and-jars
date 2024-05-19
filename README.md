# custom annotation processing and jars
 Java core concepts for Java begginers - annotation processing and jar files
 ## a custom compile time annotation processor
 Creating and processing Java annotations at compile time involves a few steps. First, you need to define the annotation, then create an annotation processor, and finally configure the build system to use your processor during compilation.
In this example, I'll create a simple annotation @ToString that generates a toString method for a class at compile time. We'll then create an annotation processor that processes this annotation to generate the toString method.
```java
// omitted import statements...
@Retention(RetentionPolicy.SOURCE)  // Source-level annotation
@Target(ElementType.TYPE)  // Can be applied to classes
public @interface ToString {
}
```
Next, create an annotation processor to process the @ToString annotation and generate a toString method in the class. Annotation processors need to implement the AbstractProcessor class from the javax.annotation.processing package.

```java
//omitted import statements...

@SupportedAnnotationTypes("com.example.ToString")  // Your annotation type
@SupportedSourceVersion(SourceVersion.RELEASE_11)  // Supported Java version
public class ToStringProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<TypeElement> elements = ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(ToString.class));

        for (TypeElement typeElement : elements) {
            try {
                // Generate the toString method for the class
                JavaFileObject file = processingEnv.getFiler().createSourceFile(typeElement.getQualifiedName() + "Generated");
                try (Writer writer = file.openWriter()) {
                    writer.write("package " + processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName() + ";\n\n");
                    writer.write("public class " + typeElement.getSimpleName() + "Generated {\n");
                    writer.write("  @Override\n");
                    writer.write("  public String toString() {\n");
                    writer.write("    return \"" + typeElement.getSimpleName() + " instance\";\n");
                    writer.write("  }\n");
                    writer.write("}\n");
                }
            } catch (Exception e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Error processing @ToString: " + e.getMessage());
            }
        }

        return true;  // Indicates that we've handled the annotation
    }
}
```
Now that you have the annotation and annotation processor, let's use the @ToString annotation in a simple class:

```java
import com.example.ToString;

@ToString
public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

```
## build configuration 
if you use gradle as build automation tool , and you choose to have your annotation processor in a separate module,
you must create a file "META-INF/services/javax.annotation.processing.Processor" inside the resources folder of that module,and you write the name of your processor in that file , in this case "ToStringProcessor" and include this snippet of code in the build.gradle file in the main module that uses the annotation processor 
```groovy
dependencies {
    
    annotationProcessor project(':your processor's module')
    implementation project(':your processor's module')
}
```
##Javac
here however I used only javac to build the java files,I like experimenting and if you choose to run the build.bat 
file you will get to see what every command does , I included Javac commands and Jar commands:

```batch
javac ToStringProcessor.java
:: you must first compile the processor , this will compile 
::the annotation automatically
pause
jar cvf lib.jar ToStringProcessor.class ToString.class
::creates a jar file called lib.jar which includes the compiled
::classes, obviously you can use the compiled classes without including them in a jar file , just run : javac -processor your_processor your_annotated_class.java
del *.class
pause
javac -cp lib.jar -processor ToStringProcessor Ayoub.java
::using the processor
pause
jar uf lib.jar Ayoub.class AyoubGenerated.class
::I want to run the Genrated ToString()method.I will invoke the genrated class ,I included the the generated class file in the jar file, again you can use the generated class directly
del *.class
jar tf lib.jar
::lists the files inside the jar file 
pause
javac -cp lib.jar Main.java
pause
java -cp ;lib.jar Main
::the above two instuctions comp and run the generated ToString().
::notice the ; included in java -cp command , not using it gave the famous "cannot find or load main class"
pause
echo Main-class: Main > manifest.txt
::a temporary text file that holds : Main-class:your_main_class
::in order to run a jar file direcrly,you must include the main class,which is known as the entry point,in a file called MANIFEST.MF wich it is automatically generated by jar command upon jar file creation
jar umf manifest.txt lib.jar
::updates the MANIFEST.MF inside the jar file
del manifest.txt
jar uf lib.jar Main.class
::adds the main class the jar file 
del Main.class
java -jar lib.jar
::runs the jar file
pause
```
