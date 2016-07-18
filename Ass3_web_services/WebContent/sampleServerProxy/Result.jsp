<%@page contentType="text/html;charset=UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<HTML>
<HEAD>
<TITLE>Result</TITLE>
</HEAD>
<BODY>
<H1>Result</H1>

<jsp:useBean id="sampleServerProxyid" scope="session" class="DefaultNamespace.ServerProxy" />
<%
if (request.getParameter("endpoint") != null && request.getParameter("endpoint").length() > 0)
sampleServerProxyid.setEndpoint(request.getParameter("endpoint"));
%>

<%
String method = request.getParameter("method");
int methodID = 0;
if (method == null) methodID = -1;

if(methodID != -1) methodID = Integer.parseInt(method);
boolean gotMethod = false;

try {
switch (methodID){ 
case 2:
        gotMethod = true;
        java.lang.String getEndpoint2mtemp = sampleServerProxyid.getEndpoint();
if(getEndpoint2mtemp == null){
%>
<%=getEndpoint2mtemp %>
<%
}else{
        String tempResultreturnp3 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(getEndpoint2mtemp));
        %>
        <%= tempResultreturnp3 %>
        <%
}
break;
case 5:
        gotMethod = true;
        String endpoint_0id=  request.getParameter("endpoint8");
            java.lang.String endpoint_0idTemp = null;
        if(!endpoint_0id.equals("")){
         endpoint_0idTemp  = endpoint_0id;
        }
        sampleServerProxyid.setEndpoint(endpoint_0idTemp);
break;
case 10:
        gotMethod = true;
        DefaultNamespace.Server getServer10mtemp = sampleServerProxyid.getServer();
if(getServer10mtemp == null){
%>
<%=getServer10mtemp %>
<%
}else{
        if(getServer10mtemp!= null){
        String tempreturnp11 = getServer10mtemp.toString();
        %>
        <%=tempreturnp11%>
        <%
        }}
break;
case 13:
        gotMethod = true;
        String managerID_1id=  request.getParameter("managerID16");
            java.lang.String managerID_1idTemp = null;
        if(!managerID_1id.equals("")){
         managerID_1idTemp  = managerID_1id;
        }
        String firstName_2id=  request.getParameter("firstName18");
            java.lang.String firstName_2idTemp = null;
        if(!firstName_2id.equals("")){
         firstName_2idTemp  = firstName_2id;
        }
        String lastName_3id=  request.getParameter("lastName20");
            java.lang.String lastName_3idTemp = null;
        if(!lastName_3id.equals("")){
         lastName_3idTemp  = lastName_3id;
        }
        String designation_4id=  request.getParameter("designation22");
            java.lang.String designation_4idTemp = null;
        if(!designation_4id.equals("")){
         designation_4idTemp  = designation_4id;
        }
        String status_5id=  request.getParameter("status24");
            java.lang.String status_5idTemp = null;
        if(!status_5id.equals("")){
         status_5idTemp  = status_5id;
        }
        String statusDate_6id=  request.getParameter("statusDate26");
            java.lang.String statusDate_6idTemp = null;
        if(!statusDate_6id.equals("")){
         statusDate_6idTemp  = statusDate_6id;
        }
        boolean createNRecord13mtemp = sampleServerProxyid.createNRecord(managerID_1idTemp,firstName_2idTemp,lastName_3idTemp,designation_4idTemp,status_5idTemp,statusDate_6idTemp);
        String tempResultreturnp14 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(createNRecord13mtemp));
        %>
        <%= tempResultreturnp14 %>
        <%
break;
case 28:
        gotMethod = true;
        String managerID_7id=  request.getParameter("managerID31");
            java.lang.String managerID_7idTemp = null;
        if(!managerID_7id.equals("")){
         managerID_7idTemp  = managerID_7id;
        }
        String recordType_8id=  request.getParameter("recordType33");
            java.lang.String recordType_8idTemp = null;
        if(!recordType_8id.equals("")){
         recordType_8idTemp  = recordType_8id;
        }
        java.lang.String getRecordCounts28mtemp = sampleServerProxyid.getRecordCounts(managerID_7idTemp,recordType_8idTemp);
