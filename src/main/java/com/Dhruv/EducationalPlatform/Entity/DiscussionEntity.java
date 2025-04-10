package com.Dhruv.EducationalPlatform.Entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@DynamoDBTable(tableName = "DiscussionTable")
public class DiscussionEntity {

    @DynamoDBAutoGeneratedKey
    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "courseId-index", attributeName = "courseId")
    @DynamoDBAttribute(attributeName = "courseId")
    private String courseId;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "userId-index", attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    private String userId;

    @DynamoDBAttribute(attributeName = "message")
    private String message;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @DynamoDBTypeConvertedTimestamp
    @DynamoDBAttribute(attributeName = "createdAt")
    private Date createdAt;
}

