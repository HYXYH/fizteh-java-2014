package ru.fizteh.fivt.students.oscar_nasibullin.Junit;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.Interfaces.TableInterface;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *  Created by Oskar on 15.11.14.
 */
public class Table implements TableInterface {

    static final int NUMBER_DIRECTORIES = DataFileHasher.HASH_NUMBER;
    static final int NUMBER_FILES_IN_DIRECTORY = DataFileHasher.HASH_NUMBER;

    private static Map<DataFileHasher, DataFile> datFiles;
    public boolean isOpen;
    private final File tableRoot;

    public Table(File table)  {
        tableRoot = table;
        isOpen = false;
        try {
            open();
            close();
        } catch (Exception e) {
            if (e.getMessage().endsWith("is not a directory")) {
                throw new RuntimeException("Can't create table: " + e.getMessage());
            }
        }
    }

    @Override
    public String getName() {
        return tableRoot.getName();
    }

    @Override
    public String get(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("Illegal argument for get");
        }

        DataFile data = datFiles.get(new DataFileHasher(key));
        return data.get(key);

    }

    @Override
    public String put(String key, String value) throws IllegalArgumentException {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Illegal arguments for put");
        }
        DataFile data = datFiles.get(new DataFileHasher(key));
        String rezultMessage = data.get(key);
        data.remove(key);
        data.put(key, value);

        return rezultMessage;
    }

    @Override
    public String remove(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("Illegal argument for remove");
        }
        DataFile data = datFiles.get(new DataFileHasher(key));
        String rezultMessage = data.get(key);
        data.remove(key);

        return rezultMessage;
    }

    @Override
    public int size() {
        int totalSize = 0;
        for (Map.Entry<DataFileHasher, DataFile> entry : datFiles.entrySet()) {
            totalSize += entry.getValue().size();
        }
        return totalSize;
    }

    @Override
    public int commit() {
        int saved = 0;
        for (Map.Entry<DataFileHasher, DataFile> entry : datFiles.entrySet()) {
            saved += entry.getValue().commit();
        }
        return saved;
    }

    @Override
    public int rollback() {
        int changesRolled = 0;
        for (Map.Entry<DataFileHasher, DataFile> entry : datFiles.entrySet()) {
            changesRolled += entry.getValue().rollback();
        }
        return changesRolled;
    }

    @Override
    public List<String> list() {
        List<String> keys = new ArrayList<String>();
        for (Map.Entry<DataFileHasher, DataFile> dataEntry : datFiles.entrySet()) {
            for (Map.Entry<String, String> element : dataEntry.getValue().entrySet()) {
                keys.add(element.getKey());
            }
        }
        return keys;
    }

    public int unsavedChangesNum() {
        int unsaved = 0;
        for (Map.Entry<DataFileHasher, DataFile> entry : datFiles.entrySet()) {
            unsaved += entry.getValue().unsavedChangesNum();
        }
        return unsaved;
    }

    public void open() throws Exception { //todo:
        if (isOpen) {
            return;
        }
        isOpen = true;
        datFiles = new TreeMap<>();

        if (!tableRoot.exists()) {
            tableRoot.mkdir();
        } else if (!tableRoot.isDirectory()) {
            throw new Exception(tableRoot.getAbsolutePath() + " - is not a directory");
        }

        for (int i = 0; i < NUMBER_DIRECTORIES; i++) {
            for (int j = 0; j < NUMBER_FILES_IN_DIRECTORY; j++) {
                DataFileHasher hasher = new DataFileHasher(i, j);
                DataFile newDataFile = new DataFile(tableRoot.getAbsolutePath(), hasher);
                datFiles.put(hasher, newDataFile);
            }
        }
    }

    public void close() throws Exception {
        isOpen = false;
        if (!datFiles.isEmpty()) {
            for (Map.Entry<DataFileHasher, DataFile> entry : datFiles.entrySet()) {
                entry.getValue().close();
            }

            File[] folders = tableRoot.listFiles();
            if (folders != null) {
                for (File folder : folders) {
                    if (folder.exists() && folder.isDirectory() && folder.list().length == 0) {
                        folder.delete();
                    }
                }
            }
            datFiles.clear();
        }
    }

    public void clear() {
        File[] folders = tableRoot.listFiles();
        if (folders == null) {
            return;
        }
        for (File folder : folders) {
            if (folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        file.delete();
                    }
                }
            }
            folder.delete();
        }
        tableRoot.delete();
    }

}
