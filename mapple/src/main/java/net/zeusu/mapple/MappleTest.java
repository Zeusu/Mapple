package net.zeusu.mapple;

import java.util.List;

import net.zeusu.mapple.annotations.Mapping;
import net.zeusu.mapple.annotations.Mappled;

/**
 * @author Pierre "Zeusu" Grenier
 *
 */
@Mappled(MappleTestEntity.class)
public class MappleTest {


    @Mapping("bgLeTest")
    private boolean test;

    @Mapping
    private String tested;


    @Mapping
    private double panda;

    @Mapping
    private List<MappleTestContentDto> dabu;

    int paoere;

    public boolean isTest() {
        return test;
    }

    public void setTest(final boolean test) {
        this.test = test;
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

    public List<MappleTestContentDto> getDabu() {
        return dabu;
    }

    public void setDabu(final List<MappleTestContentDto> dabu) {
        this.dabu = dabu;
    }

    public int getPaoere() {
        return paoere;
    }

    public void setPaoere(final int paoere) {
        this.paoere = paoere;
    }




}
