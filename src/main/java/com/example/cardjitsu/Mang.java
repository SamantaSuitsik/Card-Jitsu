package com.example.cardjitsu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class Mang extends Application {

    private String mangijanimi;
    private String salvestus;
    private int kaiguluger;

    private final Image background = new Image("taust5.png");

    private Kasi mangija;
    private Vastane vastane;
    private boolean mangkaib;

    private final Canvas canvas;
    private String mangijakestev = "";
    private String vastasekestev = "";
    private Kaart vastasekaart;
    private Kaart mangijakaart;
     private HashMap<String, Integer> mangijadict = new HashMap<>();
    private HashMap<String, Integer> vastanedict = new HashMap<>();

    public Mang(){
        this.canvas = new Canvas(800, 600);
        canvas.setOnMouseClicked(event -> handleClick(event.getX(), event.getY()));
        canvas.getGraphicsContext2D().setFill(Color.WHITE);

        nimeAken();
        alustaUusMang();

    }

    public void alustaUusMang(){
        this.setMangkaib(true);
        kaiguluger = 0;
        salvestus = "salvestus-"+ System.currentTimeMillis()+".txt";
        mangijakestev = "";
        vastasekestev = "";
        mangijakaart = null;
        vastasekaart = null;
        mangija = new Kasi();
        vastane = new Vastane();
        this.mangija.suvalisedKaardidKaes();
        this.vastane.suvalisedKaardidKaes();
        setdictalgne();
        joonistaEkraan();

    }

    public void setdictalgne(){
        this.mangijadict.put("tuli", 0);
        this.mangijadict.put("vesi", 0);
        this.mangijadict.put("lumi", 0);
        this.vastanedict.put("tuli", 0);
        this.vastanedict.put("vesi", 0);
        this.vastanedict.put("lumi", 0);
    }


    private Pane getMainPane() {
        Pane pane = new AnchorPane();
        Button button = new Button("Undo");

        button.setOnMouseClicked(event -> {
            laadija();
            //System.out.println("uhke");
            joonistaEkraan();
        });

        pane.getChildren().addAll(canvas, button);
        return pane;
    }


    public void joonistaEkraan(){
        canvas.getGraphicsContext2D().clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        canvas.getGraphicsContext2D().drawImage(background, 0, 0, canvas.getWidth(), canvas.getHeight());
        joonistaMangijaKaardid();
        joonistaEriEfektid();
        joonistaVoiduKogused();
        canvas.getGraphicsContext2D().setFont(Font.font("Impact",40));
        canvas.getGraphicsContext2D().fillText(mangijanimi, canvas.getWidth()*4/5,40 );
        canvas.getGraphicsContext2D().fillText(String.valueOf(this.kaiguluger), canvas.getWidth()-30, canvas.getHeight()-40);
        canvas.getGraphicsContext2D().setFont(Font.font("Impact",15));
        if (this.mangijakaart != null)
            joonistaKaart(this.mangijakaart, canvas.getWidth()/2+canvas.getWidth()/20, canvas.getHeight()/3);
        if (this.vastasekaart != null)
            joonistaKaart(this.vastasekaart, canvas.getWidth()/2-canvas.getWidth()/8, canvas.getHeight()/3);
    }

    public void joonistaEkraan(String teade){
        joonistaEkraan();
        teadeMangijale(teade);
    }

    public void handleClick(double x, double y) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //gc.setFill(Color.INDIGO);
        double yalgus = canvas.getHeight() * 3 / 4;
        int laius = (int)(canvas.getWidth()/10);
        double mangitud = ((8 * x + laius * 4) / canvas.getWidth()) - 2;
        if (y > yalgus && y < yalgus + canvas.getHeight() / 6 && this.mangkaib
            && x > canvas.getWidth()/4 - laius/2 && x < canvas.getWidth()/8 *6 + laius/2) {
            if (mangitud - (int) mangitud <= 0.8) {
                if (this.mangija.getKaardid()[(int)mangitud].getElement().equals(vastasekestev)){
                    gc.fillText("See element on blokeeritud ", 300, 100);
                } else {
                    kaiguluger++;
                    reageeri((int) mangitud);
                }
            }
        }
    }
    

    public void reageeri(int indeks){
        salvestaja();
        this.mangijakaart = this.mangija.mangiKaart(indeks);

        //Kui eriline võime oli kaardi tugevust muuta teeb selle siin ära
        if (this.mangijakestev.equals("+2"))
            this.mangijakaart.setTugevus(this.mangijakaart.getTugevus()+2);
        if (this.mangijakestev.equals("-2"))
            this.mangijakaart.setTugevus(this.mangijakaart.getTugevus()-2);


        //Prindib vastase käidud kaardi välja
        this.vastasekaart = this.vastane.mangiKaart(this.mangijakestev);


        //Kui eriline võime oli kaardi tugevust muuta teeb selle siin ära
        if (vastasekestev.equals("+2"))
            this.vastasekaart.setTugevus(this.vastasekaart.getTugevus()+2);
        if (vastasekestev.equals("-2"))
            this.vastasekaart.setTugevus(this.vastasekaart.getTugevus()-2);


        //Muudab meeleshoitud erilised efektid tagasi tühjaks
        this.mangijakestev = "";
        this.vastasekestev = "";

        //teeb erilisekaardi muutused ära
        if (this.mangijakaart.getEriline() != null) {
            Object[] muutused = teeEriline(this.mangijakaart, this.vastasekaart, this.mangija, this.vastane);
            this.mangijakaart = (Kaart) muutused[0];
            this.vastasekaart = (Kaart) muutused[1];
            this.mangija = (Kasi) muutused[2];
            this.vastane = (Vastane) muutused[3];
            this.mangijakestev = (String) muutused[4];
        }
        if (this.vastasekaart.getEriline() != null){
            Object[] muutused = teeEriline(this.vastasekaart, this.mangijakaart, this.vastane, this.mangija);
            this.vastasekaart = (Kaart)muutused[0];
            this.mangijakaart = (Kaart)muutused[1];
            this.vastane = (Vastane) muutused[2];
            this.mangija = (Kasi)muutused[3];
            this.vastasekestev = (String)muutused[4];
        }

        //Kontrollib kumb kaart võidab
        int tulemus = this.mangijakaart.compareTo(this.vastasekaart);
        if (tulemus == 0){
            if (this.mangkaib)
                joonistaEkraan("Jäite see round viiki");
        }
        else if (tulemus > 0) {
            this.mangijadict.put(this.mangijakaart.getElement(), this.mangijadict.get(this.mangijakaart.getElement())+1);
            if (this.mangkaib)
                joonistaEkraan("Selle roundi võitsid");
        }else {
            this.vastanedict.put(this.vastasekaart.getElement(), this.vastanedict.get(this.vastasekaart.getElement()) + 1);
            if (this.mangkaib)
                joonistaEkraan("Selle roundi kaotasid");
        }

        //if (this.mangkaib)
        //    joonistaEkraan();

        //Vastasel ei leidu kaarti mis ei ole blokeeritud
        if (eiLeiduBlokeerimataElement(this.mangijakestev, this.vastane)){
            tegeleVoit("Sinu võit! Vastasel ei ole kaarti, mida saaks käia");
            this.mangkaib = false;
        }
        //Mängijal ei leidu kaarti mis ei ole blokeeritud
        if (eiLeiduBlokeerimataElement(this.vastasekestev, this.mangija)){
            tegeleVoit("Arvuti võitis! Sul ei ole kaarti, mida saaksid käia");
            this.mangkaib = false;
        }


        //Kontroll, kas on võidetud kaartidega kombinatisoon
        if (kasVoidetud(this.mangijadict)) {
            tegeleVoit("Sinu võit!");
            this.mangkaib = false;
        }
        if (kasVoidetud(this.vastanedict)) {
            tegeleVoit("Arvuti võitis!");
            this.mangkaib = false;
        }



    }

    public void teadeMangijale(String teade){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(Font.font("Impact",25));
        gc.fillText(teade, canvas.getWidth()/3, canvas.getHeight()/4);
        gc.setStroke(Color.BLACK);
        gc.strokeText(teade, canvas.getWidth()/3, canvas.getHeight()/4);
        gc.setFont(Font.font("Impact", 15));
    }

    public void tegeleVoit(String tekst) {
        joonistaEkraan(tekst);
        restartAken();
    }

    public void restartAken(){
        Button uuesti = new Button("Uuesti?");
        Button exit = new Button("Exit");
        HBox box = new HBox(20);
        box.getChildren().addAll(uuesti, exit);
        Stage restart = new Stage();
        Scene res = new Scene(box, 150, 60);
        restart.setScene(res);
        restart.setAlwaysOnTop(true);
        restart.show();

        uuesti.setOnMouseClicked(event -> {
            alustaUusMang();
            restart.hide();
        });
        exit.setOnMouseClicked(event -> Platform.exit());
    }

    public void joonistaEriEfektid(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(Font.font("Impact", 15));
        String[] teksid = efektidValja();
        gc.fillText("Sinule : " + teksid[0], canvas.getWidth()/2-canvas.getWidth()/10, 20);
        gc.fillText("Vastasele : " + teksid[1], canvas.getWidth()/2-canvas.getWidth()/10, 40);
    }

    public void joonistaMangijaKaardid(){
        int laius = (int)(canvas.getWidth()/10);
        for (int i = 2; i <= 6; i++) {
            joonistaKaart(mangija.getKaardid()[i-2],canvas.getWidth()/8 *i - laius/2, canvas.getHeight()*3/4);
        }
    }

    public void joonistaVoiduKogused(){
        String tuleemoji = "\uD83D\uDD25";
        String veeemoji = "\uD83D\uDCA7";
        String lumeemoji = "❄";

        GraphicsContext gc = canvas.getGraphicsContext2D();

        //gc.setFill(Color.INDIGO);

        String tekst1 = "   " + tuleemoji + " : " + this.mangijadict.get("tuli") + "  "+
                veeemoji + " : " + this.mangijadict.get("vesi") + "  " + lumeemoji + " : " + this.mangijadict.get("lumi");

        String tekst2 = "   " + tuleemoji + " : " + this.vastanedict.get("tuli") + "  "+
                veeemoji + " : " + this.vastanedict.get("vesi") + "  " + lumeemoji + " : " + this.vastanedict.get("lumi");

        gc.fillText("Sinule : ", canvas.getWidth()-canvas.getWidth()/5, canvas.getHeight()/7);
        gc.fillText(tekst1, canvas.getWidth()-canvas.getWidth()/5, canvas.getHeight()/7+20);
        gc.fillText("Vastasele : ", canvas.getWidth()/40, canvas.getHeight()/7);
        gc.fillText(tekst2, canvas.getWidth()/40, canvas.getHeight()/7+20);
    }

    public void joonistaKaart(Kaart kaart, double x, double y){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image pilt = new Image(kaart.getElement() + ".png");
        gc.drawImage(pilt, x, y, canvas.getWidth()/10, canvas.getHeight()/6);
        gc.fillText(Integer.toString(kaart.getTugevus()), x+canvas.getWidth()/80, y+canvas.getHeight()/34);
        gc.fillText(kaart.erilineValja(), x+canvas.getWidth()/25, y+canvas.getHeight()/26);

    }

    @Override
    public void start(Stage peaLava) {
        Scene s = new Scene(getMainPane());

        s.widthProperty().addListener(event -> {
            canvas.setWidth(s.getWidth());
            joonistaEkraan();
        });
        s.heightProperty().addListener(event -> {
            canvas.setHeight(s.getHeight());
            joonistaEkraan();
        });


        peaLava.setTitle("Card-Jitsu");
        peaLava.setMinWidth(400.0);
        peaLava.setScene(s);
        peaLava.show();

    }

    public void setMangkaib(boolean mangkaib) {
        this.mangkaib = mangkaib;
    }

    public static void main(String[] args) {
        launch(args);}


    public void nimeAken(){
        FlowPane fp = new FlowPane();
        Stage nimeks = new Stage();
        Scene nimetseen = new Scene(fp, 200, 200);
        TextField txt = new TextField();
        Button valmis = new Button("Sisesta");
        fp.getChildren().addAll(txt, valmis);
        nimeks.setScene(nimetseen);
        nimeks.setAlwaysOnTop(true);
        nimeks.show();

        valmis.setOnMouseClicked(event -> {
            this.mangijanimi = txt.getText();
            if ("".equals(this.mangijanimi))
                this.mangijanimi = "Mangija";
            joonistaEkraan();
            nimeks.hide();
        });
    }


    //Kontrollib võitu
    public static boolean kasVoidetud(HashMap asi){
        int tuli = (int)asi.get("tuli");
        int vesi = (int)asi.get("vesi");
        int lumi = (int)asi.get("lumi");
        //kas on igat elementi 1 või siis ühte elementi 3
        return (tuli > 0 && vesi > 0 && lumi > 0) || tuli > 2 || vesi > 2 || lumi > 2;
    }

    //Võtab mängitud kaardidi ja hetkel käesolevad kaardid argumentideks
    //Muudab neid vastavalt erivõimele
    public static Object[] teeEriline(Kaart mojuvkaart, Kaart vastasekaart, Kasi mojuvkasi, Kasi vastasekasi){
        String edasine = "";
        switch (mojuvkaart.getEriline()){

            //Salvestab +2 või -2 mällu, et järgmine käik rakendada
            case "+2" -> edasine = "+2";
            case "-2" -> edasine = "-2";
            //Vahetab kaartidel tugevused salaja ära, et efektiivselt kehtiks nõrgem kaart võidab
            case "vahetus" -> {
                int hetk = mojuvkaart.getTugevus();
                mojuvkaart.setTugevus(vastasekaart.getTugevus());
                vastasekaart.setTugevus(hetk);
            }
            //Eemaldab käest kõik elemendid
            case "eemalda tuli", "eemalda vesi", "eemalda lumi" -> eemaldaElement(mojuvkaart, vastasekasi);
            //Muudab kaardi elemendid vajadusel
            case "muuda tuli" -> {
                if (vastasekaart.getElement().equals("tuli"))
                    vastasekaart.setElement("lumi");
            }
            case "muuda vesi" -> {
                if (vastasekaart.getElement().equals("vesi"))
                    vastasekaart.setElement("tuli");
            }
            case "muuda lumi" -> {
                if (vastasekaart.getElement().equals("lumi"))
                    vastasekaart.setElement("vesi");
            }
            //Salvestab blokeeringu mällu
            case "blokeeri tuli", "blokeeri vesi", "blokeeri lumi" -> //Switch statement tehakse juppideks, et saaks tagumist osa otse elemendiga võrrelda
                    edasine = mojuvkaart.getEriline().split(" ")[1];
            default -> {}
        }

        //Tagastab Objektide massiivi, et saaks mängu sees need rakendada
        return new Object[]{mojuvkaart, vastasekaart, mojuvkasi, vastasekasi, edasine};
    }


    //Väljastab efektid, mis hetkel toimel on
    public String[] efektidValja(){
        String blokemoji = "\uD83D\uDEAB";
        String tuleemoji = "\uD83D\uDD25";
        String veeemoji = "\uD83D\uDCA7";
        String lumeemoji = "❄";
        String kitsas = "\u2009";
        String teinekitsas = "\u202F";

        String sinule = "";
        String vastasele = "";

        switch (this.mangijakestev){
            case "+2" -> sinule = sinule + "  +2";
            case "-2" -> sinule = sinule + "  -2";
            case "tuli" -> vastasele = vastasele + "  " + teinekitsas+blokemoji + tuleemoji + kitsas;
            case "vesi" -> vastasele = vastasele + "  " + teinekitsas+kitsas+blokemoji + veeemoji + kitsas;
            case "lumi" -> vastasele = vastasele + "  " + teinekitsas+kitsas+blokemoji + lumeemoji + kitsas;
            default -> vastasele = vastasele + " ❌  ";
        }
        switch (this.vastasekestev){
            case "+2" -> vastasele = vastasele + "  +2";
            case "-2" -> vastasele = vastasele + "  -2";
            case "tuli" -> sinule = sinule + "  " + teinekitsas+blokemoji + tuleemoji + kitsas;
            case "vesi" -> sinule = sinule + "  " + teinekitsas+kitsas+blokemoji + veeemoji + kitsas;
            case "lumi" -> sinule = sinule + "  " + teinekitsas+kitsas+blokemoji + lumeemoji + kitsas;
            default -> sinule = sinule + " ❌  ";
        }

        return new String[]{sinule, vastasele};
    }

    public static void eemaldaElement(Kaart kaart, Kasi kasi){

        //Vaatab käe läbi ja eemaldab vastava elemendiga kaardid kuni neid enam ei ole.
        String element = kaart.getEriline().split(" ")[1];
        boolean tehtud = false;
        while (!tehtud) {
            tehtud = true;
            //Vaatab tagurpidi läbi et midagi vahele ei jääks
            for (int i = 4; i >= 0; i--) {
                if (kasi.getKaardid()[i].getElement().equals(element)) {
                    kasi.mangiKaart(i);
                    tehtud = false;
                }
            }
        }
    }


    //Kontrollib kas käes on kaart mida saaks käia
    public static boolean eiLeiduBlokeerimataElement(String element, Kasi kasi){
        for (Kaart kaart : kasi.getKaardid()) {
            if (!kaart.getElement().equals(element))
                return false;
        }
        return true;
    }

    public void salvestaja(){
        ArrayList<String> vanadread = new ArrayList<>();
        if (Files.exists(Path.of(salvestus))){
            try (BufferedReader br = new BufferedReader(new FileReader(salvestus))){
                for (int i = 0; i < this.kaiguluger-1; i++) {
                    String rida = br.readLine();
                    if (rida == null)
                        break;
                    vanadread.add(rida);
                }
            } catch (IOException e) {
                System.out.println("katki aga siin");
                //throw new RuntimeException(e);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(salvestus))){
            for (String s : vanadread) {
                bw.write(s);
                bw.newLine();
            }
            StringBuilder kirjutatav = new StringBuilder(kaiguluger + ":" + mangijanimi + ",");
            Kaart[] kaardid = this.mangija.getKaardid();
            for (Kaart kaart : kaardid) {
                kirjutatav.append(kaart.getTugevus()).append(";").append(kaart.getElement()).append(";").append(kaart.getEriline()).append("-");
            }
            if (this.mangijakaart == null)
                kirjutatav.append(99).append(";").append("tuli").append(";").append((String) null);
            else
                kirjutatav.append(mangijakaart.getTugevus()).append(";").append(mangijakaart.getElement()).append(";").append(mangijakaart.getEriline());
            kirjutatav.append(":").append(mangijakestev).append(":").append(mangijadict.get("tuli")).append(";").append(mangijadict.get("vesi")).append(";").append(mangijadict.get("lumi")).append(",");
            Kaart[] vastasekaardid = this.mangija.getKaardid();
            for (Kaart kaart : vastasekaardid) {
                kirjutatav.append(kaart.getTugevus()).append(";").append(kaart.getElement()).append(";").append(kaart.getEriline()).append("-");
            }
            if (this.vastasekaart == null)
                kirjutatav.append(99).append(";").append("tuli").append(";").append((String) null);
            else
                kirjutatav.append(vastasekaart.getTugevus()).append(";").append(vastasekaart.getElement()).append(";").append(vastasekaart.getEriline());
            kirjutatav.append(":").append(vastasekestev).append(":").append(vastanedict.get("tuli")).append(";").append(vastanedict.get("vesi")).append(";").append(vastanedict.get("lumi"));

            bw.write(kirjutatav.toString());
            bw.newLine();

        } catch (IOException e) {
            System.out.println("katki");
            //throw new RuntimeException(e);
        }

    }

    public void laadija(){
        try (BufferedReader br = new BufferedReader(new FileReader(salvestus))){
            for (int i = 0; i < this.kaiguluger; i++) {
                String rida = br.readLine();
                rida = rida.split("\n")[0];
                String[] info = rida.split(",");
                //System.out.println(info[0].split(":")[0]);
                //System.out.println(this.kaiguluger);
                if (info[0].split(":")[0].equals(Integer.toString(this.kaiguluger))){
                    //System.out.println(this.kaiguluger + " tuli siia");
                    this.mangijanimi = info[0].split(":")[1];
                    String[] mangija = info[1].split(":");
                    this.mangijakestev = mangija[1];
                    String[] dict = mangija[2].split(";");
                    String[] kaardid = mangija[0].split("-");
                    Kaart[] osad = kaardidListist(kaardid);
                    this.mangija.setKaardid(new Kaart[]{osad[0], osad[1], osad[2], osad[3], osad[4]});
                    if (osad[5].getTugevus() == 99)
                        this.mangijakaart = null;
                    else
                        this.mangijakaart = osad[5];
                    this.mangijadict = dictListist(dict);

                    String[] vastaseinfo = info[2].split(":");
                    this.vastasekestev = vastaseinfo[1];
                    String[] vastasdict = vastaseinfo[2].split(";");
                    String[] vastasekaardid = vastaseinfo[0].split("-");
                    Kaart[] vastaseosad = kaardidListist(vastasekaardid);
                    this.vastane.setKaardid(new Kaart[]{vastaseosad[0], vastaseosad[1], vastaseosad[2], vastaseosad[3], vastaseosad[4]});
                    if (vastaseosad[5].getTugevus() == 99)
                        this.vastasekaart = null;
                    else
                        this.vastasekaart = vastaseosad[5];
                    this.vastanedict = dictListist(vastasdict);
                    this.kaiguluger--;
                }
            }
        } catch (IOException e) {
            System.out.println("katki");
            throw new RuntimeException(e);
        }
    }

    public Kaart[] kaardidListist(String[] kaardid){
        Kaart[] kaardidhoidja = new Kaart[6];
        for (int j = 0; j < 6; j++) {
            String[] kaardinf = kaardid[j].split(";");
            Kaart kaarttemp = new Kaart(Integer.parseInt(kaardinf[0]), kaardinf[1]);
            if (kaardinf.length == 3) {
                kaarttemp.setEriline(kaardinf[2]);
            }
            kaardidhoidja[j] = kaarttemp;
        }

        return kaardidhoidja;
    }

    public HashMap<String, Integer> dictListist(String[] antuddict){
        HashMap<String, Integer> dict = new HashMap<>();
        dict.put("tuli", Integer.valueOf(antuddict[0]));
        dict.put("vesi", Integer.valueOf(antuddict[1]));
        dict.put("lumi", Integer.valueOf(antuddict[2]));
        return dict;
    }
}