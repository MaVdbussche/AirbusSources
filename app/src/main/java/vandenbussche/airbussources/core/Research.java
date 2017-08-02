package vandenbussche.airbussources.core;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;

import vandenbussche.airbussources.database.SQLUtility;

/**
 * This class is responsible for the processing of the research functions
 */
public abstract class Research {

    /**
     * This method provides all the products' names matching the research. Results should be sorted alphabetically.
     * I am not too happy with my code though and my come back on it later. I would like, for example,
     * to have it showing results ordered by pertinence (for example, results matching 3 words from <#param>research</#param> would appear
     * before those matchin only 2). But for now it should work at least.
     * @param research the words the research is being performed on
     * @return An ArrayList<String> containing the names of the products matching the research
     */
    public static ArrayList<String> getProductsNames(Context context, ArrayList<String> research){
        SQLUtility db = SQLUtility.prepareDataBase(context);
        ArrayList<String> result = new ArrayList<>();

        for (String word:research)
        {
            ArrayList<String> resultsForOneWord = db.getElementFromDB("Product", "Name", "Name LIKE "+"\'%"+word+"%\'");
            //Now we will add all the results for this word (there will in fact probably be one most of the time)
            //to the global result ArrayList.
            //This looks pretty inefficient to me, but can't think of anything better right now
            for (String oneResult:resultsForOneWord){
                result.add(oneResult);
            }
        }
        order(result);
        return result;
    }

    /**
     * This option removes all duplicates and sort an ArrayList of Strings
     * @param list the list to be processed
     * @return an ArrayList<String> that is duplicate-free and ordered lexicographically
     */
    private static ArrayList<String> order(ArrayList<String> list){

        ArrayList<String> result = new ArrayList<>(new LinkedHashSet<>(list));
        Collections.sort(result);
        return result;
    }
}
