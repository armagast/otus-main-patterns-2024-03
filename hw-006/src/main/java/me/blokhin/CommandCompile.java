package me.blokhin;

import lombok.Value;
import me.blokhin.codegen.CustomInputJavaFileObject;

import javax.tools.*;
import java.util.Collections;
import java.util.List;

@Value
public class CommandCompile implements Command {
    private static final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private static final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

    JavaFileManager javaFileManager;
    String className;
    String classSource;

    public CommandCompile(final JavaFileManager javaFileManager, final String className, final String classSource) {
        Assert.notNull(javaFileManager, "{javaFileManager} must not be null");
        Assert.notNull(className, "{className} must not be null");
        Assert.notNull(classSource, "{classSource} must not be null");

        this.javaFileManager = javaFileManager;
        this.className = className;
        this.classSource = classSource;
    }

    @Override
    public void execute() {
        final List<JavaFileObject> classSourceFiles = Collections.singletonList(new CustomInputJavaFileObject(className, classSource));
        final JavaCompiler.CompilationTask task = compiler.getTask(null, javaFileManager, diagnostics, null, null, classSourceFiles);

        boolean success = task.call();

        if (!success) {
            throw new RuntimeException("Compilation failed");
        }
    }
}
