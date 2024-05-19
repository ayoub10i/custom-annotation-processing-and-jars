import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("ToString")  // Your annotation type
@SupportedSourceVersion(SourceVersion.RELEASE_8)  // Supported Java version
public class ToStringProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<TypeElement> elements = ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(ToString.class));

        for (TypeElement typeElement : elements) {
            try {
                // Generate the toString method for the class
                JavaFileObject file = processingEnv.getFiler().createSourceFile(typeElement.getQualifiedName() + "Generated");
                try (Writer writer = file.openWriter()) {
                    // writer.write("package " + processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName() + ";\n\n");
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
