package ru.fizteh.fivt.students.oscar_nasibullin.Storeable;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DataFile implements Map<String, String> , AutoCloseable{
    private final Map<String, String> storage;
    private final Map<String, String> cache;
    private final DataFileHasher dataFileHasher ;
    private final File datFile;
    //boolean loaded = true;



    public DataFile(String tablePath, DataFileHasher dataFileHasherObject) throws Exception {

        dataFileHasher = dataFileHasherObject;
        Path datFilePath = Paths.get(tablePath).
                resolve(dataFileHasher.getDirNum().toString() + ".dir").
                resolve(dataFileHasher.getFileNum().toString() + ".dat");
        datFile = datFilePath.toFile();
        storage = new TreeMap<>();
        cache = new TreeMap<>();
        importData();
    }


    private  boolean dataFileExists() throws Exception {
        if (datFile.exists()) {
            if (datFile.isDirectory()) {
                throw new Exception("Expected .dat file, found directory");
            }
            if (!datFile.isFile()) {
                throw new Exception(datFile.getName() + "is not a file or corrupted");
            }
            return true;
        } else {
            return false;
        }
    }



    private  void importData() throws Exception {
        ByteArrayOutputStream bytesBuffer = new ByteArrayOutputStream();
        List<Integer> offsets = new LinkedList<>();
        List<String> keys = new LinkedList<>();
        byte b;
        int bytesCounter = 0;
        int firstOffset = -1;
        if (!dataFileExists()) {
            return;
        }

        try (RandomAccessFile datRAFile = new RandomAccessFile(datFile, "r")) {
            do {
                b = datRAFile.readByte();
                bytesCounter++;
                bytesBuffer.write(b);
                if (!dataFileHasher.contains(b)) {
                    throw new Exception(datFile.getName() + " corrupted.");
                }

                while ((b = datRAFile.readByte()) != 0) {
                    bytesCounter++;
                    bytesBuffer.write(b);
                }
                bytesCounter++;
                if (firstOffset == -1) {
                    firstOffset = datRAFile.readInt();
                } else {
                    offsets.add(datRAFile.readInt());
                }
                bytesCounter += 4;
                keys.add(bytesBuffer.toString(DataFileHasher.ENCODING));
                bytesBuffer.reset();
            } while (bytesCounter < firstOffset);

            offsets.add((int) datRAFile.length());
            Iterator<String> keyIter = keys.iterator();
            for (int nextOffset : offsets) {
                while (bytesCounter < nextOffset) {
                    bytesBuffer.write(datRAFile.readByte());
                    bytesCounter++;
                }
                if (bytesBuffer.size() > 0) {
                    storage.put(keyIter.next(), bytesBuffer.toString(DataFileHasher.ENCODING));
                    bytesBuffer.reset();
                } else {
                    throw new IOException();
                }
            }
            bytesBuffer.close();
        } catch (EOFException e) {
            throw new IOException("Unexpected end of file: " + datFile.getPath());
        }
    }
    private void exportData() throws Exception {
        int offset = 0;
        if (!dataFileExists()) {
            datFile.getParentFile().mkdir();
            datFile.createNewFile();
        }
        try (RandomAccessFile datRAFile = new RandomAccessFile(datFile, "rw")) {
            datRAFile.setLength(0);
            for (Entry<String, String> entry : storage.entrySet()) {
                offset += entry.getKey().length() + 5;
            }
            for (Entry<String, String> entry : storage.entrySet()) {
                datRAFile.write(entry.getKey().getBytes(DataFileHasher.ENCODING));
                datRAFile.write('\0');
                datRAFile.writeInt(offset);
                offset += entry.getValue().length();
            }
            for (Entry<String, String> entry : storage.entrySet()) {
                datRAFile.write(entry.getValue().getBytes(DataFileHasher.ENCODING));
            }
        }
    }

    public int commit() {
        for (Entry<String, String> entry : cache.entrySet()) {
            if (storage.containsKey(entry.getKey())) {
                storage.remove(entry.getKey());
            }
            if (!entry.getValue().equals("")) {
                storage.put(entry.getKey(), entry.getValue());
            }
        }
        int saved = cache.size();
        cache.clear();
        return saved;
        //exportData(); Todo: should it be here?
    }

    public int rollback() {
        int changesRolled = cache.size();
        cache.clear();
        return changesRolled;
    }

    public int unsavedChangesNum() {
        return cache.size();
    }

    @Override
    public int size() {
        Map<String, String> merged = new TreeMap<>(storage);
        for (Entry<String, String> entry : cache.entrySet()) {
            if (merged.containsKey(entry.getKey())) {
                merged.remove(entry.getKey());
            }
            if (!entry.getValue().equals("")) {
                merged.put(entry.getKey(), entry.getValue());
            }
        }
        return merged.size();
    }

    @Override
    public boolean isEmpty() {
        return cache.isEmpty() && storage.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return cache.containsKey(key) || storage.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return cache.containsValue(value) || storage.containsValue(value);
    }

    @Override
    public String get(Object key) {
        if (cache.containsKey(key)) {
            if (cache.get(key).equals("")) {
                return null;
            }
            return cache.get(key);
        } else if (storage.containsKey(key)) {
            return storage.get(key);
        }
        return null;
    }

    @Override
    public String put(String key, String value) {
        if (cache.containsKey(key) && cache.get(key).equals("")) {
            cache.remove(key);
        }
        return cache.put(key, value);
    }

    @Override
    public String remove(Object key) {
        if (cache.containsKey(key) || storage.containsKey(key)) {
            cache.remove(key);
            cache.put((String) key, ""); // "" - means deleted
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        cache.putAll(m);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public Set<String> keySet() {
        return storage.keySet();
    }

    @Override
    public Collection<String> values() {
        return storage.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        Map<String, String> merged = new TreeMap<>(storage);
        for (Entry<String, String> entry : cache.entrySet()) {
            if (merged.containsKey(entry.getKey())) {
                merged.remove(entry.getKey());
            }
            if (!entry.getValue().equals("")) {
                merged.put(entry.getKey(), entry.getValue());
            }
        }
        return merged.entrySet();
    }

    @Override
    public void close() throws Exception {
        commit();
        if (!storage.isEmpty()) {
            exportData();
            cache.clear();
            storage.clear();
        } else {
            datFile.delete();
        }
    }

}
