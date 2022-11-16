package todo.manager.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.manager.domain.Groups;
import todo.manager.repository.GroupsRepository;
import todo.manager.service.GroupsService;
import todo.manager.service.dto.GroupsDTO;
import todo.manager.service.mapper.GroupsMapper;
import todo.manager.service.mapper.GroupsMapperImpl;
import todo.manager.service.mapper.impl.MapperGroupsImpl;

/**
 * Service Implementation for managing {@link Groups}.
 */
@Service
@Transactional
public class GroupsServiceImpl implements GroupsService {

    private final Logger log = LoggerFactory.getLogger(GroupsServiceImpl.class);

    private final GroupsRepository groupsRepository;

    private final GroupsMapper groupsMapper;

    public GroupsServiceImpl(GroupsRepository groupsRepository, GroupsMapper groupsMapper) {
        this.groupsRepository = groupsRepository;
        this.groupsMapper = groupsMapper;
    }

    @Override
    public GroupsDTO save(GroupsDTO groupsDTO) {
        log.debug("Request to save Groups : {}", groupsDTO);
        Groups groups = groupsMapper.toEntity(groupsDTO);
        groups = groupsRepository.save(groups);
        return groupsMapper.toDto(groups);
    }

    @Override
    public GroupsDTO update(GroupsDTO groupsDTO) {
        log.debug("Request to update Groups : {}", groupsDTO);
        Groups groups = groupsMapper.toEntity(groupsDTO);
        groups = groupsRepository.save(groups);
        return groupsMapper.toDto(groups);
    }

    @Override
    public Optional<GroupsDTO> partialUpdate(GroupsDTO groupsDTO) {
        log.debug("Request to partially update Groups : {}", groupsDTO);

        return groupsRepository
            .findById(groupsDTO.getId())
            .map(existingGroups -> {
                groupsMapper.partialUpdate(existingGroups, groupsDTO);

                return existingGroups;
            })
            .map(groupsRepository::save)
            .map(groupsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupsDTO> findAll() {
        log.debug("Request to get all Groups");

        return groupsRepository.findAll().stream().map(MapperGroupsImpl::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GroupsDTO> findOne(Long id) {
        log.debug("Request to get Groups : {}", id);
        return groupsRepository.findById(id).map(groupsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Groups : {}", id);
        groupsRepository.deleteById(id);
    }
}
