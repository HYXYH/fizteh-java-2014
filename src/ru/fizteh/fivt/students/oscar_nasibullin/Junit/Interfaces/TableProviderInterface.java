package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Interfaces;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.Table;

import java.util.List;

/**
 * @author Fedor Lavrentyev
 * @author Dmitriy Komanov
 */
public interface TableProviderInterface {

    /**
     * Возвращает таблицу с указанным названием.
     *
     * @param name Название таблицы.
     * @return Объект, представляющий таблицу. Если таблицы с указанным именем не существует, возвращает null.
     * @throws IllegalArgumentException Если название таблицы null или имеет недопустимое значение.
     */
    Table getTable(String name);


    /**
      * Возвращает имена существующих таблиц, которые могут быть получены с помощью {@link #getTable(String)}.
      *
      * @return Имена существующих таблиц.
      */
    List<String> getTableNames();

    /**
     * Создаёт таблицу с указанным названием.
     *
     * @param name Название таблицы.
     * @return Объект, представляющий таблицу. Если таблица уже существует, возвращает null.
     * @throws IllegalArgumentException Если название таблицы null или имеет недопустимое значение.
     */
    Table createTable(String name);

    /**
     * Удаляет таблицу с указанным названием.
     *
     * @param name Название таблицы.
     * @throws IllegalArgumentException Если название таблицы null или имеет недопустимое значение.
     * @throws IllegalStateException Если таблицы с указанным названием не существует.
     */
    void removeTable(String name);
}
