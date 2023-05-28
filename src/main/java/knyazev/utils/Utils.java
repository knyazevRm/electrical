package knyazev.utils;

import knyazev.dao.IdentityInterface;

import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    public static Integer getCurrentYear() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return Integer.parseInt(simpleDateFormat.format(date));
    }

    public static List<Long> strToList(String str) {
        String worked = str.substring(1, str.length() - 1).replaceAll("\\s+","");
        String[] objects = worked.split(",");
        List<Long> id = new ArrayList<>();
        for (String i : objects) {
            if (i.equals("")) {
                return id;
            }
            id.add(Long.parseLong(i));
        }

        return id;
    }

    public static <T extends IdentityInterface<Long>> List<Long> objectsToList(Collection<T> objects) {
        List<Long> set = new ArrayList<>();

        for (T o : objects) {
            set.add(o.getIdentity());
        }

        return set;
    }
}
