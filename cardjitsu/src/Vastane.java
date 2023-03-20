public class Vastane extends Kasi {

    public Kaart mangiKaart(String eriline){
        while (true) {
            int i = (int) (Math.random() * 5);
            if (!this.getKaardid()[i].getElement().equals(eriline))
                return this.mangiKaart(i);
        }
    }
}
