package ru.fizteh.fivt.students.oscar_nasibullin.Storeable;

import ru.fizteh.fivt.storage.structured.TableProviderFactory;

import java.io.File;
import java.nio.file.Paths;

/**
 *  Created by Oskar on 15.11.14.
 */
public class TableProviderFactoryImpl implements TableProviderFactory {
    @Override
    public TableProviderImpl create(String dir) throws  IllegalArgumentException {
        if (dir == null) {
            throw new IllegalArgumentException("TabeProviderFactory: no directory");
        }

        try {
            File dbFile = Paths.get(dir).toFile();
            if (!dbFile.exists()) {
                if (dbFile.mkdir()) {
                   // System.err.println("created root folder: " + dir);   Todo: add to log in Proxy
                } else {
                   //  Todo: add to log in Proxy
                    throw new IllegalArgumentException("TabeProviderFactory: folder not exist and cannot be created");
                }
            }
        } catch (Exception e) {
           //  Todo: add to log in Proxy
            throw new IllegalArgumentException("TabeProviderFactory: database root directory error: " + e.getMessage());
        }
        return new TableProviderImpl(dir);
    }
}
