package todo.manager.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.manager.domain.Comments;
import todo.manager.repository.CommentsRepository;
import todo.manager.service.CommentsService;
import todo.manager.service.dto.CommentsDTO;
import todo.manager.service.mapper.CommentsMapper;

/**
 * Service Implementation for managing {@link Comments}.
 */
@Service
@Transactional
public class CommentsServiceImpl implements CommentsService {

    private final Logger log = LoggerFactory.getLogger(CommentsServiceImpl.class);

    private final CommentsRepository commentsRepository;

    private final CommentsMapper commentsMapper;

    public CommentsServiceImpl(CommentsRepository commentsRepository, CommentsMapper commentsMapper) {
        this.commentsRepository = commentsRepository;
        this.commentsMapper = commentsMapper;
    }

    @Override
    public CommentsDTO save(CommentsDTO commentsDTO) {
        log.debug("Request to save Comments : {}", commentsDTO);
        Comments comments = commentsMapper.toEntity(commentsDTO);
        comments = commentsRepository.save(comments);
        return commentsMapper.toDto(comments);
    }

    @Override
    public CommentsDTO update(CommentsDTO commentsDTO) {
        log.debug("Request to update Comments : {}", commentsDTO);
        Comments comments = commentsMapper.toEntity(commentsDTO);
        comments = commentsRepository.save(comments);
        return commentsMapper.toDto(comments);
    }

    @Override
    public Optional<CommentsDTO> partialUpdate(CommentsDTO commentsDTO) {
        log.debug("Request to partially update Comments : {}", commentsDTO);

        return commentsRepository
            .findById(commentsDTO.getId())
            .map(existingComments -> {
                commentsMapper.partialUpdate(existingComments, commentsDTO);

                return existingComments;
            })
            .map(commentsRepository::save)
            .map(commentsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentsDTO> findAll() {
        log.debug("Request to get all Comments");
        return commentsRepository.findAll().stream().map(commentsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentsDTO> findOne(Long id) {
        log.debug("Request to get Comments : {}", id);
        return commentsRepository.findById(id).map(commentsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Comments : {}", id);
        commentsRepository.deleteById(id);
    }
}
