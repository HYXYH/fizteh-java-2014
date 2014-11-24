package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class cmdShowTables extends Command {

    public cmdShowTables() {
        setName("show");
    }

    @Override
    public String run(List<String> args) throws Exception {
        if (args.size() != 2 ) {
            throw new IllegalArgumentException(
                    "Illegal arguments for show tables");
        }
        if (!args.get(1).equals("tables")) {
            throw new Exception("show: no such command");
        }
        String resultMessage = "";
        List<String> tableNames = tableProvider.getTableNames();

        for (String tableName : tableNames) {
            if (!tableName.equals(currentTableName)) {
                tableProvider.getTable(tableName).open();
            }
            resultMessage += tableName + " " + tableProvider.getTable(tableName).size() + "\n";
            if (!tableName.equals(currentTableName)) {
                tableProvider.getTable(tableName).close();
            }
        }
        if (resultMessage.equals("")) {
            resultMessage = "no tables found";
        } else {
            resultMessage = resultMessage.substring(0, resultMessage.length() - 1);
        }
        return resultMessage;
    }
}
