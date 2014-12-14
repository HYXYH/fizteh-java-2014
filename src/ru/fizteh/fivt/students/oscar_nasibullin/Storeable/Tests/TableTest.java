package ru.fizteh.fivt.students.oscar_nasibullin.Storeable.Tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.fizteh.fivt.storage.structured.*;
import ru.fizteh.fivt.students.oscar_nasibullin.Storeable.TableProviderImpl;
import ru.fizteh.fivt.students.oscar_nasibullin.Storeable.TableImpl;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TableTest {
    Table testTable;
    List<Class<?>> types;
    Storeable testValue1;
    Storeable testValue2;
    Storeable testValue3;
    TableProvider provider;

    @Before
    public void setUp() throws Exception {
        Class<?>[] t = {Integer.class, Long.class, Byte.class, Float.class, Double.class, Boolean.class, String.class};
        types = new ArrayList<>(Arrays.asList(t));
        File tableFile = Paths.get("testTable").toFile();
        if (!tableFile.exists()) {
            tableFile.mkdir();
        }
        provider = new TableProviderImpl(tableFile.getAbsolutePath());
        provider.createTable("testTable", types);
        testTable = provider.getTable("testTable");
        testValue1 = provider.createFor(testTable);
        testValue2 = provider.createFor(testTable);
        testValue3 = provider.createFor(testTable);
        testValue1.setColumnAt(6, "some text for testing, other fields are nulls");
        testValue2.setColumnAt(4, 0.7777777);
        ((TableImpl) testTable).open();
    }

    @After
    public void tearDown() throws Exception {
        File[] files = Paths.get("testTable").resolve("testTable").toFile().listFiles();
        for (File file : files) {
            file.delete();
        }
        files = Paths.get("testTable").toFile().listFiles();
        for (File file : files) {
            file.delete();
        }
        Paths.get("testTable").toFile().delete();
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("testTable", testTable.getName());
    }

    @Test
    public void testGetUnexistElement() throws Exception {
        assertEquals(null, testTable.get("key_which_is_not_in_table"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetThrowsIllegalArgumentException() throws Exception {
        testTable.get(null);
    }

    @Test
    public void testPutNewElement() throws Exception {
        assertEquals(null, testTable.put("key", testValue1));
        assertEquals(null, testTable.put("k", testValue2));
        assertEquals(null, testTable.put("x", testValue3));
    }

    @Test
    public void testPutOverwritelement() throws Exception {
        testTable.put("key", testValue1);
        testTable.put("x", testValue3);
        testTable.put("k", testValue2);
        assertEquals(testValue1, testTable.put("key", testValue2));
        assertEquals(testValue2, testTable.put("k", testValue3));
        assertEquals(testValue3, testTable.put("x", testValue1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutThrowsIllegalArgumentException() throws Exception {
        testTable.put(null, testValue1);
    }

    @Test
    public void testGet() throws Exception {
        testTable.put("key", testValue1);
        testTable.put("k", testValue2);
        testTable.put("x", testValue3);
        assertEquals(testValue1, testTable.get("key"));
        assertEquals(testValue2, testTable.get("k"));
        assertEquals(testValue3, testTable.get("x"));

    }

    @Test
    public void testRemoveUnexistElement() throws Exception {
        assertEquals(null, testTable.remove("key_which_is_not_in_table"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveThrowsIllegalArgumentException() throws Exception {
        testTable.remove(null);
    }

    @Test
    public void testRemove() throws Exception {
        testTable.put("key", testValue1);
        assertEquals(testValue1, testTable.remove("key"));
    }

    @Test
    public void testSize() throws Exception {
        testTable.put("k", testValue1);
        testTable.put("x", testValue2);
        assertEquals(2, testTable.size());
    }

    @Test
    public void testCommitNewElements() throws Exception {
        testTable.put("k", testValue1);
        testTable.put("x", testValue2);
        assertEquals(2, testTable.commit());
    }

    @Test
    public void testCommitRemoveElements() throws Exception {
        testTable.put("k", testValue1);
        testTable.put("x", testValue2);
        assertEquals(2, testTable.commit());
        testTable.remove("k");
        testTable.remove("x");
        assertEquals(2, testTable.commit());
    }

    @Test
    public void testRollback() throws Exception {
        testTable.put("k", testValue1);
        assertEquals(1, testTable.rollback());
    }

    @Test
    public void testRollbackRemovedElements() throws Exception {
        testTable.put("k", testValue1);
        testTable.put("x", testValue2);
        assertEquals(2, testTable.commit());
        testTable.remove("x");
        assertEquals(1, testTable.rollback());
    }

    @Test
    public void testList() throws Exception {
        testTable.put("k", testValue1);
        testTable.put("x", testValue2);
        List<String> trueResult = new ArrayList<>(Arrays.asList(new String[]{"x", "k"}));
        assertEquals(trueResult, testTable.list());
    }
}
