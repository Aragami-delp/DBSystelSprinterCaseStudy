package com.aragami.casestudy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;

@RestController
public class CaseStudyRESTController {
    @RequestMapping(path = "/station/{_ril100}/train/{_trainNumber}/waggon/{_waggonNumber}", method = RequestMethod.GET)
    public String getTrainSectionResponse(@PathVariable String _ril100, @PathVariable int _trainNumber, @PathVariable int _waggonNumber) {
        return getTrainSection(_ril100, _trainNumber, _waggonNumber);
    }

    public String getTrainSection(String _ril100, int _trainNumber,int _waggonNumber) {
        if (!isValidParameters(_ril100, _trainNumber, _waggonNumber)) {
            return null;
        }

        String stationXmlPath = StationFileInput.getStationXmlPath(_ril100);
        String[] sectionsArray;
        try {
            sectionsArray = StationXmlParser.getSection(stationXmlPath, _trainNumber, _waggonNumber);
        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException _e) {
            return null;
        }
        return convertToJson(sectionsArray);
    }

    private static boolean isValidParameters(String _ril100, int _trainNumber,int _waggonNumber) {
        String trainNumberAsString = Integer.toString(_trainNumber);
        String waggonNumberAsString = Integer.toString(_waggonNumber);
        return ((_ril100.length() >= 2 && _ril100.length() <=5)
                && (trainNumberAsString.length() >= 2 && trainNumberAsString.length() <= 4)
                && (waggonNumberAsString.length() == 1 || waggonNumberAsString.length() == 2)
        );
    }

    private static String convertToJson(String[] _sections) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(_sections);
    }
}
