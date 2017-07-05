package com.example.pawel.appfavorite;

/**
 * Created by Pawel on 02.07.2017.
 */

public class Coordinates {

    public Coordinates()
    {

    }
    public String changeDecimalToDeegre(double decimal)
    {
        String exit;
        //deegre
        int deegre = (int) decimal;

        //minutes
        double w = decimal - deegre;
        double hm = w *60;
        int minutes = (int) hm;

        //sec
        double hs = hm - minutes;
        double ws = hs*60;
        int sec  = (int)ws;  //ewentualnie zrobic w float

        return String.valueOf(deegre)+"\u00b0 "+String.valueOf(minutes)+"' "+String.valueOf(sec)+"\u02EE";
    }
    public Double changeDeegreToDecimal(String position)
    {
        String doubPosition="";
        String s1=position.replace("\u00b0 ",",");
        String s2=s1.replace("\u02EE",",");
        String s3 =s2.replace("' ",",");
        String [] tabOfPosition = s3.split(",");
        Double exit=0.0;
        exit+=Double.valueOf(tabOfPosition[0]);
        exit+=Double.valueOf(tabOfPosition[1])/60;
        exit+=Double.valueOf(tabOfPosition[2])/3600;
        return exit;

    }
}
