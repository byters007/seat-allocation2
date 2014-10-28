import java.util.*;
import java.io.*;

public class VirtualProgramme  
{
	private String programID;
	private String instiID;
	private String category;	
	private Boolean pdStatus;
	private int quota;
	private int meritListIndex;
	private int seatsFilled;
	private ArrayList<Candidate> waitList;
	private ArrayList<Candidate> waitListForeign;
	private ArrayList<Candidate> waitListDS;
	private ArrayList<Candidate> tempList;

	public VirtualProgramme(VirtualProgramme prog) {
		programID = prog.programID ;
		instiID = prog.instiID;
		category = prog.category ;
		pdStatus = prog.pdStatus ;
		quota = prog.quota ;
		meritListIndex = prog.meritListIndex ;
		seatsFilled = prog.seatsFilled ;
		waitList = new ArrayList<Candidate>(prog.waitList) ;
		waitListForeign = new ArrayList<Candidate>(prog.waitListForeign) ;
		waitListDS = new ArrayList<Candidate>(prog.waitListDS) ;
		tempList = new ArrayList<Candidate>(prog.tempList) ;
	}
	public VirtualProgramme(String category_ , Boolean pdStatus_ , int quota_, String programID_, String instiID_)
	{
		category = category_;
		pdStatus = pdStatus_;
		programID = programID_;
		instiID = instiID_;

		if(pdStatus == false)
		{
			if(category == "GE")
			{
				meritListIndex = 0;
			}
			if(category == "OBC")
			{
				meritListIndex = 1;
			}
			if(category == "SC")
			{
				meritListIndex = 2;
			}
			if(category == "ST")
			{
				meritListIndex = 3;
			}
		}
		else
		{
			if(category == "GE_PD")
			{
				meritListIndex = 4;
			}
			if(category == "OBC_PD")
			{
				meritListIndex = 5;
			}
			if(category == "SC_PD")
			{
				meritListIndex = 6;
			}
			if(category == "ST_PD")
			{
				meritListIndex = 7;
			}
		}

		quota = quota_;
		//list of candidates who have applied for that programme in 1 iteration
		tempList = new ArrayList<Candidate>();
		//list of candidates who have been wait listed after filtering
		waitList = new ArrayList<Candidate>();
		waitListDS = new ArrayList<Candidate>();
		waitListForeign = new ArrayList<Candidate>();

		//meritList = new MeritList(recievedList[meritListIndex]);
		seatsFilled = 0 ;
	}

	public String getProgramID(){
		return programID;
	}
	public String getInstiID(){
		return instiID;
	}
	public String getCategory() {
		return category ;
	}

	/** @debug: maybe you can pass tempId(string) ,instead of Candidate*/
	public void receiveApplication(Candidate newCandidate, HashMap<String , Candidate> rejectionList)	
	{	//check if the candidate is present in the merit list, which is available in gale-shapley class.
		if(meritList.getRank(newCandidate.getUniqueID())!=-1)
		{
			tempList.add(newCandidate);
		}
		else
		{
			rejectionList.put(newCandidate.getUniqueID(), newCandidate);	//otherwise add the candidate to the rejection list for that iteration of the gale sharpley algorithm.
		}
		//newCandidate.setAppliedUpto(5);

	}

	public HashMap<String , Candidate> filter(HashMap<String , Candidate> rejectionList)
	{
		SelectionSort(tempList) ;
		if(quota > 0) {
			waitList.clear() ;
			int max_size = Math.min(quota , tempList.size()) ;
			waitList.addAll(tempList.subList(0, max_size)) ;
			int i ;
			for(i = max_size ; i < tempList.size() ; i++) {
				if(meritList.compareRank(waitList.get(waitList.size()-1), tempList.get(i), meritListIndex)==2) {
					waitList.add(tempList.get(i)) ;
				}
				else {
					break ;
				}
			}
			for( ; i < tempList.size() ; i++) {
				rejectionList.put(tempList.get(i).getUniqueID(), tempList.get(i));
			}
		}
		else {
			for(int i=0; i<tempList.size(); i++){
				rejectionList.put(tempList.get(i).getUniqueID(), tempList.get(i));
			}
		}

		tempList.clear();
		return rejectionList;
	}

	public HashMap<String , Candidate> fFilter(HashMap<String , Candidate> rejectionList)
	{
		SelectionSort(tempList) ;
		if(quota>0){
			int i ;
			for(i = 0 ; i < tempList.size() ; i++) {
				if(waitList.size()<quota){
					waitList.add(tempList.get(i)) ;
				}
				else if(meritList.compareRank(waitList.get(waitList.size()-1), tempList.get(i), meritListIndex)==2) {
					waitList.add(tempList.get(i)) ;
				}
				else {
					break ;
				}
			}
			for( ; i < tempList.size() ; i++) {
				rejectionList.put(tempList.get(i).getUniqueID(), tempList.get(i));
			}
		}
		else {
			for(int i=0; i<tempList.size(); i++){
				rejectionList.put(tempList.get(i).getUniqueID(), tempList.get(i));
			}
		}

		tempList.clear();
		return rejectionList;
	}

	public void print_program() {
		System.out.println(programID + " " + quota + " " + category) ;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/**********************************************************Functions for MeritOrder(Specific)******************************************************************/
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	public Boolean checkApplication(Candidate canndidate){
		if(quota>0){
			if(seatsFilled<quota){
				waitList.add(candidate);
				seatsFilled++;
				return true;
			}
			else if(waitList.get(waitList.size()-1).getRank(meritListIndex)==candidate.getRank(meritListIndex)){
				waitList.add(candidate);
				seatsFilled++;
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}

	public void setQuota(int x){
		quota=x;
	}

	public int getDiff(){
		if(seatsFilled<quota){
			setQuota(seatsFilled);
			return quota - seatsFilled;
		}
		else
			return 0;
	}

	public void dereserveSeats(ArrayList<VirtualProgramme> progList){
		int vacancy=0;
		for(int i=1;i<progList.size();i++){
			vacancy += progList.get(i).getDiff();
		}
		//there is one doubt, should it be
		//setQuota(quota+vacancy);
		setQuota(seatsFilled+vacancy);
	}
}