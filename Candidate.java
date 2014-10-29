import java.io.*;
import java.util.*;

public class Candidate{
	
	//Personal Information
	private String uniqueID;
	private String category;
	private boolean pdStatus;
	private boolean dsStatus;
	private boolean nationality;
	private int[] rank;

	//Information regarding preferences
	private ArrayList<VirtualProgramme> preferenceList;
	private ArrayList<VirtualProgramme> dsPreferenceList;
	private ArrayList<VirtualProgramme> fPreferenceList;
	private int appliedUpto;
	private boolean isWaitListed;
	private VirtualProgramme waitListedFor; 
	//Functions to manipulate data members
	public String getUniqueID(){
		return uniqueID;
	}

	public String getCategory(){
		return category;
	}

	public void setCategory(String x){
		category=x;
	}

	public boolean getPDStatus(){
		return pdStatus;
	}

	public boolean getDSStatus(){
		return dsStatus;
	}
	public void setDSStatus(boolean x){
		dsStatus=x;
	}

	public boolean getNationality(){
		return nationality;
	}

	public int getAppliedUpto(){
		return appliedUpto;
	}

	public void setAppliedUpto(int x){
		appliedUpto = x;
	}

	public VirtualProgramme getWaitListedFor(){
		return waitListedFor;
	}

	public void setWaitListedFor(VirtualProgramme x){
		waitListedFor = x;
	}

	public VirtualProgramme getChoice(int i){
		return preferenceList.get(i);
	}

	public VirtualProgramme getDSChoice(int i){
		return dsPreferenceList.get(i);
	}

	public VirtualProgramme getFChoice(int i){
		return fPreferenceList.get(i);
	}	

	public void addRank(int[] x){
		for(int i=0;i<8;i++){
			rank[i]=x[i];
		}
	}

	public int getRank(int index){
		return rank[index];
	}

	//Constructor
	public Candidate(String uniqueID, String category, boolean pdStatus, boolean dsStatus, boolean nationality){
		this.uniqueID=uniqueID;
		this.category=category;
		this.pdStatus=pdStatus;
		this.dsStatus=dsStatus;
		this.nationality=nationality;
		preferenceList = new ArrayList<VirtualProgramme>();
		dsPreferenceList = new ArrayList<VirtualProgramme>();
		fPreferenceList = new ArrayList<VirtualProgramme>();
		appliedUpto = 0;
		isWaitListed = false;
		rank = new int[8];
	}

	//Copy Constructor
	public Candidate(Candidate x){
		uniqueID=x.uniqueID;
		category=x.category;
		pdStatus=x.pdStatus;
		dsStatus=x.dsStatus;
		nationality=x.nationality;
		preferenceList = new ArrayList<VirtualProgramme>(x.preferenceList);    //I am not sure whether u need preferencelist, appliedupto or waitlistedfor too and whether this will work
		dsPreferenceList = new ArrayList<VirtualProgramme>(x.dsPreferenceList);
		fPreferenceList = new ArrayList<VirtualProgramme>(x.fPreferenceList);
		appliedUpto=x.appliedUpto;
		//waitListedFor = new VirtualProgramme(x.waitListedFor);
		isWaitListed=x.isWaitListed;
		addRank(x.rank);

	}

