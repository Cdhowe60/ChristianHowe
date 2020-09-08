package edu.bsu.cs222;

import org.junit.Test;

public class WikipediaJSONGetterTest {

    @Test
    public void WikipediaJSONGetterTest() {
        WikipediaJSONGetter wikipediaJSONGetter = new WikipediaJSONGetter();
        wikipediaJSONGetter.JSONGetter("Soup");
    }
}
