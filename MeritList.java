import java.io.*;
import java.util.*;
//I have no clue what to do here in this class please suggest
public class MeritList{
	
	//data members
	private ArrayList<Candidate> rankList;
	private int meritListIndex;
	
	//constructor
	public MeritList(){
		rankList = new ArrayList<Candidate>();
	}
	public MeritList(int index){
		rankList = new ArrayList<Candidate>();
		meritListIndex = index;
	}

	//Functions for accessing data members
	public int getMeritListIndex(){
		return meritListIndex;
	}
	public ArrayList<Candidate> getRankList(){
		return rankList;
	}

	//Function for adding candidate
	public void addCandidate(Candidate newCandidate){
		rankList.add(newCandidate);
	}

	//Function for appending lists
	public void appendList(ArrayList<Candidate> list){
		for (ListIterator<Candidate> current = list.listIterator(); current.hasNext(); )
		{
			addCandidate(current.next());
		}
	}

	//Function for sorting lists
	public void sortList(){
		Collections.sort(rankList, new Comparator<Candidate>() {
	        @Override
	        public int compare(Candidate candidate1, Candidate candidate2)
	        {
	        	if(candidate1.getRank(getMeritListIndex()) < candidate2.getRank(getMeritListIndex()))
	        		return -1;
	        	else
	        		return 1;
	        }
    	});
	}
}