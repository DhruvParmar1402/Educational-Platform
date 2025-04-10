package com.Dhruv.EducationalPlatform.Repository;


import com.Dhruv.EducationalPlatform.Config.ModelMapperConfig;
import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Entity.UserEntity;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    @Autowired
    private DynamoDBMapper mapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Value("${page.SIZE}")
    private int pageSize;

    private ModelMapper modelMapper= ModelMapperConfig.getModelMapper();



    public UserDTO findUserByEmail(String email) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":email", new AttributeValue().withS(email));

        DynamoDBQueryExpression<UserEntity> queryExpression = new DynamoDBQueryExpression<UserEntity>()
                .withIndexName("email-index")
                .withKeyConditionExpression("email = :email")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false)
                .withScanIndexForward(false);

        List<UserEntity> userDTO=mapper.query(UserEntity.class, queryExpression);
        return userDTO.isEmpty()? null : modelMapper.map(userDTO.getFirst(),UserDTO.class);
    }

    public UserDTO findUserById(String id)
    {
        UserEntity user=mapper.load(UserEntity.class,id);
        return user==null?null:modelMapper.map(user,UserDTO.class);
    }

    public void save(UserDTO user) {
        mapper.save(modelMapper.map(user,UserEntity.class));
    }

    public ScanResult getAll(String lastEvaluatedKey, int pageSize) {
        Map<String , AttributeValue> exclusiveStartKey=new HashMap<>();

        if (lastEvaluatedKey != null && !lastEvaluatedKey.isEmpty()) {
            exclusiveStartKey = new HashMap<>();
            exclusiveStartKey.put("id", new AttributeValue().withS(lastEvaluatedKey));
        }

        ScanRequest scanRequest = new ScanRequest()
                .withTableName("UserTable")
                .withLimit(pageSize);

        if(lastEvaluatedKey!=null)
        {
            scanRequest.withExclusiveStartKey(exclusiveStartKey);
        }

        return amazonDynamoDB.scan(scanRequest);
    }

}
