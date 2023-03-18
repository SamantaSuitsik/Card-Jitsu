public class Hand {
    private Kaart[] kaardid;

    public Hand(Kaart[] kaardid) {
        this.kaardid = kaardid;
    }
    public Hand() {
        this.kaardid = new Kaart[5];
    }


    public void handValja(){
        for (int i = 0; i < kaardid[0].kaartReturn().length; i++) {
            System.out.println(
                            kaardid[0].kaartReturn()[i]+ "   "+
                            kaardid[1].kaartReturn()[i]+ "   "+
                            kaardid[2].kaartReturn()[i]+ "   "+
                            kaardid[3].kaartReturn()[i]+ "   "+
                            kaardid[4].kaartReturn()[i]
                    );
        }
    }

    public Kaart uusKaart(){
        int uustugevus = (int)(Math.random()*11+2);
        int uuselement = (int)(Math.random()*3);
        String[] elemendid = new String[]{"tuli", "vesi", "lumi"};
        return new Kaart(uustugevus, elemendid[uuselement]);
    }

    public Kaart mangiKaart(int index){
        //savekaart on kaart, mis käidi ära
        //liigutab kaardid vasakule ja paneb uue kaardi loppu
        Kaart savekaart = this.kaardid[index];
        for (int i = index; i < 4; i++) {
            this.kaardid[i] = this.kaardid[i+1];
        }
        this.kaardid[4] = uusKaart();
        return savekaart;
    }

    public void suvalisedKaardidKaes() {
        //paneme kätte suvalised kaardid
        for (int i = 0; i < 5; i++) {
            this.kaardid[i] = uusKaart();
        }
    }




}
