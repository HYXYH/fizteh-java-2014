package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TableTest {
    TableImpl testTable;

    @Before
    public void setUp() throws Exception {
        File tableFile = Paths.get("testTable").toFile();
        if (!tableFile.exists()) {
            tableFile.mkdir();
        }
        testTable = new TableImpl(tableFile);
        testTable.open();
    }

    @After
    public void tearDown() throws Exception {
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
        assertEquals(null, testTable.put("key", "val"));
        assertEquals(null, testTable.put("k", "v"));
        assertEquals(null, testTable.put("x", "y"));
    }

    @Test
    public void testPutOverwritelement() throws Exception {
        testTable.put("key", "val");
        testTable.put("k", "v");
        testTable.put("x", "y");
        assertEquals("val", testTable.put("key", "value"));
        assertEquals("v", testTable.put("k", "v2"));
        assertEquals("y", testTable.put("x", "y2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutThrowsIllegalArgumentException() throws Exception {
        testTable.put(null, "value2");
    }

    @Test
    public void testGet() throws Exception {
        testTable.put("key", "value");
        testTable.put("k", "v");
        testTable.put("x", "y");
        assertEquals("value", testTable.get("key"));
        assertEquals("v", testTable.get("k"));
        assertEquals("y", testTable.get("x"));

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
        testTable.put("key", "value");
        assertEquals("value", testTable.remove("key"));
    }

    @Test
    public void testSize() throws Exception {
        testTable.put("k", "v");
        testTable.put("x", "y");
        assertEquals(2, testTable.size());
    }

    @Test
    public void testCommit() throws Exception {
        testTable.put("k", "v");
        testTable.put("x", "y");
        assertEquals(2, testTable.commit());
    }

    @Test
    public void testRollback() throws Exception {
        testTable.put("k", "v");
        assertEquals(1, testTable.rollback());
    }

    @Test
    public void testList() throws Exception {
        testTable.put("k", "v");
        testTable.put("x", "y");
        List<String> trueResult = new ArrayList<String>(Arrays.asList(new String[]{"x", "k"}));
        assertEquals(trueResult, testTable.list());
    }
}
