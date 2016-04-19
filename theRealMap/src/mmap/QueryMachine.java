/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

/**
 *
 * @author gbjohnson
 */
public class QueryMachine {

    public String[][] sessions;
    private String responseText = "";
    private String server;

    public void getMachineQuery(String Server) throws IOException, InterruptedException, ParseException {
        server = Server;
        try {
            Process p = Runtime.getRuntime().exec("cmd /c query user /server:" + Server);
            p.waitFor();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                responseText += line + "\n";
              
            }
            //System.out.println(responseText);

        } catch (IOException | InterruptedException e1) {
            System.out.println("You done MESSED UP A-A-RON");
        }
        getSessions();
    }

    private void getSessions() throws ParseException {
        QueryParser qp = new QueryParser();
        qp.loadResponse(responseText, server);
        sessions = qp.parseSessions();
    }

    public int getNumSessions() {
        return sessions.length;
    }

    public String[] getSessionArray(int i) {
        return sessions[i];
    }
}
