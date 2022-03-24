package net.zeusu.mapple;

import java.util.List;

/**
 * @author Pierre "Zeusu" Grenier
 *
 */
public class MappleTestEntity {


    private boolean bgLeTest;

    private String tested;

    private double panda;

    private List<MappleTestContent> dabu;

    int paoere;

    public boolean isBgLeTest() {
        return bgLeTest;
    }

    public void setBgLeTest(final boolean bgLeTest) {
        this.bgLeTest = bgLeTest;
    }

    public String getTested() {
        return tested;
    }

    public void setTested(final String tested) {
        this.tested = tested;
    }

    public double getPanda() {
        return panda;
    }

    public void setPanda(final double panda) {
        this.panda = panda;
    }

    public int getPaoere() {
        return paoere;
    }

    public void setPaoere(final int paoere) {
        this.paoere = paoere;
    }

    public List<MappleTestContent> getDabu() {
        return dabu;
    }

    public void setDabu(final List<MappleTestContent> dabu) {
        this.dabu = dabu;
    }


}
