package todo.manager.service.mapper.impl;

import todo.manager.domain.User;
import todo.manager.service.dto.AdminUserDTO;
import todo.manager.service.dto.UserDTO;

public class MapperUserImpl {

    public static AdminUserDTO userDtoToAdminDto(UserDTO userDTO) {
        AdminUserDTO adminUserDTO = new AdminUserDTO();

        adminUserDTO.setLogin(userDTO.getLogin());
        adminUserDTO.setFirstName(userDTO.getFirstName());
        adminUserDTO.setLastName(userDTO.getLastName());
        adminUserDTO.setEmail(userDTO.getEmail());
        adminUserDTO.setLangKey(userDTO.getLangKey());

        return adminUserDTO;
    }

    public static UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setLangKey(user.getLangKey());

        return dto;
    }
}
