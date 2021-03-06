package com.softwarewithpassion.nrgyinvoicr.backend.readings.control.files;

import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.softwarewithpassion.nrgyinvoicr.backend.DateTimeFormat.ISO_8601_DATE;

@Component
class FileNameParser {
    private static final String FILE_NAME_REGEX = "mr_([a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12})_(\\d{4}-\\d{2}-\\d{2})_(\\d{3})\\.csv";
    private static final Pattern fileNamePattern = Pattern.compile(FILE_NAME_REGEX);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ISO_8601_DATE);

    ParsedFileName parse(String fileName) throws InvalidReadingDateInFileName, InvalidReadingFileName {
        Matcher matcher = findGroups(fileName);
        String meterId = matcher.group(1);
        String readingDateAsString = matcher.group(2);
        LocalDate readingDate = parseDate(readingDateAsString);
        return new ParsedFileName(meterId, readingDate);
    }

    private Matcher findGroups(String fileName) throws InvalidReadingFileName {
        Matcher matcher = fileNamePattern.matcher(fileName);
        if (!matcher.matches()) {
            throw new InvalidReadingFileName(fileName);
        }
        return matcher;
    }

    private LocalDate parseDate(String readingDateAsString) throws InvalidReadingDateInFileName {
        try {
            return LocalDate.parse(readingDateAsString, formatter);
        } catch (DateTimeException e) {
            throw new InvalidReadingDateInFileName(readingDateAsString);
        }
    }
}
