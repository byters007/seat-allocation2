import java.io.*;
import java.util.*;
//I have no clue what to do here in this class please suggest
public class MeritList{
	
	//data members
	private Arraylist<Candidate> rankList;
	private meritListIndex;
	
	//constructor
	public MeritList(){
		rankList = new Arraylist<Candidate>();
	}
	public MeritList(int index){
		rankList = new Arraylist<Candidate>();
		meritListIndex = index;
	}

	//Functions for accessing data members
	public int getMeritListIndex(){
		return meritListIndex;
	}

	//Function for adding candidate
	public void addCandidate(Candidate newCandidate){
		rankList.add(newCandidate);
	}

	//Function for appending lists
	public appendList(MeritList list){
		rankList.addAll(list);
	}

	//Function for sorting lists
	public sortList(){
		Collections.sort(rankList, new Comparator<Candidate>() {
	        @Override
	        public int compare(Candidate candidate1, Candidate candidate2)
	        {
	        	if(candidate1.getRank(getMeritListIndex()) < candidate2.getRank(getMeritListIndex()))
	        		return 1;
	        	else
	        		return -1;
	        }
    	}
	}
}