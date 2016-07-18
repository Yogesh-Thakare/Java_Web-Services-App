

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import dsms.objects.Practitioner;
import dsms.objects.DoctorRecord;
import dsms.objects.NurseRecord;



class UDPThread extends Thread
{
	private String aServerName;
	private int aUDPPort;
	private int aOnline;
	private int aOffline;
	private boolean bCrashed;
	
	public UDPThread(String pServerName, int pUDPPort)
	{
		aServerName = pServerName;
		aUDPPort = pUDPPort;
		aOnline = 0;
		aOffline = 0;
		bCrashed = false;
		
	}
	
	public void setNumberOfPlayers(int pOnline, int pOffline)
	{
		aOnline = pOnline;
		aOffline = pOffline;
	}
	
	public boolean crashed()
	{
		return bCrashed;
	}
	
	public void run()
	{
		try {
			DatagramSocket datagramSocket = new DatagramSocket(aUDPPort);
			byte [] buffer = new byte [1000];
			DatagramPacket request;
			DatagramPacket reply;
			while(true)
			{
				request = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(request);
				buffer = (aServerName + ": " + aOnline + " online, " + aOffline + " offline. ").getBytes();
				reply = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
				datagramSocket.send(reply);
			}
		} catch (IOException e) {
			System.out.println("IOException : " + e.getMessage());
			bCrashed = true;
		}
	}
}



public class Server implements Runnable{
	
	private HashMap<Character, ArrayList<Practitioner>> practitionerRecords = 
			new HashMap<Character, ArrayList<Practitioner>>();
	private String clinicName;
	private static String staticRecordType;
	private int UDPPort;
	private static ArrayList<Server> clinicServers = null;
	private Logger logger;
	static int drRecord=10000;
	static int nrRecord=10000;

	/**
	 * default ctor
	 */
	public Server()
	{}
	
	/**
	 * @param create clinic with clinicName
	 */
	public Server(String clinicName) 
	{
		this.clinicName = clinicName;
		this.setLogger(clinicName+".txt");
	}
	
	/**
	 * @param create clinic with clinicName
	 * @param portNumber UDP port for current clinic 
	 */
	public Server(String clinicName, int portNumber, String[] pArgs)
	{
		practitionerRecords = new HashMap<Character, ArrayList<Practitioner>>();
		this.clinicName = clinicName;
		UDPPort = portNumber;
		this.setLogger(clinicName+".txt");
	}
	
	// For constructing the IDL interface object
	public Server(String clinicName,HashMap<Character, ArrayList<Practitioner>> recordList, int pUDPPort)
	{
		this.clinicName = clinicName;
		this.setLogger(clinicName+".txt");
		practitionerRecords =  new HashMap<Character, ArrayList<Practitioner>>();
		UDPPort = pUDPPort;
	}
	
	/**
	 * @param clinicName for which seperate log file created
	 */
	private void setLogger(String clinicName) 
	{
		try
		{
			this.logger = Logger.getLogger(this.clinicName);
			FileHandler fileTxt 	 = new FileHandler(clinicName);
			SimpleFormatter formatterTxt = new SimpleFormatter();
			fileTxt.setFormatter(formatterTxt);
			logger.addHandler(fileTxt);
		}
		catch(Exception err) 
		{
			System.out.println("Failed to initiate logger. Please check file permission");
		}
	}

