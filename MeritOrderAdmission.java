import java.io.*;
import java.util.*;

class MeritOrderAdmission{
	private Map<String,Candidate> candidateMap = new HashMap<String,Candidate>();
	//private Map<String,Candidate> dsCandidateMap = new HashMap<String,Candidate>();
	//private Map<String,Candidate> fCandidateMap = new HashMap<String,Candidate>();
	private ArrayList<String> orderedCandidate = new ArrayList<String>();
	private Map<String , ArrayList<VirtualProgramme> > programMap = new HashMap<String , ArrayList<VirtualProgramme> >();						//the program map contains the program code as the key and the arrayList of virtual program as its key value
	private Map<String , Integer > instiSeats = new HashMap<String , Integer >();
	private Map<String , ArrayList<Candidate> > instiAppliedMap = new HashMap<String , ArrayList<Candidate> >();
	private Map<String , String> dsChoice = new HashMap<String , String>();
	private MeritList commonMeritList;
	private MeritList dsMeritList;
	private MeritList fMeritList;
	private MeritList[] categoryList = new MeritList[8];


//Candidate tempCandidate;
	String tempId;
	String tempCategory;
	String tempPDStatus;
	String tempChoices;
	String garbage;
	boolean booleanTempPDStatus;
	boolean booleanTempDSStatus;
	boolean booleanTempNationality;

	public MeritOrderAdmission(){
		commonMeritList = new MeritList();
		for(int i=0;i<8;i++){
			categoryList[i] = new MeritList(i);
		}
		dsMeritList = new MeritList(0);
		fMeritList = new MeritList(0);
	}
	
	public void printResult(){
		for (int i=0;i<orderedCandidate.size();i++){
			if(candidateMap.get(orderedCandidate.get(i)).isWaitListedFor()){
				System.out.println(orderedCandidate.get(i) + "," + candidateMap.get(orderedCandidate.get(i)).getWaitListedFor().getProgramID() + "," + candidateMap.get(orderedCandidate.get(i)).getWaitListedFor().getCategory() ); /** @debug : Proper Syntax*/
			}
			else{
				System.out.println(orderedCandidate.get(i) + ",-1");
			}
		}
	}

	public Candidate getCandidate(Candidate candidate){
		return candidateMap.get(candidate.getUniqueID());
	}

	public ArrayList<VirtualProgramme> getProgram(VirtualProgramme program){
		return programMap.get(program.getProgramID());
	}

	public boolean checkDSApplication(String instiID, Candidate applicant){
		if(instiSeats.get(instiID)<2){
			instiAppliedMap.get(instiID).add(applicant);
			instiSeats.put(instiID, instiSeats.get(instiID)+1);
			return true;
		}
		else if(applicant.getRank(0)==instiAppliedMap.get(instiID).get(instiSeats.get(instiID)-1).getRank(0)){
			instiAppliedMap.get(instiID).add(applicant);
			instiSeats.put(instiID, instiSeats.get(instiID)+1);
			return true;
		}
		else
			return false;
	}

