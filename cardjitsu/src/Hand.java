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


}
