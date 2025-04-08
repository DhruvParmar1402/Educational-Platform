package com.Dhruv.EducationalPlatform.Config;

import com.Dhruv.EducationalPlatform.DTO.UserDTO;
import com.Dhruv.EducationalPlatform.Entity.UserEntity;
import com.Dhruv.EducationalPlatform.Util.Role;
import org.modelmapper.ModelMapper;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class ModelMapperConfig {

    public static ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<String, Role> roleConverter = new Converter<String, Role>() {
            @Override
            public Role convert(MappingContext<String, Role> context) {
                return Role.fromString(context.getSource());
            }
        };

        modelMapper.typeMap(UserDTO.class, UserEntity.class)
                .addMappings(mapper -> mapper.using(roleConverter)
                        .map(UserDTO::getRole, UserEntity::setRole));

        return modelMapper;
    }
}