	//Function for adding preferences to the preference list
	public void addPreference(ArrayList<VirtualProgramme> choice) {
		if(category.equals("GE")) { 
			if(!pdStatus){
				preferenceList.add(choice.get(0));
			}
			else {
				preferenceList.add(choice.get(0));
				preferenceList.add(choice.get(4));
			}
		}
		else if(category.equals("OBC")) {
			if(!pdStatus){
				preferenceList.add(choice.get(0));
				preferenceList.add(choice.get(1));
			}
			else{
				preferenceList.add(choice.get(0));
				preferenceList.add(choice.get(4));
				preferenceList.add(choice.get(1));
				preferenceList.add(choice.get(5));
			}
		}
		else if(category.equals("SC")) {
			if(!pdStatus){
				preferenceList.add(choice.get(0));
				preferenceList.add(choice.get(2));
			}
			else{
				preferenceList.add(choice.get(0));
				preferenceList.add(choice.get(2));
				preferenceList.add(choice.get(4));
				preferenceList.add(choice.get(6));
			}
		}
		else if(category.equals("ST")) {
			if(!pdStatus){
				preferenceList.add(choice.get(0));
				preferenceList.add(choice.get(3));
			}
			else{
				preferenceList.add(choice.get(0));
				preferenceList.add(choice.get(3));
				preferenceList.add(choice.get(4));
				preferenceList.add(choice.get(7));
			}
		}
		if(dsStatus){
			dsPreferenceList.add(choice.get(0));
		}
		if(!nationality){
			if(!pdStatus){
				fPreferenceList.add(choice.get(0));
			}
			else{
				fPreferenceList.add(choice.get(0));
			}
		}
	}

	public VirtualProgramme currentVirtualProgramme(){
		return preferenceList.get(appliedUpto);
	}

	public VirtualProgramme currentDSVirtualProgramme(){
		return dsPreferenceList.get(appliedUpto);
	}

	public VirtualProgramme currentFVirtualProgramme(){
		return fPreferenceList.get(appliedUpto);
	}

	public boolean isWaitListedFor(){
		return isWaitListed;
	}

	public void setWaitListedForBool(boolean p){
		isWaitListed=p;
	}
	//Function for finding the next Virtual Programme
	public void nextVirtualProgramme(){
		appliedUpto++;												/** @note to Pranjal: Maybe you should call the function "setWaitListedFor( preferenceList.get(appliedUpto))"
													so that when this function is called from galeShapley class, the current waitListed Programme also gets updated automatically*/
		if(appliedUpto < preferenceList.size() )
		{
			setWaitListedFor(preferenceList.get(appliedUpto));		//@anmol: I think this should be in VirtualProgramme as when applying to the next programme we can get rejected so only the
		}															//programme knows we are waitlisted or rejected
		//Reappear for JEE
		if(appliedUpto==preferenceList.size()){
			//System.out.println("Yes");
			appliedUpto=-1; //-1 means reappear next year
		}
	}

	public void nextDSVirtualProgramme(){
		appliedUpto++;												/** @note to Pranjal: Maybe you should call the function "setWaitListedFor( preferenceList.get(appliedUpto))"
													so that when this function is called from galeShapley class, the current waitListed Programme also gets updated automatically*/
		if(appliedUpto < dsPreferenceList.size() )
		{
			setWaitListedFor(dsPreferenceList.get(appliedUpto));		//@anmol: I think this should be in VirtualProgramme as when applying to the next programme we can get rejected so only the
		}															//programme knows we are waitlisted or rejected
		//Reappear for JEE
		if(appliedUpto==dsPreferenceList.size()){
			//System.out.println("Yes");
			appliedUpto=-1; //-1 means reappear next year
		}
	}
	
	public void nextFVirtualProgramme(){
		appliedUpto++;												/** @note to Pranjal: Maybe you should call the function "setWaitListedFor( preferenceList.get(appliedUpto))"
													so that when this function is called from galeShapley class, the current waitListed Programme also gets updated automatically*/
		if(appliedUpto < fPreferenceList.size() )
		{
			setWaitListedFor(fPreferenceList.get(appliedUpto));		//@anmol: I think this should be in VirtualProgramme as when applying to the next programme we can get rejected so only the
		}															//programme knows we are waitlisted or rejected
		//Reappear for JEE
		if(appliedUpto==fPreferenceList.size()){
			//System.out.println("Yes");
			appliedUpto=-1; //-1 means reappear next year
		}
	}
	
	void print_preference() {
		for(int i = 0 ; i < preferenceList.size() ; i++)
			System.out.println(preferenceList.get(i).getProgramID() + " , " + preferenceList.get(i).getCategory()) ;
	}
}