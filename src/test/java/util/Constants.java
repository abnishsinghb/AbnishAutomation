package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Used for constants declaration
 */

public class Constants {
	
	public static final String COPYRIGHT_TEXT = "Copyright © ITE - Robert Bosch Automotive Steering - 2024  v2.18";
	
	public static Map<String, Object> map = null;
	//Input map
	public static LinkedHashMap<String, Map<String, Object>> masterMap = new LinkedHashMap<>();
	//Output map
	public static LinkedHashMap<String, Map<String, Object>> masterOPMap = new LinkedHashMap<>();
	public static LinkedHashMap<String, LinkedHashMap<String, String>> masterWriteOPMap = new LinkedHashMap<>();
	public static LinkedHashMap<String, Boolean> masterDependenceTestMa = new LinkedHashMap<>();
	public static Map<String, Object> inputMap = new LinkedHashMap<>();
	public static Map<String, Object> outputMap = new LinkedHashMap<>();
	public static LinkedHashMap<String, String> writeOPMap = new LinkedHashMap<>();
	public static final String OBJECTREPOSITORY_FILE_LOCATION = System.getProperty("user.dir") + "\\ObjectRepository.json";
	public static final String INPUT_DATA_FILE_LOCATION = System.getProperty("user.dir") + "\\testData.xlsx";
	public static final String TEST_INPUT_FILE_LOCATION = System.getProperty("user.dir") + "\\test-input\\";
	public static final String PROPERTIES_FILE_LOCATION = System.getProperty("user.dir") + "\\config.properties";
	public static final String OUTPUT_DATA_FILE_LOCATION = System.getProperty("user.dir") + "\\testOutput.xlsx";
	public static final String Dependence_DATA_FILE_LOCATION = System.getProperty("user.dir") + "\\testDependence.xlsx";
	public static final String INPUJSON_FILE_LOCATION = System.getProperty("user.dir") + "\\test-inputJsonFiles\\";
	public static int numberOfVeraInCalculation;	
	public static ArrayList<String> permissions = new ArrayList<>(Arrays.asList("BNG_ManageAdminBNG", "BNG_ManageBNGBeltDataSets","BNG_ManageBNGStiffnessDataSets","Calculations_BNG", "Calculations_RackSim", "Calculations_Vera", "Calculations_TieRodTool","General_ManageCustomers","General_ManageStandardLoadCases","General_ManageStandardReports","KinematicPositions_StandardPositions","Material_ManageMaterialDataSets","Projects_ReopenCalculations","TieRodTool_ManageTieRodToolDataSets","Vera_FootnoteManager","Vera_ManageHobberToolbox","Vera_ManageTolerances","Vera_ManageToleranceSets","Vera_ManageUnihobberToolbox","Vera_ManageVeraCriteria","Vera_ManageVeraCriteriaSets","Vera_ManageVeraToothingDatabase","Vera_UseTheToothingManager","WormGear_ManageWormGearDatasets"));
}

