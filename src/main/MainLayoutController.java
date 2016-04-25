/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author ABC
 */
public class MainLayoutController implements Initializable {

    final Recorder rec;
    private FXMain main;
    Recorder recorder;
    HashMap<String, String> data;

    public MainLayoutController() {
        rec = new Recorder();
    }

    public void setMain(FXMain main) {
        this.main = main;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recorder = new Recorder();
        stop.setDisable(true);
        data = new HashMap<>();
        data.put("KAPANJADWALSEMINAR", "SEMINAR DISELENGGARAKAN TANGGAL 2 MEI 2016");
        data.put("KAPANJADWALWISUDA", "WISUDA DISELENGGARAKAN BULAN JULI");
        data.put("KAPANUJIANALPRO", "UJIAN ALPRO HARI SELASA");
        data.put("KAPANUJIANARSIKOM", "UJIAN ARSIKOM DIBATALKAN");
        data.put("DIMANARUANGKAPRODI", "RUANG KAPRODI ADA DI LANTAI DUA");
        data.put("DIMANARUANGDEKAN", "RUANG DEKAN ADA DI GEDUNG STEI");
        data.put("DIMANALABGAIB", "LAB GAIB ADA DI LANTAI 4");
        data.put("DIMANALABSISTER", "LAB SISTER ADA DI SEBELAH LAB IRK");
        data.put("SIAPADOSENALPRO", "DOSEN ALPRO ADALAH PAK RILA MANDALA");
        data.put("SIAPADOSENARSIKOM", "DOSEN ARSIKOM ADALAH BU AYU");
        data.put("SIAPAKAPRODIINFORMATIKA", "KAPRODI INFORMATIKA ADALAH BU AYU");
        data.put("SIAPAKAPRODIELEKTRO", "KAPRODI ELEKTRO ADALAH PAK JAJANG");
        data.put("APAEMAILKAPRODI", "EMAIL KAPRODI APA ?");
        data.put("APAEMAILDEKAN", "EMAIL DEKAN APA ?");
        data.put("APASYARATSEMINAR", "SYARAT SEMINAR YAITU TELAH MEMILIKI PEMBIMBING");
        data.put("APASYARATWISUDA", "SYARAT WISUDA YAITU TELAH SIDANG");
    }
    @FXML
    public Button start;
    @FXML
    public Button stop;
    @FXML
    public TextArea output;

    Thread thread;

    @FXML
    public void onStart() {
        start.setDisable(true);
        stop.setDisable(false);
        thread = new Thread(() -> {
            recorder.start();
        });
        thread.start();
    }

    @FXML
    public void onStop() throws IOException {
        start.setDisable(false);
        stop.setDisable(true);
        recorder.finish();
        System.out.println(thread.isAlive());
        output();
    }

    @FXML
    public void output() throws IOException {
        API api = new API();
        String result = api.recog(Paths.get("src/test/test.wav"));
        String tanya = "Tanya : " + result + " ?\n";
        String jawab = "Jawab : " + data.get(result.replaceAll("\\s", ""));
        output.setText(tanya + jawab);
    }
}
