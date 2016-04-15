package pt.upa.transporter.ws.it;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;

/**
 *  Integration Test example
 *  
 *  Invoked by Maven in the "verify" life-cycle phase
 *  Should invoke "live" remote servers 
 */
public class AbstractIT {

    // static members
	protected static TransporterClient localPort1;
	protected static TransporterClient localPort2;
	
    // one-time initialization and clean-up

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {	
    	UDDINaming uddiNaming = new UDDINaming("http://localhost:9090");
    	Collection<String> endpoints = uddiNaming.list("UpaTransporter%");
    	
    	int flag=0;
    	
    	for(String i : endpoints) {
    		if(flag==0){
    			localPort2= new TransporterClient(i);
    			flag=1;
    		}
    		else{
    			localPort1= new TransporterClient(i);
    		}
		}
    }
    
    @AfterClass
    public static void oneTimeTearDown() {
    	localPort1=null;
    	localPort2=null;
    }
    
    @After
    public void tearDown(){
    	
    	localPort1.clearJobs();
    	localPort2.clearJobs();
    }

}