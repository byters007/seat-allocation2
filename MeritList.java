import java.io.*;
import java.util.*;
//I have no clue what to do here in this class please suggest
public class MeritList{
	
	//data members
	private Arraylist<Candidate> rankList;

	//constructor
	public MeritList(){
		rankList = new Arraylist<Candidate>();
	}

	//Function for adding candidate
	public void addCandidate(Candidate newCandidate){
		rankList.add(newCandidate);
	}

	public appendList(MeritList list){
		rankList.addAll(list);
	}

}