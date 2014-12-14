package ru.fizteh.fivt.students.oscar_nasibullin.Storeable;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.storage.structured.TableProvider;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
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
                initTable(newTableFile.getName());
            }
        }
    }

    @Override
    public Table getTable(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Illegal argument for get table");
        }
        return tables.get(name);
    }

    private void initTable(String name) {
        try {
            if (!tables.containsKey(name)) {
                tables.put(name, new TableImpl(Paths.get(dbDir + "/" + name).toFile(),
                        (TableProvider) this));
            }
        } catch (Exception e) {
            throw new RuntimeException("signature.tsv for table <" + name + "> not found or corrupted", e);
        }
    }

    @Override
    public Table createTable(String name, List<Class<?>> columnTypes) throws IOException {
        if (name == null) {
            throw new IllegalArgumentException("Illegal argument for create");
        }
        if (!tables.containsKey(name)) {
            tables.put(name, new TableImpl(Paths.get(dbDir + "/" + name).toFile(), columnTypes,
                    (TableProvider) this));
            return tables.get(name);
        }
        return null;
    }

    @Override
    public void removeTable(String name) throws IOException {
        if (name == null) {
            throw new IllegalArgumentException("Illegal argument for remove");
        }
        if (!tables.containsKey(name)) {
            throw new IllegalStateException("Table not exist");
        }
        tables.get(name).clear();
        tables.remove(name);
    }

    @Override
    public Storeable deserialize(Table table, String value) throws ParseException {
        List<String> strings = parseToList(value); // todo can be ""
        List<Object> values = new ArrayList<>();
        List<Class<?>> types = new ArrayList<>();
        int i = 0;
        try {
            for (i = 0; i < table.getColumnsCount(); i++) {
                values.add(castToType(new String(strings.get(i)), table.getColumnType(i)));
                types.add(table.getColumnType(i));
            }
        } catch (Exception e) {
            throw new ParseException(e.getMessage(), value.indexOf(strings.get(i)));
        }
        Storeable result = new StoreableImpl(values, types);
        return result;
    }

    private Object castToType(String value, Class<?> type) throws Exception {
        if (value.replaceAll("[ ]","").equals("null")) {
            return null;
        }
        switch(type.getName()) {
            case "java.lang.Integer":
                return Integer.parseInt(value);
                //break;
            case "java.lang.Long":
                return Long.parseLong(value);
                //break;
            case "java.lang.Byte":
                return Byte.parseByte(value);
                //break;
            case "java.lang.Float":
                return Float.parseFloat(value);
                //break;
            case "java.lang.Double":
                return Double.parseDouble(value);
                //break;
            case "java.lang.Boolean":
                return Boolean.parseBoolean(value);
                //break;
            case "java.lang.String":
                return value;
                //break;

        }
        return null;
    }

    private List<String> parseToList(String value) throws ParseException {
        List<String> result = new ArrayList<>();
        if (value.startsWith("[") && value.endsWith("]")) {
            value = value.substring(1, value.length() - 1);
        } else {
            throw new ParseException("expected [ ... ]", 0);
        }
        boolean quote = false;
        boolean slash = false;
        String current = "";
        int position = -1;
        for (char c : value.toCharArray()) {
            position++;
            if (slash) { // todo:  /t /r /u ...
                current += c;
                slash = false;
            } else {
                switch (c) {
                    case '\\':
                        slash = true;
                        break;
                    case '\"':
                        quote = !quote;
                        break;
                    case ',':
                        if (quote) {
                            current += ",";
                        } else {
                            result.add(new String(current));
                            current = "";
                        }
                        break;
                    case ' ':
                        if (quote) {
                            current += " ";
                        }
                        break;
                    default:
                        if (!quote && (c == '[' || c == ']')) {
                            throw new ParseException("unexpected " + String.valueOf(c), position);
                        } else {
                            current += c;
                        }
                        break;
                }
            }
        }
        result.add(current);
        return result;
    }

    @Override
    public String serialize(Table table, Storeable value) throws ColumnFormatException {
        if (!((TableImpl) table).storeableIsSuitable((StoreableImpl) value)) {
            throw new ColumnFormatException("Can't serialize: type mismatch");
        }
        String result = "[ ";
        List<String> values = new ArrayList<>();
        for (int i = 0; i < table.getColumnsCount(); i++) {
            if (value.getColumnAt(i) == null) {
                values.add("null");
            } else if (table.getColumnType(i) == String.class) {
                values.add("\""+ value.getColumnAt(i) + "\"");
            } else {
                values.add(table.getColumnType(i).cast(value.getColumnAt(i)).toString());
            }
        }
        result += String.join(", ", values);
        result += " ]";
        return result;
    }

    @Override
    public Storeable createFor(Table table) {
        List<Object> values = new ArrayList<>();
        for(int i = 0; i < table.getColumnsCount(); i++) {
            values.add(null);
        }
        return new StoreableImpl(values, ((TableImpl) table).getTypes());
    }

    @Override
    public Storeable createFor(Table table, List<?> values) throws ColumnFormatException, IndexOutOfBoundsException {
        if (values.size() != table.getColumnsCount()) {
            throw new IndexOutOfBoundsException();
        }
        List<Object> valuesToPut = new ArrayList<>();
        for(int i = 0; i < table.getColumnsCount(); i++) {
            try {
                valuesToPut.add(table.getColumnType(i).cast(values.get(i)));
            } catch (Exception e) {
                throw new ColumnFormatException("Can't cast <" +
                        values.get(i).toString() + "> to <" + table.getColumnType(i) );
            }
        }
        return new StoreableImpl(valuesToPut, ((TableImpl) table).getTypes());
    }

    @Override
    public List<String> getTableNames() {
        List<String> names = new ArrayList<String>();
        for (Map.Entry<String, TableImpl> entry : tables.entrySet()) {
            names.add(entry.getKey());
        }
        return names;
    }
}
