package ru.dingo3.streamingmusicbmbf.converters;

import ru.dingo3.streamingmusicbmbf.converters.BeatSageCore;

import java.util.ArrayList;

public class SingletonConverterArray {
    private static SingletonConverterArray instance;

    private final ArrayList<AbstractConverter> converters = new ArrayList<>();

    private SingletonConverterArray() {
        converters.add(new BeatSageCore());
    }

    public static SingletonConverterArray getInstance() {
        if (instance == null) {
            instance = new SingletonConverterArray();
        }
        return instance;
    }

    public ArrayList<AbstractConverter> getConverters() {
        return converters;
    }

    public AbstractConverter getConverterByName(String converterName) {
        for (AbstractConverter converter : converters) {
            if (converter.getConverterName().equals(converterName)) {
                return converter;
            }
        }
        return null;
    }

    public AbstractConverter getConverterById(String converterId) {
        for (AbstractConverter converter : converters) {
            if (converter.getConverterId().equals(converterId)) {
                return converter;
            }
        }
        return null;
    }
}
