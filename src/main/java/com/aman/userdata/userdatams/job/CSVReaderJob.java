package com.aman.userdata.userdatams.job;

import com.aman.userdata.userdatams.service.UserDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class CSVReaderJob {
    private static final Logger log = LogManager.getLogger(CSVReaderJob.class);

    @Autowired
    private UserDataService userDataService;

    private final String USER_DATA_FOLDER_PATH = "UserDataFolder//";
    //private final int JOB_EXECUTION_INTERVAL=3600000;
    private final int JOB_EXECUTION_INTERVAL=10000;

    @Scheduled(fixedRate = JOB_EXECUTION_INTERVAL)
    public void processCSVFile() {
        log.info("Received a request to process the data from CSV File");
        checkPrerequisites();
        processDataFromFiles();
    }
    private void checkPrerequisites(){
        File directory = new File(USER_DATA_FOLDER_PATH);
        if (! directory.exists()){
            log.info("Creating the user data folder to keep the file to be processed.");
            directory.mkdir();
        }
        File directorySuccess = new File(USER_DATA_FOLDER_PATH + "success");
        if (! directorySuccess.exists()){
            log.info("Creating the success folder to keep the successfully processed files.");
            directorySuccess.mkdir();
        }
        File directoryFailure = new File(USER_DATA_FOLDER_PATH + "failure");
        if (! directoryFailure.exists()){
            log.info("Creating the failure folder to keep the files which failed to processed.");
            directoryFailure.mkdir();
        }
    }
    private void processDataFromFiles()  {
        String[] pathnames = getListOfFiles();
        for (String dataFilePath : pathnames){
            String fileCompletePath = USER_DATA_FOLDER_PATH + dataFilePath;
            try {
                BufferedReader fileContent = readFileContent(fileCompletePath);
                log.info("Starting to process the content of file: " + dataFilePath);
                userDataService.saveUser(fileContent);
                log.info("Completed the processing the content of file: " + dataFilePath);
                log.info("Starting to move the file: "  + dataFilePath + " to success folder");
                Files.move(Paths.get(fileCompletePath), Paths.get(USER_DATA_FOLDER_PATH + "success//" + dataFilePath));
                log.info("Completed the move of file: "  + dataFilePath + " to success folder");
            } catch (Exception excep){
                log.error("Error while processing: " + fileCompletePath, excep);
                try {
                    log.info("Starting to move the file: "  + dataFilePath + " to failure folder");
                    Files.move(Paths.get(fileCompletePath), Paths.get(USER_DATA_FOLDER_PATH + "failure//" + dataFilePath));
                    log.info("Completed the move of file: "  + dataFilePath + " to failure folder");
                } catch (IOException e) {
                    log.error("Error while processing: " + fileCompletePath, e);
                }
            }
        }
    }
    private String[] getListOfFiles(){
        String[] pathnames;
        try {
            File f = new File(USER_DATA_FOLDER_PATH);
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return name.endsWith(".csv");
                }
            };
            pathnames = f.list(filter);
            if (pathnames != null && pathnames.length > 0){
                log.info("Found " + pathnames.length + " files in the folder. Name of files: " + pathnames);
            }else{
                log.info("No file found in the folder");
            }
        }catch (Exception exception){
            log.error("Error while finding the directory data files on path: " + USER_DATA_FOLDER_PATH, exception);
            throw exception;
        }
        return pathnames;
    }
    private BufferedReader readFileContent(String fileName) throws IOException {
        log.info("Starting to read the content of file: " + fileName);
        Path pathToFile = Paths.get(fileName);
        return Files.newBufferedReader(pathToFile);
    }
}