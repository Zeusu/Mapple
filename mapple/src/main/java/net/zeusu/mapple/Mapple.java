package net.zeusu.mapple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import net.zeusu.mapple.annotations.Mappled;
import net.zeusu.mapple.mapping.Mapper;

/**
 * @author Pierre "Zeusu" Grenier
 *
 */
@Mappled(value = Mapple.class, inherit = MappleTest.class)
public class Mapple {

    private final static Map<Class<?>, Mapper> MAPPERS = new HashMap<>();
    private final static Map<Class<?>, Mapper> REVERT_MAPPERS = new HashMap<>();


    public static void init(final String packageName) {

        System.out.println(" == {{ Initialyzing Mapple  }} == ");

        final Reflections r = new Reflections(packageName);
        Set<Class<?>> classes = r.getTypesAnnotatedWith(Mappled.class);

        for (Class<?> c : classes) {
            final Class<?> revertMapping = c.getAnnotation(Mappled.class).value();
            final Mapper m = new Mapper(c, revertMapping);
            REVERT_MAPPERS.put(revertMapping, m);
            MAPPERS.put(c, m);
        }

        for (Mapper m : MAPPERS.values()) {
            if (m.inheritFrom() != null) {
                m.setSupperMapper(MAPPERS.get(m.inheritFrom()));
            }
            System.out.println(m);
        }

        System.out.println(" == {{ Mapple Ready  }} == ");

    }

    public static Mapper get(final Class<?> cls) {
        return MAPPERS.get(cls);
    }

    public static Mapper getRevert(final Class<?> cls) {
        return REVERT_MAPPERS.get(cls);
    }



    public static void main(final String[] args) {
        Mapple.init("net.zeusu.mapple");
        System.out.println("Generated target: ");
        MappleTestEntity mte = new MappleTestEntity();
        mte.setBgLeTest(true);
        mte.setPanda(666.666);
        mte.setPaoere(-1);
        mte.setTested("Test worked");
        ArrayList<MappleTestContent> testContent = new ArrayList<>();
        testContent.add(new MappleTestContent());
        testContent.add(new MappleTestContent());
        mte.setDabu(testContent);
        MappleTest mt = (MappleTest) Mapple.get(MappleTest.class).toSource(mte);
        System.out.println(mt.getTested());
        System.out.println(mt.isTest());
        System.out.println(mt.paoere);
        System.out.print("Target Collection Size:");
        System.out.println(mt.getDabu().size());
        for (MappleTestContentDto dabu : mt.getDabu()) {
            System.out.println(dabu.getTest());
        }

    }



}