if(getRecordCounts28mtemp == null){
%>
<%=getRecordCounts28mtemp %>
<%
}else{
        String tempResultreturnp29 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(getRecordCounts28mtemp));
        %>
        <%= tempResultreturnp29 %>
        <%
}
break;
case 35:
        gotMethod = true;
        String managerID_9id=  request.getParameter("managerID38");
            java.lang.String managerID_9idTemp = null;
        if(!managerID_9id.equals("")){
         managerID_9idTemp  = managerID_9id;
        }
        String firstName_10id=  request.getParameter("firstName40");
            java.lang.String firstName_10idTemp = null;
        if(!firstName_10id.equals("")){
         firstName_10idTemp  = firstName_10id;
        }
        String lastName_11id=  request.getParameter("lastName42");
            java.lang.String lastName_11idTemp = null;
        if(!lastName_11id.equals("")){
         lastName_11idTemp  = lastName_11id;
        }
        String address_12id=  request.getParameter("address44");
            java.lang.String address_12idTemp = null;
        if(!address_12id.equals("")){
         address_12idTemp  = address_12id;
        }
        String phone_13id=  request.getParameter("phone46");
            java.lang.String phone_13idTemp = null;
        if(!phone_13id.equals("")){
         phone_13idTemp  = phone_13id;
        }
        String specialization_14id=  request.getParameter("specialization48");
            java.lang.String specialization_14idTemp = null;
        if(!specialization_14id.equals("")){
         specialization_14idTemp  = specialization_14id;
        }
        String location_15id=  request.getParameter("location50");
            java.lang.String location_15idTemp = null;
        if(!location_15id.equals("")){
         location_15idTemp  = location_15id;
        }
        boolean createDRecord35mtemp = sampleServerProxyid.createDRecord(managerID_9idTemp,firstName_10idTemp,lastName_11idTemp,address_12idTemp,phone_13idTemp,specialization_14idTemp,location_15idTemp);
        String tempResultreturnp36 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(createDRecord35mtemp));
        %>
        <%= tempResultreturnp36 %>
        <%
break;
case 52:
        gotMethod = true;
        String managerID_16id=  request.getParameter("managerID55");
            java.lang.String managerID_16idTemp = null;
        if(!managerID_16id.equals("")){
         managerID_16idTemp  = managerID_16id;
        }
        String recordID_17id=  request.getParameter("recordID57");
            java.lang.String recordID_17idTemp = null;
        if(!recordID_17id.equals("")){
         recordID_17idTemp  = recordID_17id;
        }
        String fieldName_18id=  request.getParameter("fieldName59");
            java.lang.String fieldName_18idTemp = null;
        if(!fieldName_18id.equals("")){
         fieldName_18idTemp  = fieldName_18id;
        }
        String newValue_19id=  request.getParameter("newValue61");
            java.lang.String newValue_19idTemp = null;
        if(!newValue_19id.equals("")){
         newValue_19idTemp  = newValue_19id;
        }
        boolean editRecord52mtemp = sampleServerProxyid.editRecord(managerID_16idTemp,recordID_17idTemp,fieldName_18idTemp,newValue_19idTemp);
        String tempResultreturnp53 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(editRecord52mtemp));
        %>
        <%= tempResultreturnp53 %>
        <%
break;
case 63:
        gotMethod = true;
        String managerID_20id=  request.getParameter("managerID66");
            java.lang.String managerID_20idTemp = null;
        if(!managerID_20id.equals("")){
         managerID_20idTemp  = managerID_20id;
        }
        String recordID_21id=  request.getParameter("recordID68");
            java.lang.String recordID_21idTemp = null;
        if(!recordID_21id.equals("")){
         recordID_21idTemp  = recordID_21id;
        }
        String remoteClinic_22id=  request.getParameter("remoteClinic70");
            java.lang.String remoteClinic_22idTemp = null;
        if(!remoteClinic_22id.equals("")){
         remoteClinic_22idTemp  = remoteClinic_22id;
        }
        java.lang.String transferRecord63mtemp = sampleServerProxyid.transferRecord(managerID_20idTemp,recordID_21idTemp,remoteClinic_22idTemp);
if(transferRecord63mtemp == null){
%>
<%=transferRecord63mtemp %>
<%
}else{
        String tempResultreturnp64 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(transferRecord63mtemp));
        %>
        <%= tempResultreturnp64 %>
        <%
}
break;
case 72:
        gotMethod = true;
        String recordID_23id=  request.getParameter("recordID75");
            java.lang.String recordID_23idTemp = null;
        if(!recordID_23id.equals("")){
         recordID_23idTemp  = recordID_23id;
        }
        java.lang.String deleteRecordFromServer72mtemp = sampleServerProxyid.deleteRecordFromServer(recordID_23idTemp);
if(deleteRecordFromServer72mtemp == null){
%>
<%=deleteRecordFromServer72mtemp %>
<%
}else{
        String tempResultreturnp73 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(deleteRecordFromServer72mtemp));
        %>
        <%= tempResultreturnp73 %>
        <%
}
break;
}
} catch (Exception e) { 
%>
Exception: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.toString()) %>
Message: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.getMessage()) %>
<%
return;
}
if(!gotMethod){
%>
result: N/A
<%
}
%>
</BODY>
</HTML>