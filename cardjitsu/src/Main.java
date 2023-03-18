import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int kaik;
        boolean mangLabi = false;
        String tuleemoji = "\uD83D\uDD25";
        String veeemoji = "\uD83D\uDCA7";
        String lumeemoji = "\uD83C\uDF28";
        Scanner scanner = new Scanner(System.in);
        HashMap<String, Integer> mangijadict = new HashMap<>();
        mangijadict.put("tuli", 0);
        mangijadict.put("vesi", 0);
        mangijadict.put("lumi", 0);
        HashMap<String, Integer> vastanedict = new HashMap<>();
        vastanedict.put("tuli", 0);
        vastanedict.put("vesi", 0);
        vastanedict.put("lumi", 0);
//        Kaart kaart1 = new Kaart(1, "lumi");
//        Kaart kaart2 = new Kaart(3, "tuli");
//        Kaart kaart3 = new Kaart(40, "vesi");
//        Kaart kaart4 = new Kaart(12, "lumi");
//        Kaart kaart5 = new Kaart(9, "vesi");
//        Hand uuskasi = new Hand(new Kaart[]{kaart1, kaart2, kaart3, kaart4, kaart5});
        //teeme käe, millel on kaardikohad
        Hand kaes = new Hand();
        kaes.suvalisedKaardidKaes();

        //uuskasi.handValja();
        /**
         for (int i = 0; i < 1; i++) {
         int index = (int)(Math.random()*5);
         uuskasi.mangiKaart(index).kaartString();
         uuskasi.handValja();
         }
         **/

        System.out.println("Kuidas mängida?");
        System.out.println("Mängijale on ette antud 5 kaarti, " +
                "mille hulgast valitakse üks, mis käiakse välja. \nVastane (ehk arvuti) käib samal ajal oma kaardi." +
                "\nÜhel kaardil on kindel tugevus (arv 2-st 12-ni) ja element (tuli, vesi või lumi). \nVälja käidud kaartidest võidab see, mis on elemendi või numbri poolest tugevam: ");
        System.out.println("Tuli võidab lume,\nLumi võidab vee,\nVesi võidab tule.");
        System.out.println("Kui käidud kaartidel on sama element võidab see, kelle kaardil on suurem number. \nKui number ja element on mõlemal samad, " +
                "siis on tegemist viigiga.");
        System.out.println("Peale käiku antakse võitjale tema käidud kaart tagasi, " +
                "mis läheb võidetud kaartide komplekti. " +
                "\nKui käik lõppes viigiga, ei saa kumbki mängija kaarti." +
                "\nMängija, kes saab esimesena komplektis kokku nii tule, vee kui ka lume elemendiga kaardid, on võitnud.");
        System.out.println();

        while (!mangLabi) {
            System.out.println("Siin on sinu kaardid: ");
            kaes.handValja();

            System.out.print("Sinu voidukaardid ");
            voiduKogus(mangijadict);
            System.out.print("Vastase voidukaardid ");
            voiduKogus(vastanedict);

            System.out.println("Vali, mitmendat kaarti vasakult soovid käia: ");
            boolean valikkorras = false;
            kaik = 1;
            //Kontrollib, et mängija valiks sobiva numbri
            while (!valikkorras) {
                kaik = scanner.nextInt();
                if (kaik > 0 && kaik < 6)
                    valikkorras = true;
                else
                    System.out.println("Palun vali sobiv number (1-5)");
            }
            System.out.println("Käisid kaardi:");
            Kaart mangijakaart = kaes.mangiKaart(kaik - 1);
            mangijakaart.kaartString();
            //Et oleks mängu ajal kaarte näha
            Thread.sleep(1000);

            System.out.println("Vastane käis kaardi: ");
            Kaart vastanekaart = kaes.uusKaart();
            vastanekaart.kaartString();
            //Et oleks mängu ajal kaarte näha
            Thread.sleep(1000);

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

//            System.out.println("\uD83D\uDD25"); // Tule emoji
//            System.out.println("\uD83D\uDCA7"); // vee emoji
//            System.out.println("\uD83C\uDF28"); // lumi emoji
//
//            mangLabi = true;
        }
        /**
        if (voitja == mangija)
            System.out.println("SA VOITSID!");
        else
            System.out.println("Sa kaotasid!");
         **/
    }

    public static boolean kasVoidetud(HashMap asi){
        int tuli = (int)asi.get("tuli");
        int vesi = (int)asi.get("vesi");
        int lumi = (int)asi.get("lumi");
        if ((tuli>0 && vesi>0 && lumi>0)|| tuli>2 || vesi>2 || lumi>2)
            return true;
        return false;
    }

    public static void voiduKogus(HashMap dict){
        String tuleemoji = "\uD83D\uDD25";
        String veeemoji = "\uD83D\uDCA7";
        String lumeemoji = "\uD83C\uDF28";
        System.out.println("   " + tuleemoji + " : " + dict.get("tuli") + "  "+
                veeemoji + " : " + dict.get("vesi") + "  " + lumeemoji + " : " + dict.get("lumi"));
    }
}