	/**
	 * @return current server's UDP port
	 */
	public int getUDPPort()
	{
		return this.UDPPort;
	}
	

		
	/**
	 * checks if record already present with given recordID
	 * @param recordID
	 * @param fName
	 * @param lName
	 * @param chactr
	 * @return true or false
	 */
	public boolean checkUniqueRecord( String recordID,String fName,String lName,Character chactr)
	{
		boolean isUnique=true;
		
		if(!practitionerRecords.isEmpty()&&practitionerRecords.get(chactr)!=null)
		for(Practitioner practitioner:practitionerRecords.get(chactr))
		{
			if(practitioner.getRecordID().equals(recordID) && practitioner.getFirstName().equals(fName)&& practitioner.getLastName().equals(lName))
			isUnique=false;
			break;
		}
		return isUnique;
	}


	
	/**
	 * gets record count from current server
	 * @param recordType "DR" for Doctor Record "NR" for Nurse Record
	 * @return count of records
	 */
	private String getRecordsFromServer(String recordType)
	{	
		int recordCount=0;
		StringBuilder recordString = new StringBuilder();
		Iterator<?> it = practitionerRecords.entrySet().iterator();
		while(it.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
			@SuppressWarnings("unchecked")
			ArrayList<Practitioner> practitionerList = (ArrayList<Practitioner>) pair.getValue();
			if(!practitionerList.isEmpty())
			{					
				for(Practitioner practitioner : practitionerList)
				{
					if(practitioner.getRecordID().startsWith(recordType))
					{
						recordCount++;
					}
				}
			}
		}
		recordString.append(recordCount);
		return recordString.toString();
	}

	
	/**
	 * Initializes database for each server with dummy records
	 * @param server
	 * @throws RemoteException
	 */
	public static void loadData(Server server) throws RemoteException
	{
		//load database for Montreal server
		if(server.clinicName.equals("MTL"))
		{
		server.createDRecord("MTL1111","adoctor", "adoctor", "2150,st-hubert", "5145645655", "orthopaedic", "mtl");
		server.createDRecord("MTL1111","bdoctor", "bdoctor", "5750,st-laurent", "5145645655", "surgeon", "mtl");
		server.createDRecord("MTL1111","ydoctor", "ydoctor", "3150,st-marc", "5145645611", "orthopaedic", "mtl");
		server.createNRecord("MTL1111","anurse", "anurse", "junior", "active","20-05-2016");
		server.createNRecord("MTL1111","ynurse", "ynurse", "senior", "terminated","24-05-2015");
		server.createNRecord("MTL1111","bnurse", "bnurse", "junior", "active","21-05-2016");			
		}
		//load database for Laval server
		else if (server.clinicName.equals("LVL"))
		{
			
			server.createDRecord("LVL1111","adoctor", "adoctor", "2150,st-hubert", "5145645655", "orthopaedic", "lvl");
			server.createDRecord("LVL1111","bdoctor", "bdoctor", "5750,st-laurent", "5145645655", "surgeon", "lvl");
			server.createDRecord("LVL1111","ydoctor", "ydoctor", "3150,st-marc", "5145645611", "orthopaedic", "lvl");
			server.createNRecord("LVL1111","anurse", "anurse", "junior", "active","20-05-2016");
			server.createNRecord("LVL1111","ynurse", "ynurse", "senior", "terminated","24-05-2015");
			server.createNRecord("LVL1111","bnurse", "bnurse", "junior", "active","21-05-2016");
		}
		//load database for Dollard server
		else
		{
			server.createDRecord("DDO1111","adoctor", "adoctor", "2150,st-hubert", "5145645655", "orthopaedic", "ddo");
			server.createDRecord("DDO1111","bdoctor", "bdoctor", "5750,st-laurent", "5145645655", "surgeon", "ddo");
			server.createDRecord("DDO1111","ydoctor", "ydoctor", "3150,st-marc", "5145645611", "orthopaedic", "ddo");
			server.createNRecord("DDO1111","anurse", "anurse", "junior", "active","20-05-2016");
			server.createNRecord("DDO1111","ynurse", "ynurse", "senior", "terminated","24-05-2015");
			server.createNRecord("DDO1111","bnurse", "bnurse", "junior", "active","21-05-2016");
		}
		
	}
	
