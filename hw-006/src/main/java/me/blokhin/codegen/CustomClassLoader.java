package me.blokhin.codegen;

import me.blokhin.Assert;

public class CustomClassLoader extends ClassLoader {
    private final CustomJavaFileManager manager;

    public CustomClassLoader(final ClassLoader parent, final CustomJavaFileManager manager) {
        super(parent);

        Assert.notNull(manager, "{manager} must not be null");

        this.manager = manager;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        return manager.findClass(className)
                .map(CustomOutputJavaFileObject::getBytes)
                .map(bytes -> defineClass(className, bytes, 0, bytes.length))
                .orElseThrow(() -> new ClassNotFoundException(className));
    }
}
