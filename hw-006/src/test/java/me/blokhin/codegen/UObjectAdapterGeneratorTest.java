package me.blokhin.codegen;

import me.blokhin.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.tools.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class UObjectAdapterGeneratorTest {
    private static final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

    private static final DependencySupplier getterDependencySupplier = mock(DependencySupplier.class);
    private static final DependencySupplier methodDependencySupplier = mock(DependencySupplier.class);
    private static final DependencySupplier setterDependencySupplier = mock(DependencySupplier.class);

    private static final UObjectAdapterGenerator generator = new UObjectAdapterGenerator(
            anInterface -> anInterface.getSimpleName() + "Adapter",
            (anInterface, property) -> anInterface.getCanonicalName() + ":" + property + ".get",
            (anInterface, property) -> anInterface.getCanonicalName() + ":" + property + ".set",
            (anInterface, method) -> anInterface.getCanonicalName() + "." + method
    );

    private static Class<?> Adapter;

    @BeforeAll
    static void beforeAll() throws Exception {
        new CommandIOCInitialize().execute();

        final String scopeId = "AdapterGeneratorTest";

        ((Command) IOC.resolve("IOC.scopes.new", scopeId)).execute();
        ((Command) IOC.resolve("IOC.scopes.useScope", scopeId)).execute();

        ((Command) IOC.resolve("IOC.register", UObjectAdapterGeneratorTestInterface.class.getCanonicalName() + ":integer.get", getterDependencySupplier)).execute();
        ((Command) IOC.resolve("IOC.register", UObjectAdapterGeneratorTestInterface.class.getCanonicalName() + ":integer.set", setterDependencySupplier)).execute();
        ((Command) IOC.resolve("IOC.register", UObjectAdapterGeneratorTestInterface.class.getCanonicalName() + ".method", methodDependencySupplier)).execute();


        final JavaFileManager fileManager = new CustomJavaFileManager(ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null));

        final String className = "me.blokhin.codegen.UObjectAdapterGeneratorTestInterfaceAdapter";
        final String classSource = generator.generateAdapter(UObjectAdapterGeneratorTestInterface.class);

        final List<JavaFileObject> classSourceFiles = Collections.singletonList(new CustomInputJavaFileObject(className, classSource));

        final JavaCompiler.CompilationTask task = ToolProvider.getSystemJavaCompiler()
                .getTask(null, fileManager, diagnostics, null, null, classSourceFiles);

        task.call();

        Adapter = fileManager.getClassLoader(null).loadClass(className);
    }


    @DisplayName("Generates correct constructor implementation")
    @Test
    void constructor() {
        final UObject target = mock(UObject.class);

        assertDoesNotThrow(() -> Adapter.getConstructor(UObject.class).newInstance(target));
    }

    @DisplayName("Generates correct getter implementation")
    @Test
    void getter() throws Exception {
        final UObject target = mock(UObject.class);
        final UObjectAdapterGeneratorTestInterface instance = (UObjectAdapterGeneratorTestInterface) Adapter.getConstructor(UObject.class).newInstance(target);

        final ArgumentCaptor<UObject> targetCaptor = ArgumentCaptor.forClass(UObject.class);
        when(getterDependencySupplier.get(targetCaptor.capture())).thenReturn(42);

        assertEquals(42, instance.getInteger());

        verify(getterDependencySupplier, times(1)).get(targetCaptor.capture());
        assertEquals(targetCaptor.getValue(), target);
    }

    @DisplayName("Generates correct setter implementation")
    void setter() throws Exception {
        final UObject target = mock(UObject.class);
        final UObjectAdapterGeneratorTestInterface instance = (UObjectAdapterGeneratorTestInterface) Adapter.getConstructor(UObject.class).newInstance(target);

        final ArgumentCaptor<UObject> targetCaptor = ArgumentCaptor.forClass(UObject.class);
        final ArgumentCaptor<Integer> valueCaptor = ArgumentCaptor.forClass(Integer.class);
        final Command command = mock(Command.class);
        when(setterDependencySupplier.get(targetCaptor.capture(), valueCaptor.capture())).thenReturn(command);

        instance.setInteger(42);

        assertEquals(targetCaptor.getValue(), target);
        assertEquals(valueCaptor.getValue(), 42);
        verify(command, times(1)).execute();
        verify(setterDependencySupplier, times(1)).get(any(), any());
    }

    @DisplayName("Generates correct method implementation")
    @Test
    void method() throws Exception {
        final UObject target = mock(UObject.class);
        final UObjectAdapterGeneratorTestInterface instance = (UObjectAdapterGeneratorTestInterface) Adapter.getConstructor(UObject.class).newInstance(target);

        final UObject returnValue = mock(UObject.class);
        final UObject arg0 = mock(UObject.class);
        final UObject arg1 = mock(UObject.class);

        final ArgumentCaptor<UObject> targetCaptor = ArgumentCaptor.forClass(UObject.class);
        final ArgumentCaptor<UObject> arg0Captor = ArgumentCaptor.forClass(UObject.class);
        final ArgumentCaptor<UObject> arg1Captor = ArgumentCaptor.forClass(UObject.class);

        when(methodDependencySupplier.get(targetCaptor.capture(), arg0Captor.capture(), arg1Captor.capture())).thenReturn(returnValue);

        final UObject actualReturnValue = instance.method(arg0, arg1);

        assertSame(returnValue, actualReturnValue);
        verify(methodDependencySupplier, times(1)).get(target, arg0, arg1);
    }
}