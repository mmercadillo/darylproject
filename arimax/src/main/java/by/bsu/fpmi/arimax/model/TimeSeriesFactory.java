package by.bsu.fpmi.arimax.model;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public final class TimeSeriesFactory {
    private static final char GAP = ',';

    private TimeSeriesFactory() {
    }

    public static TimeSeries createTimeSeries(File file) {
        return createTimeSeries(file, GAP);
    }

    public static TimeSeries createTimeSeries(File file, char separator) {
        try {
            CSVReader reader = new CSVReader(new FileReader(file), separator);

            ColumnPositionMappingStrategy<Moment> mappingStrategy = new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(Moment.class);

            String[] columns = new String[]{ Moment.TIME_FIELD, Moment.VALUE_FIELD };
            mappingStrategy.setColumnMapping(columns);

            CsvToBean<Moment> csv = new CsvToBean<>();
            List<Moment> moments = csv.parse(mappingStrategy, reader);

            return new TimeSeries(moments, file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
