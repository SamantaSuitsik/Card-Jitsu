public class Main {
    public static void main(String[] args) {
        Kaart kaart1 = new Kaart(1, "ice");
        Kaart kaart2 = new Kaart(3, "tuli");
        Kaart kaart3 = new Kaart(40, "vesi");
        Kaart kaart4 = new Kaart(12, "ice");
        Kaart kaart5 = new Kaart(9, "vesi");
        Hand uuskasi = new Hand(new Kaart[]{kaart1, kaart2, kaart3, kaart4, kaart5});
        uuskasi.handValja();
    }
}