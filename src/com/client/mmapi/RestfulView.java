/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.mmapi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.client.mmapi.dao.JSONFacilityResponseData;
import com.client.mmapi.dao.JSONTimeStampResponseData;
import com.client.mmapi.dao.MMDao;
import com.client.mmapi.dao.MMFacilityData;
import com.client.mmapi.dao.MMTimeStampData;
import com.client.mmapi.utils.ConfigRestful;
import com.client.mmapi.utils.NameValuePairs;
import com.client.mmapi.utils.TestException;
import com.client.mmapi.webgateway.UIResponse;


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
                if(response.getStatusCode() == 200) {
            		
	                if(apiMessage.equals("api_timestamp_data")) {
	                	JSONTimeStampResponseData jsonTimeStampResponseData =  convertJSONIntoJavaObject(response);
	                	insertTimeStampData(jsonTimeStampResponseData.getItems());
	                } else if(apiMessage.equals("api_facility_id")) {
	                	JSONFacilityResponseData jsonFacilityResponseData = convertJSONIntoJavaObjectForFacility(response);
	                	insertFacilityInfoById(jsonFacilityResponseData.getItems());
	                }
                }
                
                
            break;    
        }
    }
    
    
	private void insertFacilityInfoById(List<MMFacilityData> items) {
		/*
		 * MMDao mmDao = new MMDao(); String insertFacilityByID =
		 * getInsertFacilityByIDQuery(); for(MMFacilityData mmFacilityData: items) {
		 * String[] columnValues = getColumnValuesForFacilityInfo(mmFacilityData);
		 * mmDao.insertDataUsingPrepStatement(insertFacilityByID, columnValues); }
		 */
		
	}
	
	//Fixme: Replace this code with JPA
	private String[] getColumnValuesForFacilityInfo(MMFacilityData mmFacilityData) {

		List<String> columnValues= new ArrayList<>();
		columnValues.add(mmFacilityData.getPkFacility());
		columnValues.add(mmFacilityData.getFkCorpFacility());
		columnValues.add(mmFacilityData.getFkDispatchRegion());
		columnValues.add(mmFacilityData.getName());
		columnValues.add(mmFacilityData.getAddr1());
		columnValues.add(mmFacilityData.getAddr2());
		columnValues.add(mmFacilityData.getCity());
		columnValues.add(mmFacilityData.getState());
		columnValues.add(mmFacilityData.getPostalCode());
		columnValues.add(mmFacilityData.getCounty());
		columnValues.add(mmFacilityData.getPhone());
		columnValues.add(mmFacilityData.getFax());
		columnValues.add(mmFacilityData.getEmail());
		columnValues.add(mmFacilityData.getMaxUsers());
		columnValues.add(mmFacilityData.getNursingContact());
		columnValues.add(mmFacilityData.getBillingContact());
		columnValues.add(mmFacilityData.getBillingAddr1());
		columnValues.add(mmFacilityData.getBillingAddr2());
		columnValues.add(mmFacilityData.getBillingCity());
		columnValues.add(mmFacilityData.getBillingState());
		columnValues.add(mmFacilityData.getBillingPostalCode());
		columnValues.add(mmFacilityData.getFinanceChargePct());
		columnValues.add(mmFacilityData.getPricingSchedule());
		columnValues.add(mmFacilityData.getPlaceOfServiceCode());
		columnValues.add(mmFacilityData.getFacilityComments());
		columnValues.add(mmFacilityData.getBedCount());
		columnValues.add(mmFacilityData.getFacilityNPI());
		columnValues.add(mmFacilityData.getStatusFlag());
		columnValues.add(mmFacilityData.getBillingEmail());
		columnValues.add(mmFacilityData.getBillingPhone());
		columnValues.add(mmFacilityData.getAdminContact());
		columnValues.add(mmFacilityData.getAdminEmail());
		columnValues.add(mmFacilityData.getStartDate().toString());
		columnValues.add(mmFacilityData.getIdtfDiscountPct());
		columnValues.add(mmFacilityData.getXrayDiscountPct());
		columnValues.add(mmFacilityData.getEkgDiscountPct());
		columnValues.add(mmFacilityData.getTransDiscountPct());
		columnValues.add(mmFacilityData.getSetupDiscountPct());
		columnValues.add(mmFacilityData.getSpecialDiscountPct());
		columnValues.add(mmFacilityData.getReportExportFlag());
		columnValues.add(mmFacilityData.getProviderSalesRep());
		columnValues.add(mmFacilityData.getProviderServiceRep());		
		columnValues.add(mmFacilityData.getEmrStatusFlag());
		columnValues.add(mmFacilityData.getEmrHoldForReview());
		columnValues.add(mmFacilityData.getFkEMR());
		columnValues.add(mmFacilityData.getEmrName());
		columnValues.add(mmFacilityData.getPatientsReqInsFlag());
		columnValues.add(mmFacilityData.getDefaultRG());
		columnValues.add(mmFacilityData.getLat());
		columnValues.add(mmFacilityData.getLon());
		columnValues.add(mmFacilityData.getOrderScript());
		
		return columnValues.toArray(new String[0]);
	}
	
	
	/*
	 * private String getInsertFacilityByIDQuery() { String insertQuery = new
	 * String("INSERT INTO mmapi_facility_data " +
	 * 
	 * return insertQuery; }
	 */
	private void insertTimeStampData(List<MMTimeStampData> items) {
		MMDao mmDao = new MMDao();
		String   insertTimeStampQuery = getInsertTimeStampQuery();
		for(MMTimeStampData mmtimeStampData: items) {
			Object[] columnValues = getColumnValues(mmtimeStampData);
			mmDao.insertDataUsingPrepStatement(insertTimeStampQuery, columnValues);
		}
	}
	
	private Object[] getColumnValues(MMTimeStampData mmtimeStampData) {
		
		List<Object> columnValues= new ArrayList<>();
		columnValues.add(mmtimeStampData.getPkExam());
		columnValues.add(mmtimeStampData.getFkPatient());
		columnValues.add(mmtimeStampData.getFkTechnician());
		columnValues.add(mmtimeStampData.getFkReviewEntityID());
		columnValues.add(mmtimeStampData.getFkTranscriberEntityID());
		columnValues.add(mmtimeStampData.getFkReferringPhysician());
		columnValues.add(mmtimeStampData.getModality());
		columnValues.add(mmtimeStampData.getExamRequested());
		columnValues.add(mmtimeStampData.getReasonOrdered());
		columnValues.add(mmtimeStampData.getScheduleServiceDate());
		columnValues.add(mmtimeStampData.getServicePriority());
		columnValues.add(mmtimeStampData.getProcedureType());
		columnValues.add(mmtimeStampData.getNumPatientSeen());
		columnValues.add(mmtimeStampData.getOrderPersonName());
		
		columnValues.add(convertToSmallDatetimeFormat(mmtimeStampData.getOrderTS()));
		columnValues.add(convertToSmallDatetimeFormat(mmtimeStampData.getOrderAssignedTS()));
		columnValues.add(convertToSmallDatetimeFormat(mmtimeStampData.getOrderCompleteTS()));
		columnValues.add(mmtimeStampData.getResultsPhonedToNumber());
		columnValues.add(mmtimeStampData.getTechConfirmed());
		columnValues.add(mmtimeStampData.getJobProgress());
		columnValues.add(mmtimeStampData.getExamComments());
		columnValues.add(mmtimeStampData.getModuleMask());
		columnValues.add(mmtimeStampData.getCptCountProvider());
		columnValues.add(mmtimeStampData.getCptCountRadiologist());
		columnValues.add(mmtimeStampData.getTechComments());
		columnValues.add(mmtimeStampData.getOrderTakenBy());
		columnValues.add(mmtimeStampData.getOrderedOnLine());
		columnValues.add(mmtimeStampData.getStudyUID());
		columnValues.add(mmtimeStampData.getStudyDueTS());
		columnValues.add(mmtimeStampData.getOrigOrder());
		columnValues.add(mmtimeStampData.getFkFacility());
		columnValues.add(mmtimeStampData.getRefBatchNum());
		columnValues.add(mmtimeStampData.getRefSignedFlag());
		columnValues.add(mmtimeStampData.getRefTimes());
		columnValues.add(mmtimeStampData.getAutoFaxNumber());
		columnValues.add(mmtimeStampData.getPractitioner());
		columnValues.add(mmtimeStampData.getScheduleServiceTime());
		columnValues.add(mmtimeStampData.getImageViews());
		columnValues.add(mmtimeStampData.getImageRetakes());
		columnValues.add(mmtimeStampData.getRetakeReason());
		columnValues.add(mmtimeStampData.getEmrOrderid());
		columnValues.add(mmtimeStampData.getMedNecessity());
		columnValues.add(mmtimeStampData.getEmrSanityError());
		columnValues.add(mmtimeStampData.getTechArriveTS());
		columnValues.add(mmtimeStampData.getTechDispatchedBy());
		columnValues.add(mmtimeStampData.getPriorAuthorization());
		columnValues.add(mmtimeStampData.getETX());
		columnValues.add(mmtimeStampData.getPatientLastName());
		columnValues.add(mmtimeStampData.getPatientFirstName());
		columnValues.add(mmtimeStampData.getPatientDob());
		columnValues.add(mmtimeStampData.getPatientRoomNum());
		columnValues.add(mmtimeStampData.getStudyID());
		columnValues.add(mmtimeStampData.getAccessionNumber());
		columnValues.add(mmtimeStampData.getFacilityName());
		columnValues.add(mmtimeStampData.getTechLast());
		columnValues.add(mmtimeStampData.getTechFirst());
		columnValues.add(mmtimeStampData.getRegionName());
		columnValues.add(mmtimeStampData.getCorpName());
		columnValues.add(mmtimeStampData.getReferLast());
		columnValues.add(mmtimeStampData.getReferFirst());
		columnValues.add(mmtimeStampData.getReferSuffix());
		columnValues.add(mmtimeStampData.getReferPhysicianID());
		columnValues.add(mmtimeStampData.getReferTaxonomyCode());
		columnValues.add(mmtimeStampData.getFirstReportDocID());
		columnValues.add(mmtimeStampData.getFirstImageUploadTS());
		columnValues.add(mmtimeStampData.getFirstReportTranscribeTS());
		columnValues.add(mmtimeStampData.getFirstReportReviewedByDoctor());
		columnValues.add(mmtimeStampData.getFirstReportFaxedTS());
		columnValues.add(mmtimeStampData.getFirstReportResultsPhonedTS());
		columnValues.add(mmtimeStampData.getCritFind());
		
		return columnValues.toArray(new Object[0]);		
	}

	private static java.sql.Timestamp convertToSmallDatetimeFormat(String originalDate) {
		
		  
		 
		/*
		 * DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		 * .parseCaseInsensitive() .appendPattern("MM/dd/yyyy hh:mm:ss a")
		 * .toFormatter(Locale.US); LocalDateTime localDate =
		 * LocalDateTime.parse(originalDate,formatter);
		 * System.out.println("****"+localDate); System.out.println("Converted Value: "+
		 * Timestamp.valueOf(localDate));
		 * 
		 * return Timestamp.valueOf(localDate);
		 */
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US);
		try {
			java.util.Date date = formatter.parse(originalDate);
			return new java.sql.Timestamp(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
		
	private String getInsertTimeStampQuery() {

		String insertQuery =
				new String("INSERT INTO mmapi_timestamp_data "
			 + "(pkExam,"
			 + "fkPatient,"
			 + "fkTechnician, "
			 + "fkReviewEntityID, "
			 + "fkTranscriberEntityID,"
			 + "fkReferringPhysician,"
			 + "modality,"
			 + "examRequested,"
			 + " reasonOrdered," + 
				"scheduleServiceDate,"
				+ "servicePriority,"
				+ " procedureType,"
				+ " numPatientSeen,"
				+ " orderPersonName,"
				+ " orderTS,"
				+ " orderAssignedTS,"
				+ "orderCompleteTS, " + 
				"resultsPhonedToNumber,"
				+ " techConfirmed,"
				+ " jobProgress,"
				+ " examComments,"
				+ " moduleMask,"
				+ "cptCountProvider, "
				+ "cptCountRadiologist, "
				+ "techComments," + 
				" orderTakenBy,"
				+ " orderedOnLine,"
				+ "studyDueTS,"
				+ "studyUID,"
				+ "origOrder,"
				+ "fkFacility, "
				+ "refBatchNum,"
				+ " refSignedFlag, "
				+ "refTimes,autoFaxNumber, " + 
				"practitioner,"
				+ "scheduleServiceTime,"
				+ "imageViews,imageRetakes,"
				+ " retakeReason,"
				+ "emrOrderid,"
				+ "medNecessity,"
				+ "emrSanityError,"
				+ "techArriveTS," + 
				"techDispatchedBy,"
				+ "priorAuthorization,"
				+ "ETX, "
				+ "patientFirstName, "
				+ "patientDob,"
				+ " patientRoomNum,"
				+ "studyID, "
				+ "accessionNumber,"
				+ "facilityName," + 
				" techLast,"
				+ "  techFirst,"
				+ " regionName, "
				+ "corpName, "
				+ "referLast, "
				+ "referFirst,"
				+ "referSuffix,"
				+ "referPhysicianID,"
				+ "referTaxonomyCode,"
				+ " firstReportDocID," + 
				" firstImageUploadTS,"
				+ " firstReportTranscribeTS,"
				+ " firstReportReviewedByDoctor,"
				+ " firstReportFaxedTS, "
				+ "firstReportResultsPhonedTS,"
				+ " critFind)" + 
				"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				
		
		return insertQuery;				
	}
	
	private JSONTimeStampResponseData convertJSONIntoJavaObject(UIResponse response) {
	
		
		Object data = response.getData();
		
		ObjectMapper ob_Objectmapper = new ObjectMapper();
		
		JSONTimeStampResponseData jsonResponseData = new JSONTimeStampResponseData();
		try {
			jsonResponseData = ob_Objectmapper.readValue(data.toString(),JSONTimeStampResponseData.class);
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
	
	//FIXME: Use generics instead of this bad code.
	private JSONFacilityResponseData convertJSONIntoJavaObjectForFacility(UIResponse response) {
	
		
		Object data = response.getData();
		
		ObjectMapper ob_Objectmapper = new ObjectMapper();
		
		JSONFacilityResponseData jsonResponseData = new JSONFacilityResponseData();
		try {
			jsonResponseData = ob_Objectmapper.readValue(data.toString(),JSONFacilityResponseData.class);
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