	public void startAlgorithm()
	{
		
		/** To read in all available programs, create their respective virtual programmes*/
		String programCode;
		String programName;
		String instiCode;
		int ge,obc,sc,st,ge_pd,obc_pd,sc_pd,st_pd;
		//ArrayList<VirtualProgramme> tempVirtualProgrammeList;
		try{
		 Scanner sb = new Scanner(new File("programs.csv")).useDelimiter(",|\n");
		//fstream inProgrammes("programs.csv" , ios::in);	/** create proper file stream object */
		//inProgrammes>>garbage>>garbage>>garbage>>garbage>>garbage>>garbage>>garbage>>garbage>>garbage>>garbage>>garbage;
		garbage = sb.next();garbage = sb.next();garbage = sb.next();garbage = sb.next();garbage = sb.next();garbage = sb.next();
		garbage = sb.next();garbage = sb.next();garbage = sb.next();garbage = sb.next();garbage = sb.next();
		while(sb.hasNext())
		{
	  	//	inProgrammes>>garbage>>programCode>>programName>>ge>>obc>>sc>>st>>ge_pd>>obc_pd>>sc_pd>>st_pd;
	  		garbage = sb.next();
	  		programCode = sb.next();
	  		instiCode = programCode.substring(0,1);
	  		programName = sb.next();
	  		ge = sb.nextInt();
	  		obc = sb.nextInt();
	  		sc = sb.nextInt();
	  		st = sb.nextInt();
	  		ge_pd = sb.nextInt();
	  		obc_pd = sb.nextInt();
	  		sc_pd = sb.nextInt();
	  		st_pd = sb.nextInt();
	  		instiSeats.put(instiCode , 0);
	  		instiAppliedMap.put(instiCode , new ArrayList<Candidate>());
			programMap.put(programCode , new ArrayList<VirtualProgramme>());
			programMap.get(programCode).add(new VirtualProgramme("GE",false,ge,programCode,instiCode));
			programMap.get(programCode).add(new VirtualProgramme("OBC",false,obc,programCode,instiCode));
			programMap.get(programCode).add(new VirtualProgramme("SC",false,sc,programCode,instiCode));
			programMap.get(programCode).add(new VirtualProgramme("ST",false,st,programCode,instiCode));
			programMap.get(programCode).add(new VirtualProgramme("GE_PD",true,ge_pd,programCode,instiCode));
			programMap.get(programCode).add(new VirtualProgramme("OBC_PD",true,obc_pd,programCode,instiCode));
			programMap.get(programCode).add(new VirtualProgramme("SC_PD",true,sc_pd,programCode,instiCode));
			programMap.get(programCode).add(new VirtualProgramme("ST_PD",true,st_pd,programCode,instiCode));
			//programMap.put(programCode);

		}
		sb.close();
		} catch(FileNotFoundException e){
			System.exit(1);
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/** To Read in the candidate list and their choices */
		try{
		 Scanner s = new Scanner(new File("choices.csv")).useDelimiter(",|\n");
		//fstream inChoices("choices.csv" , ios::in); /** create proper file stream object */
		//inChoices>>garbage>>garbage>>garbage>>garbage;	/** reading in the first line which contains the field names */
		 garbage = s.next();garbage = s.next();garbage = s.next();garbage = s.next();
		while(s.hasNext())				/** Write the correct syntax for reading in from the files */
		{
			//inChoices>>tempId>>tempCategory>>tempPDStatus>>tempChoices;
			tempId = s.next();
			tempCategory = s.next();
			tempPDStatus = s.next();
			tempChoices = s.next();
			if(tempPDStatus.equals("Y"))
				booleanTempPDStatus = true;
			else 
				booleanTempPDStatus = false;
			
			if(tempCategory.equals("DS")){
				booleanTempDSStatus = false;
				dsChoice.put(tempId,tempChoices);
			}

			if(tempCategory.equals("F"))
				booleanTempNationality = false;
			else 
				booleanTempNationality = true;
			
			Candidate tempCandidate = new Candidate(tempId,tempCategory,booleanTempPDStatus,booleanTempDSStatus,booleanTempNationality);
			orderedCandidate.add(tempId);
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//Added loop for adding preferencelist but not sure if it will work as i am using its member function inside its constructor
			//@anmol: maybe we can do this in galeshapley
			String[] choices = tempChoices.split("_");
			for(int i=0;i<choices.length;i++){
				//VirtualProgramme tempProg = new VirtualProgramme(tempChoice,pdStatus,/*@anmol: I need qouta over here. You have not read it in GaleShapley currently*/);
				tempCandidate.addPreference(programMap.get(choices[i]));
			}
			//I think we should read the programme file before student choice file then we can directly get it from programme map
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if(booleanTempNationality && !tempCategory.equals("DS"))
				tempCandidate.setWaitListedFor(tempCandidate.getChoice(0));
			if(!booleanTempNationality)
				tempCandidate.setWaitListedFor(tempCandidate.getFChoice(0));
			candidateMap.put(tempId, tempCandidate);
		}
		s.close();
		} catch(FileNotFoundException e){
			System.exit(1);
		}


/** To read in the rank list of candidates and create the merit lists of different categories */
		try{
			Scanner sd = new Scanner(new File("ranklist.csv")).useDelimiter(",|\n");
		
		//fstream inRankList("ranklist.csv" , ios::in);
		String tempId;
		int tempGender,tempCML,tempCML_PD;
		int[] tempRank = new int[8];
		//inRankList>>garbage>>garbage>>garbage>>garbage>>garbage>>garbage>>garbage>>garbage>>garbage>>garbage>>garbage>>garbage;
		garbage = sd.next();garbage = sd.next();garbage = sd.next();garbage = sd.next();garbage = sd.next();
		garbage = sd.next();garbage = sd.next();garbage = sd.next();garbage = sd.next();garbage = sd.next();
		garbage = sd.next();garbage = sd.next();
		while(sd.hasNext())					/** @debug Write proper syntax */
		{
			tempId = sd.next();
	  		tempGender = sd.nextInt();
	  		tempCML = sd.nextInt();
	  		for(int i=0;i<4;i++)
	  			tempRank[i] = sd.nextInt();
	  		tempCML_PD = sd.nextInt();
	  		for(int i=0;i<4;i++)
	  			tempRank[i+4] = sd.nextInt();

	  		candidateMap.get(tempId).addRank(tempRank);
	  		
	  		if(candidateMap.get(tempId).getCategory().equals("DS")){
	  			candidateMap.get(tempId).setDSStatus(true);
				if(candidateMap.get(tempId).getRank(0)==0)
					candidateMap.get(tempId).setDSStatus(false);
				if(candidateMap.get(tempId).getRank(1)!=0 || candidateMap.get(tempId).getRank(5)!=0)
					candidateMap.get(tempId).setCategory("OBC");
				else if(candidateMap.get(tempId).getRank(2)!=0 || candidateMap.get(tempId).getRank(6)!=0)
					candidateMap.get(tempId).setCategory("SC");
				else if(candidateMap.get(tempId).getRank(3)!=0 || candidateMap.get(tempId).getRank(7)!=0)
					candidateMap.get(tempId).setCategory("ST");
				else
					candidateMap.get(tempId).setCategory("GE");
				String[] choices = dsChoice.get(tempId).split("_");
				for(int i=0;i<choices.length;i++){
					//VirtualProgramme tempProg = new VirtualProgramme(tempChoice,pdStatus,/*@anmol: I need qouta over here. You have not read it in GaleShapley currently*/);
					candidateMap.get(tempId).addPreference(programMap.get(choices[i]));
				}
				candidateMap.get(tempId).setWaitListedFor(candidateMap.get(tempId).getDSChoice(0));
			}
		//	System.out.println(candidateMap.get(tempId).getUniqueID() + " " + candidateMap.get(tempId).getAppliedUpto() + " CAT" );

	  		if(tempRank[0] != 0)
			{
				categoryList[0].addCandidate(candidateMap.get(tempId));
			}
			
			if(tempRank[1] != 0)
			{
				categoryList[1].addCandidate(candidateMap.get(tempId));
			}

			if(tempRank[2] != 0)
			{
				categoryList[2].addCandidate(candidateMap.get(tempId));
			}							

			if(tempRank[3] != 0)
			{
				categoryList[3].addCandidate(candidateMap.get(tempId));
			}				

			if(tempRank[4] != 0)
			{
				categoryList[4].addCandidate(candidateMap.get(tempId));
			}

			if(tempRank[5] != 0)
			{
				categoryList[5].addCandidate(candidateMap.get(tempId));
			}

			if(tempRank[6] != 0)
			{
				categoryList[6].addCandidate(candidateMap.get(tempId));
			}

			if(tempRank[7] != 0)
			{
				categoryList[7].addCandidate(candidateMap.get(tempId));
			}

			if(candidateMap.get(tempId).getDSStatus())
			{
				dsMeritList.addCandidate(candidateMap.get(tempId));
			}

			if(!candidateMap.get(tempId).getNationality())
			{
				fMeritList.addCandidate(candidateMap.get(tempId));
			}
		}
		for(int i=0;i<8;i++)
		{
			categoryList[i].sortList();
			commonMeritList.appendList(categoryList[i].getRankList());
		}
		for(int i=0;i<commonMeritList.getRankList().size();i++)
				System.out.println(commonMeritList.getRankList().get(i).getUniqueID() + " " + commonMeritList.getRankList().get(i).getAppliedUpto() + " CML" );

		dsMeritList.sortList();
		fMeritList.sortList();
		
		sd.close();
		} catch(FileNotFoundException e){
			System.exit(1);
		}
		//All merit lists checked
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Checked, everything OK till here
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			/****************************************************Start of the MeritOrder Algorithm***************************************************************/
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			/**************************************************************DS Candidates Apply***************************************************************************/
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		for (ListIterator<Candidate> currentPointer = dsMeritList.getRankList().listIterator(); currentPointer.hasNext(); )
		{
			Candidate current = currentPointer.next();
			//System.out.println(getCandidate(current).getUniqueID() + " " + getCandidate(current).getAppliedUpto() + " DS" );

			while(!getCandidate(current).isWaitListedFor() && getCandidate(current).getAppliedUpto()!=-1){
				if(checkDSApplication(getCandidate(current).currentDSVirtualProgramme().getInstiID(),current)){
					getCandidate(current).setWaitListedForBool(true);
					getCandidate(current).setWaitListedFor(current.currentDSVirtualProgramme());
					break;
				}
				else{
					getCandidate(current).nextDSVirtualProgramme();
				}
			}
			if(getCandidate(current).getAppliedUpto()==-1)
				getCandidate(current).setAppliedUpto(0);
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			/******************************************************************Phase-1***************************************************************************/
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/*for (Map.Entry<String , Candidate> entry : candidateMap.entrySet())
			{
				System.out.println(entry.getKey() + " " + entry.getValue().getAppliedUpto() + " CMAP");
			}*/
		for (ListIterator<Candidate> currentPointer = commonMeritList.getRankList().listIterator(); currentPointer.hasNext(); )
		{
			Candidate current = currentPointer.next();
			getCandidate(current).setAppliedUpto(0);
			//System.out.println(getCandidate(current).getUniqueID() + " " + getCandidate(current).getAppliedUpto());
			while(!getCandidate(current).isWaitListedFor() && getCandidate(current).getAppliedUpto()!=-1 && getCandidate(current).getNationality()){
				if(getProgram(getCandidate(current).currentVirtualProgramme()).get(getCandidate(current).currentVirtualProgramme().getMeritListIndex()).checkApplication(current)){
					getCandidate(current).setWaitListedForBool(true);
					getCandidate(current).setWaitListedFor(current.currentVirtualProgramme());
					//System.out.println(getCandidate(current).getUniqueID() + " " + getCandidate(current).getAppliedUpto());

					currentPointer.remove();
					break;
				}
				else{
					getCandidate(current).nextVirtualProgramme();
				}
			}
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			/******************************************************************Dereservation***************************************************************************/
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		for (Map.Entry<String , ArrayList<VirtualProgramme> > entry : programMap.entrySet())
		{
			entry.getValue().get(0).dereserveSeats(entry.getValue());
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			/******************************************************************Phase-2***************************************************************************/
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		for (ListIterator<Candidate> currentPointer = commonMeritList.getRankList().listIterator(); currentPointer.hasNext(); )
		{
			Candidate current = currentPointer.next();
			getCandidate(current).setAppliedUpto(0);
			while(!getCandidate(current).isWaitListedFor() && getCandidate(current).getAppliedUpto()!=-1 && getCandidate(current).getNationality()){
				if(getProgram(getCandidate(current).currentVirtualProgramme()).get(getCandidate(current).currentVirtualProgramme().getMeritListIndex()).checkApplication(current)){
					getCandidate(current).setWaitListedForBool(true);
					getCandidate(current).setWaitListedFor(current.currentVirtualProgramme());
					currentPointer.remove();
					break;
				}
				else{
					getCandidate(current).nextVirtualProgramme();
				}
			}
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			/******************************************************************Foreign Allocattion***************************************************************************/
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		for (ListIterator<Candidate> currentPointer = fMeritList.getRankList().listIterator(); currentPointer.hasNext(); )
		{
			Candidate current = currentPointer.next();
			while(!getCandidate(current).isWaitListedFor() && getCandidate(current).getAppliedUpto()!=-1){
				if(getProgram(getCandidate(current).currentFVirtualProgramme()).get(getCandidate(current).currentFVirtualProgramme().getMeritListIndex()).checkFApplication(current)){
					getCandidate(current).setWaitListedForBool(true);
					getCandidate(current).setWaitListedFor(current.currentFVirtualProgramme());
					break;
				}
				else{
					getCandidate(current).nextFVirtualProgramme();
				}
			}
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


		printResult();
	}
}