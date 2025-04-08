package com.Dhruv.EducationalPlatform.Util;

import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ScanResultToDTO {


    private static final SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public static List<UserDTO> convert(ScanResult result) {
        List<UserDTO> paginatedUserList = new ArrayList<>();

        for (Map<String, AttributeValue> data : result.getItems()) {
            UserDTO user = new UserDTO();

            user.setId(getAttributeValue(data, "id"));
            user.setName(getAttributeValue(data, "name"));
            user.setPassword(getAttributeValue(data, "password"));
            user.setEmail(getAttributeValue(data, "email"));
            user.setRole(getAttributeValue(data, "role"));
            user.setGender(getAttributeValue(data, "gender"));
            user.setCreatedAt(parseDate(getAttributeValue(data, "createdAt")));

            paginatedUserList.add(user);
        }

        return paginatedUserList;
    }

    private static String getAttributeValue(Map<String, AttributeValue> data, String key) {
        return (data.containsKey(key) && data.get(key) != null) ? data.get(key).getS() : null;
    }

    private static Date parseDate(String dateString) {
        if (dateString == null) return null;
        try {
            return isoFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
