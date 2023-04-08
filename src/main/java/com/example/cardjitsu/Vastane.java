package com.example.cardjitsu;

public class Vastane extends Kasi {


    //Arvuti saaks suvalise kaardi käia
    public Kaart mangiKaart(String eriline){
        while (true) {
            int i = (int) (Math.random() * 5);
            if (!this.getKaardid()[i].getElement().equals(eriline))
                return this.mangiKaart(i);
        }
    }
}