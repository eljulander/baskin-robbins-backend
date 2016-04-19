/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmap;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


/**
 *
 * @author gbjohnson
 */
public class MMap {

    /**
     * @param args the command line arguments
     * @throws java.text.ParseException
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
	 // This can easily be stored in an external file probably. 
   public static String[][] Machines
            = {
            {"workstation_a", "LAM213-156907"},
            {"workstation_b", "LAM213-146828"},
            {"workstation_c", "LAM213-145595"},
            {"workstation_d", "LAM213-146902"},
            {"workstation_e", "LAM213-148102"},
            {"workstation_f", "LAM213-148661"},
            {"workstation_g", "LAM213-146979"},
            {"workstation_h", "LAM213-145158"},
            {"workstation_i", "LAM213-148164"},
            {"workstation_j", "LAM213-148693"},
            {"workstation_k", "LAM213-148697"},
            {"workstation_l", "LAM213-145621"},
            {"workstation_m", "LAM213-158150"},
            {"workstation_n", "LAM213-148265"},
            {"workstation_o", "LAM213-156983"},
            {"workstation_p", "LAM213-156916"},
            {"workstation_q", "LAM213-158137"},
            {"workstation_r", "LAM213-154638"},
            {"workstation_s", "LAM213-148679"},
            {"workstation_t", "LAM213-148677"},
            {"workstation_u", "LAM213-148681"},
            {"workstation_v", "LAM213-156428"},
            {"workstation_w", "LAM213-156909"},
            {"workstation_x", "LAM213-148675"},
            {"workstation_y", "LAM215-156971"},
            /*{"workstation_Z", "LAM215-156922"}*/};
   public static boolean inSearch = false;
	public static void start()  throws ParseException, IOException, InterruptedException{
ArrayList<Computer> cpus = new ArrayList<Computer>();
    	
        
        
        System.out.println("Starting Search...");
        if(!inSearch){
        	inSearch  = true;
        for(String[] m: Machines){
        	QueryMachine machine = new QueryMachine();
        	machine.getMachineQuery(m[1]);	
        	//System.out.println(m[1]);
        	boolean found = false;
        	for(String s[]:machine.sessions){
        		
        		if(s[2].equals("console")){
        			System.out.println(s[4] +" "+ m[0]+" "+getActual(s[1])+" "+s[6]);
        			System.out.println(s[5] + " Time Idle");
        			cpus.add(new Computer(m[0],getActual(s[1])));
        			found = true;
        			break;
        		}
        		
        	}
        	if(!found)cpus.add(new Computer(m[0],"Vacant"));
        }

       
//        for (String[] Machine : Machines) {
//            QueryMachine qm = new QueryMachine();
//            qm.getMachineQuery(Machine[1]);
//
//            boolean foundUname;
//            for (int i = 0; i < qm.getNumSessions(); i++) {
//                if ("Active".equals((qm.getSessionArray(i))[4])) {
//                    foundUname = false;
//                    for (String[] User : Users) {
//                        if (User[0].equals((qm.getSessionArray(i))[1])) {
//                            Computer cpu = new Computer();
//                            cpu.currentUser = getActual(User[0]);
//                           cpu.name = Machine[0].toLowerCase();
//                            cpus.add(cpu);
//                       	System.out.println(User[1] + "\t is at " + Machine[0] + " Since " + (qm.getSessionArray(i))[6]);
//                            foundUname = true;
//                        }
//                    }
//                    if (!foundUname) {
//                    	Computer cpu = new Computer();
//                    	cpu.currentUser = "Name Not Found";
//                        cpu.name = Machine[0].toLowerCase();
//                        cpus.add(cpu);
//                        System.out.println((qm.getSessionArray(i))[1] + "\t is at " + Machine[0] + " Since " + (qm.getSessionArray(i))[6]);
//                    }
//                }
//            }
//        }
        
        
        for(Computer c : cpus)
        	saveUserToFirebase(c);
        
		//start();
        
        System.out.println("Finishing Search...");
        inSearch = false;
        }
		
	}
	
	public static void throwError(String Error){
		MMap.status = "Status: ERROR: "+Error;
		Render.h.text = MMap.status;
		Render.h.textColor = Color.RED;
		Render.h.repaint();
	}
	
	public static void preform(){
		if(status.split("Status: ")[1].trim().equals("Running...")){
			
				try {
					Thread tr = new Thread(){
						public void run(){
							try {
								MMap.start();
								System.out.println("Running...");
							} catch (ParseException e) {
								throwError(e.toString());
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								throwError(e.toString());
								e.printStackTrace();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								throwError(e.toString());
							}
							
						}
					};
					tr.start();
				} catch (Exception e) {
					throwError(e.toString());
				}
					
				
			}else{
				System.out.println("FAILED: "+status.split("Status: ")[1].trim());
			}
			Render.log.text = "Hello World";
			System.out.println(Render.log.text);
			Render.log.repaint();
	}
	
	public static void update(){
		Timer t = new Timer();
		TimerTask tm = new TimerTask(){
			public void run(){
				preform();
				update();
			}
				
		};
		
		t.schedule(tm, (5*60*1000));
		
	}
	
	public static String status = "Status: Idle";
    public static void main(String[] args) {
    	Render.renderMenu();
    	preform();
    	update();
    	
    		
    }
    
    public static String getActual(String dir) throws IOException{
		long time  = System.currentTimeMillis();
		//System.out.println(dir);
		String actual = dir;
		
		FileReader fr;
		try {
			fr = new FileReader("\\\\LAM213-146828\\c$\\Users\\undoneeagle\\Desktop\\users.txt");
			BufferedReader br = new BufferedReader(fr);
			String stu;
			while((stu = br.readLine())!= null){
				String dirz = stu.split(",")[0].replace(" ","");
				//System.out.println(dirz +"STUD");
				if(dir.equals(dirz)){
					actual = stu.split(",")[1];
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(actual.equals(dir)){
			actual = getName(dir);
		}
		
		return actual;
	}
    
public static String getName(String name){
	
	ArrayList<Student> studz = new ArrayList<Student>();
	String l2 = name;
	try 
    { 
        Process p=Runtime.getRuntime().exec("cmd /c net user "+l2+" /domain | FIND /I \"Full Name\""); 
        p.waitFor(); 
        BufferedReader reader=new BufferedReader(
            new InputStreamReader(p.getInputStream())
        ); 
        String linez; 
        while((linez = reader.readLine()) != null) 
        { 
          name = linez.split("Name")[1].trim();
          String[] names  = name.split(",");
          name = names[1] + " " + names[0];
          System.out.println(name);
         
        } 

    }
    catch(IOException e1) {} 
    catch(InterruptedException e2) {} 
	return name;
}
	

    
public static void saveUserToFirebase(Computer c){
		
		Firebase ref = new Firebase("https://baskin-robbins.firebaseio.com/computer/"+c.name);
		
		ref.setValue("I'm writing data", new Firebase.CompletionListener() {
		    @Override
		    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
		        if (firebaseError != null) {
		            System.out.println("Data could not be saved. " + firebaseError.getMessage());
		        } else {
		            System.out.println("Data saved successfully.");
		        }
		    }
		});
		
		final CountDownLatch done = new CountDownLatch(1);
		ref.setValue(c.currentUser, new Firebase.CompletionListener() {
		    @Override
		    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
		        System.out.println("done");
		        done.countDown();
		    }
		});
		try {
			done.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		System.out.println("wasup");
	}
    
    
    
}
