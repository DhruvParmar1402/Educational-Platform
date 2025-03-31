package com.Dhruv.EducationalPlatform.Util;

import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class ScanResultToDTO {
    public static List<UserDTO> convert(ScanResult result) {
        List<UserDTO> paginatedUserList = new ArrayList<>();

        for (Map<String, AttributeValue> data : result.getItems()) {
            UserDTO user = new UserDTO();

            user.setId(getAttributeValue(data, "id"));
            user.setName(getAttributeValue(data, "name"));
            user.setPassword(getAttributeValue(data, "password"));
            user.setEmail(getAttributeValue(data, "email"));
            user.setRole(getAttributeValue(data, "role"));

            paginatedUserList.add(user);
        }

        return paginatedUserList;
    }

    private static String getAttributeValue(Map<String, AttributeValue> data, String key) {
        return (data.containsKey(key) && data.get(key) != null) ? data.get(key).getS() : null;
    }
}
