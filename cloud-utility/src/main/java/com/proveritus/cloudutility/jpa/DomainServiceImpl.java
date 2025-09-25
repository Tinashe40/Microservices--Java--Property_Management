package com.proveritus.cloudutility.jpa;

import com.proveritus.cloudutility.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public abstract class DomainServiceImpl<T extends BaseEntity, C, U extends Updatable, D>
        implements DomainService<T, C, U, D> {

    private final BaseDao<T, Long> baseDao;
    private final EntityDtoMapper<D, T, C, U> entityMapper;

    protected DomainServiceImpl(BaseDao<T, Long> baseDao, EntityDtoMapper<D, T, C, U> entityMapper) {
        this.baseDao = baseDao;
        this.entityMapper = entityMapper;
    }

    @Override
    public D create(C createCommand) {
        T entity = entityMapper.fromCreateDto(createCommand);
        T savedEntity = baseDao.save(entity);
        return entityMapper.toDto(savedEntity);
    }

    @Override
    public D update(U updateCommand) {
        Long entityId = updateCommand.getId();
        T entity = findEntityById(entityId);
        entityMapper.updateFromUpdateDto(updateCommand, entity);
        T updatedEntity = baseDao.save(entity);
        return entityMapper.toDto(updatedEntity);
    }

    @Override
    public D findById(Long id) {
        return entityMapper.toDto(findEntityById(id));
    }

    public T findEntityById(Long id) {
        return baseDao.findOne(Specification.where(notDeleted()).and((root, query, cb) -> cb.equal(root.get("id"), id)))
                .orElseThrow(() -> new EntityNotFoundException(getEntityClass().getSimpleName().concat(" record not found with id: " + id)));
    }

    public void deleteById(Long id) {
        T entity = findEntityById(id);
        entity.pseudoDelete();
        baseDao.save(entity);
    }

    @Override
    public Collection<D> findAll() {
        return entityMapper.toDto(baseDao.findAll(notDeleted()));
    }

    @Override
    public Page<D> findAll(Pageable pageable) {
        return baseDao.findAll(notDeleted(), pageable).map(entityMapper::toDto);
    }

    private Specification<T> notDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("deleted"));
    }

    public abstract Class<T> getEntityClass();
}
