package com.example.cardjitsu;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Mang extends Application {

    private Kasi mangija;
    private Vastane vastane;
    private boolean mangkaib;

    private Canvas canvas;
    private String mangijakestev = "";
    private String vastasekestev = "";
     private HashMap<String, Integer> mangijadict = new HashMap<>();
    private HashMap<String, Integer> vastanedict = new HashMap<>();

    public Mang(){
        this.canvas = new Canvas(800, 600);
        canvas.setOnMouseClicked(event -> {
            handleClick(event.getX(), event.getY());
        });

        alustaUusMang();
    }

    public void alustaUusMang(){
        this.setMangkaib(true);
        mangija = new Kasi();
        vastane = new Vastane();
        this.mangija.suvalisedKaardidKaes();
        this.vastane.suvalisedKaardidKaes();
        joonistaristkulikud();
        joonistaMangijaKaardid();
        setdictalgne();

    }

    public void setdictalgne(){
        this.mangijadict.put("tuli", 0);
        this.mangijadict.put("vesi", 0);
        this.mangijadict.put("lumi", 0);
        this.vastanedict.put("tuli", 0);
        this.vastanedict.put("vesi", 0);
        this.vastanedict.put("lumi", 0);
    }

    private void joonistaristkulikud() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        int laius = (int)(canvas.getWidth()/10);
        int korgus = (int)(canvas.getHeight()/6);
        for (int i = 2; i <= 6; i++) {
            gc.strokeRect(canvas.getWidth()/8 *i - laius/2, canvas.getHeight()*3/4, laius, korgus);
        }

    }

    private Pane getMainPane() {
        Pane pane = new FlowPane();
        //Pane buttonPane = this.getButtonPane();
        pane.getChildren().addAll(canvas);
        return pane;
    }




    public void handleClick(double x, double y) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double yalgus = canvas.getHeight() * 3 / 4;
        double mangitud = ((8 * x + canvas.getWidth() / 10 * 4) / canvas.getWidth()) - 2;
        if (y > yalgus && y < yalgus + canvas.getHeight() / 6 && this.mangkaib) {
            if (mangitud - (int) mangitud <= 0.8) {
                //TODO peab kontrollima et seda kaarti saaks k'ia
                reageeri((int) mangitud);
                gc.setFill(Color.INDIGO);
                gc.fillRect(x, y, 10, 10);
            }
        }
    }

    public void reageeri(int indeks){
        Kaart mangitud = this.mangija.mangiKaart(indeks);

        //Prindib kõik mängja kaardid välja
        joonistaMangijaKaardid();

        //Prindib kõik võidetud kaartide kogused välja
        joonistaVoiduKogused();

        //Väljastab hetkel kehtivad efektid
        joonistaEriEfektid();

        //Prindib mängija käidud kaardi välja
        joonistaKaart(mangitud, 400, 200);

        //Kui eriline võime oli kaardi tugevust muuta teeb selle siin ära
        if (this.mangijakestev.equals("+2"))
            mangitud.setTugevus(mangitud.getTugevus()+2);
        if (this.mangijakestev.equals("-2"))
            mangitud.setTugevus(mangitud.getTugevus()-2);

        joonistaKaart(mangitud, 400, 200);

        //Prindib vastase käidud kaardi välja
        Kaart vastanekaart = this.vastane.mangiKaart(this.mangijakestev);

        joonistaKaart(vastanekaart, 200, 200);

        //Kui eriline võime oli kaardi tugevust muuta teeb selle siin ära
        if (vastasekestev.equals("+2"))
            vastanekaart.setTugevus(vastanekaart.getTugevus()+2);
        if (vastasekestev.equals("-2"))
            vastanekaart.setTugevus(vastanekaart.getTugevus()-2);

        joonistaKaart(vastanekaart, 200, 200);

        //Muudab meeleshoitud erilised efektid tagasi tühjaks
        this.mangijakestev = "";
        this.vastasekestev = "";

        //teeb erilisekaardi muutused ära
        if (mangitud.getEriline() != null) {
            Object[] muutused = teeEriline(mangitud, vastanekaart, this.mangija, this.vastane);
            mangitud = (Kaart) muutused[0];
            vastanekaart = (Kaart) muutused[1];
            this.mangija = (Kasi) muutused[2];
            this.vastane = (Vastane) muutused[3];
            this.mangijakestev = (String) muutused[4];
        }
        if (vastanekaart.getEriline() != null){
            Object[] muutused = teeEriline(vastanekaart, mangitud, this.vastane, this.mangija);
            vastanekaart = (Kaart)muutused[0];
            mangitud = (Kaart)muutused[1];
            this.vastane = (Vastane) muutused[2];
            this.mangija = (Kasi)muutused[3];
            this.vastasekestev = (String)muutused[4];
        }

        //Prindib kõik mängja kaardid välja
        joonistaMangijaKaardid();

        //Prindib kõik võidetud kaartide kogused välja
        joonistaVoiduKogused();

        //Väljastab hetkel kehtivad efektid
        joonistaEriEfektid();


        //Kontrollib kumb kaart võidab
        int tulemus = mangitud.compareTo(vastanekaart);
        if (tulemus == 0)
            System.out.println("Jäite see round viiki.");
        else if (tulemus > 0) {
            System.out.println("Selle roundi võitsid");
            this.mangijadict.put(mangitud.getElement(), this.mangijadict.get(mangitud.getElement())+1);
        }else {
            System.out.println("Selle roundi kaotasid");
            this.vastanedict.put(vastanekaart.getElement(), this.vastanedict.get(vastanekaart.getElement()) + 1);
        }


        //Vastasel ei leidu kaarti mis ei ole blokeeritud
        if (eiLeiduBlokeerimataElement(this.mangijakestev, this.vastane)){
            System.out.println("Sinu võit! Vastasel ei ole kaarti, mida saaks käia");
            tegeleVoit("Mangija");
            this.mangkaib = false;
        }
        //Mängijal ei leidu kaarti mis ei ole blokeeritud
        if (eiLeiduBlokeerimataElement(this.vastasekestev, this.mangija)){
            System.out.println("Arvuti võitis! Sul ei ole kaarti, mida saaksid käia");
            tegeleVoit("Arvuti ");
            this.mangkaib = false;
        }


        //Kontroll, kas on võidetud kaartidega kombinatisoon
        if (kasVoidetud(this.mangijadict)) {
            System.out.println("Sinu võit! :)");
            tegeleVoit("Mangija");
            this.mangkaib = false;
        }
        if (kasVoidetud(this.vastanedict)) {
            System.out.println("Arvuti võitis! :(");
            tegeleVoit("Arvuti ");
            this.mangkaib = false;
        }
        //Prindib kõik mängja kaardid välja
        joonistaMangijaKaardid();

        //Prindib kõik võidetud kaartide kogused välja
        joonistaVoiduKogused();

        //Väljastab hetkel kehtivad efektid
        joonistaEriEfektid();
    }

    public void tegeleVoit(String tekst){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.INDIGO);
        gc.fillText(tekst + " Voitis, mang labi", 300, 150);

        //TODO uus aken, mis annab v'imaluse uuesti alustada v]i v;ljuda
    }

    public void joonistaEriEfektid(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(580, 190, 200, 100);
        gc.setFill(Color.INDIGO);
        String[] teksid = efektidValja();
        gc.fillText("Sinule : " + teksid[0], 600, 200);
        gc.fillText("Vastasele : " + teksid[1], 600, 230);
    }

    public void joonistaMangijaKaardid(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        int laius = (int)(canvas.getWidth()/10);
        int korgus = (int)(canvas.getHeight()/6);
        for (int i = 2; i <= 6; i++) {
            joonistaKaart(mangija.getKaardid()[i-2],canvas.getWidth()/8 *i - laius/2, canvas.getHeight()*3/4);
        }
    }

    public void joonistaVoiduKogused(){
        String tuleemoji = "\uD83D\uDD25";
        String veeemoji = "\uD83D\uDCA7";
        String lumeemoji = "❄";

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.fillRect(480, 390, 300, 50);
        gc.setFill(Color.INDIGO);


        String tekst1 = "   " + tuleemoji + " : " + this.mangijadict.get("tuli") + "  "+
                veeemoji + " : " + this.mangijadict.get("vesi") + "  " + lumeemoji + " : " + this.mangijadict.get("lumi");

        String tekst2 = "   " + tuleemoji + " : " + this.vastanedict.get("tuli") + "  "+
                veeemoji + " : " + this.vastanedict.get("vesi") + "  " + lumeemoji + " : " + this.vastanedict.get("lumi");

        gc.fillText("Sinule : " + tekst1, 500, 400);
        gc.fillText("Vastasele : " + tekst2, 500, 430);
    }

    public void joonistaKaart(Kaart kaart, double x, double y){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(x-10, y-10, 100, 100);
        gc.setFill(Color.INDIGO);
        gc.strokeRect(x, y, 80, 100);
        gc.fillText(Integer.toString(kaart.getTugevus()), x+10, y+15);
        gc.fillText(kaart.erilineValja(), x+40, y+20);
        gc.fillText(kaart.getElement(), x+10, y+30);

    }

    @Override
    public void start(Stage peaLava) throws IOException {
        Scene s = new Scene(getMainPane());
        // Lisame CSS'iga taustapildi ja määrame, kui suureks see pilt peaks venitatama.
        //s.getRoot().setStyle("-fx-background-image: url('background.png'); -fx-background-size: 800px 600px;");

        peaLava.setTitle("Card-Jitsu");
        peaLava.setMinWidth(400.0);
        peaLava.setScene(s);
        peaLava.show();

    }

    public void setMangkaib(boolean mangkaib) {
        this.mangkaib = mangkaib;
    }

    public static void main(String[] args) throws InterruptedException {
        launch(args);}


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
            case "eemalda tuli", "eemalda vesi", "eemalda lumi" -> vastasekasi = eemaldaElement(mojuvkaart, vastasekasi);
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

    public static Kasi eemaldaElement(Kaart kaart, Kasi kasi){

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
        return kasi;
    }


    //Kontrollib kas käes on kaart mida saaks käia
    public static boolean eiLeiduBlokeerimataElement(String element, Kasi kasi){
        for (Kaart kaart : kasi.getKaardid()) {
            if (!kaart.getElement().equals(element))
                return false;
        }
        return true;
    }
}