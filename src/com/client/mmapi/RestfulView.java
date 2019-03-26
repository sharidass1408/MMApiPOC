/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.mmapi;
import com.client.mmapi.dao.JSONResponseData;
import com.client.mmapi.dao.MMDao;
import com.client.mmapi.dao.MMTimeStampData;
import com.client.mmapi.dao.TimeStampData;
import com.client.mmapi.utils.ConfigRestful;
import com.client.mmapi.utils.NameValuePairs;
import com.client.mmapi.utils.TestException;
import com.client.mmapi.webgateway.UIResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author brado
 * Set up the view and interaction with the test
 * 
 */
public class RestfulView {
    private RestfulController control;
    
    public RestfulView() {
        control=null;
    };
    /**
     * 
     * @param control 
     */
    public void setContoller(RestfulController control) {
        this.control=control;
    }
    
    /**
     * Run a test as described in the API documentation.
     * The test will keep recursing until Stop is entered
     */
    public void runTests()  {
        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String accStr="";  

            System.out.println("Entry Format: apimessage,parmName=parmValue,parmName=parmValue \r\n");
            System.out.println("Enter API Message with Data.  Enter Stop to quit: \r\n");
            try { 
                accStr = br.readLine();
                if (accStr.toLowerCase().equalsIgnoreCase("stop"))
                    break;
                String[] sa = accStr.split(",");
                String mess = sa[0];
                mess=mess.toLowerCase();
                HashMap<String,String> dataMap = new HashMap();
                for (int x=1; x<sa.length;x++) {
                    String dtPairs=sa[x];
                    String dt[] = dtPairs.split("=");
                    dataMap.put(dt[0], dt[1]);

                }
                if(mess.equals("api_timestamp_data")) {
                	LocalDate start = LocalDate.of(2018, Month.JANUARY, 2);
                	LocalDate end = LocalDate.of(2018, Month.DECEMBER, 31);
                	
                	for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                		dataMap.put("startdate",date.toString());
                		runApiTest(mess, dataMap);
                	}
                	
                }else
                runApiTest(mess, dataMap);
                
            } catch (IOException ex) {
                System.out.print(ex.getMessage());
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.print("Error parsing Entry Data");
            }
        }
    }
    
    
    private void runApiTest(String apiMessage,HashMap<String,String> dataMap) {
        NameValuePairs np = new NameValuePairs();
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {   
            String key = entry.getKey();
            String value = entry.getValue();
            np.addPair(key, value);
        }    
        
        switch (apiMessage) {
            case "api_login":
                np.add(ConfigRestful.getCredentials());
                np.addPair("msg_id", "api_login");
                // reply type to use for this login session
                np.addPair("reply_type", ConfigRestful.getReplyType());
            break;    
            case "api_ping":
                np.addPair("msg_id", "api_ping");
            break;   
            case "api_facility_name":
                np.addPair("msg_id", "api_facility_name");
            break; 
            case "api_facility_name_status":
                np.addPair("msg_id", "api_facility_name_status");
            break; 
            case "api_facility_id":
                np.addPair("msg_id", "api_facility_id");
            break;    
            case "api_patient_name":
                np.addPair("msg_id", "api_patient_name");
            break;    
            case "api_patient_id":
                np.addPair("msg_id", "api_patient_id");
            break;
            case "api_corp_facility_id":
                np.addPair("msg_id", "api_corp_facility_id");
            break;
            case "api_corp_facility_name_status":
                np.addPair("msg_id", "api_corp_facility_name_status");
            break;
            case "api_corp_facility_name":
                np.addPair("msg_id", "api_corp_facility_name");
            break;
            case "api_dispatch_region_list":
                np.addPair("msg_id", "api_dispatch_region_list");
            break;
            case "api_dispatch_region_id":
                np.addPair("msg_id", "api_dispatch_region_id");
            break;
            case "api_referring_id":
                np.addPair("msg_id", "api_referring_id");
            break;
            case "api_referring_name":
                np.addPair("msg_id", "api_referring_name");
            break;
            case "api_referring_name_status":
                np.addPair("msg_id", "api_referring_name_status");
            break;
            case "api_provider":
                np.addPair("msg_id", "api_provider");
            break;
            case "api_invoice_num":
                np.addPair("msg_id", "api_invoice_num");
            break;
            case "api_tech_id":
                np.addPair("msg_id", "api_tech_id");
            break;
            case "api_tech_list_name_status":
                np.addPair("msg_id", "api_tech_list_name_status");
            break;
            case "api_claim_id":
                np.addPair("msg_id", "api_claim_id");
            break;
            case "api_claim_inv_num":
                np.addPair("msg_id", "api_claim_inv_num");
            break;
            case "api_claim_detail":
                np.addPair("msg_id", "api_claim_detail");
            break;
            case "api_exam_id":
                np.addPair("msg_id", "api_exam_id");
            break;
            case "api_exam_patient_date_range":
                np.addPair("msg_id", "api_exam_patient_date_range");
            break;
            case "api_timestamp_data":
                np.addPair("msg_id", "api_timestamp_data");
            break;
            case "api_cpt_code":
                np.addPair("msg_id", "api_cpt_code");
            break;
            
            case "api_logout":
                np.addPair("msg_id", "api_logout");
            break;    
        }
        
        
        
        try {
            execute(np,apiMessage);
        } catch (TestException ex) {
            Logger.getLogger(RestfulView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Call the controller to execute the test.
     * @param np
     * @param apiMessage
     * @throws TestException 
     */
    private void execute(NameValuePairs np,String apiMessage) throws TestException {
        UIResponse response=null;
        response = control.executeTest(np);
        viewResponse(response,apiMessage);
    }
    
    public void viewResponse(UIResponse response,String apiMessage) {
        String replyType = ConfigRestful.getReplyType();
        switch (replyType) {
            case "json":
            case "xml":
                RestfulStringResults strResults = new RestfulStringResults();
                displayResults(strResults,response,apiMessage);
                if(apiMessage.equals("api_timestamp_data")) {
                	insertData(response);
                }
                
                
            break;    
        }
    }
    
    private void insertData(UIResponse response) {
    	if(response.getStatusCode() == 200) {
    		JSONResponseData jsonResponseData =  convertJSONIntoJavaObject(response);
    		performInsert(jsonResponseData.getItems());
    	}
    	
	}
    
    
	private void performInsert(List<MMTimeStampData> items) {
		MMDao mmDao = new MMDao();
		String   insertQuery = getInsertQuery();
		for(MMTimeStampData mmtimeStampData: items) {
			String[] inputSelections = getInputSelections(mmtimeStampData);
			mmDao.insertDataUsingPrepStatement(insertQuery, inputSelections);
		}
	}
	
	private String[] getInputSelections(MMTimeStampData mmtimeStampData) {
		
		List<String> inputSelections= new ArrayList<>();
		inputSelections.add(mmtimeStampData.getPkExam());
		inputSelections.add(mmtimeStampData.getFkPatient());
		inputSelections.add(mmtimeStampData.getFkTechnician());
		inputSelections.add(mmtimeStampData.getFkReviewEntityID());
		inputSelections.add(mmtimeStampData.getFkTranscriberEntityID());
		inputSelections.add(mmtimeStampData.getFkReferringPhysician());
		inputSelections.add(mmtimeStampData.getModality());
		inputSelections.add(mmtimeStampData.getExamRequested());
		inputSelections.add(mmtimeStampData.getReasonOrdered());
		inputSelections.add(mmtimeStampData.getScheduleServiceDate());
		inputSelections.add(mmtimeStampData.getServicePriority());
		inputSelections.add(mmtimeStampData.getProcedureType());
		inputSelections.add(mmtimeStampData.getNumPatientSeen());
		inputSelections.add(mmtimeStampData.getOrderPersonName());
		inputSelections.add(mmtimeStampData.getOrderTS());
		inputSelections.add(mmtimeStampData.getOrderAssignedTS());
		inputSelections.add(mmtimeStampData.getOrderCompleteTS());
		inputSelections.add(mmtimeStampData.getResultsPhonedToNumber());
		inputSelections.add(mmtimeStampData.getTechConfirmed());
		inputSelections.add(mmtimeStampData.getJobProgress());
		inputSelections.add(mmtimeStampData.getExamComments());
		inputSelections.add(mmtimeStampData.getModuleMask());
		inputSelections.add(mmtimeStampData.getCptCountProvider());
		inputSelections.add(mmtimeStampData.getCptCountRadiologist());
		inputSelections.add(mmtimeStampData.getTechComments());
		inputSelections.add(mmtimeStampData.getOrderTakenBy());
		inputSelections.add(mmtimeStampData.getOrderedOnLine());
		inputSelections.add(mmtimeStampData.getStudyUID());
		inputSelections.add(mmtimeStampData.getStudyDueTS());
		inputSelections.add(mmtimeStampData.getOrigOrder());
		inputSelections.add(mmtimeStampData.getFkFacility());
		inputSelections.add(mmtimeStampData.getRefBatchNum());
		inputSelections.add(mmtimeStampData.getRefSignedFlag());
		inputSelections.add(mmtimeStampData.getRefTimes());
		inputSelections.add(mmtimeStampData.getAutoFaxNumber());
		inputSelections.add(mmtimeStampData.getPractitioner());
		inputSelections.add(mmtimeStampData.getScheduleServiceTime());
		inputSelections.add(mmtimeStampData.getImageViews());
		inputSelections.add(mmtimeStampData.getImageRetakes());
		inputSelections.add(mmtimeStampData.getRetakeReason());
		inputSelections.add(mmtimeStampData.getEmrOrderid());
		inputSelections.add(mmtimeStampData.getMedNecessity());
		inputSelections.add(mmtimeStampData.getEmrSanityError());
		inputSelections.add(mmtimeStampData.getTechArriveTS());
		inputSelections.add(mmtimeStampData.getTechDispatchedBy());
		inputSelections.add(mmtimeStampData.getPriorAuthorization());
		inputSelections.add(mmtimeStampData.getETX());
		inputSelections.add(mmtimeStampData.getPatientFirstName());
		inputSelections.add(mmtimeStampData.getPatientDob());
		inputSelections.add(mmtimeStampData.getPatientRoomNum());
		inputSelections.add(mmtimeStampData.getStudyID());
		inputSelections.add(mmtimeStampData.getAccessionNumber());
		inputSelections.add(mmtimeStampData.getFacilityName());
		inputSelections.add(mmtimeStampData.getTechLast());
		inputSelections.add(mmtimeStampData.getTechFirst());
		inputSelections.add(mmtimeStampData.getRegionName());
		inputSelections.add(mmtimeStampData.getCorpName());
		inputSelections.add(mmtimeStampData.getReferLast());
		inputSelections.add(mmtimeStampData.getReferFirst());
		inputSelections.add(mmtimeStampData.getReferSuffix());
		inputSelections.add(mmtimeStampData.getReferPhysicianID());
		inputSelections.add(mmtimeStampData.getReferTaxonomyCode());
		inputSelections.add(mmtimeStampData.getFirstReportDocID());
		inputSelections.add(mmtimeStampData.getFirstImageUploadTS());
		inputSelections.add(mmtimeStampData.getFirstReportTranscribeTS());
		inputSelections.add(mmtimeStampData.getFirstReportReviewedByDoctor());
		inputSelections.add(mmtimeStampData.getFirstReportFaxedTS());
		inputSelections.add(mmtimeStampData.getFirstReportResultsPhonedTS());
		inputSelections.add(mmtimeStampData.getCritFind());
		
		return inputSelections.toArray(new String[0]);		
	}
	private String getInsertQuery() {

		String insertQuery =
				new String("INSERT INTO mmapi_timestamp_data "
			 + "(pkExam,fkPatient,fkTechnician, fkReviewEntityID, fkTranscriberEntityID,fkReferringPhysician,modality,examRequested, reasonOrdered," + 
				"scheduleServiceDate,servicePriority, procedureType, numPatientSeen, orderPersonName, orderTS, orderAssignedTS,orderCompleteTS, " + 
				"resultsPhonedToNumber, techConfirmed, jobProgress, examComments, moduleMask,cptCountProvider, cptCountRadiologist, techComments," + 
				" orderTakenBy, orderedOnLine,studyDueTS,studyUID,origOrder,fkFacility, refBatchNum, refSignedFlag, refTimes,autoFaxNumber, " + 
				"practitioner,scheduleServiceTime,imageViews,imageRetakes, retakeReason,emrOrderid,medNecessity,emrSanityError,techArriveTS," + 
				"techDispatchedBy,priorAuthorization,ETX, patientFirstName, patientDob, patientRoomNum, studyID, accessionNumber,facilityName," + 
				" techLast,  techFirst, regionName, corpName, referLast, referFirst,referSuffix,referPhysicianID,referTaxonomyCode, firstReportDocID," + 
				" firstImageUploadTS, firstReportTranscribeTS, firstReportReviewedByDoctor, firstReportFaxedTS, firstReportResultsPhonedTS, critFind)" + 
				"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				
		
		return insertQuery;				
	}
	private JSONResponseData convertJSONIntoJavaObject(UIResponse response) {
	
		
		Object data = response.getData();
		
		ObjectMapper ob_Objectmapper = new ObjectMapper();
		
		JSONResponseData jsonResponseData = new JSONResponseData();
		try {
			jsonResponseData = ob_Objectmapper.readValue(data.toString(),JSONResponseData.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonResponseData;
	}
	
	
	private HashMap<String, String> getHashMap(String data) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		data = data.replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .replace("table:", "");

        String[] elems = data.split(",");

        for (String string : elems) {
            String[] keyval = string.split(":");

            map.put(keyval[0].trim(), keyval[0].trim());
        }

        //Printing the map
        for (Entry<String, String> kv : map.entrySet()) {
            System.out.println(kv.getKey() + " : " + kv.getValue());
        }
		return map;
	}
	
	
	private void displayResults(RestfulStringResults strResults,UIResponse response,String apiMessage) {
        strResults.display(response, apiMessage);
    }
    
}
