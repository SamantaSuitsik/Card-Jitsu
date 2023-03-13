public class Hand {
    private Kaart[] kaardid;

    public Hand(Kaart[] kaardid) {
        this.kaardid = kaardid;
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
        int uustugevus = (int)(Math.random()*10);
        int uuselement = (int)(Math.random()*3);
        String[] elemendid = new String[]{"tuli", "vesi", "ice"};
        return new Kaart(uustugevus, elemendid[uuselement]);
    }

    public Kaart mangiKaart(int index){
        Kaart savekaart = this.kaardid[index];
        for (int i = index; i < 4; i++) {
            this.kaardid[i] = this.kaardid[i+1];
        }
        this.kaardid[4] = uusKaart();
        return savekaart;
    }


}
