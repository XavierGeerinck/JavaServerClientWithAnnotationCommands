package Command;

import java.lang.reflect.Method;

public class CommandObj {
    // The class that holds this command
    private final Class classToInvoke;
    
    // The method that should be called
    private final Method method;
    
    // The annotation
    private final Command annotation;

    public CommandObj(Class classToInvoke, Command annotation, Method m) {
        this.method = m;
        this.annotation = annotation;
        this.classToInvoke = classToInvoke;
    }

    public String getAnnotationDescription() {
        return annotation.description();
    }

    public String getAnnotationName() {
        return annotation.name();
    }

    public String getAnnotationDelimiter() {
        return annotation.delimiter();
    }

    public Method getMethod() {
        return method;
    }
    
    public Class getClassToInvoke() {
        return this.classToInvoke;
    }
}
