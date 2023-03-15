import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int kaik;
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
        kaes.handValja();
        //uuskasi.handValja();
        /**
         for (int i = 0; i < 1; i++) {
         int index = (int)(Math.random()*5);
         uuskasi.mangiKaart(index).kaartString();
         uuskasi.handValja();
         }
         **/

        System.out.println("kuidas kaib");
        while (!mangLabi) {
            System.out.println("siin on sinu kasi");
            System.out.println("mitmendat kaarti vasakult soovid kaia: ");
            kaik = scanner.nextInt();
            System.out.println("Kaisid kaardi:");
            kaes.mangiKaart(kaik - 1).kaartString();
            System.out.println("Vastane kais kaardi");
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