package com.bikeescape.api.util;

import com.bikeescape.api.exceptions.RaceValidationException;
import com.bikeescape.api.model.AnswerType;
import com.bikeescape.api.model.Race;
import com.bikeescape.api.model.RaceCheckpoint;
import org.apache.commons.lang3.EnumUtils;

import java.util.ArrayList;
import java.util.List;

public class Validator {

    public static void validateRace(Race race) throws RaceValidationException {
        List<String> messages = new ArrayList<>();
        if (race.getUser() == null) {
            messages.add("Race should have an author");
        }
        if (race.getName() == null || race.getName().length() < 3 || race.getName().length() > 50) {
            messages.add("Name should have 3-50 characters");
        }
        if (race.getCity() == null || race.getCity().length() < 3 || race.getCity().length() > 30) {
            messages.add("City should have 3-30 characters");
        }
        if (race.getDescription() == null || race.getDescription().length() < 20 || race.getName().length() > 50) {
            messages.add("Description should have 20-2000 characters");
        }
        if (race.getRaceType() == null) {
            messages.add("Type can not be empty");
        }
        if (race.getDifficulty() == null) {
            messages.add("Difficulty can not be empty");
        }
        if (race.getEstimatedTime() == null || race.getEstimatedTime() < 10 || race.getEstimatedTime() > 240) {
            messages.add("Time should be between 10 and 240 minutes");
        }
        if (race.getSummary() == null || race.getSummary().length() < 20 || race.getSummary().length() > 2000) {
            messages.add("Win race description should have 20-2000 characters");
        }
        if (race.getFailDescription() == null || race.getFailDescription().length() < 20 || race.getFailDescription().length() > 2000) {
            messages.add("Fail race description should have 20-2000 characters");
        }
        if (!messages.isEmpty()) throw new RaceValidationException(Helper.convertListToString(messages));
    }

    public static void validateCheckpoint(RaceCheckpoint raceCheckpoint) throws RaceValidationException {
        List<String> messages = new ArrayList<>();
        if (raceCheckpoint.getRace() == null) {
            messages.add("Checkpoint should have a race");
        }
        if (raceCheckpoint.getNumber() == null) {
            messages.add("Checkpoint should have a number");
        }
        if (raceCheckpoint.getName() == null || raceCheckpoint.getName().length() < 5 || raceCheckpoint.getName().length() > 30) {
            messages.add("Name should have 5-30 characters");
        }
        if (raceCheckpoint.getDescription() == null || raceCheckpoint.getDescription().length() < 20 || raceCheckpoint.getDescription().length() > 1000) {
            messages.add("Description should have 20-1000 characters");
        }
        if (raceCheckpoint.getQuestion() == null || raceCheckpoint.getQuestion().length() < 10 || raceCheckpoint.getQuestion().length() > 50) {
            messages.add("Question should have 5-50 characters");
        }
        if (!EnumUtils.isValidEnumIgnoreCase(AnswerType.class, raceCheckpoint.getAnswerType().getType())) {
            messages.add("Answer type should be existing in available list");
        }
        if (raceCheckpoint.getAnswer() == null || raceCheckpoint.getAnswer().length() < 1 || raceCheckpoint.getAnswer().length() > 30) {
            messages.add("Answer should have 1-30 characters");
        }
        if (raceCheckpoint.getHint() == null || raceCheckpoint.getHint().length() < 10 || raceCheckpoint.getHint().length() > 255) {
            messages.add("Hint should have 10-255 characters");
        }
        if (!messages.isEmpty()) throw new RaceValidationException(Helper.convertListToString(messages));
    }

}
