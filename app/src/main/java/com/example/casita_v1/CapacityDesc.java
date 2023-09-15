package com.example.casita_v1;

import java.util.Comparator;

public class CapacityDesc implements Comparator<DataStoreAdmin> {
    public int compare(DataStoreAdmin s1, DataStoreAdmin s2)
    {
        int a = Integer.parseInt(s1.capacity);
        int b = Integer.parseInt(s2.capacity);
        if (a == b)
            return 0;
        else if (a < b)
            return 1;
        else
            return -1;
    }
}
