package com.example.cardjitsu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class Mang extends Application {


    //Kõik muutujad, mis määravad mängu seisu
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
        //Tava meetod, mis javaFX käivitamisel kutsutakse
        this.canvas = new Canvas(800, 600);
        //Mänguaknal click tehes saadetakse selle koordinaadid edasi handleClickile
        canvas.setOnMouseClicked(event -> handleClick(event.getX(), event.getY()));
        canvas.getGraphicsContext2D().setFill(Color.WHITE);

        //alustusmeetodid
        nimeAken();
        alustaUusMang();

    }

    //Meetod, mis initsialiseerib kõik vajalikud muutujad
    public void alustaUusMang(){
        this.setMangkaib(true);
        kaiguluger = 0;
        //Salvestuse fail on määratud hetke ajaga
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

    //Meetod, mis initsialiseerib vajalikud mapid
    public void setdictalgne(){
        this.mangijadict.put("tuli", 0);
        this.mangijadict.put("vesi", 0);
        this.mangijadict.put("lumi", 0);
        this.vastanedict.put("tuli", 0);
        this.vastanedict.put("vesi", 0);
        this.vastanedict.put("lumi", 0);
    }


    //Meetod, mis seab üldise javafx elementide paigutuse
    private Pane getMainPane() {
        Pane pane = new AnchorPane();
        Button button = new Button("Undo");

        button.setOnMouseClicked(event -> {
            laadija();
            joonistaEkraan();
        });

        pane.getChildren().addAll(canvas, button);
        return pane;
    }


    //Meetod, mis uuendab kõik ekraanil oleva
    public void joonistaEkraan(){
        canvas.getGraphicsContext2D().clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        canvas.getGraphicsContext2D().drawImage(background, 0, 0, canvas.getWidth(), canvas.getHeight());
        joonistaMangijaKaardid();
        joonistaEriEfektid();
        joonistaVoiduKogused();
        canvas.getGraphicsContext2D().setFont(Font.font("Impact",40));
        canvas.getGraphicsContext2D().fillText(mangijanimi, canvas.getWidth()*4/5,40 );
        //canvas.getGraphicsContext2D().fillText(String.valueOf(this.kaiguluger), canvas.getWidth()-30, canvas.getHeight()-40);
        canvas.getGraphicsContext2D().setFont(Font.font("Impact",15));
        if (this.mangijakaart != null)
            joonistaKaart(this.mangijakaart, canvas.getWidth()/2+canvas.getWidth()/20, canvas.getHeight()/3);
        if (this.vastasekaart != null)
            joonistaKaart(this.vastasekaart, canvas.getWidth()/2-canvas.getWidth()/8, canvas.getHeight()/3);
    }

    //joonistaEkraan teine variant, mis tagastab mängijale ekraani keskele teate
    public void joonistaEkraan(String teade){
        joonistaEkraan();
        teadeMangijale(teade);
    }


    //Meetod, mis arvutab hiire klõpsu koordinaatide põhjal välja, mis kaardile vajutati ja kas see on sobiv käik
    public void handleClick(double x, double y) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        double yalgus = canvas.getHeight() * 3 / 4;
        int laius = (int)(canvas.getWidth()/10);
        //arvutab mis kaardi peale vajutati
        double mangitud = ((8 * x + laius * 4) / canvas.getWidth()) - 2;
        //kontroll, et click oleks kaardi sees
        if (y > yalgus && y < yalgus + canvas.getHeight() / 6 && this.mangkaib
            && x > canvas.getWidth()/4 - laius/2 && x < canvas.getWidth()/8 *6 + laius/2) {
            if (mangitud - (int) mangitud <= 0.8) {
                //kui klikitud kaardi element on blokeeritud siis teavitatakse mängijat sellest
                if (this.mangija.getKaardid()[(int)mangitud].getElement().equals(vastasekestev)){
                    gc.fillText("See element on blokeeritud ", 300, 100);
                } else {
                    //kui klikitud kaart sobis siis saadetakse valitud kaardi indeks edasi main loopile
                    kaiguluger++;
                    reageeri((int) mangitud);
                }
            }
        }
    }
    
    //Mängu main loop
    public void reageeri(int indeks){
        //käigu alguses salvestatakse "eelmine" käik
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

    //Meetod, mis annab mängijale teate
    public void teadeMangijale(String teade){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(Font.font("Impact",25));
        gc.fillText(teade, canvas.getWidth()/3, canvas.getHeight()/4);
        gc.setStroke(Color.BLACK);
        gc.strokeText(teade, canvas.getWidth()/3, canvas.getHeight()/4);
        gc.setFont(Font.font("Impact", 15));
    }

    //Meetod juhuks kui mäng on võidetud/läbi
    public void tegeleVoit(String tekst) {
        joonistaEkraan(tekst);
        restartAken();
    }

    //Aken, mis näidatakse mängijale, kui mäng on läbi
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

        //Uuesti nupul vajutades alustatakse uus mäng
        uuesti.setOnMouseClicked(event -> {
            alustaUusMang();
            restart.hide();
        });
        exit.setOnMouseClicked(event -> Platform.exit());
    }

    //Joonistab ekraanile hetkel kehtivad eriefektid
    public void joonistaEriEfektid(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(Font.font("Impact", 15));
        String[] teksid = efektidValja();
        gc.fillText("Sinule : " + teksid[0], canvas.getWidth()/2-canvas.getWidth()/10, 20);
        gc.fillText("Vastasele : " + teksid[1], canvas.getWidth()/2-canvas.getWidth()/10, 40);
    }

    //Joonistab ekraanile mängija kaardid
    public void joonistaMangijaKaardid(){
        int laius = (int)(canvas.getWidth()/10);
        for (int i = 2; i <= 6; i++) {
            joonistaKaart(mangija.getKaardid()[i-2],canvas.getWidth()/8 *i - laius/2, canvas.getHeight()*3/4);
        }
    }

    //Joonistab ekraanile mitu korda on iga mängija mis elemendiga võitnud
    public void joonistaVoiduKogused(){
        String tuleemoji = "\uD83D\uDD25";
        String veeemoji = "\uD83D\uDCA7";
        String lumeemoji = "❄";

        GraphicsContext gc = canvas.getGraphicsContext2D();

        String tekst1 = "   " + tuleemoji + " : " + this.mangijadict.get("tuli") + "  "+
                veeemoji + " : " + this.mangijadict.get("vesi") + "  " + lumeemoji + " : " + this.mangijadict.get("lumi");

        String tekst2 = "   " + tuleemoji + " : " + this.vastanedict.get("tuli") + "  "+
                veeemoji + " : " + this.vastanedict.get("vesi") + "  " + lumeemoji + " : " + this.vastanedict.get("lumi");

        gc.fillText("Sinule : ", canvas.getWidth()-canvas.getWidth()/5, canvas.getHeight()/7);
        gc.fillText(tekst1, canvas.getWidth()-canvas.getWidth()/5, canvas.getHeight()/7+20);
        gc.fillText("Vastasele : ", canvas.getWidth()/40, canvas.getHeight()/7);
        gc.fillText(tekst2, canvas.getWidth()/40, canvas.getHeight()/7+20);
    }

    //Meetod, ühe kaardi joonistamiseks kindlale koordinaadile
    public void joonistaKaart(Kaart kaart, double x, double y){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image pilt = new Image(kaart.getElement() + ".png");
        gc.drawImage(pilt, x, y, canvas.getWidth()/10, canvas.getHeight()/6);
        gc.fillText(Integer.toString(kaart.getTugevus()), x+canvas.getWidth()/80, y+canvas.getHeight()/34);
        gc.fillText(kaart.erilineValja(), x+canvas.getWidth()/25, y+canvas.getHeight()/26);

    }


    //Peamine JavaFX meetod, kus seatakse lava ja tseen, koos akna suuruse muutmisega
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

    //Meetod, mängu käimasoleku muutmiseks
    public void setMangkaib(boolean mangkaib) {
        this.mangkaib = mangkaib;
    }


    //Main meetod, mis alustab kogu mängu
    public static void main(String[] args) {
        launch(args);}


    //Aken, kus on algne õpetus ja mängija nimesisestus (sest peab ju olema mingi klaviatuuri kasutus)
    public void nimeAken(){
        FlowPane fp = new FlowPane();
        Stage nimeks = new Stage();
        Scene nimetseen = new Scene(fp, 600, 300);
        Text abijutt = new Text();
        abijutt.setText("""
                    Mängijale on ette antud 5 kaarti, mille hulgast valitakse üks, mis käiakse välja.
                    Vastane (ehk arvuti) käib samal ajal oma kaardi.
                    Ühel kaardil on kindel tugevus (arv 2-st 12-ni) ja element (tuli, vesi või lumi).
                    Välja käidud kaartidest võidab see, mis on elemendi või numbri poolest tugevam:
                    Tuli võidab lume, Lumi võidab vee, Vesi võidab tule.
                    Kui käidud kaartidel on sama element võidab see, kelle kaardil on suurem number. \s
                    Kui number ja element on mõlemal samad, siis on tegemist viigiga.
                    Peale käiku antakse võitjale tema käidud kaart tagasi, mis läheb võidetud kaartide komplekti.
                    Kui käik lõppes viigiga, ei saa kumbki mängija kaarti.
                    Mängija, kes saab esimesena komplektis kokku nii tule, vee kui ka lume elemendiga kaardid, on võitnud.
                    Kaartidel võivad olla ka efektid (üleval paremal), mis mõjutavad võiduvõimalust või järgmist käiku:
                    Kui vastasel on kõik kaardid blokeeritud, kaotab ta mängu automaatselt.
                \s
                \s
                 Sisesta mängija nimi:\s""");
        TextField txt = new TextField();
        Button valmis = new Button("Sisesta");
        fp.getChildren().addAll(abijutt, txt, valmis);
        nimeks.setScene(nimetseen);
        nimeks.setAlwaysOnTop(true);
        nimeks.show();

        //Nupule vajutusel seatakse mängija nimi
        valmis.setOnMouseClicked(event -> {
            this.mangijanimi = txt.getText();
            if ("".equals(this.mangijanimi))
                this.mangijanimi = "Mangija";
            joonistaEkraan();
            nimeks.hide();
        });
        txt.setOnKeyPressed(event ->  {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    this.mangijanimi = txt.getText();
                    if ("".equals(this.mangijanimi))
                        this.mangijanimi = "Mangija";
                    joonistaEkraan();
                    nimeks.hide();
                }

        });

        nimeks.setOnCloseRequest(event -> {
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


    //Meetod, mis salvestab mänguseisundi ja eelnevad käigud faili, sobivas formaadis
    public void salvestaja(){
        ArrayList<String> vanadread = new ArrayList<>();
        //Teeb kindlaks, et ei tekiks ridade kordusi ja undoga ei hakkaks fail branchima
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

        //kirjutab uuele reale mänguseisu
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(salvestus))){
            for (String s : vanadread) {
                bw.write(s);
                bw.newLine();
            }
            StringBuilder kirjutatav = new StringBuilder(kaiguluger + ":" + mangijanimi + ",");
            Kaart[] kaardid = this.mangija.getKaardid();
            //kirjutab kõik käes olevad kaardid
            for (Kaart kaart : kaardid) {
                kirjutatav.append(kaart.getTugevus()).append(";").append(kaart.getElement()).append(";").append(kaart.getEriline()).append("-");
            }

            //kui hetkel pole kaarti mängitud lisatakse faili kaart tugevusega 99, et hiljem see kerge leida oleks
            if (this.mangijakaart == null)
                kirjutatav.append(99).append(";").append("tuli").append(";").append((String) null);
            else
                kirjutatav.append(mangijakaart.getTugevus()).append(";").append(mangijakaart.getElement()).append(";").append(mangijakaart.getEriline());
            kirjutatav.append(":").append(mangijakestev).append(":").append(mangijadict.get("tuli")).append(";").append(mangijadict.get("vesi")).append(";").append(mangijadict.get("lumi")).append(",");

            //kordab vastase jaoks kõike
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


    //Fail, mis loeb vajaliku rea failist tagasi
    public void laadija(){
        try (BufferedReader br = new BufferedReader(new FileReader(salvestus))){
            for (int i = 0; i < this.kaiguluger; i++) {
                String rida = br.readLine();
                rida = rida.split("\n")[0];
                String[] info = rida.split(",");

                //Leiab eelmise käigu
                if (info[0].split(":")[0].equals(Integer.toString(this.kaiguluger))){

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
            joonistaEkraan("Ei saa käiku tagasi võtta");
            throw new RuntimeException(e);
        }
    }

    //Tekitab kaardiinfost Kaardi objektid
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

    //Tekitab dict infost Map objekti
    public HashMap<String, Integer> dictListist(String[] antuddict){
        HashMap<String, Integer> dict = new HashMap<>();
        dict.put("tuli", Integer.valueOf(antuddict[0]));
        dict.put("vesi", Integer.valueOf(antuddict[1]));
        dict.put("lumi", Integer.valueOf(antuddict[2]));
        return dict;
    }
}