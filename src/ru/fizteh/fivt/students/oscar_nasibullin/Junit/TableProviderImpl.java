package ru.fizteh.fivt.students.oscar_nasibullin.Junit;

import ru.fizteh.fivt.storage.strings.TableProvider;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Oskar on 15.11.14.
 */
public class TableProviderImpl implements TableProvider {

    private Map<String, TableImpl> tables;
    private String dbDir;

    public TableProviderImpl(String dir) {
        tables = new TreeMap<>();
        dbDir = dir;
        File[] tableFiles = Paths.get(dbDir).toFile().listFiles();
        for (File newTableFile : tableFiles) {
            if (newTableFile.isDirectory()) {
                createTable(newTableFile.getName());
            }
        }
    }

    @Override
    public TableImpl getTable(String name)  throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Illegal argument for get table");
        }
        return tables.get(name); // todo: check for null
    }


    public List<String> getTableNames()  {
        List<String> names = new ArrayList<String>();
        for (Map.Entry<String, TableImpl> entry : tables.entrySet()) {
            names.add(entry.getKey());
        }
        return names;
    }

    @Override
    public TableImpl createTable(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Illegal argument for create");
        }

        if (!tables.containsKey(name)) {
            tables.put(name, new TableImpl(Paths.get(dbDir + "/" + name).toFile()));
            return tables.get(name);
        }
        return null;
    }

    @Override
    public void removeTable(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Illegal argument for remove");
        }
        if (!tables.containsKey(name)) {
            throw new IllegalStateException("Table not exist");
        }
        tables.get(name).clear();
        tables.remove(name);
    }
}
