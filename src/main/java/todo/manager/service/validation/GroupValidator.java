package todo.manager.service.validation;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import todo.manager.domain.Groups;
import todo.manager.repository.GroupsRepository;
import todo.manager.service.dto.GroupsDTO;

@Component
public class GroupValidator {

    @Autowired
    private GroupsRepository groupsRepository;

    public String newGroup(GroupsDTO groupsDTO) {
        if (groupsDTO == null) {
            return "GroupDTO is null!";
        } else if (groupsDTO.getGroupName().trim().isEmpty()) {
            return "Field \"GroupName\" of GroupDTO is empty(Invalid group name)!";
        }
        Optional<Groups> groupsOptional = groupsRepository.findByGroupName(groupsDTO.getGroupName());
        if (groupsDTO.getId() != null) {
            Optional<Groups> optional = groupsRepository.findById(groupsDTO.getId());
            return optional.isEmpty() ? "Group is not found!" : "Group is already exists!";
        }
        if (groupsOptional.isPresent()) {
            return "Group is already exists!";
        }
        return "OK";
    }
}
