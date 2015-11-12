/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.hials.trainingapp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author pitmairen
 */
public class Config {
   
    public static void loadConfigFromFile(String configFile) throws IOException{
        sInstance = new Config();
        sInstance.readConfigFromFilename(configFile);
    }
    
    public static void loadConfigFromResource(String resourceName) throws IOException{
        sInstance = new Config();
        sInstance.readConfigFromResource(resourceName);
    }
    
    
    public static String getValue(String key){
        return getInstance().props.getProperty(key);
    }
    
    public static String getValue(String key, String defaultValue){
        return getInstance().props.getProperty(key, defaultValue);
    }
    
    
    private static Config sInstance = null;
    private final Properties props;
    
    private static Config getInstance(){
        if(sInstance == null){
            throw new RuntimeException("The config file must be loaded before "
                    + "reading from the config object.");
        }
        return sInstance;
    }
    
    
    private Config() throws IOException {
        props = new Properties();
    }
    
    private void readConfigFromFilename(String fileName) throws IOException{
	try(InputStream input = new FileInputStream(fileName)){
             props.load(input);
        }
    }
    
    private void readConfigFromResource(String resourceName) throws IOException{
	try(InputStream input = getClass().getResourceAsStream(resourceName)){
             props.load(input);
        }
    }
}
