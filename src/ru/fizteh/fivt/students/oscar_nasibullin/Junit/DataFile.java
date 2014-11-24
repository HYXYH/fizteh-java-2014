package ru.fizteh.fivt.students.oscar_nasibullin.Junit;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DataFile implements Map<String, String> , AutoCloseable{
    private final Map<String, String> getCache;
    private final Map<String, String> putCache;
    private final DataFileHasher dataFileHasher ;
    private final File datFile;
    //boolean loaded = true;



    public DataFile(String tablePath, DataFileHasher dataFileHasherObject) throws Exception {

        dataFileHasher = dataFileHasherObject;
        Path datFilePath = Paths.get(tablePath).
                resolve(dataFileHasher.getDirNum().toString() + ".dir").
                resolve(dataFileHasher.getFileNum().toString() + ".dat");
        datFile = datFilePath.toFile();
        getCache = new TreeMap<String, String>();
        putCache = new TreeMap<String, String>();
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
        List<Integer> offsets = new LinkedList<Integer>();
        List<String> keys = new LinkedList<String>();
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
                keys.add(bytesBuffer.toString("UTF-8"));
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
                    getCache.put(keyIter.next(), bytesBuffer.toString("UTF-8"));
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
            for (Entry<String, String> entry : getCache.entrySet()) {
                offset += entry.getKey().length() + 5;
            }
            for (Entry<String, String> entry : getCache.entrySet()) {
                datRAFile.write(entry.getKey().getBytes("UTF-8"));
                datRAFile.write('\0');
                datRAFile.writeInt(offset);
                offset += entry.getValue().length();
            }
            for (Entry<String, String> entry : getCache.entrySet()) {
                datRAFile.write(entry.getValue().getBytes("UTF-8"));
            }
        }
    }

    public int commit() {
        for (Entry<String, String> entry : putCache.entrySet()) {
            if (getCache.containsKey(entry.getKey())) {
                getCache.remove(entry.getKey());
            }
            getCache.put(entry.getKey(), entry.getValue());
        }
        int saved = putCache.size();
        putCache.clear();
        return saved;
        //exportData(); Todo: should it be here?
    }

    public int rollback() {
        int changesRolled = putCache.size();
        putCache.clear();
        return changesRolled;
    }

    public int unsavedChangesNum() {
        return putCache.size();
    }

    @Override
    public int size() {
        Map<String, String> merged = new TreeMap<>(getCache);
        for (Entry<String, String> entry : putCache.entrySet()) {
            if (merged.containsKey(entry.getKey())) {
                merged.remove(entry.getKey());
            }
            merged.put(entry.getKey(), entry.getValue());
        }
        return merged.size();
    }

    @Override
    public boolean isEmpty() {
        return putCache.isEmpty() || getCache.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return putCache.containsKey(key) || getCache.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return putCache.containsValue(value) || getCache.containsValue(value);
    }

    @Override
    public String get(Object key) {
        if (putCache.containsKey(key)) {
            return putCache.get(key);
        } else if (getCache.containsKey(key)) {
            return getCache.get(key);
        }
        return null;
    }

    @Override
    public String put(String key, String value) {
        return putCache.put(key, value);
    }

    @Override
    public String remove(Object key) {
        if (putCache.containsKey(key)) {
            putCache.remove(key);
        }
        if (getCache.containsKey(key)) {
            getCache.remove(key);
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        putCache.putAll(m);
    }

    @Override
    public void clear() {
        putCache.clear();
    }

    @Override
    public Set<String> keySet() {
        return getCache.keySet();
    }

    @Override
    public Collection<String> values() {
        return getCache.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        Map<String, String> merged = new TreeMap<>(getCache);
        for (Entry<String, String> entry : putCache.entrySet()) {
            if (merged.containsKey(entry.getKey())) {
                merged.remove(entry.getKey());
            }
            merged.put(entry.getKey(), entry.getValue());
        }
        return merged.entrySet();
    }

    @Override
    public void close() throws Exception {
        commit();
        if (!getCache.isEmpty()) {
            exportData();
            putCache.clear();
            getCache.clear();
        } else {
            datFile.delete();
        }
    }

    public void clearAndDelete() {
        putCache.clear();
        getCache.clear();
        datFile.delete();
    }
}
