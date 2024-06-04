package me.blokhin.codegen;

import me.blokhin.Assert;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

public class CustomInputJavaFileObject extends SimpleJavaFileObject {
    private final String source;

    public CustomInputJavaFileObject(final String className, final String source) {
        super(
                URI.create(String.format(
                        "string:///%s%s",
                        Assert.notNull(className, "{className} must not be null").replace('.', '/'),
                        Kind.SOURCE.extension
                )),
                Kind.SOURCE
        );

        this.source = Assert.notNull(source, "{source} must not be null");
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return source;
    }
}
