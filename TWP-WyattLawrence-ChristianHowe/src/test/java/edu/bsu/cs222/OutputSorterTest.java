package edu.bsu.cs222;

import org.junit.Test;

public class OutputSorterTest {

    @Test
    public void OutputSorterTest() {
        OutputSorter outputSorter = new OutputSorter();
        outputSorter.sortByMostRecent("Soup", true);
    }

}
