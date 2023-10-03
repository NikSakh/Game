package com.game.serialization;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RCDatabase extends RCBase {

    public static final byte[] HEADER = "RCDB".getBytes();
    public static final short VERSION = 0x0100;
    public static final byte CONTAINER_TYPE = ContainerType.DATABASE;
    private short objectCount;
    public List<RCObject> objects = new ArrayList<RCObject>();

    private RCDatabase() {
    }

    public RCDatabase(String name) {
        setName(name);

        size += HEADER.length + 2 + 1 + 2;
    }

    public void addObject(RCObject object) {
        objects.add(object);
        size += object.getSize();

        objectCount = (short)objects.size();
    }

    public int getSize() {
        return size;
    }

    public int getBytes(byte[] dest, int pointer) {
        pointer = SerializationUtils.writeBytes(dest, pointer, HEADER);
        pointer = SerializationUtils.writeBytes(dest, pointer, VERSION);
        pointer = SerializationUtils.writeBytes(dest, pointer, CONTAINER_TYPE);
        pointer = SerializationUtils.writeBytes(dest, pointer, nameLength);
        pointer = SerializationUtils.writeBytes(dest, pointer, name);
        pointer = SerializationUtils.writeBytes(dest, pointer, size);

        pointer = SerializationUtils.writeBytes(dest, pointer, objectCount);
        for (RCObject object : objects)
            pointer = object.getBytes(dest, pointer);

        return pointer;
    }

    public static RCDatabase Deserialize(byte[] data) {
        int pointer = 0;
        //assert(readString(data, pointer, HEADER.length).equals(HEADER));
        pointer += HEADER.length;

        if (SerializationUtils.readShort(data, pointer) != VERSION) {
            System.err.println("Invalid RCDB version!");
            return null;
        }
        pointer += 2;

        byte containerType = SerializationUtils.readByte(data, pointer++);
        assert(containerType == CONTAINER_TYPE);

        RCDatabase result = new RCDatabase();
        result.nameLength = SerializationUtils.readShort(data, pointer);
        pointer += 2;
        result.name = SerializationUtils.readString(data, pointer, result.nameLength).getBytes();
        pointer += result.nameLength;

        result.size = SerializationUtils.readInt(data, pointer);
        pointer += 4;

        result.objectCount = SerializationUtils.readShort(data, pointer);
        pointer += 2;

        for (int i = 0; i < result.objectCount; i++) {
            RCObject object = RCObject.Deserialize(data, pointer);
            result.objects.add(object);
            pointer += object.getSize();
        }

        return result;
    }

    public RCObject findObject(String name) {
        for (RCObject object : objects) {
            if (object.getName().equals(name))
                return object;
        }
        return null;
    }

    public static RCDatabase DeserializeFromFile(String path) {
        byte[] buffer = null;
        try {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(path));
            buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();
        } catch (IOException e) {
            return null;
        }

        return Deserialize(buffer);
    }

    public void serializeToFile(String path) {
        byte[] data = new byte[getSize()];
        getBytes(data, 0);
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path));
            stream.write(data);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
