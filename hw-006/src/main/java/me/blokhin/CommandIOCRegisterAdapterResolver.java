package me.blokhin;

import lombok.Value;
import me.blokhin.codegen.CustomInputJavaFileObject;
import me.blokhin.codegen.CustomJavaFileManager;
import me.blokhin.codegen.UObjectAdapterGenerator;

import javax.tools.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Value
public class CommandIOCRegisterAdapterResolver implements Command {
    private static final Function<Class<?>, String> adapterClassNamingStrategy = anInterface -> anInterface.getSimpleName() + "Adapter";

    private static final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

    private static final UObjectAdapterGenerator generator = new UObjectAdapterGenerator(
            adapterClassNamingStrategy,
            (anInterface, property) -> anInterface.getCanonicalName() + ":" + property + ".get",
            (anInterface, property) -> anInterface.getCanonicalName() + ":" + property + ".set",
            (anInterface, method) -> anInterface.getCanonicalName() + "." + method
    );

    private static final JavaFileManager fileManager = new CustomJavaFileManager(ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null));

    @Override
    public void execute() {
        ((Command) IOC.resolve("IOC.register", "Adapter", (DependencySupplier) args -> createAdapter((Class<?>) args[0], (UObject) args[1]))).execute();
    }

    private Object createAdapter(final Class<?> anInterface, final UObject target) {
        try {
            return getAdapterClass(anInterface).getConstructor(UObject.class).newInstance(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Class<?> getAdapterClass(Class<?> anInterface) throws Exception {
        final String className = anInterface.getPackageName() + "." + adapterClassNamingStrategy.apply(anInterface);

        try {
            return fileManager.getClassLoader(null).loadClass(className);
        } catch (ClassNotFoundException ignored) {
        }

        final List<JavaFileObject> classSourceFiles = Collections.singletonList(new CustomInputJavaFileObject(className, generator.generateAdapter(anInterface)));
        final JavaCompiler.CompilationTask task = ToolProvider.getSystemJavaCompiler()
                .getTask(null, fileManager, diagnostics, null, null, classSourceFiles);

        if (!task.call()) throw new RuntimeException("Cannot compile generated adapter");

        return fileManager.getClassLoader(null).loadClass(className);
    }
}
