package DefaultNamespace;

public class ServerProxy implements DefaultNamespace.Server {
  private String _endpoint = null;
  private DefaultNamespace.Server server = null;
  
  public ServerProxy() {
    _initServerProxy();
  }
  
  public ServerProxy(String endpoint) {
    _endpoint = endpoint;
    _initServerProxy();
  }
  
  private void _initServerProxy() {
    try {
      server = (new DefaultNamespace.ServerServiceLocator()).getServer();
      if (server != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)server)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)server)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (server != null)
      ((javax.xml.rpc.Stub)server)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public DefaultNamespace.Server getServer() {
    if (server == null)
      _initServerProxy();
    return server;
  }
  
  public boolean createNRecord(java.lang.String managerID, java.lang.String firstName, java.lang.String lastName, java.lang.String designation, java.lang.String status, java.lang.String statusDate) throws java.rmi.RemoteException{
    if (server == null)
      _initServerProxy();
    return server.createNRecord(managerID, firstName, lastName, designation, status, statusDate);
  }
  
  public java.lang.String getRecordCounts(java.lang.String managerID, java.lang.String recordType) throws java.rmi.RemoteException{
    if (server == null)
      _initServerProxy();
    return server.getRecordCounts(managerID, recordType);
  }
  
  public boolean createDRecord(java.lang.String managerID, java.lang.String firstName, java.lang.String lastName, java.lang.String address, java.lang.String phone, java.lang.String specialization, java.lang.String location) throws java.rmi.RemoteException{
    if (server == null)
      _initServerProxy();
    return server.createDRecord(managerID, firstName, lastName, address, phone, specialization, location);
  }
  
  public boolean editRecord(java.lang.String managerID, java.lang.String recordID, java.lang.String fieldName, java.lang.String newValue) throws java.rmi.RemoteException{
    if (server == null)
      _initServerProxy();
    return server.editRecord(managerID, recordID, fieldName, newValue);
  }
  
  public java.lang.String transferRecord(java.lang.String managerID, java.lang.String recordID, java.lang.String remoteClinic) throws java.rmi.RemoteException{
    if (server == null)
      _initServerProxy();
    return server.transferRecord(managerID, recordID, remoteClinic);
  }
  
  public java.lang.String deleteRecordFromServer(java.lang.String recordID) throws java.rmi.RemoteException{
    if (server == null)
      _initServerProxy();
    return server.deleteRecordFromServer(recordID);
  }
  
  
}