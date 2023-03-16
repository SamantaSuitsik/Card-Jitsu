import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int kaik;
        String valmis;
        boolean mangLabi = false;
        Scanner scanner = new Scanner(System.in);
        Kaart kaart1 = new Kaart(1, "ice");
        Kaart kaart2 = new Kaart(3, "tuli");
        Kaart kaart3 = new Kaart(40, "vesi");
        Kaart kaart4 = new Kaart(12, "ice");
        Kaart kaart5 = new Kaart(9, "vesi");
        Hand uuskasi = new Hand(new Kaart[]{kaart1, kaart2, kaart3, kaart4, kaart5});
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
            System.out.println("Vali, mitmendat kaarti vasakult soovid käia: ");
            kaik = scanner.nextInt();
            System.out.println("Käisid kaardi:");
            kaes.mangiKaart(kaik - 1).kaartString();
            System.out.println("Vastane käis kaardi: ");
            kaes.uusKaart().kaartString();

            /**
             if (voitja == mangija) {
             System.out.println(" ".repeat(40) + "Sinu voidetud kaardid on "+ "tuli");

             }
             **/

            System.out.println("\uD83D\uDD25"); // Tule emoji
            System.out.println("\uD83D\uDCA7"); // vee emoji
            System.out.println("\uD83C\uDF28"); // lumi emoji

            mangLabi = true;
        }
        /**
        if (voitja == mangija)
            System.out.println("SA VOITSID!");
        else
            System.out.println("Sa kaotasid!");
         **/
    }
}