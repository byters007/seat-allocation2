import java.io.*;
import java.util.*;

class MeritOrderAdmission{
	private Map<String,Candidate> candidateMap = new HashMap<String,Candidate>();
	private Map<String,Candidate> dsCandidateMap = new HashMap<String,Candidate>();
	private Map<String,Candidate> fCandidateMap = new HashMap<String,Candidate>();
	private ArrayList<String> orderedCandidate = new ArrayList<String>();
	private Map<String , ArrayList<VirtualProgramme> > programMap = new HashMap<String , ArrayList<VirtualProgramme> >();						//the program map contains the program code as the key and the arrayList of virtual program as its key value
	private Map<String , ArrayList<Candidate> > instiAppliedMap = new HashMap<String , ArrayList<Candidate> >();
	private MeritList commonMeritList;
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
		commonMeritList[i] = new MeritList();
		for(int i=0;i<8;i++){
			categoryList[i] = new MeritList();
		}
	}
	
	public Candidate getCandidate(String uniqueID){
		return candidateMap.get(uniqueID);
	}

	public ArrayList<VirtualProgramme> getProgram(String programCode){
		return programMap.get(programCode);
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
				booleanTempDSStatus = true;
				if(categoryList[3].getRank(tempId)==-1)
					booleanTempDSStatus = false;
				if(categoryList[0].getRank(tempId)!=-1)
					tempCategory="OBC";
				else if(categoryList[1].getRank(tempId)!=-1)
					tempCategory="SC";
				else if(categoryList[2].getRank(tempId)!=-1)
					tempCategory="ST";
				else
					tempCategory="GE";
			}
			else 
				booleanTempDSStatus = false;

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
			if(booleanTempDSStatus)
				tempCandidate.setWaitListedFor(tempCandidate.getDSChoice(0));
			else if(booleanTempNationality)
				tempCandidate.setWaitListedFor(tempCandidate.getChoice(0));
			else
				tempCandidate.setWaitListedFor(tempCandidate.getFChoice(0));
			candidateMap.put(tempId, tempCandidate);
			if(booleanTempDSStatus)
				dsCandidateMap.put(tempId, tempCandidate);
			if(!booleanTempNationality)
				fCandidateMap.put(tempId, tempCandidate);
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
		int tempGender,tempCML,tempCML_PD,tempRank[8];
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
	  			rank[i] = sd.nextInt();
	  		tempCML_PD = sd.nextInt();
	  		for(int i=0;i<4;i++)
	  			rank[i+4] = sd.nextInt();

	  		candidateMap.get(tempId).addRank(rank);
	  		
	  		if(tempGE != 0)
			{
				categoryList[0].addCandidate(candidateMap.get(tempId));
			}
			
			if(tempOBC != 0)
			{
				categoryList[1].addCandidate(candidateMap.get(tempId));
			}

			if(tempSC != 0)
			{
				categoryList[2].addCandidate(candidateMap.get(tempId));
			}							

			if(tempST != 0)
			{
				categoryList[3].addCandidate(candidateMap.get(tempId));
			}				

			if(tempGE_PD != 0)
			{
				categoryList[4].addCandidate(candidateMap.get(tempId));			}

			if(tempOBC_PD != 0)
			{
				categoryList[5].addCandidate(candidateMap.get(tempId));
			}

			if(tempSC_PD != 0)
			{
				categoryList[6].addCandidate(candidateMap.get(tempId));
			}
			if(tempST_PD != 0)
			{
				categoryList[7].addCandidate(candidateMap.get(tempId));
			}
		}
		for(int i=0;i<8;i++)
		{
			categoryList[i].sortList(i);
			commonMeritList.appendList(categoryList[i]);
		}
		
		sd.close();
		} catch(FileNotFoundException e){
			System.exit(1);
		}
		//All merit lists checked
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Checked, everything OK till here
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}