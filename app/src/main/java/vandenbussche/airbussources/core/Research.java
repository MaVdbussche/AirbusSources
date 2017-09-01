package vandenbussche.airbussources.core;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import vandenbussche.airbussources.activity.SearchByMember;
import vandenbussche.airbussources.database.SQLUtility;

/**
 * This class is responsible for the processing of the research functions
 */
public class Research {

    private Context context;

    public Research(Context c){
        this.context = c;
    }

    private class Result implements Comparable {
        Object value;
        int pertinence;

        Result(Object value){
            this.value = value;
            this.pertinence = 0;
        }

        int incrPertinence(int i){
            this.pertinence = pertinence + i;
            return this.pertinence;
        }
        int decrPertinence(int i){
            this.pertinence = pertinence - i;
            return this.pertinence;
        }
        public int compareTo(Object o) throws ClassCastException {
            if( ! (o instanceof Result)){
                throw new ClassCastException("Could not compare those, as one of them is not a Result instance");
            }
            return this.pertinence - ((Result) o).pertinence;
        }
    }

    /**
     * This method provides all the products' names matching the research. Results should be sorted alphabetically.
     * I am not too happy with my code though and my come back on it later. I would like, for example,
     * to have it showing results ordered by pertinence (for example, results matching 3 words from <#param>research</#param> would appear
     * before those matching only 2). But for now it should work at least.
     * @param research the words the research is being performed on
     * @return An ArrayList<String> containing the names of the products matching the research
     */
    public ArrayList<String> getProductsNames(Context context, ArrayList<String> research){
        SQLUtility db = SQLUtility.prepareDataBase(context);
        ArrayList<String> result = new ArrayList<>();

        for (String word:research)
        {
            ArrayList<String> resultsForOneWord = db.getElementFromDB("Product", "Name", "Name LIKE "+"\'%"+word+"%\'");
            db.close();
            //Now we will add all the results for this word (there will in fact probably only be one most of the time)
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
     * This method provides all the suppliers' names matching the research. Results should be sorted alphabetically.
     * I am not too happy with my code though and my come back on it later. I would like, for example,
     * to have it showing results ordered by pertinence (for example, results matching 3 words from <#param>research</#param> would appear
     * before those matching only 2). But for now it should work at least.
     * @param research the words the research is being performed on
     * @return An ArrayList<String> containing the names of the suppliers matching the research
     */
    public ArrayList<String> getSuppliersNames(Context context, ArrayList<String> research){
        SQLUtility db = SQLUtility.prepareDataBase(context);
        ArrayList<String> result = new ArrayList<>();

        for (String word:research)
        {
            ArrayList<String> resultsForOneWord = db.getElementFromDB("Supplier", "Name", "Name LIKE "+"\'%"+word+"%\'");
            db.close();
            //Now we will add all the results for this word (there will in fact probably only be one most of the time)
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
     * This method provides all the members' names matching the research. Results should be sorted alphabetically.
     * I am not too happy with my code though and my come back on it later. But for now it should work at least.
     * @param research the words the research is being performed on
     * @return An ArrayList<String> containing the idProfiles of the members matching the research
     */
    public ArrayList<String> getMembersIdProfiles(Context context, ArrayList<String> research){
        SQLUtility db = SQLUtility.prepareDataBase(context);
        ArrayList<Result> results = new ArrayList<>();
        ArrayList<String> returnedList = new ArrayList<>();
        boolean flag = false;

        //For each word typed in by the user
        for (String word:research)
        {
            //We get idProfiles of all matching members
            Cursor c = db.getEntriesFromDB("Member", new String[]{"IDProfile"}, "Name LIKE "+"\'%"+word+"%\'", null);
            if(c.moveToFirst()){
                //For all of those idProfiles
                for(int i=0; i<c.getCount(); i++){
                    String idProfile = c.getString(c.getColumnIndex("IDProfile"));

                    for (Result result : results){
                        //If it is already contained in the list, we increase its pertinence
                        if(idProfile.equals(result.value)){
                            result.incrPertinence(1);
                            flag = true;
                        }
                    }
                    if( ! flag){
                        //Otherwise we add it to the list
                        results.add(new Result(idProfile));
                    }
                    c.moveToNext();
                }
            }
            c.close();
            db.close();
        }
        //Now we have a list of Result instances, which we will copy in pertinence order into an ArrayList<String>
        Collections.sort(results);
        for(int i=0; i<results.size(); i++){
            returnedList.add((String) results.get(i).value);
        }
        return returnedList;
    }

    /**
     * This option removes all duplicates and sorts an ArrayList of Strings
     * @param list the list to be processed
     * @return an ArrayList<String> that is duplicate-free and ordered lexicographically
     */
    private static ArrayList<String> order(ArrayList<String> list){

        ArrayList<String> result = new ArrayList<>(new LinkedHashSet<>(list));
        Collections.sort(result);
        return result;
    }
}
