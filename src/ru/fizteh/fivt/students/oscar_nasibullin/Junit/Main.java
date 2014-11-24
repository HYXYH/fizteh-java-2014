package ru.fizteh.fivt.students.oscar_nasibullin.Junit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands.*;


/**
 *  Created by Oskar on 23.11.14.
 */
public class Main {

    private Main() {
        // Disable instantiation to this class.
    }

    public static void main(final String[] args) {
        try {
            TableProviderFactory factory = new TableProviderFactory();
            TableProvider provider = factory.create(System.getProperty("fizteh.db.dir"));


            Command[] commands = {
                    new cmdCommit(), new cmdCreate(), new cmdDrop(), new cmdExit(),
                    new cmdGet(), new cmdList(), new cmdPut(), new cmdRemove(),
                    new cmdRollback(), new cmdShowTables(), new cmdUse()
            };

            List<Command> commandList = new ArrayList<Command>(Arrays.asList(commands));
            Command.currentTableName = "";
            Command.tableProvider = provider;
            Shell client = new Shell(commandList, args);
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
