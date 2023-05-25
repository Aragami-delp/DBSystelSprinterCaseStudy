package com.aragami.casestudy;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

@RestController
public class CaseStudyRESTController {
    @RequestMapping(path = "/station/{_ril100}/train/{_trainNumber}/waggon/{_waggonNumber}", method = RequestMethod.GET)
    public String getTrainSectionResponse(@PathVariable String _ril100, @PathVariable int _trainNumber, @PathVariable int _waggonNumber) {
        return getTrainSection(_ril100, _trainNumber, _waggonNumber);
    }

    public String getTrainSection(String _ril100, int _trainNumber,int _waggonNumber) {
        String[] sectionsArray;

        try {
            isValidParameters(_ril100, _trainNumber, _waggonNumber);

            String stationXmlPath = StationFileInput.getStationXmlPath(_ril100);
            sectionsArray = StationXmlParser.getSection(stationXmlPath, _trainNumber, _waggonNumber);
        }
        catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException _e) {
            return "Error processing xml";
        } catch (StationFileInput.StationNotFoundException _e) {
            return "Station ril100 not found. Value: " + _e.getRil100();
        } catch (InvalidHttpParametersException _e) {
            return "Invalid http parameter. Parameter: "
                    + _e.getInvalidName().toString()
                    + ", Value: "
                    + _e.getInvalidValue();
        } catch (StationFileInput.NoStationFileFoundException _e) {
            return _e.getMessage();
        }
        return convertToJson(sectionsArray);
    }

    private static boolean isValidParameters(String _ril100, int _trainNumber,int _waggonNumber)
            throws InvalidHttpParametersException {
        if (!(_ril100.length() >= 2 && _ril100.length() <= 5))
            throw new InvalidHttpParametersException(_ril100, InvalidHttpParametersException.ParameterName.RIL100);
        String trainNumberAsString = Integer.toString(_trainNumber);
        if (!(trainNumberAsString.length() >= 2 && trainNumberAsString.length() <= 4))
            throw new InvalidHttpParametersException(trainNumberAsString, InvalidHttpParametersException.ParameterName.TRAINNUMBER);
        String waggonNumberAsString = Integer.toString(_waggonNumber);
        if (!(waggonNumberAsString.length() == 1 || waggonNumberAsString.length() == 2))
            throw new InvalidHttpParametersException(waggonNumberAsString, InvalidHttpParametersException.ParameterName.WAGGONNUMBER);
        return true;
    }

    private static String convertToJson(String[] _sections) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("sections", gson.toJsonTree(_sections));

        return gson.toJson(jsonObject);
    }


    public static class InvalidHttpParametersException extends Exception {
        private final String invalidValue;
        private final ParameterName invalidName;

        public InvalidHttpParametersException(String _invalidValue, ParameterName _invalidField) {
            this.invalidValue = _invalidValue;
            this.invalidName = _invalidField;
        }

        public String getInvalidValue() {
            return invalidValue;
        }

        public ParameterName getInvalidName() {
            return invalidName;
        }

        public enum ParameterName {
            RIL100,
            @SuppressWarnings("SpellCheckingInspection") TRAINNUMBER,
            @SuppressWarnings("SpellCheckingInspection") WAGGONNUMBER
        }
    }
}
