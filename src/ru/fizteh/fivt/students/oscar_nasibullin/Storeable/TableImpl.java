package ru.fizteh.fivt.students.oscar_nasibullin.Storeable;

import ru.fizteh.fivt.storage.structured.TableProvider;
import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;

import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *  Created by Oskar on 15.11.14.
 */
public class TableImpl implements Table {

    static final int NUMBER_DIRECTORIES = DataFileHasher.HASH_NUMBER;
    static final int NUMBER_FILES_IN_DIRECTORY = DataFileHasher.HASH_NUMBER;

    private Map<DataFileHasher, DataFile> datFiles;
    private TableProvider provider;
    private List<Class<?>> types;
    public boolean isOpen;
    private final File tableRoot;

    public TableImpl(File table, List<Class<?>> columnTypes, TableProvider myProvider)  {
        provider = myProvider;
        types = columnTypes;
        tableRoot = table;
        isOpen = false;
        try {
            open();
            close();
            File tsvFile =  tableRoot.toPath().resolve("signature.tsv").toFile();
            tsvFile.createNewFile();
            RandomAccessFile sign = new RandomAccessFile( tsvFile, "rw");
            for (Class<?> type : types) {
                sign.writeBytes(convertClassName(type.getName()) + " ");
            }
            sign.close();
        } catch (Exception e) {
            if (e.getMessage().endsWith("is not a directory")) {
                throw new RuntimeException("Can't create table: " + e.getMessage());
            }
            throw new RuntimeException(e);
        }
    }

    public TableImpl(File table, TableProvider myProvider) throws IOException, ClassNotFoundException {
        provider = myProvider;
        tableRoot = table;
        isOpen = false;
        try {
            open();
            close();
            types = new ArrayList<>();
            byte[] data = Files.readAllBytes(tableRoot.toPath().resolve("signature.tsv"));
            String[] typeNames = new String(data).split(" ");
            for (String typeName : typeNames) {
                types.add(Class.forName(convertClassName(typeName)));
            }

        } catch (Exception e) {
            if (e.getMessage().endsWith("is not a directory")) {
                throw new RuntimeException("Can't create table: " + e.getMessage());
            } else {
                throw new RuntimeException(e);
            }
        }
    }


    public void open() throws Exception { //todo:
        if (isOpen) {
            return;
        }
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
        isOpen = true;
    }

    public void close() throws Exception {
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
        isOpen = false;
    }

    private void checkTableIsOpen() {
        if (!isOpen) {
            throw new RuntimeException("Trying to operate with table <" + getName()+"> which is not open");
        }
    }

    public static String convertClassName (String name) {
        if (name.startsWith("java.lang.")) {
            name = name.replaceAll("(java.lang.)", "");
            if (name.equals("String")) {
                return name;
            }
        }
        if (name.equals("int")) {
            name = "java.lang.Integer";
        } else if (name.equals("Integer")) {
            name = "int";
        } else if (name.equals("String")) {
            return "java.lang.String";
        } else if (name.substring(0,1).equals(name.substring(0,1).toUpperCase())) {
            return name.toLowerCase();
        } else {
            name = "java.lang." + name.substring(0,1).toUpperCase() + name.substring(1, name.length());
        }
        return name;
    }

    public boolean storeableIsSuitable (StoreableImpl value) {
        List<Class<?>> storedTypes = value.getTypes();
        return types.equals(storedTypes);
    }

    public List<Class<?>> getTypes () {
        return new ArrayList<>(types);
    }

    @Override
    public Storeable put(String key, Storeable value) throws ColumnFormatException {
        checkTableIsOpen();
        if (key == null || value == null) {
            throw new IllegalArgumentException("Illegal arguments for put");
        }
        if (!storeableIsSuitable((StoreableImpl) value)) {
            throw new ColumnFormatException("Can't put: type mismatch");
        }
        DataFile data = datFiles.get(new DataFileHasher(key));
        Storeable result = null;
        try {
            if (data.get(key) != null) {
                result = provider.deserialize((Table) this, data.get(key));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e); //todo: try to catch in shell
        }
        data.remove(key);
        data.put(key,provider.serialize((Table) this, value));

        return result;
    }

    @Override
    public Storeable remove(String key) {
        checkTableIsOpen();
        if (key == null) {
            throw new IllegalArgumentException("Illegal argument for remove");
        }
        DataFile data = datFiles.get(new DataFileHasher(key));
        Storeable result = null;
        if (data.containsKey(key)) {
            try {
                result = provider.deserialize((Table) this, data.get(key));
            } catch (ParseException e) {
                throw new RuntimeException(e); //todo: try to catch in shell
            }
            data.remove(key);
        }
        return result;
    }

    @Override
    public int size() {
        checkTableIsOpen();
        int totalSize = 0;
        for (Map.Entry<DataFileHasher, DataFile> entry : datFiles.entrySet()) {
            totalSize += entry.getValue().size();
        }
        return totalSize;
    }

    @Override
    public List<String> list() {
        checkTableIsOpen();
        List<String> keys = new ArrayList<String>();
        for (Map.Entry<DataFileHasher, DataFile> dataEntry : datFiles.entrySet()) {
            for (Map.Entry<String, String> element : dataEntry.getValue().entrySet()) {
                keys.add(element.getKey());
            }
        }
        return keys;
    }

    @Override
    public int commit() throws IOException {
        checkTableIsOpen();
        int saved = 0;
        for (Map.Entry<DataFileHasher, DataFile> entry : datFiles.entrySet()) {
            saved += entry.getValue().commit();
        }
        return saved;
    }

    @Override
    public int rollback() {
        checkTableIsOpen();
        int changesRolled = 0;
        for (Map.Entry<DataFileHasher, DataFile> entry : datFiles.entrySet()) {
            changesRolled += entry.getValue().rollback();
        }
        return changesRolled;
    }

    @Override
    public int getNumberOfUncommittedChanges() {
        checkTableIsOpen();
        int unsaved = 0;
        for (Map.Entry<DataFileHasher, DataFile> entry : datFiles.entrySet()) {
            unsaved += entry.getValue().unsavedChangesNum();
        }
        return unsaved;
    }

    @Override
    public int getColumnsCount() {
        return types.size();
    }

    @Override
    public Class<?> getColumnType(int columnIndex) throws IndexOutOfBoundsException {
        return types.get(columnIndex);
    }

    @Override
    public String getName() {
        return tableRoot.getName();
    }

    @Override
    public Storeable get(String key) {
        checkTableIsOpen();
        if (key == null) {
            throw new IllegalArgumentException("Illegal argument for get");
        }
        DataFile data = datFiles.get(new DataFileHasher(key));
        if (data.containsKey(key)) {
            try {
                return provider.deserialize((Table) this, data.get(key));
            } catch (ParseException e) {
                throw new RuntimeException(e); //todo: try to catch in shell
            }
        }
        return null;
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
