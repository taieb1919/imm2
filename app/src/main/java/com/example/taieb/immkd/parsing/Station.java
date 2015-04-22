package com.example.taieb.immkd.parsing;

/**
 * Created by KHALED on 13/04/2015.
 */
public class Station {
    private String Stat_Name;
    private String QTY;

    public Station() {
    }

    public Station(String stat_Name, String QTY) {
        Stat_Name = stat_Name;
        this.QTY = QTY;
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
