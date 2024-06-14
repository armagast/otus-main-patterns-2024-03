package me.blokhin.codegen;

import me.blokhin.Command;
import me.blokhin.CommandIOCInitialize;
import me.blokhin.DependencySupplier;
import me.blokhin.IOC;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;

import javax.tools.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdapterGeneratorTest {
    private final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    private final AdapterGenerator adapterGenerator = new AdapterGenerator();

    private static final DependencySupplier getterDependencySupplier = mock(DependencySupplier.class);
    private static final DependencySupplier methodDependencySupplier = mock(DependencySupplier.class);
    private static final DependencySupplier setterDependencySupplier = mock(DependencySupplier.class);

    @BeforeAll
    static void beforeAll() {
        new CommandIOCInitialize().execute();

        final String scopeId = "AdapterGeneratorTest";

        ((Command) IOC.resolve("IOC.scopes.new", scopeId)).execute();
        ((Command) IOC.resolve("IOC.scopes.useScope", scopeId)).execute();

        ((Command) IOC.resolve("IOC.register", AdapterGeneratorTestInterface.class.getCanonicalName() + ":integer.get", getterDependencySupplier)).execute();
        ((Command) IOC.resolve("IOC.register", AdapterGeneratorTestInterface.class.getCanonicalName() + ":integer.set", setterDependencySupplier)).execute();
        ((Command) IOC.resolve("IOC.register", AdapterGeneratorTestInterface.class.getCanonicalName() + ".method", methodDependencySupplier)).execute();
    }

    @DisplayName("Creates class, that can be instantiated with target instance")
    @Test
    void constructor() throws Exception {
        final JavaFileManager fileManager = new CustomJavaFileManager(ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null));

        final String className = "me.blokhin.codegen.AdapterGeneratorTestInterfaceAdapter";
        final String classSource = adapterGenerator.generateSourceCode(AdapterGeneratorTestTarget.class, AdapterGeneratorTestInterface.class);

        final List<JavaFileObject> classSourceFiles = Collections.singletonList(new CustomInputJavaFileObject(className, classSource));

        final JavaCompiler.CompilationTask task = ToolProvider.getSystemJavaCompiler()
                .getTask(null, fileManager, diagnostics, null, null, classSourceFiles);

        task.call();

        final Class<?> Adapter = fileManager.getClassLoader(null).loadClass(className);
        final AdapterGeneratorTestTarget target = mock(AdapterGeneratorTestTarget.class);

        assertDoesNotThrow(() -> {
            AdapterGeneratorTestInterface instance = (AdapterGeneratorTestInterface) Adapter.getConstructor(AdapterGeneratorTestTarget.class).newInstance(target);
        });
    }

    @DisplayName("Creates correct getter implementation")
    @Test
    void getter() throws Exception {
        final JavaFileManager fileManager = new CustomJavaFileManager(ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null));

        final String className = "me.blokhin.codegen.AdapterGeneratorTestInterfaceAdapter";
        final String classSource = adapterGenerator.generateSourceCode(AdapterGeneratorTestTarget.class, AdapterGeneratorTestInterface.class);

        final List<JavaFileObject> classSourceFiles = Collections.singletonList(new CustomInputJavaFileObject(className, classSource));

        final JavaCompiler.CompilationTask task = ToolProvider.getSystemJavaCompiler()
                .getTask(null, fileManager, diagnostics, null, null, classSourceFiles);

        task.call();

        final Class<?> Adapter = fileManager.getClassLoader(null).loadClass(className);
        final AdapterGeneratorTestTarget target = mock(AdapterGeneratorTestTarget.class);
        final AdapterGeneratorTestInterface instance = (AdapterGeneratorTestInterface) Adapter.getConstructor(AdapterGeneratorTestTarget.class).newInstance(target);

        final ArgumentCaptor<AdapterGeneratorTestTarget> targetCaptor = ArgumentCaptor.forClass(AdapterGeneratorTestTarget.class);
        when(getterDependencySupplier.get(targetCaptor.capture())).thenReturn(42);

        assertEquals(42, instance.getInteger());

        verify(getterDependencySupplier, times(1)).get(any());
        assertEquals(targetCaptor.getValue(), target);
    }

    @DisplayName("Creates correct setter implementation")
    @Test
    void setter() throws Exception {
        final JavaFileManager fileManager = new CustomJavaFileManager(ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null));

        final String className = "me.blokhin.codegen.AdapterGeneratorTestInterfaceAdapter";
        final String classSource = adapterGenerator.generateSourceCode(AdapterGeneratorTestTarget.class, AdapterGeneratorTestInterface.class);

        final List<JavaFileObject> classSourceFiles = Collections.singletonList(new CustomInputJavaFileObject(className, classSource));

        final JavaCompiler.CompilationTask task = ToolProvider.getSystemJavaCompiler()
                .getTask(null, fileManager, diagnostics, null, null, classSourceFiles);

        task.call();

        final Class<?> Adapter = fileManager.getClassLoader(null).loadClass(className);
        final AdapterGeneratorTestTarget target = mock(AdapterGeneratorTestTarget.class);
        final AdapterGeneratorTestInterface instance = (AdapterGeneratorTestInterface) Adapter.getConstructor(AdapterGeneratorTestTarget.class).newInstance(target);

        final ArgumentCaptor<AdapterGeneratorTestTarget> targetCaptor = ArgumentCaptor.forClass(AdapterGeneratorTestTarget.class);
        final ArgumentCaptor<Integer> valueCaptor = ArgumentCaptor.forClass(Integer.class);
        final Command command = mock(Command.class);
        when(setterDependencySupplier.get(targetCaptor.capture(), valueCaptor.capture())).thenReturn(command);

        instance.setInteger(42);

        verify(setterDependencySupplier, times(1)).get(any(), any());
        verify(command, times(1)).execute();
        assertEquals(targetCaptor.getValue(), target);
        assertEquals(valueCaptor.getValue(), 42);
    }

    @DisplayName("Create correct method implementation")
    @Test
    void method() throws Exception {
        final JavaFileManager fileManager = new CustomJavaFileManager(ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null));

        final String className = "me.blokhin.codegen.AdapterGeneratorTestInterfaceAdapter";
        final String classSource = adapterGenerator.generateSourceCode(AdapterGeneratorTestTarget.class, AdapterGeneratorTestInterface.class);

        final List<JavaFileObject> classSourceFiles = Collections.singletonList(new CustomInputJavaFileObject(className, classSource));

        final JavaCompiler.CompilationTask task = ToolProvider.getSystemJavaCompiler()
                .getTask(null, fileManager, diagnostics, null, null, classSourceFiles);

        task.call();

        final Class<?> Adapter = fileManager.getClassLoader(null).loadClass(className);
        final AdapterGeneratorTestTarget target = mock(AdapterGeneratorTestTarget.class);
        final AdapterGeneratorTestInterface instance = (AdapterGeneratorTestInterface) Adapter.getConstructor(AdapterGeneratorTestTarget.class).newInstance(target);
        when(methodDependencySupplier.get(any(), any(), any())).thenReturn(42);

        final Integer actual = instance.method(123, "123");

        verify(methodDependencySupplier, times(1)).get(target, 123, "123");
        assertEquals(42, actual);
    }

}