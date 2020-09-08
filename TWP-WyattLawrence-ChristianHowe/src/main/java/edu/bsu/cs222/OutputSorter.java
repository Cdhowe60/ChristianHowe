package edu.bsu.cs222;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Test;
import java.util.*;
import java.util.Map.Entry;

public class OutputSorter {

    @Test
    public String sortByMostRecent(String searchText, boolean isMostFrequent) {
        WikipediaJSONGetter wikipediaJSONGetter = new WikipediaJSONGetter();
        JsonObject pages = wikipediaJSONGetter.JSONGetter(searchText);

        //This code was based off the work of Professor Gestwicki. Namely from the video "Parsing JSON using GSON within a Maven project in IntelliJ IDEA"
        JsonArray revisionsArray = null;
        for (Entry<String, JsonElement> entry : pages.entrySet()) {
            JsonObject entryObject = entry.getValue().getAsJsonObject();
            revisionsArray = entryObject.getAsJsonArray("revisions");
        }

        String[] usernameAndTimestampArray = new String[24];
        String[] usernameOnlyArray = new String[24];

        try {
            for (int i = 0; i < revisionsArray.size(); i++) {

            String fullInfo = String.valueOf(revisionsArray.get(i)).replace("{", "")
                    .replace("}", "").replace(":", ": ")
                    .replace("u", "U").replace("t", "T")
                    .replace("[", "").replace("]", "");
            fullInfo += "\n";
            usernameAndTimestampArray[i] = fullInfo;
            int ofComma = fullInfo.indexOf(",");
            String userNameOnly = String.valueOf(revisionsArray.get(i)).substring(9, ofComma - 1);
            usernameOnlyArray[i] = userNameOnly;
        }

        if (isMostFrequent == true) {
            String mostFrequentList = sortByMostFrequent(usernameOnlyArray);
            return mostFrequentList;
        } else {
            String mostRecentList = Arrays.toString(usernameAndTimestampArray);
            mostRecentList.replaceAll(",", "\n").toLowerCase();
            mostRecentList.replace("[","").toLowerCase();
            return mostRecentList;
        }
        } catch (NullPointerException e){
            return "Connection not found.";
        }
    }

    public String sortByMostFrequent(String[] userNames){

        List unsortedUserList = Arrays.asList(userNames);
        Set<String> unsortedUserSet = new HashSet<String>(unsortedUserList);
        HashMap<String, Integer> userSorted = new HashMap<>();
        for (String s: unsortedUserSet) {
            userSorted.put(s, Collections.frequency(unsortedUserList, s));
        }
        String mostFrequentList = String.valueOf(sortByValueInMap(userSorted));
        return mostFrequentList;
    }

    public static String sortByValueInMap(HashMap<String, Integer> hm)
    {
        String mostFrequentList="";
        List<Entry<String, Integer> > list =
                new LinkedList<>(hm.entrySet());

        Collections.sort(list, new Comparator<Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        Iterator<Entry<String, Integer>> iterator = temp.entrySet().iterator();

        while (iterator.hasNext()){
            mostFrequentList+=iterator.next()+" Edit(s), "+"\n";
        }
        return mostFrequentList;
    }
}

