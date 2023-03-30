import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int kaik;
        boolean mangLabi = false;
        //Kehtivate efektide meeleshoid
        String mangijakestev = "";
        String vastasekestev = "";

        //Mängija soovi teadmiseks
        Scanner scanner = new Scanner(System.in);

        //Võidukaartide kogus
        HashMap<String, Integer> mangijadict = new HashMap<>();
        mangijadict.put("tuli", 0);
        mangijadict.put("vesi", 0);
        mangijadict.put("lumi", 0);
        HashMap<String, Integer> vastanedict = new HashMap<>();
        vastanedict.put("tuli", 0);
        vastanedict.put("vesi", 0);
        vastanedict.put("lumi", 0);


        //teeme käe, millel on kaardikohad
        Kasi kaes = new Kasi();
        kaes.suvalisedKaardidKaes();
        //Vastasele samamoodi
        Vastane vastane = new Vastane();
        vastane.suvalisedKaardidKaes();


        System.out.println("Kuidas mängida?");
        System.out.println("""
                Mängijale on ette antud 5 kaarti, mille hulgast valitakse üks, mis käiakse välja.\s
                Vastane (ehk arvuti) käib samal ajal oma kaardi.
                Ühel kaardil on kindel tugevus (arv 2-st 12-ni) ja element (tuli, vesi või lumi).\s
                Välja käidud kaartidest võidab see, mis on elemendi või numbri poolest tugevam:\s""");
        System.out.println("Tuli võidab lume,\nLumi võidab vee,\nVesi võidab tule.");
        System.out.println("Kui käidud kaartidel on sama element võidab see, kelle kaardil on suurem number. \nKui number ja element on mõlemal samad, " +
                "siis on tegemist viigiga.");
        System.out.println("""
                Peale käiku antakse võitjale tema käidud kaart tagasi, mis läheb võidetud kaartide komplekti.\s
                Kui käik lõppes viigiga, ei saa kumbki mängija kaarti.
                Mängija, kes saab esimesena komplektis kokku nii tule, vee kui ka lume elemendiga kaardid, on võitnud.""");
        System.out.println();
        System.out.println("""
                Kaartidel võivad olla ka efektid (üleval paremal), mis mõjutavad võiduvõimalust või järgmist käiku:
                +2 - järgmisel käigul on mängija käidud kaardi number kahe võrra suurem.
                -2 - järgmisel käigul on mängija käidud kaardi number kahe võrra väiksem.
                1\uD83D\uDD019 - väiksema numbriga kaart võidab (kui kaardid on sama elemendiga).
                \uD83D\uDCA3\uD83D\uDD25 - kõik selle elemendiga kaardid vahetatakse vastase käes välja.
                \uD83D\uDD25➡\uD83D\uDCA7 - vasakpoolse elemendiga kaardid vahetatakse vastase käes parempoolsete vastu välja.
                \uD83D\uDEAB❄ - järgmisel käigul ei saa vastane ühtegi selle elemendiga kaarti käia.
                Kui vastasel on kõik kaardid blokeeritud, kaotab ta mängu automaatselt.""");
        System.out.println();

        while (!mangLabi) {
            //Prindib kõik mängja kaardid välja
            System.out.println("Siin on sinu kaardid: ");
            kaes.kasiValja();

            //Prindib kõik võidetud kaartide kogused välja
            System.out.print("Sinu võidukaardid    ");
            voiduKogus(mangijadict);
            System.out.print("Vastase võidukaardid ");
            voiduKogus(vastanedict);

            //Väljastab hetkel kehtivad efektid
            efektidValja(mangijakestev, vastasekestev);

            System.out.println("Vali, mitmendat kaarti vasakult soovid käia: ");
            boolean valikkorras = false;
            kaik = 1;
            //Kontrollib, et mängija valiks sobiva numbri
            while (!valikkorras) {
                kaik = scanner.nextInt();
                //valitud kaart on sobiva indeksid
                if (kaik > 0 && kaik < 6) {
                    //Kaart ei ole blokeeritud
                    if (kaes.getKaardid()[kaik - 1].getElement().equals(vastasekestev)) {
                        System.out.println("See element on vastase poolt blokeeritud");
                    }else {
                        valikkorras = true;}
                } else {
                    System.out.println("Palun vali sobiv number (1-5)");
                }
            }


            //Prindib mängija käidud kaardi välja
            System.out.println("Käisid kaardi:");
            Kaart mangijakaart = kaes.mangiKaart(kaik - 1);
            //Kui eriline võime oli kaardi tugevust muuta teeb selle siin ära
            if (mangijakestev.equals("+2"))
                mangijakaart.setTugevus(mangijakaart.getTugevus()+2);
            if (mangijakestev.equals("-2"))
                mangijakaart.setTugevus(mangijakaart.getTugevus()-2);
            mangijakaart.kaartString();
            //Et oleks mängu ajal kaarte näha
            Thread.sleep(1000);


            //Prindib vastase käidud kaardi välja
            System.out.println("Vastane käis kaardi: ");
            Kaart vastanekaart = vastane.mangiKaart(mangijakestev);

            //Kui eriline võime oli kaardi tugevust muuta teeb selle siin ära
            if (vastasekestev.equals("+2"))
                vastanekaart.setTugevus(vastanekaart.getTugevus()+2);
            if (vastasekestev.equals("-2"))
                vastanekaart.setTugevus(vastanekaart.getTugevus()-2);
            vastanekaart.kaartString();
            //Et oleks mängu ajal kaarte näha
            Thread.sleep(1000);

            //Muudab meeleshoitud erilised efektid tagasi tühjaks
            mangijakestev = "";
            vastasekestev = "";

            //teeb erilisekaardi muutused ära
            if (mangijakaart.getEriline() != null) {
                Object[] muutused = teeEriline(mangijakaart, vastanekaart, kaes, vastane);
                mangijakaart = (Kaart) muutused[0];
                vastanekaart = (Kaart) muutused[1];
                kaes = (Kasi) muutused[2];
                vastane = (Vastane) muutused[3];
                mangijakestev = (String) muutused[4];
            }
            if (vastanekaart.getEriline() != null){
                Object[] muutused = teeEriline(vastanekaart, mangijakaart, vastane, kaes);
                vastanekaart = (Kaart)muutused[0];
                mangijakaart = (Kaart)muutused[1];
                vastane = (Vastane) muutused[2];
                kaes = (Kasi)muutused[3];
                vastasekestev = (String)muutused[4];
            }


            //Kontrollib kumb kaart võidab
            int tulemus = mangijakaart.compareTo(vastanekaart);
            if (tulemus == 0)
                System.out.println("Jäite see round viiki.");
            else if (tulemus > 0) {
                System.out.println("Selle roundi võitsid");
                mangijadict.put(mangijakaart.getElement(), mangijadict.get(mangijakaart.getElement())+1);
            }else {
                System.out.println("Selle roundi kaotasid");
                vastanedict.put(vastanekaart.getElement(), vastanedict.get(vastanekaart.getElement()) + 1);
            }


            //Vastasel ei leidu kaarti mis ei ole blokeeritud
            if (eiLeiduBlokeerimataElement(mangijakestev, vastane)){
                System.out.println("Sinu võit! Vastasel ei ole kaarti, mida saaks käia");
                mangLabi = true;
            }
            //Mängijal ei leidu kaarti mis ei ole blokeeritud
            if (eiLeiduBlokeerimataElement(vastasekestev, kaes)){
                System.out.println("Arvuti võitis! Sul ei ole kaarti, mida saaksid käia");
                mangLabi = true;
            }


            //Kontroll, kas on võidetud kaartidega kombinatisoon
            if (kasVoidetud(mangijadict)) {
                System.out.println("Sinu võit! :)");
                mangLabi = true;
            }
            if (kasVoidetud(vastanedict)) {
                System.out.println("Arvuti võitis! :(");
                mangLabi = true;
            }

        }

    }


    //Kontrollib võitu
    public static boolean kasVoidetud(HashMap asi){
        int tuli = (int)asi.get("tuli");
        int vesi = (int)asi.get("vesi");
        int lumi = (int)asi.get("lumi");
        //kas on igat elementi 1 või siis ühte elementi 3
        return (tuli > 0 && vesi > 0 && lumi > 0) || tuli > 2 || vesi > 2 || lumi > 2;
    }


    //Väljastab võidetud kaartide koguse
    public static void voiduKogus(HashMap dict){
        String tuleemoji = "\uD83D\uDD25";
        String veeemoji = "\uD83D\uDCA7";
        String lumeemoji = "❄";
        System.out.println("   " + tuleemoji + " : " + dict.get("tuli") + "  "+
                veeemoji + " : " + dict.get("vesi") + "  " + lumeemoji + " : " + dict.get("lumi"));
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
    public static void efektidValja(String mangija, String vastane){
        String blokemoji = "\uD83D\uDEAB";
        String tuleemoji = "\uD83D\uDD25";
        String veeemoji = "\uD83D\uDCA7";
        String lumeemoji = "❄";
        String kitsas = "\u2009";
        String teinekitsas = "\u202F";

        String sinule = "";
        String vastasele = "";

        switch (mangija){
            case "+2" -> sinule = sinule + "  +2";
            case "-2" -> sinule = sinule + "  -2";
            case "tuli" -> vastasele = vastasele + "  " + teinekitsas+blokemoji + tuleemoji + kitsas;
            case "vesi" -> vastasele = vastasele + "  " + teinekitsas+kitsas+blokemoji + veeemoji + kitsas;
            case "lumi" -> vastasele = vastasele + "  " + teinekitsas+kitsas+blokemoji + lumeemoji + kitsas;
            default -> vastasele = vastasele + " ❌  ";
        }
        switch (vastane){
            case "+2" -> vastasele = vastasele + "  +2";
            case "-2" -> vastasele = vastasele + "  -2";
            case "tuli" -> sinule = sinule + "  " + teinekitsas+blokemoji + tuleemoji + kitsas;
            case "vesi" -> sinule = sinule + "  " + teinekitsas+kitsas+blokemoji + veeemoji + kitsas;
            case "lumi" -> sinule = sinule + "  " + teinekitsas+kitsas+blokemoji + lumeemoji + kitsas;
            default -> sinule = sinule + " ❌  ";
        }

        //Prindib välja hetke efektid
        System.out.println("Hetkel kehtivad efektid - sinule : " + sinule + "  vastasele : " + vastasele);
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