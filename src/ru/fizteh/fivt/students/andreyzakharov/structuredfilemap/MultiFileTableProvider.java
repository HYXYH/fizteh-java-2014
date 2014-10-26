package ru.fizteh.fivt.students.andreyzakharov.structuredfilemap;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.storage.structured.TableProvider;
import ru.fizteh.fivt.students.andreyzakharov.structuredfilemap.serialized.TableEntryJsonReader;
import ru.fizteh.fivt.students.andreyzakharov.structuredfilemap.serialized.TableEntryJsonWriter;
import ru.fizteh.fivt.students.andreyzakharov.structuredfilemap.serialized.TableEntryReader;
import ru.fizteh.fivt.students.andreyzakharov.structuredfilemap.serialized.TableEntryWriter;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiFileTableProvider implements AutoCloseable, TableProvider {
    private Path dbRoot;
    private Map<String, MultiFileTable> tables;
    private MultiFileTable activeTable;
    TableEntryReader reader = new TableEntryJsonReader();
    TableEntryWriter writer = new TableEntryJsonWriter();

    MultiFileTableProvider(Path dbPath) throws ConnectionInterruptException {
        if (!Files.exists(dbPath)) {
            try {
                Files.createDirectory(dbPath);
            } catch (IOException e) {
                throw new ConnectionInterruptException("connection: destination does not exist, can't be created");
            }
        }
        if (!Files.isDirectory(dbPath)) {
            throw new ConnectionInterruptException("connection: destination is not a directory");
        }
        dbRoot = dbPath;
        open();
    }

    @Override
    public Table getTable(String name) {
        if (name == null) {
            throw new IllegalArgumentException("null argument");
        }
        return tables.get(name);
    }

    public MultiFileTable getCurrent() {
        return activeTable;
    }

    public Map<String, MultiFileTable> getAllTables() {
        return tables;
    }

    @Override
    public Table createTable(String name, List<Class<?>> columnTypes) throws IOException {
        if (name == null) {
            throw new IllegalArgumentException("null argument");
        }
        Path path = dbRoot.resolve(name);
        if (!tables.containsKey(name)) {
            if (Files.exists(path)) {
                if (!Files.isDirectory(path)) {
                    throw new IOException("connection: destination is not a directory");
                }
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                    if (stream.iterator().hasNext()) {
                        throw new IOException("connection: destination is not empty");
                    }
                }
            }
            tables.put(name, new MultiFileTable(dbRoot.resolve(name), reader, writer));
            return tables.get(name);
        } else {
            return null;
        }
    }

    @Override
    public void removeTable(String name) {
        if (name == null) {
            throw new IllegalArgumentException("null argument");
        }
        MultiFileTable table = tables.get(name);
        if (table != null) {
            if (activeTable == table) {
                activeTable = null;
            }
            tables.remove(name);
            try {
                table.delete();
            } catch (ConnectionInterruptException e) {
                //
            }
        } else {
            throw new IllegalStateException("table does not exist");
        }
    }

    public void useTable(String name) throws IllegalStateException {
        if (name == null) {
            throw new IllegalArgumentException("null argument");
        }
        MultiFileTable table = tables.get(name);
        if (table != null) {
            activeTable = table;
        } else {
            throw new IllegalStateException("table does not exist");
        }
    }

    @Override
    public Storeable deserialize(Table table, String value) throws ParseException {
        Storeable entry = reader.deserialize(value);
        // verify
        return entry;
    }

    @Override
    public String serialize(Table table, Storeable value) throws ColumnFormatException {
        String data = writer.serialize(value);
        // verify
        return data;
    }

    @Override
    public Storeable createFor(Table table) {
        return null;
    }

    @Override
    public Storeable createFor(Table table, List<?> values) throws ColumnFormatException, IndexOutOfBoundsException {
        return null;
    }

    public void open() throws ConnectionInterruptException {
        if (tables == null) {
            tables = new HashMap<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dbRoot)) {
                for (Path file : stream) {
                    if (Files.isDirectory(file)) {
                        MultiFileTable table = new MultiFileTable(file, reader, writer);
                        table.load();
                        tables.put(file.getFileName().toString(), table);
                    }
                }
            } catch (IOException e) {
                throw new ConnectionInterruptException("connection: unable to load the database");
            }
        }
    }

    @Override
    public void close() {
        if (tables != null) {
            try {
                for (MultiFileTable table : tables.values()) {
                    table.unload();
                }
            } catch (ConnectionInterruptException e) {
                // suppress the exception
            }
        }
    }
}
