package me.blokhin.codegen;

import me.blokhin.Assert;
import me.blokhin.Command;
import me.blokhin.IOC;
import me.blokhin.UObject;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UObjectAdapterGenerator {
    private final Class<?> commandClass = Command.class;
    private final Class<?> iocClass = IOC.class;
    private final Class<?> targetClass = UObject.class;

    private final Function<Class<?>, String> adapterClassNamingStrategy;

    private final BiFunction<Class<?>, String, String> getterDependencyNamingStrategy;
    private final BiFunction<Class<?>, String, String> setterDependencyNamingStrategy;

    private final BiFunction<Class<?>, String, String> methodDependencyNamingStrategy;

    public UObjectAdapterGenerator(
            Function<Class<?>, String> adapterClassNamingStrategy,
            BiFunction<Class<?>, String, String> getterDependencyNamingStrategy,
            BiFunction<Class<?>, String, String> setterDependencyNamingStrategy,
            BiFunction<Class<?>, String, String> methodDependencyNamingStrategy) {

        Assert.notNull(adapterClassNamingStrategy, "{adapterClassNamingStrategy} must not be null");
        Assert.notNull(getterDependencyNamingStrategy, "{getterDependencyNamingStrategy} must not be null");
        Assert.notNull(setterDependencyNamingStrategy, "{setterDependencyNamingStrategy} must not be null");
        Assert.notNull(methodDependencyNamingStrategy, "{methodDependencyNamingStrategy} must not be null");

        this.adapterClassNamingStrategy = adapterClassNamingStrategy;
        this.getterDependencyNamingStrategy = getterDependencyNamingStrategy;
        this.setterDependencyNamingStrategy = setterDependencyNamingStrategy;
        this.methodDependencyNamingStrategy = methodDependencyNamingStrategy;
    }

    public String generateAdapter(Class<?> anInterface) {
        Assert.notNull(anInterface, "{anInterface} must not be null");
        Assert.isTrue(anInterface.isInterface(), "{anInterface} must be an interface");
        Assert.isTrue(!(anInterface.isLocalClass()), "{anInterface} must not be a local class");

        final String adapterPackageName = anInterface.getPackageName();
        final String adapterSimpleName = adapterClassNamingStrategy.apply(anInterface);

        return "\n" +
                "package " + adapterPackageName + ";\n\n" +
                "public class " + adapterSimpleName + " implements " + anInterface.getCanonicalName() + " {\n" +
                generateFields() +
                generateConstructor(anInterface) +
                generateMethods(anInterface) +
                "\n}\n";
    }

    private String generateFields() {
        final String fieldType = targetClass.getCanonicalName();

        return "\n" +
                "  private final " + fieldType + " target;\n";
    }

    private String generateConstructor(final Class<?> anInterface) {
        final String targetType = targetClass.getCanonicalName();
        final String constructorName = adapterClassNamingStrategy.apply(anInterface);

        return "\n" +
                "  public " + constructorName + "(" + targetType + " target) {\n" +
                "    this.target = target;\n" +
                "  }\n";
    }

    private String generateMethods(final Class<?> anInterface) {
        return Arrays.stream(anInterface.getMethods()).map(method -> {
            if (isGetter(method)) {
                return generateGetter(anInterface, propertyName(method), method.getReturnType());
            }

            if (isSetter(method)) {
                return generateSetter(anInterface, propertyName(method), method.getParameterTypes()[0]);
            }

            return generateMethod(anInterface, method);
        }).collect(Collectors.joining(""));

    }

    private String generateGetter(final Class<?> interfaceClass, final String property, final Class<?> propertyClass) {
        final String returnType = propertyClass.getCanonicalName();
        final String methodName = "get" + pascalCase(property);

        return "\n" +
                "  public " + returnType + " " + methodName + "() {\n" +
                "    return " + iocClass.getCanonicalName() + ".resolve(\"" + getterDependencyNamingStrategy.apply(interfaceClass, property) + "\", target);\n" +
                "  }\n";

    }

    private String generateSetter(final Class<?> interfaceClass, final String property, final Class<?> propertyClass) {
        return "\n" +
                "  public void set" + pascalCase(property) + "(" + propertyClass.getCanonicalName() + " value) {\n" +
                "    ((" + commandClass.getCanonicalName() + ") " + iocClass.getCanonicalName() + ".resolve(\"" + setterDependencyNamingStrategy.apply(interfaceClass, property) + "\", target, value)).execute();\n" +
                "  }\n";
    }

    private String generateMethod(final Class<?> interfaceClass, final Method method) {
        final String returnType = method.getReturnType().getCanonicalName();
        final String parametersList = IntStream.range(0, method.getParameterCount())
                .mapToObj(index -> "arg" + index)
                .collect(Collectors.joining(", "));
        final String parametersTypedList = IntStream.range(0, method.getParameterCount())
                .mapToObj(index -> method.getParameterTypes()[index].getCanonicalName() + " arg" + index)
                .collect(Collectors.joining(", "));


        return "\n" +
                "  public " + returnType + " " + method.getName() + "(" + parametersTypedList + ") {\n" +
                (method.getReturnType() == void.class ?
                        "    " + iocClass.getCanonicalName() + ".resolve(\"" + methodDependencyNamingStrategy.apply(interfaceClass, method.getName()) + "\", target, " + parametersList + ");\n" :
                        "    return (" + returnType + ") " + iocClass.getCanonicalName() + ".resolve(\"" + methodDependencyNamingStrategy.apply(interfaceClass, method.getName()) + "\", target, " + parametersList + ");\n") +
                "  }\n";
    }

    private boolean isGetter(final Method method) {
        return method.getName().startsWith("get") && method.getName().length() > 3 && method.getParameterCount() == 0;
    }

    private boolean isSetter(final Method method) {
        return method.getName().startsWith("set") && method.getName().length() > 3 && method.getParameterCount() == 1 && method.getReturnType() == void.class;
    }

    private String propertyName(final Method method) {
        return method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
    }

    private String pascalCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
