package ru.nsu.ccfit.boltava.filter.serializer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.nsu.ccfit.boltava.filter.IFilter;
import ru.nsu.ccfit.boltava.filter.composite.AndFilter;
import ru.nsu.ccfit.boltava.filter.leaf.FileExtensionFilter;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class AndFilterSerializerTest {

    private String[] validBodies;
    private String[] invalidBodies;

    @Before
    public void setUp() throws Exception {
        validBodies = new String[] {
                "&(sequence)",
                "  &(sequence)  ",
                "  &  ( e v e n   chars)  ",
                "  &  ( f1 f2    f  3 )  ",
                "  &(  .h )",
                " &   (  .       cpp   )  "
        };

        invalidBodies = new String[] {
                "|(sequence)",
                "  &  ( o d d )  ",
                "a/b.java",
                "a/b/c/d/e/f/g.py",
                "java",
                " &     (  <   5555 >  9 .)",
                " &     (  <   5555 | (>  9 . )  )"
        };

    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void expectThrowOnNull() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Null pointer argument passed");
        new AndFilterSerializer().getFilter(null);
    }

    @Test
    public void expectThrowIllegalArgument() {

        for (String wrongFormat : invalidBodies) {
            try {
                new AndFilterSerializer().getFilter(wrongFormat);
            } catch (IllegalArgumentException e) {
                assertEquals("Wrong filter format: " + wrongFormat, e.getMessage());
            }
        }

    }

    @Test
    public void checkCreationOnValidPatterns() {
        AndFilterSerializer s = new AndFilterSerializer();

        for (String filterBody : validBodies) {
            assertEquals(AndFilter.class, s.getFilter(filterBody).getClass());
        }
    }

}