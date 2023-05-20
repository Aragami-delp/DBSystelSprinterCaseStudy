package com.aragami.casestudy;

import java.io.File;
import java.util.Arrays;

public class StationFileInput {
    private final static String directoryPath = "src\\main\\resources\\stations";
    public static String getStationXmlPath(String _ril100) {
        File directory = new File(directoryPath);
        File[] stationFiles = directory.listFiles();

        if (stationFiles != null) {
            File[] filteredStationFiles = Arrays.stream(stationFiles)
                    .filter(file -> file.isFile() && isValidStationFilename(file, _ril100))
                    .toArray(File[]::new);
            return filteredStationFiles[0].getPath();
        }
        return null;
    }

    private static boolean isValidStationFilename(File file, String fileNamePrefix) {
        String fileName = file.getName();
        String prefix = fileName.split("_", 2)[0];
        return fileNamePrefix.equalsIgnoreCase(prefix);
    }

}
