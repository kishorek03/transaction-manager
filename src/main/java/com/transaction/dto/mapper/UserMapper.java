package com.transaction.dto.mapper;

import com.transaction.dto.UserDTO;
import com.transaction.entity.User;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO dto);
    List<UserDTO> toDTOs(List<User> users);
}