	/**
	 * @param dateStr
	 * @return Date in "dd-MM-yyyy" format
	 */
	public static Date getFormattedDate(String dateStr)
	{
		Date formattedDate=null;
		try 
		{
			DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			formattedDate =sdf.parse(dateStr);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		return formattedDate;
	}
	
	
	

	public boolean createDRecord(String managerID, String firstName,
			String lastName, String address, String phone,
			String specialization, String location) {
		++drRecord;
		Practitioner Practitioner=new DoctorRecord("DR"+drRecord,firstName,lastName,address,phone,specialization,location);
		
		//use of synchronized block here lock is achieved on each record inside list 
		ArrayList<Practitioner> practitionerList = practitionerRecords.get(lastName.charAt(0));
			
		if(practitionerList == null && checkUniqueRecord(Practitioner.getRecordID(),Practitioner.getFirstName(),Practitioner.getLastName(),lastName.charAt(0)))
		{
			practitionerList = new ArrayList<Practitioner>();
			practitionerList.add(Practitioner);
			practitionerRecords.put(lastName.charAt(0), practitionerList);
		}
		else if(checkUniqueRecord(Practitioner.getRecordID(),Practitioner.getFirstName(),Practitioner.getLastName(),lastName.charAt(0)))
		{
			synchronized(practitionerList)
			{
				practitionerList.add(Practitioner);
			}
		}
		else
		{
			logger.info("Failed to add Doctor Record with record ID : "+Practitioner.getRecordID()+" duplicate record ID");
			return false;
		}
			logger.info("Doctor Record created :\nRecordID \"" +  "DR"+drRecord +  "\", FirstName \"" +  firstName + 
					"\", LastName \"" +  lastName +  "\", Address \"" +  address +  "\", Phone \"" + phone + "\", Specialization \"" + 
					specialization + "\", Location \""+location+"\"");
			
		return true;
	}

	public boolean createNRecord(String managerID, String firstName,
			String lastName, String designation, String status,
			String statusDate) {
		++nrRecord;
		Practitioner Practitioner=new NurseRecord("NR"+nrRecord,firstName,lastName,designation,status,getFormattedDate(statusDate));
		
		//use of synchronized block here lock is achieved on each record inside list 
		ArrayList<Practitioner> practitionerList = practitionerRecords.get(lastName.charAt(0));
		if(practitionerList == null && checkUniqueRecord(Practitioner.getRecordID(),Practitioner.getFirstName(),Practitioner.getLastName(),lastName.charAt(0))) 
		{
			practitionerList = new ArrayList<Practitioner>();
			practitionerList.add(Practitioner);
			practitionerRecords.put(lastName.charAt(0), practitionerList);
		}
		else if(checkUniqueRecord(Practitioner.getRecordID(),Practitioner.getFirstName(),Practitioner.getLastName(),lastName.charAt(0)))
		{
			synchronized(practitionerList)
			{
				practitionerList.add(Practitioner);
			}
		}
		else
		{
			logger.info("Failed to add Nurse Record with record ID : "+Practitioner.getRecordID()+" duplicate record ID");
			return false;
		}
			logger.info("Nurse Record created :\nRecordID \"" +  "NR"+nrRecord +  "\", FirstName \"" +  firstName + 
					"\", LastName \"" +  lastName +  "\", Designation \"" +  designation +  "\", status \"" + status + "\", StatusDate \"" + 
					statusDate+ "\"" );
			
		return true;
	}

	public String getRecordCounts(String managerID, String recordType) {
		DatagramSocket datagramSocket=null;
		staticRecordType=recordType;
		
		try 
		{
			// Create a Datagram Socket and bind it to a local port
			datagramSocket = new DatagramSocket();
			// Place data in byte array
			String data;
			byte [] message;
			InetAddress host = InetAddress.getByName("localhost");
			DatagramPacket request1;
			DatagramPacket request2;
			DatagramPacket reply1;
			DatagramPacket reply2;
			// Create a Datagram Packet to send the request to the server
			if(UDPPort == 6001)
			{
				data = "MTL";
				message = data.getBytes();
				request1 = new DatagramPacket(message, data.length(), host, 6002);
				request2 = new DatagramPacket(message, data.length(), host, 6003);
			}
			else if(UDPPort == 6002)
			{
				data = "LVL";
				message = data.getBytes();
				request1 = new DatagramPacket(message, data.length(), host, 6001);
				request2 = new DatagramPacket(message, data.length(), host, 6003);
			}
			else if(UDPPort == 6003)
			{
				data = "DDO";
				message = data.getBytes();
				request1 = new DatagramPacket(message, data.length(), host, 6001);
				request2 = new DatagramPacket(message, data.length(), host, 6002);
			}
			else
			{
				logger.info("Error getting record counts : Invalid Port number");
				return "Error getting record counts : Invalid Port number";
			}
			datagramSocket.send(request1);
			message = new byte [1000];
			reply1 = new DatagramPacket(message, message.length);
			datagramSocket.receive(reply1);
			datagramSocket.send(request2);
			message = new byte [1000];
			reply2 = new DatagramPacket(message, message.length);
			datagramSocket.receive(reply2);
			String formattedReply1=new String(reply1.getData(), reply1.getOffset(), reply1.getLength());
			String formattedReply2=new String(reply2.getData(), reply2.getOffset(), reply2.getLength());
			
			logger.info(clinicName + ":" + getRecordsFromServer(staticRecordType)+","+ formattedReply1 + "," + formattedReply2);
			return clinicName + ":" + getRecordsFromServer(staticRecordType)+","+ formattedReply1 + "," + formattedReply2;
					
		} 
		catch (SocketException e) 
		{
			System.out.println("SocketException : " + e.getMessage());
		} 
		catch (IOException e) 
		{
			System.out.println("IOException : " + e.getMessage());
		}
		finally
		{
			datagramSocket.close();
		}
		logger.info("Error getting record counts : Socket Excpetion");
		return "Error getting record counts : Socket Excpetion";
	}

	public boolean editRecord(String managerID, String recordID,
			String fieldName, String newValue) {

		boolean isSucess=false;
		if(recordID.startsWith("DR")&&fieldName.equalsIgnoreCase("location") && !(newValue.equalsIgnoreCase("mtl")||newValue.equalsIgnoreCase("lvl")||newValue.equalsIgnoreCase("ddo")))
		{
			logger.info(" could not update doctor record with record ID: "+recordID+" Because of Invalid data for field name: "+fieldName);			
		}
		else if(recordID.startsWith("NR")&& fieldName.equalsIgnoreCase("designation") && !(newValue.equalsIgnoreCase("junior")|| newValue.equalsIgnoreCase("senior")))
		{
			logger.info(" could not update nurse record with record ID: "+recordID+" Because of Invalid data for field name: "+fieldName);
		}
		else if(recordID.startsWith("NR")&& fieldName.equalsIgnoreCase("status") && !(newValue.equalsIgnoreCase("terminated")|| newValue.equalsIgnoreCase("active")))
		{
			logger.info(" could not update nurse record with record ID: "+recordID+" Because of Invalid data for field name: "+fieldName);
		}
		else if(fieldName.equalsIgnoreCase("address")||fieldName.equalsIgnoreCase("phone")||fieldName.equalsIgnoreCase("location")||
				fieldName.equalsIgnoreCase("designation")||fieldName.equalsIgnoreCase("status")||fieldName.equalsIgnoreCase("statusDate"))
		{
			Practitioner practitionerUpdate=null;
			/*synchronized(practitionerRecords) 
			{*/
			boolean recordFound=false;
			for (Map.Entry<Character, ArrayList<Practitioner>> entry : practitionerRecords.entrySet())
		    {
			    ArrayList<Practitioner> practitionerList = entry.getValue();
			    synchronized(practitionerList) 
			    {
			    	for(Practitioner practitioner:practitionerList)
			    	{
			    		if(recordID.startsWith("DR")&& practitioner instanceof DoctorRecord)
			    		{
			    			practitionerUpdate=practitioner;
			    			if(recordID.equals(practitioner.getRecordID()))
			    			{
			    				if(fieldName.equalsIgnoreCase("address")){((DoctorRecord)practitioner).setAddress(newValue); recordFound=true; break;}
			    				if(fieldName.equalsIgnoreCase("phone")){((DoctorRecord)practitioner).setPhone(newValue);recordFound=true; break;}
			    				if(fieldName.equalsIgnoreCase("location")){((DoctorRecord)practitioner).setLocation(newValue);recordFound=true; break;}
			    			}
			    		}
			    		else if(recordID.startsWith("NR")&& practitioner instanceof NurseRecord)
			    		{
			    			practitionerUpdate=practitioner;
			    			if(recordID.equals(practitioner.getRecordID()))
			    			{
			    				if(fieldName.equalsIgnoreCase("designation")){((NurseRecord)practitioner).setDesignation(newValue);recordFound=true; break;}
			    				if(fieldName.equalsIgnoreCase("status")){((NurseRecord)practitioner).setStatus(newValue);recordFound=true; break;}
			    				if(fieldName.equalsIgnoreCase("statusDate")){((NurseRecord)practitioner).setStatusDate(getFormattedDate(newValue));recordFound=true; break;}
			    			}
			    		}
			    	}
		        }
			    	if(recordFound) break;
		        }
			if(practitionerUpdate instanceof DoctorRecord)
				logger.info("!!!Doctor Record updated :\nRecordID \"" +  practitionerUpdate.getRecordID() +  "\", FirstName \"" +  practitionerUpdate.getFirstName() + 
						"\", LastName \"" +  practitionerUpdate.getLastName() +  "\", Address \"" +  ((DoctorRecord)practitionerUpdate).getAddress() +  "\", Phone \"" + ((DoctorRecord)practitionerUpdate).getPhone() + "\", Specialization \"" + 
						((DoctorRecord)practitionerUpdate).getSpecialization() + "\", Location \""+((DoctorRecord)practitionerUpdate).getLocation()+"\"");
			if(practitionerUpdate instanceof NurseRecord)
				logger.info("!!!Nurse Record updated :\nRecordID \"" +  practitionerUpdate.getRecordID() +  "\", FirstName \"" +   practitionerUpdate.getFirstName()  + 
						"\", LastName \"" +  practitionerUpdate.getLastName() +  "\", Designation \"" +  ((NurseRecord)practitionerUpdate).getDesignation() +  "\", status \"" + ((NurseRecord)practitionerUpdate).getStatus() + "\", StatusDate \"" + 
						((NurseRecord)practitionerUpdate).getStatusDate()+ "\"" );	
			isSucess=true;
		}
		else
		{
			logger.info(" could not update record with record ID: "+recordID+" Invalid field name: "+fieldName);
		}
		return isSucess;
	}
	
	/**
	 * Deletes record from this server
	 * @param recordID
	 * @return
	 */
	public String deleteRecordFromServer(String recordID)
	{
		
		for (Map.Entry<Character, ArrayList<Practitioner>> entry : practitionerRecords.entrySet())
	    {
		    ArrayList<Practitioner> practitionerList = entry.getValue();
		    synchronized(practitionerList) 
		    {
		    	for(Practitioner practitioner:practitionerList)
		    	{
		    		if(practitioner.getRecordID().equals(recordID) && recordID.startsWith("DR"))
		    		{
		    			practitionerList.remove(practitioner);
		    			practitionerRecords.remove(entry.getKey());
		    			practitionerRecords.put(entry.getKey(), practitionerList);
		    			return "DR"+"|"+((DoctorRecord)practitioner).getFirstName()+"|"+((DoctorRecord)practitioner).getLastName()+"|"+
		    			((DoctorRecord)practitioner).getAddress()+"|"+((DoctorRecord)practitioner).getPhone()+"|"+
		    			((DoctorRecord)practitioner).getSpecialization()+"|"+((DoctorRecord)practitioner).getLocation();
		    			
		    		}
		    		if(practitioner.getRecordID().equals(recordID) && recordID.startsWith("NR"))
		    		{
		    			practitionerList.remove(practitioner);
		    			practitionerRecords.remove(entry.getKey());
		    			practitionerRecords.put(entry.getKey(), practitionerList);
		    			return "NR"+"|"+((NurseRecord)practitioner).getFirstName()+"|"+((NurseRecord)practitioner).getLastName()+"|"+
		    			((NurseRecord)practitioner).getDesignation()+"|"+((NurseRecord)practitioner).getStatus()+"|"+
		    			((NurseRecord)practitioner).getStatusDate();
		    			
		    		}
		    	
		    	
		    	}
		    }
		   
	    }
		
		return "fail";
		
	}
	
	public String transferRecord(String managerID, String recordID,String remoteClinic) 
	{
		String response = deleteRecordFromServer(recordID);
		
		String code="";
		String firstName="";
		String lastName="";
		String address="";
		String phone="";
		String specialization="";
		String location="";
		String designation="";
		String status="";
		String statusDate="";
		
			
			if(!response.equals("fail") && response.startsWith("DR")) 
			{
				String[] parts = response.split("\\|");
				code = parts[0]; 
				firstName = parts[1];
				lastName= parts[2];
				address=parts[3];
				phone=parts[4];
				specialization=parts[5];
				location =parts[6];
			
				
				logger.info("Doctor Record deleted with recordID "+recordID+" : "+"\nFirstName \"" +  firstName +  "\", LastName \"" + lastName + "\", location \"" + location + "\"" +" from server "+clinicName);
				
				//logger.info("response from clinic server"+response)	;
				
				return response;
					
				}
			
			else if(!response.equals("fail") && response.startsWith("NR"))
			{
				String[] parts = response.split("|");
				 code = parts[0]; 
				 firstName = parts[1];
				 lastName= parts[2];
				 designation=parts[3];
				 status=parts[4];
				 statusDate=parts[5];
				 
					logger.info("Nurse Record deleted with recordID "+recordID+" : "+"\nFirstName \"" +  firstName +  "\", LastName \"" + lastName + "\", location \"" + location + "\"" +" from server "+clinicName);
					return response;
				}
			
			else
			{
				logger.info("Error Transferring Account : removal of record failed");
				return "ERROR_TRANSFER_ACCOUNT";
			}
			
		
			
	}

	@Override
	public void run() {
		DatagramSocket datagramSocket=null;
		try 
		{
			datagramSocket = new DatagramSocket(UDPPort);
			byte [] buffer = new byte [1000];
			DatagramPacket request;
			DatagramPacket reply;
			while(true)
			{
				request = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(request);
				buffer = (clinicName + ":" + getRecordsFromServer(staticRecordType)).getBytes();
				reply = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
				datagramSocket.send(reply);
			}
		} 
		catch (SocketException e) 
		{
			System.out.println("SocketException : " + e.getMessage());
		} 
		catch (IOException e) 
		{
			System.out.println("IOException : " + e.getMessage());
		}
		finally
		{
			datagramSocket.close();
		}
		
	}
	
	/**
	 * execution of thread starts here.thread for all 3 servers being created 
	 * @param args
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	public static void main(String[] args) throws RemoteException, AlreadyBoundException 
	{
		
		
		
		new Thread(new Server("MTL",6001,args)).start();
		new Thread(new Server("LVL",6002,args)).start();
		new Thread( new Server("DDO",6003,args)).start();
		
	}


}
