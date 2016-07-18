/**
 * Server.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package DefaultNamespace;

public interface Server extends java.rmi.Remote {
    public boolean createNRecord(java.lang.String managerID, java.lang.String firstName, java.lang.String lastName, java.lang.String designation, java.lang.String status, java.lang.String statusDate) throws java.rmi.RemoteException;
    public java.lang.String getRecordCounts(java.lang.String managerID, java.lang.String recordType) throws java.rmi.RemoteException;
    public boolean createDRecord(java.lang.String managerID, java.lang.String firstName, java.lang.String lastName, java.lang.String address, java.lang.String phone, java.lang.String specialization, java.lang.String location) throws java.rmi.RemoteException;
    public boolean editRecord(java.lang.String managerID, java.lang.String recordID, java.lang.String fieldName, java.lang.String newValue) throws java.rmi.RemoteException;
    public java.lang.String transferRecord(java.lang.String managerID, java.lang.String recordID, java.lang.String remoteClinic) throws java.rmi.RemoteException;
    public java.lang.String deleteRecordFromServer(java.lang.String recordID) throws java.rmi.RemoteException;
}
