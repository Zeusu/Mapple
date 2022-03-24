package net.zeusu.mapple;

import net.zeusu.mapple.annotations.Mapping;
import net.zeusu.mapple.annotations.Mappled;

/**
 * @author Pierre "Zeusu" Grenier
 *
 */
@Mappled(MappleTestContent.class)
public class MappleTestContentDto {

    @Mapping
    String test;

    public String getTest() {
        return test;
    }

    public void setTest(final String test) {
        this.test = test;
    }

}
