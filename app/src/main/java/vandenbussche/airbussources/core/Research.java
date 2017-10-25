package vandenbussche.airbussources.core;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import vandenbussche.airbussources.database.SQLUtility;

/**
 * This class is responsible for the processing of the research functions
 */
public class Research {

    private Context context;

    public Research(Context c){
        this.context = c;
    }

    private class Result implements Comparable<Result> {
        String value;
        int pertinence;

        Result(String value){
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
        public int compareTo(Result o) throws ClassCastException {
            //if( o == null){
                //throw new ClassCastException("Could not compare those, as one of them is not a Result instance");
            //}
            return this.pertinence - o.pertinence;
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
            //Now we will add all the results for this word (there will in fact probably only be one most of the time)
            //to the global result ArrayList.
            result.addAll(resultsForOneWord);
        }
        db.close();
        rmvDuplicates(result);
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
            //Now we will add all the results for this word (there will in fact probably only be one most of the time)
            //to the global result ArrayList.
            result.addAll(resultsForOneWord);
        }
        db.close();
        rmvDuplicates(result);
        return result;
    }

    /**
     * This method provides all the members' names matching the research. Results should be sorted alphabetically.
     * I am not too happy with my code though and my come back on it later. But for now it should work at least.
     * @param names the words the research is being performed on
     * @return An ArrayList<String> containing the idProfiles of the members matching the research
     */
    public ArrayList<String> getMembersIdProfilesByNames(Context context, ArrayList<String> names) {
        SQLUtility db = SQLUtility.prepareDataBase(context);
        ArrayList<Result> resultsIDProfiles = new ArrayList<>();
        ArrayList<String> returnedList = new ArrayList<>();
        boolean alreadyIn = false;

        //For each word typed in by the user
        for (String word:names)
        {
            //We get idProfiles of all matching members
            Cursor c = db.getEntriesFromDB("Member", new String[]{"IDProfile"}, "Name = \""+word+"\"", null);
            if(c.moveToFirst()){
                //For all of those idProfiles
                for(int i=0; i<c.getCount(); i++){
                    String idProfile = c.getString(c.getColumnIndex("IDProfile"));

                    for (Result result : resultsIDProfiles){
                        //If it is already contained in the list, we increase its pertinence
                        if(idProfile.equals(result.value)){
                            alreadyIn = true;
                            result.incrPertinence(1);
                        }
                    }
                    if( ! alreadyIn){
                        //Otherwise we add it to the list
                        resultsIDProfiles.add(new Result(idProfile));
                    }
                    c.moveToNext();
                    alreadyIn = false;
                }
            }
            c.close();
            Cursor d = db.getEntriesFromDB("Member", new String[]{"IDProfile"}, "Surname LIKE "+"\'%"+word+"%\'", null);
            if(d.moveToFirst()){
                System.out.println("Code exécuté");
                //For all of those idProfiles
                for(int i=0; i<d.getCount(); i++){
                    String idProfile = d.getString(d.getColumnIndex("IDProfile"));

                    for (Result result : resultsIDProfiles){
                        //If it is already contained in the list, we increase its pertinence
                        if(idProfile.equals(result.value)){
                            alreadyIn = true;
                            result.incrPertinence(1);
                        }
                    }
                    if( ! alreadyIn){
                        //Otherwise we add it to the list
                        resultsIDProfiles.add(new Result(idProfile));
                    }
                    d.moveToNext();
                    alreadyIn = false;
                }
            }
            d.close();
        }
        db.close();
        //Now we have a list of Result instances, which we will copy in alphabetical rmvDuplicates into an ArrayList<String>
        for(int i=0; i<resultsIDProfiles.size(); i++){
            returnedList.add(resultsIDProfiles.get(i).value);
        }
        returnedList = rmvDuplicates(returnedList);
        Collections.sort(returnedList);
        return returnedList;
    }

    /**
     * This method provides all the members' names matching the research. Results should be sorted alphabetically.
     * I am not too happy with my code though and my come back on it later. But for now it should work at least.
     * @param BUs the words the research is being performed on
     * @return An ArrayList<String> containing the idProfiles of the members matching the research
     */
    public ArrayList<String> getMembersIDProfilesByBU(Context context, ArrayList<String> BUs) {
        SQLUtility db = SQLUtility.prepareDataBase(context);
        ArrayList<Result> resultsIDProfiles = new ArrayList<>();
        ArrayList<String> returnedList = new ArrayList<>();
        boolean alreadyIn = false;

        for(String bu : BUs) {
            Cursor e = db.getEntriesFromDB("Member", new String[]{"IDProfile"}, "BU LIKE " + "\'%" +bu+ "%\'", null);
            if (e.moveToFirst()) {
                //For all of those idProfiles
                for (int i = 0; i < e.getCount(); i++) {
                    String idProfile = e.getString(e.getColumnIndex("IDProfile"));

                    for (Result result : resultsIDProfiles) {
                        //If it is already contained in the list, we increase its pertinence
                        if (idProfile.equals(result.value)) {
                            result.incrPertinence(1);
                            alreadyIn = true;
                        }
                    }
                    if (!alreadyIn) {
                        //Otherwise we add it to the list
                        resultsIDProfiles.add(new Result(idProfile));
                    }
                    e.moveToNext();
                    alreadyIn = false;
                }
            }
            e.close();
        }
        //Now we have a list of Result instances, which we will copy in alphabetical rmvDuplicates into an ArrayList<String>
        for(int i=0; i<resultsIDProfiles.size(); i++){
            returnedList.add(resultsIDProfiles.get(i).value);
        }
        returnedList = rmvDuplicates(returnedList);
        Collections.sort(returnedList);
        return returnedList;
    }

    /**
     * This option removes all duplicates from an ArrayList of Strings
     * @param list the list to be processed
     * @return an ArrayList<String> that is duplicate-free and ordered lexicographically
     */
    public static ArrayList<String> rmvDuplicates(ArrayList<String> list) {

        ArrayList<String> al = new ArrayList<>();
        Set<String> hs = new HashSet<>();

        hs.addAll(list);
        al.addAll(hs);

        return al;
    }
}
