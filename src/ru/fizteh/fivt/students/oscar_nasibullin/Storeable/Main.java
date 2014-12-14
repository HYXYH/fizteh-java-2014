package ru.fizteh.fivt.students.oscar_nasibullin.Storeable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.fizteh.fivt.students.oscar_nasibullin.Storeable.Comands.*;


/**
 *  Created by Oskar on 23.11.14.
 */
public class Main {

    private Main() {
        // Disable instantiation to this class.
    }

    public static void main(final String[] args) {
        try {
            TableProviderFactoryImpl factory = new TableProviderFactoryImpl();
            TableProviderImpl provider = factory.create(System.getProperty("fizteh.db.dir"));


            Command[] commands = {
                    new CmdCommit(), new CmdCreate(), new CmdDrop(), new CmdExit(),
                    new CmdGet(), new CmdList(), new CmdPut(), new CmdRemove(),
                    new CmdRollback(), new CmdShowTables(), new CmdUse()
            };

            List<Command> commandList = new ArrayList<>(Arrays.asList(commands));
            Command.currentTableName = "";
            Command.tableProvider = provider;
            Shell client = new Shell(commandList, args);
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            //e.printStackTrace();
            System.exit(1);
        }
    }
}
