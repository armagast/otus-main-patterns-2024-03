package me.blokhin.codegen;

import me.blokhin.Assert;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.util.HashMap;
import java.util.Optional;

public class CustomJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
    private final HashMap<String, CustomOutputJavaFileObject> classes;
    private final ClassLoader classLoader;

    public CustomJavaFileManager(final StandardJavaFileManager manager) {
        super(Assert.notNull(manager, "{manager} must not be null"));

        this.classes = new HashMap<>();
        this.classLoader = new CustomClassLoader(this.getClass().getClassLoader(), this);
    }

    @Override
    public ClassLoader getClassLoader(final Location location) {
        return classLoader;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(final Location location,
                                               final String className,
                                               final JavaFileObject.Kind kind,
                                               final FileObject sibling)  {

        final CustomOutputJavaFileObject object = new CustomOutputJavaFileObject(className, kind);
        classes.put(className, object);

        return object;
    }

    public Optional<CustomOutputJavaFileObject> findClass(final String className) {
        return Optional.ofNullable(classes.get(className));
    }
}
