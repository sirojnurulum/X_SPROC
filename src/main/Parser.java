/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ABC
 */
public class Parser {

    static String result;

    public static String openFile(String pathFile) throws IOException {
        File file = new File(pathFile);
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        }
    }

    public static List<String> match(String pathFile) throws IOException {
        List<String> res = new ArrayList<>();
        String t = openFile(pathFile);
        Pattern pat = Pattern.compile("\\s+[A-Z]+\\s+");
        Matcher match = pat.matcher(t);
        System.out.println(t);
        while (match.find()) {
            String f = match.group();
            res.add(f.replaceAll("\\s+", ""));
        }
        return res;
    }

    public static String getFileContent(String pathFile) throws IOException {
        List<String> res = new ArrayList<>();
        res.addAll(match(pathFile));
        String returnVal = "";
        for (String re : res) {
            System.out.println(re);
            returnVal += re + " ";
        }
        return returnVal;
    }
}
