package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableProvider;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TableProviderTest {
    TableProvider provider;

    @Before
    public void setUp() throws Exception {
        File tableFile = Paths.get("testTableProvider").toFile();
        if (!tableFile.exists()) {
            tableFile.mkdir();
        }
        provider = new TableProvider(tableFile.getAbsolutePath());

    }

    @After
    public void tearDown() throws Exception {
        File[] files = Paths.get("testTableProvider").toFile().listFiles();
        for (File file : files) {
            file.delete();
        }
        Paths.get("testTableProvider").toFile().delete();
    }

    @Test
    public void testGetTable() throws Exception {
        provider.createTable("table");
        assertEquals("table", provider.getTable("table").getName());
    }

    @Test
    public void testGetUnexistTable() throws Exception {
        assertEquals(null, provider.getTable("table"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTableThrowsIllegalArgumentException() throws Exception {
       provider.getTable(null);
    }

    @Test
    public void testGetTableNames() throws Exception {
        provider.createTable("table1");
        provider.createTable("table2");
        provider.createTable("table3");
        List<String> trueResult = new ArrayList<String>(Arrays.asList(new String[]{"table1", "table2", "table3"}));
        assertEquals(trueResult, provider.getTableNames());

    }

    @Test
    public void testCreateTable() throws Exception {
        assertEquals(provider.createTable("table"), provider.getTable("table"));
        assertEquals(null, provider.createTable("table"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTableThrowsIllegalArgumentException() throws Exception {
        provider.createTable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveTableThrowsIllegalArgumentException() throws Exception {
        provider.removeTable(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveTableThrowsIllegalStateException() throws Exception {
        provider.removeTable("unexisting_table");
    }

    @Test
    public void testRemoveTable() throws Exception {
        provider.createTable("table");
        assertEquals(1, provider.getTableNames().size());
        provider.removeTable("table");
        assertEquals(0, provider.getTableNames().size());
    }
}