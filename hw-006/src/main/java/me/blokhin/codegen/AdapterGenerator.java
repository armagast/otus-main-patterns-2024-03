package me.blokhin.codegen;

import me.blokhin.Assert;
import me.blokhin.Command;
import me.blokhin.IOC;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AdapterGenerator {
    private static final Class<?> containerClass = IOC.class;
    private static final Class<?> commandClass = Command.class;

    public static final Function<Class<?>, String> DEFAULT_CLASS_NAMING_STRATEGY =
            (interfaceClass) -> interfaceClass.getSimpleName() + "Adapter";

    public static final BiFunction<Class<?>, String, String> DEFAULT_GETTER_DEPENDENCY_NAMING_STRATEGY =
            (interfaceClass, property) -> interfaceClass.getCanonicalName() + ":" + property + ".get";

    public static final BiFunction<Class<?>, String, String> DEFAULT_METHOD_DEPENDENCY_NAMING_STRATEGY =
            (interfaceClass, method) -> interfaceClass.getCanonicalName() + "." + method;

    public static final BiFunction<Class<?>, String, String> DEFAULT_SETTER_DEPENDENCY_NAMING_STRATEGY =
            (interfaceClass, property) -> interfaceClass.getCanonicalName() + ":" + property + ".set";

    private final Function<Class<?>, String> classNamingStrategy;

    private final BiFunction<Class<?>, String, String> getterDependencyNamingStrategy;
    private final BiFunction<Class<?>, String, String> methodDependencyNamingStrategy;
    private final BiFunction<Class<?>, String, String> setterDependencyNamingStrategy;

    public AdapterGenerator() {
        this(
                DEFAULT_CLASS_NAMING_STRATEGY,
                DEFAULT_GETTER_DEPENDENCY_NAMING_STRATEGY,
                DEFAULT_METHOD_DEPENDENCY_NAMING_STRATEGY,
                DEFAULT_SETTER_DEPENDENCY_NAMING_STRATEGY
        );
    }

    private AdapterGenerator(final Function<Class<?>, String> classNamingStrategy,
                             final BiFunction<Class<?>, String, String> getterDependencyNamingStrategy,
                             final BiFunction<Class<?>, String, String> methodDependencyNamingStrategy,
                             final BiFunction<Class<?>, String, String> setterDependencyNamingStrategy) {

        Assert.notNull(classNamingStrategy, "{classNamingStrategy} must not be null");
        Assert.notNull(getterDependencyNamingStrategy, "{getterDependencyNamingStrategy} must not be null");
        Assert.notNull(methodDependencyNamingStrategy, "{methodDependencyNamingStrategy} must not be null");
        Assert.notNull(setterDependencyNamingStrategy, "{setterDependencyNamingStrategy} must not be null");

        this.classNamingStrategy = classNamingStrategy;
        this.getterDependencyNamingStrategy = getterDependencyNamingStrategy;
        this.methodDependencyNamingStrategy = methodDependencyNamingStrategy;
        this.setterDependencyNamingStrategy = setterDependencyNamingStrategy;
    }

    public String generateSourceCode(final Class<?> targetClass, final Class<?> interfaceClass) {
        Assert.notNull(interfaceClass, "{interfaceClass} must not be null");
        Assert.isTrue(interfaceClass.isInterface(), "{interfaceClass} must be an interface");
        Assert.isTrue(!(interfaceClass.isAnonymousClass() || interfaceClass.isLocalClass()), "{interfaceClass} must not be an anonymous or local class");
        Assert.notNull(targetClass, "{targetClass} must not be null");
        Assert.isTrue(!(targetClass.isAnonymousClass() || targetClass.isLocalClass()), "{targetClass} must not be an anonymous or local class");

        final String adapterPackageName = interfaceClass.getPackageName();
        final String adapterSimpleName = classNamingStrategy.apply(interfaceClass);

        return "\n" +
                "package " + adapterPackageName + ";\n\n" +
                "import " + containerClass.getCanonicalName() + ";\n\n" +
                "public class " + adapterSimpleName + " implements " + interfaceClass.getCanonicalName() + "{\n" +
                generateFields(targetClass) +
                generateConstructor(targetClass, interfaceClass) +
                generateMethods(targetClass, interfaceClass) +
                "\n}\n";
    }

    private String generateFields(final Class<?> targetClass) {
        final String fieldType = targetClass.getCanonicalName();

        return "\n" +
                "  private final " + fieldType + " target;\n";
    }

    private String generateConstructor(final Class<?> targetClass, final Class<?> interfaceClass) {
        final String argumentType = targetClass.getCanonicalName();
        final String adapterSimpleName = classNamingStrategy.apply(interfaceClass);

        return "\n" +
                "  public " + adapterSimpleName + "(" + argumentType + " target) {\n" +
                "    this.target = target;\n" +
                "  }\n";
    }

    private String generateMethods(final Class<?> targetClass, final Class<?> interfaceClass) {
        return Arrays.stream(interfaceClass.getMethods()).map(method -> {
            if (isGetter(method)) {
                return generateGetter(interfaceClass, propertyName(method), method.getReturnType());
            }

            if (isSetter(method)) {
                return generateSetter(interfaceClass, propertyName(method), method.getParameterTypes()[0]);
            }

            return generateMethod(interfaceClass, method);
        }).collect(Collectors.joining(""));

    }

    private String generateGetter(final Class<?> interfaceClass, final String property, final Class<?> propertyClass) {
        final String returnType = propertyClass.getCanonicalName();
        final String methodName = "get" + pascalCase(property);

        return "\n" +
                "  public " + returnType + " " + methodName + "() {\n" +
                "    return " + containerClass.getSimpleName() + ".resolve(\"" + getterDependencyNamingStrategy.apply(interfaceClass, property) + "\", target);\n" +
                "  }\n";

    }

    private String generateSetter(final Class<?> interfaceClass, final String property, final Class<?> propertyClass) {
        return "\n" +
                "  public void set" + pascalCase(property) + "(" + propertyClass.getCanonicalName() + " value) {\n" +
                "    ((" + commandClass.getCanonicalName() + ") " + containerClass.getSimpleName() + ".resolve(\"" + setterDependencyNamingStrategy.apply(interfaceClass, property) + "\", target, value)).execute();\n" +
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
                        "    " + containerClass.getSimpleName() + ".resolve(\"" + methodDependencyNamingStrategy.apply(interfaceClass, method.getName()) + "\", target, " + parametersList + ");\n" :
                        "    return (" + returnType + ") " + containerClass.getSimpleName() + ".resolve(\"" + methodDependencyNamingStrategy.apply(interfaceClass, method.getName()) + "\", target, " + parametersList + ");\n") +
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
