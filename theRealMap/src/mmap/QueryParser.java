/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author gbjohnson
 */
public class QueryParser {

    private String lines[];
    private String server;

    public void loadResponse(String response, String Server) {
        lines = response.split("\\r?\\n");
        server = Server;
    }

    public String[][] parseSessions() throws ParseException {
        String users[][] = new String[lines.length - 1][7];

        for (int i = 1; i < lines.length; i++) {
            users[i - 1][0] = server; // Machine Name
            users[i - 1][1] = cleanString(lines[i].substring(1, 23)); // Username
            users[i - 1][2] = cleanString(lines[i].substring(23, 42)); // Session Name
            users[i - 1][3] = cleanString(lines[i].substring(42, 46)); // Id
            users[i - 1][4] = cleanString(lines[i].substring(46, 54)); // State
            users[i - 1][5] = cleanDuration(lines[i].substring(54, 65)); // Idle Time
        try{    users[i - 1][6] = cleanDate(lines[i].substring(65, lines[i].length()));}catch(Exception e){} // Logon Time
        }
        return users;
    }

    private String cleanString(String rawString) {
        return rawString.replaceAll("\\s", "");
    }

    private String cleanDuration(String rawDuration) throws ParseException {
        String duration =rawDuration.replaceAll("\\s", "");
        if ("none".equals(duration)) {
            duration = "0:00";
        }
        duration = duration.replaceAll(":", "h");
        duration = duration.replaceAll("\\+", "d");
        return duration.concat("m");
    }

    private String cleanDate(String rawDate) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy h:mm a");
        Date date = (Date) formatter.parse(rawDate);
        return date.toString();
    }

}
