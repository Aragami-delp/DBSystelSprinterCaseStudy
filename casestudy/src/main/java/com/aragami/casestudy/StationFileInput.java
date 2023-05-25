package com.aragami.casestudy;

import java.io.File;
import java.util.Arrays;

public class StationFileInput {
    private final static String directoryPath = "src\\main\\resources\\stations";
    public static String getStationXmlPath(String _ril100) throws StationNotFoundException, NoStationFileFoundException {
        File directory = new File(directoryPath);
        File[] stationFiles = directory.listFiles();

        if (stationFiles.length > 0) {
            File[] filteredStationFiles = Arrays.stream(stationFiles)
                    .filter(file -> file.isFile() && isValidStationFilename(file, _ril100))
                    .toArray(File[]::new);
            if (filteredStationFiles.length == 1)
                return filteredStationFiles[0].getPath();
            throw new StationNotFoundException(_ril100);
        }
        throw new NoStationFileFoundException("Cannot find any station file");
    }

    private static boolean isValidStationFilename(File _file, String _fileNamePrefix) {
        String fileName = _file.getName();
        String prefix = fileName.split("_")[0];
        return _fileNamePrefix.equalsIgnoreCase(prefix);
    }

    public static class StationNotFoundException extends Exception {
        private String ril100;

        public StationNotFoundException(String _ril100) {
            this.ril100 = _ril100;
        }

        public String getRil100() {
            return ril100;
        }
    }

    public static class NoStationFileFoundException extends Exception {
        public NoStationFileFoundException(String _message) {
            super(_message);
        }
    }
}
