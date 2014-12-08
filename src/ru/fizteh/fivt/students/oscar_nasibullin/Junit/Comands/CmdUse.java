package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdUse extends Command {

    public CmdUse() {
        setName("use");
        setArgs(2);
    }

    @Override
    public String run(List<String> args) throws Exception {
        checkArgumentsAmount(args);
        String resultMessage;
        TableImpl newTable = tableProvider.getTable(args.get(1));
        if (newTable != null) {
            if (tableProvider.getTable(currentTableName) != null) {
                Integer unsaved = tableProvider.getTable(currentTableName).unsavedChangesNum();
                if (unsaved == 0) {
                    tableProvider.getTable(currentTableName).close();
                } else {
                    throw new Exception(unsaved.toString() + " unsaved changes");
                }
            }
            currentTableName = args.get(1);
            tableProvider.getTable(currentTableName).open();
            resultMessage = "using " + currentTableName;
        } else {
            resultMessage = args.get(1) + " not exists";
        }

        return resultMessage;
    }
}
