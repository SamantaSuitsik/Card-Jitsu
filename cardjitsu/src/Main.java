import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int kaik;
        boolean mangLabi = false;
        String mangijakestev = new String();
        String vastasekestev = new String();

        Scanner scanner = new Scanner(System.in);
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

        while (!mangLabi) {
            System.out.println("Siin on sinu kaardid: ");
            kaes.kasiValja();

            System.out.print("Sinu voidukaardid    ");
            voiduKogus(mangijadict);
            System.out.print("Vastase voidukaardid ");
            voiduKogus(vastanedict);


            efektidValja(mangijakestev, vastasekestev);

            System.out.println("Vali, mitmendat kaarti vasakult soovid käia: ");
            boolean valikkorras = false;
            kaik = 1;
            //Kontrollib, et mängija valiks sobiva numbri
            while (!valikkorras) {
                kaik = scanner.nextInt();
                if (kaik > 0 && kaik < 6) {
                    if (kaes.getKaardid()[kaik - 1].getElement().equals(vastasekestev)) {
                        System.out.println("See element on vastase poolt blokeeritud");
                    }else {
                        valikkorras = true;}
                } else {
                    System.out.println("Palun vali sobiv number (1-5)");
                }
            }


            System.out.println("Käisid kaardi:");
            Kaart mangijakaart = kaes.mangiKaart(kaik - 1);
            if (mangijakestev == "+2")
                mangijakaart.setTugevus(mangijakaart.getTugevus()+2);
            if (mangijakestev == "-2")
                mangijakaart.setTugevus(mangijakaart.getTugevus()-2);
            mangijakaart.kaartString();
            //Et oleks mängu ajal kaarte näha
            Thread.sleep(1000);


            System.out.println("Vastane käis kaardi: ");
            Kaart vastanekaart = vastane.mangiKaart(mangijakestev);
            if (vastasekestev == "+2")
                vastanekaart.setTugevus(vastanekaart.getTugevus()+2);
            if (vastasekestev == "-2")
                vastanekaart.setTugevus(vastanekaart.getTugevus()-2);
            vastanekaart.kaartString();
            //Et oleks mängu ajal kaarte näha
            Thread.sleep(1000);

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

            int tulemus = mangijakaart.compareTo(vastanekaart);
            if (tulemus == 0)
                System.out.println("See round tuli viik");
            else if (tulemus > 0) {
                System.out.println("Selle roundi võitsid");
                mangijadict.put(mangijakaart.getElement(), mangijadict.get(mangijakaart.getElement())+1);
            }else {
                System.out.println("Selle roundi kaotasid");
                vastanedict.put(vastanekaart.getElement(), vastanedict.get(vastanekaart.getElement()) + 1);
            }

            if (kasVoidetud(mangijadict)) {
                System.out.println("Wooo voitsid m'ngu jejejejeje");
                mangLabi = true;
            }
            if (kasVoidetud(vastanedict)) {
                System.out.println("nooo kaotasid m'ngu nooooo :((((");
                mangLabi = true;
            }

        }

    }

    public static boolean kasVoidetud(HashMap asi){
        int tuli = (int)asi.get("tuli");
        int vesi = (int)asi.get("vesi");
        int lumi = (int)asi.get("lumi");
        return (tuli > 0 && vesi > 0 && lumi > 0) || tuli > 2 || vesi > 2 || lumi > 2;
    }

    public static void voiduKogus(HashMap dict){
        String tuleemoji = "\uD83D\uDD25";
        String veeemoji = "\uD83D\uDCA7";
        String lumeemoji = "\u2744";
        System.out.println("   " + tuleemoji + " : " + dict.get("tuli") + "  "+
                veeemoji + " : " + dict.get("vesi") + "  " + lumeemoji + " : " + dict.get("lumi"));
    }


    public static Object[] teeEriline(Kaart mojuvkaart, Kaart vastasekaart, Kasi mojuvkasi, Kasi vastasekasi){
        String edasine = new String();
        switch (mojuvkaart.getEriline()){
            case "+2" -> {
                edasine = "+2";
            }
            case "-2" -> {
                edasine = "-2";
            }
            case "vahetus" -> {
                int hetk = mojuvkaart.getTugevus();
                mojuvkaart.setTugevus(vastasekaart.getTugevus());
                vastasekaart.setTugevus(hetk);
            }
            case "eemalda tuli" -> {
                System.out.println("sajmsdasnd tee ;ra juhba");
            }
            case "eemalda vesi" -> {
                System.out.println("samanta plns kaua saaba");
            }
            case "eemalda lumi" -> {
                System.out.println("\uD83D\uDE2D \uD83D\uDE2D \uD83D\uDE2D samantaaaaaaaaaa");
            }
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
            case "blokeeri tuli" -> {
                edasine = "tuli";
            }
            case "blokeeri vesi" -> {
                edasine = "vesi";
            }
            case "blokeeri lumi" -> {
                edasine = "lumi";
            }
            default -> {}
        }

        return new Object[]{mojuvkaart, vastasekaart, mojuvkasi, vastasekasi, edasine};
    }

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
            case "+2" -> {
                sinule = sinule + "  +2";
            }
            case "-2" -> {
                sinule = sinule + "  -2";
            }
            case "tuli" -> {
                vastasele = vastasele + "  " + teinekitsas+blokemoji + tuleemoji + kitsas;
            }
            case "vesi" -> {
                vastasele = vastasele + "  " + teinekitsas+kitsas+blokemoji + veeemoji + kitsas;
            }
            case "lumi" -> {
                vastasele = vastasele + "  " + teinekitsas+kitsas+blokemoji + lumeemoji + kitsas;
            }
            default -> {
                vastasele = vastasele + " ❌  ";
            }
        }
        switch (vastane){
            case "+2" -> {
                vastasele = vastasele + "  +2";
            }
            case "-2" -> {
                vastasele = vastasele + "  -2";
            }
            case "tuli" -> {
                sinule = sinule + "  " + teinekitsas+blokemoji + tuleemoji + kitsas;
            }
            case "vesi" -> {
                sinule = sinule + "  " + teinekitsas+kitsas+blokemoji + veeemoji + kitsas;
            }
            case "lumi" -> {
                sinule = sinule + "  " + teinekitsas+kitsas+blokemoji + lumeemoji + kitsas;
            }
            default -> {
                sinule = sinule + " ❌  ";
            }
        }

        System.out.println("Hetkel kehtivad efektid - sinule : " + sinule + "  vastasele : " + vastasele);
    }

    public static Kasi eemaldaElement(Kaart kaart, Kasi kasi){
        /**SAMANTAAAAAAAAAAAAAAAAAAA TEEEEEE
         */
        return kasi;
    }
}