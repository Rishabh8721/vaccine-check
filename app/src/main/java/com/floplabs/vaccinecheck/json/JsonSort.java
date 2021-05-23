package com.floplabs.vaccinecheck.json;

import com.floplabs.vaccinecheck.model.Slot;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JsonSort {
    private static final String TAG = "json_sort";

    public List<Slot> getDateSortedSlots(List<Slot> slots, boolean highToLow) {
        if (highToLow)
            return slots.stream().sorted((a, b) -> b.getDate().compareTo(a.getDate())).collect(Collectors.toList());
        else
            return slots.stream().sorted((a, b) -> a.getDate().compareTo(b.getDate())).collect(Collectors.toList());
    }

    public List<Slot> getDoseSortedSlots(List<Slot> slots, boolean highToLow) {
        if (highToLow)
            return slots.stream().sorted(Comparator.comparingInt(Slot::getAvailableCapacity).reversed()).collect(Collectors.toList());
        else
            return slots.stream().sorted(Comparator.comparingInt(Slot::getAvailableCapacity)).collect(Collectors.toList());
    }
}
