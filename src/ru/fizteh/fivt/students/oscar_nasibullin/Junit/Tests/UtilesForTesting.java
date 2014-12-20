package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Tests;

import java.io.File;

/**
 * Created by Oskar on 21.12.14.
 */
public class UtilesForTesting {
    public static void deleteRecursivly(File fileToDelete) {
        if (fileToDelete.isDirectory()) {
            File[] files = fileToDelete.listFiles();
            for (File file : files) {
                deleteRecursivly(file);
            }
        }
        fileToDelete.delete();
    }
}
