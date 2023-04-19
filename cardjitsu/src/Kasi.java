public class Kasi {
    private Kaart[] kaardid;

    public static final String[] erilised = new String[]{"+2", "-2", "vahetus",
            "eemalda tuli", "eemalda vesi", "eemalda lumi",
            "muuda tuli", "muuda vesi", "muuda lumi",
            "blokeeri tuli", "blokeeri vesi", "blokeeri lumi"};


    public Kasi(Kaart[] kaardid) {
        this.kaardid = kaardid;
    }
    public Kasi() {
        this.kaardid = new Kaart[5];
    }


    //Väljastab kogu käe kaardid reas
    public void kasiValja(){
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

    //Tekitab uue juhusliku kaardi
    public Kaart uusKaart(){
        int uustugevus = (int)(Math.random()*11+2);
        int uuselement = (int)(Math.random()*3);
        String[] elemendid = new String[]{"tuli", "vesi", "lumi"};
        //Võimalus, et tekiks erivõimega kaart
        if ((int)(Math.random()*21)<5){
            int valik = (int)(Math.random()*12);
            //Muutmise efekt ei tekita kaotust
            while (uuselement == 0 && valik == 8 || uuselement == 1 && valik == 6 || uuselement == 2 && valik == 7)
                valik = (int)(Math.random()*12);

            //Tagasta efektiga kaart
            return new Kaart(uustugevus, elemendid[uuselement], erilised[valik]);
        }
        //Tagasta tavaline kaart
        return new Kaart(uustugevus, elemendid[uuselement]);
    }


    //Tagastab indeksil leiduva kaardi ja tekitab uue juhusliku kaardi
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

    public Kaart[] getKaardid() {
        return kaardid;
    }

    public void setKaardid(Kaart[] kaardid){
        this.kaardid = kaardid;
    }
}
