package com.example.taieb.immkd.parsing;

/**
 * Created by KHALED on 13/04/2015.
 */
public class Station {
    private String Stat_Name;
    private String Stat_Num;
    private String QTY;

    public Station() {
    }

    public Station(String stat_Name, String QTY,String Stat_Num) {
        Stat_Name = stat_Name;
        this.QTY = QTY;
        this.Stat_Num=Stat_Num;
    }

    public String getStat_Num() {
        return Stat_Num;
    }

    public void setStat_Num(String stat_Num) {
        Stat_Num = stat_Num;
    }

    public String getStat_Name() {
        return Stat_Name;
    }

    public void setStat_Name(String stat_Name) {
        Stat_Name = stat_Name;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;

        Station station = (Station) o;

        if (!QTY.equals(station.QTY)) return false;
        if (!Stat_Name.equals(station.Stat_Name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Stat_Name.hashCode();
        result = 31 * result + QTY.hashCode();
        return result;
    }
}
