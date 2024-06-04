package me.blokhin.codegen;

import me.blokhin.Assert;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class CustomOutputJavaFileObject extends SimpleJavaFileObject {
    private final ByteArrayOutputStream stream = new ByteArrayOutputStream();

    public CustomOutputJavaFileObject(final String className, final Kind kind) {
        super(
                URI.create(String.format(
                        "string:///%s%s",
                        Assert.notNull(className, "{className} must not be null").replace('.', '/'),
                        Assert.notNull(kind, "{kind} must not be null").extension
                )),
                kind
        );
    }

    public byte[] getBytes() {
        return stream.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return stream;
    }
}
