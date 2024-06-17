package me.blokhin;

import me.blokhin.codegen.CustomJavaFileManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileManager;
import javax.tools.ToolProvider;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandCompileTest {
    @DisplayName("Throws IllegalArgumentException when {javaFileManager}, {className} or {classSource} is null")
    @Test
    void assertsArgs() {
        final JavaFileManager javaFileManager = mock(JavaFileManager.class);
        final String className = "class name goes here";
        final String classSource = "class source code goes here";

        assertThrows(IllegalArgumentException.class, () -> new CommandCompile(null, className, classSource));
        assertThrows(IllegalArgumentException.class, () -> new CommandCompile(javaFileManager, null, classSource));
        assertThrows(IllegalArgumentException.class, () -> new CommandCompile(javaFileManager, className, null));
    }

    @DisplayName("Throws RuntimeException on compilation failure")
    @Test
    void throwsOnCompilationFailure() {
        final JavaFileManager javaFileManager = new CustomJavaFileManager(ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null));
        final String className = "me.blokhin.CommandCompileTest$ThrowsRuntimeException";
        final String classSource = "abracadabra";

        final Command command = new CommandCompile(javaFileManager, className, classSource);

        assertThrows(RuntimeException.class, command::execute);
    }

    @DisplayName("Compiles and loads class")
    @Test
    void compilesClass() throws Exception {
        final JavaFileManager javaFileManager = new CustomJavaFileManager(ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null));
        final String className = "me.blokhin.CommandCompileTest$CompilesClass";
        final String classSource = "package me.blokhin; public class CommandCompileTest$CompilesClass { public int getAnswerToTheUltimateQuestionOfLife() { return 42; } }";

        final Command command = new CommandCompile(javaFileManager, className, classSource);

        command.execute();

        final Class<?> classCompiled = javaFileManager.getClassLoader(null).loadClass(className);

        final Object instance = classCompiled.getConstructor().newInstance();
        final Method method = classCompiled.getDeclaredMethod("getAnswerToTheUltimateQuestionOfLife");

        assertEquals(42, (int) method.invoke(instance));
    }
}
