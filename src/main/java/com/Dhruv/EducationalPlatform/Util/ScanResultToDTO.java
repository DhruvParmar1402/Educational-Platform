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
            user.setId(data.get("id").getS());
            user.setName(data.get("name").getS());
            user.setPassword(data.get("password").getS());
            user.setEmail(data.get("email").getS());
            user.setPassword(data.get("createdAt").getS());
            user.setRole(data.get("role").getS());

            paginatedUserList.add(user);
        }
        return paginatedUserList;
    }
}
