/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Parser;

/**
 *
 * @author dimas
 */
public class API {

    private final String pathBin = "src\\bin\\";
    private final String pathAPI = "src\\api\\";

    private String key;

    public String recog(Path pathWav) {
        String result = null;
        generateKey();
        try {
            Files.copy(pathWav, Paths.get(pathAPI, "wav", key + ".wav"), REPLACE_EXISTING);
            write(Paths.get(pathAPI, "scp", "wav2scp_" + key + ".scp"),
                    Paths.get(pathAPI, "wav", key + ".wav") + "    "
                    + Paths.get(pathAPI, "mfc", key + ".mfc") + "\n");
            write(Paths.get(pathAPI, "scp", "mfc_" + key + ".scp"),
                    Paths.get(pathAPI, "mfc", key + ".mfc") + "\n");
            call(new ProcessBuilder(Paths.get(pathBin, "HCopy").toString(),
                    "-A", "-D", "-T", "1",
                    "-C", Paths.get(pathAPI, "model", "config").toString(),
                    "-S", Paths.get(pathAPI, "scp", "wav2scp_" + key + ".scp").toString())
                    .start());
            call(new ProcessBuilder(Paths.get(pathBin, "HVite").toString(),
                    "-H", Paths.get(pathAPI, "model", "hmm15", "macros").toString(),
                    "-H", Paths.get(pathAPI, "model", "hmm15", "hmmdefs").toString(),
                    "-S", Paths.get(pathAPI, "scp", "mfc_" + key + ".scp").toString(),
                    "-l", "*",
                    "-i", Paths.get(pathAPI, "mlf", key + ".mlf").toString(),
                    "-w", Paths.get(pathAPI, "model", "wdnet").toString(),
                    "-p", "0.0", "-s", "5.0",
                    Paths.get(pathAPI, "model", "dict").toString(),
                    Paths.get(pathAPI, "model", "tiedlist").toString())
                    .start());
            result = Parser.getFileContent(Paths.get(pathAPI, "mlf", key + ".mlf").toString());
        } catch (IOException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private void generateKey() {
        String epoch = System.currentTimeMillis() + "";
        Map<String, String> map = new HashMap<>();
        map.put("0", "A");
        map.put("1", "B");
        map.put("2", "C");
        map.put("3", "D");
        map.put("4", "E");
        map.put("5", "F");
        map.put("6", "G");
        map.put("7", "H");
        map.put("8", "I");
        map.put("9", "J");
        key = "";
        for (int i = 0; i < epoch.length(); i++) {
            key += map.get(epoch.charAt(i) + "");
        }
    }

    private void write(Path path, String content) {
        try (PrintWriter writer = new PrintWriter(path.toString(), "UTF-8")) {
            writer.println(content);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void call(Process process) {
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        try {
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
