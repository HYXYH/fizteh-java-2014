package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableProviderImpl;

import java.util.List;

/**
 * Created by Oskar on 19.11.14.
 */
public abstract class Command {

    private String name;
    public static TableProviderImpl tableProvider;
    public static String currentTableName; // or even whole Table ?

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    /**
     * Выполняет что-то.
     *
     * @param args Список аргументов.
     * @return Сообщение с результатом работы. Если возвращает null, то на экран ничего не печатается.
     *
     * @throws Exception проброс наверх любого исключения.
     */
    public String run(List<String> args) throws Exception {
        return null;
    }
}
