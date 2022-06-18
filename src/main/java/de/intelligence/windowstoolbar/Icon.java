package de.intelligence.windowstoolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import de.intelligence.windowstoolbar.exceptions.ConditionNotMetException;

public final class Icon {

    private static final int HEADER_SIZE = 0x06;

    public static Icon fromPath(String id, String path) {
        return Icon.fromFile(id, new File(Conditions.notNull(path)));
    }

    public static Icon fromFile(String id, File file) {
        Conditions.fileExists(Conditions.notNull(file));
        try {
            return Icon.fromInputStream(id, file.getAbsolutePath(), new FileInputStream(file));
        } catch (IOException ex) {
            throw new ConditionNotMetException(ex);
        }
    }

    static Icon fromInputStream(String id, String path, InputStream input) throws IOException {
        Conditions.notNull(id);
        Conditions.notNull(path);
        try (PushbackInputStream pushbackInput = new PushbackInputStream(Conditions.notNull(input), Icon.HEADER_SIZE)) {
            return new Icon(id, new IconPath(path, IconPath.IconPathType.ABSOLUTE), Icon.validateHeader(pushbackInput));
        }
    }

    public static Icon fromClassPath(String id, String path) {
        Conditions.notNull(id);
        Conditions.notNull(path);
        try (PushbackInputStream pushbackInputStream = new PushbackInputStream(Utils.WALKER.getCallerClass()
                .getClassLoader().getResourceAsStream(path), Icon.HEADER_SIZE)) {
            return new Icon(id, new IconPath(path, IconPath.IconPathType.CLASSPATH), Icon.validateHeader(pushbackInputStream));
        } catch (IOException ex) {
            throw new ConditionNotMetException(ex);
        }
    }

    private static int validateHeader(PushbackInputStream inputStream) throws IOException {
        final byte[] header = new byte[Icon.HEADER_SIZE];
        Conditions.checkState(inputStream.read(header) == header.length, "Invalid ico file detected (Invalid size)");
        final ByteBuffer byteBuf = ByteBuffer.wrap(header);
        byteBuf.order(ByteOrder.LITTLE_ENDIAN);
        final int numImages;
        Conditions.checkState(byteBuf.getShort() == 0, "Invalid ico file detected (Invalid ico header)");
        Conditions.checkState(byteBuf.getShort() == 1, "Invalid ico file detected (Invalid image type)");
        Conditions.checkState((numImages = byteBuf.getShort()) > 0, "Invalid ico file detected (No image in directory)");
        inputStream.unread(header);
        return numImages;
    }

    private final String id;
    private final IconPath path;
    private final int numImages;

    private Icon(String id, IconPath path, int numImages) {
        this.id = id;
        this.path = path;
        this.numImages = numImages;
    }

    public String getId() {
        return this.id;
    }

    public IconPath getPath() {
        return this.path;
    }

    public int getNumImages() {
        return this.numImages;
    }

}
