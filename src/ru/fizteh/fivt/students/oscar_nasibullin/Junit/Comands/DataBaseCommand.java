package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableProviderImpl;

/**
 * Created by Oskar on 20.12.14.
 */
public abstract class DataBaseCommand extends Command {
    public static TableProviderImpl tableProvider;
    public static String currentTableName;

    public DataBaseCommand(String name, int numOfArgs) {
        super(name, numOfArgs);
    }

    public DataBaseCommand(String name) {
        super(name);
    }
}
