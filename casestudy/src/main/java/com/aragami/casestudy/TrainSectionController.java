package com.aragami.casestudy;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrainSectionController {
    @RequestMapping(path = "/station/{ril100}/train/{trainNumber}/waggon/{waggonNumber}", method = RequestMethod.GET)
    public String getTrainSection(@PathVariable String ril100, @PathVariable int trainNumber, @PathVariable int waggonNumber) {
        // TODO
        return (ril100 + "|" + trainNumber + "|" + waggonNumber);
    }
}
