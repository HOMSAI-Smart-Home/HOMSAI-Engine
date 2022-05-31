package app.homsai.engine.common.infrastructure.repositories;


import app.homsai.engine.common.domain.models.SystemConsts;
import app.homsai.engine.common.domain.specifications.BaseEntitySpecificationsBuilder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SoftDeletesRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements SoftDeletesRepository<T, ID> {

    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;
    private final Class<T> domainClass;
    private static final String DELETED_FIELD = "deletedAt";

    public SoftDeletesRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
        this.domainClass = domainClass;
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);
    }

    public SoftDeletesRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
            EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
        this.domainClass = entityInformation.getJavaType();

    }

    public Iterable<T> findAllActive() {
        return super.findAll(notDeleted());
    }

    @Override
    public Iterable<T> findAllActive(Sort sort) {
        return super.findAll(notDeleted(), sort);
    }

    @Override
    public Page<T> findAllActive(Pageable pageable, Specification<T> specification) {
        return super.findAll(notDeleted(specification), pageable);
    }

    @Override
    public Page<T> findAllActive(Pageable pageable, String search) {
        Specification<T> spec = buildSpecification(search);
        return super.findAll(notDeleted(spec), pageable);
    }

    public Specification<T> buildSpecification(String search) {
        Specification<T> spec = null;
        if (search != null) {
            BaseEntitySpecificationsBuilder builder = new BaseEntitySpecificationsBuilder();
            Pattern pattern = Pattern.compile(SystemConsts.searchRegEx);
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(toCamelCase(matcher.group(1)), matcher.group(2), matcher.group(3),
                        matcher.group(4));
            }
            spec = builder.build();
        }
        return spec;
    }

    @Override
    public Page<T> findAllActive(Pageable pageable, String search, Specification<T> specification) {
        Specification<T> spec = buildSpecification(search);
        return super.findAll(notDeleted(spec, specification), pageable);
    }

    @Override
    public List<T> findAllActiveList(Pageable pageable, String search,
            Specification<T> specification) {
        Specification<T> spec = buildSpecification(search);
        return super.findAll(notDeleted(spec, specification), pageable).getContent();
    }

    @Override
    public Page<T> findAllActive(Pageable pageable, String search, Specification<T> specification,
            Specification<T> specification1) {
        Specification<T> spec = buildSpecification(search);
        return super.findAll(notDeleted(spec, specification, specification1), pageable);
    }

    private String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = "";
        Boolean b = true;
        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part, b);
            b = false;
        }
        return camelCaseString;
    }

    private String toProperCase(String s, Boolean b) {
        return b ? s.substring(0, 1).toLowerCase() + s.substring(1).toLowerCase()
                : s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }


    @Override
    public Page<T> findAllActive(Pageable pageable) {
        return super.findAll(notDeleted(), pageable);
    }

    @Override
    public Iterable<T> findAllActive(Iterable<ID> ids) {
        if (ids == null || !ids.iterator().hasNext())
            return Collections.emptyList();

        if (entityInformation.hasCompositeId()) {

            List<T> results = new ArrayList<T>();

            for (ID id : ids)
                results.add(findOneActive(id));

            return results;
        }

        ByIdsSpecification<T> specification = new ByIdsSpecification<T>(entityInformation);
        TypedQuery<T> query =
                getQuery(Specification.where(specification).and(notDeleted()), (Sort) null);

        return query.setParameter(specification.parameter, ids).getResultList();
    }

    @Override
    public T findOneActive(ID id) {
        return super.findOne(Specification.where(new ByIdSpecification<>(entityInformation, id))
                .and(notDeleted())).orElse(null);
    }

    @Override
    public T findOneActive(ID id, String search) {
        Specification<T> spec = buildSpecification(search);
        return super.findOne(Specification.where(new ByIdSpecification<>(entityInformation, id))
                .and(notDeleted()).and(spec)).orElse(null);
    }

    @Override
    @Transactional
    public void softDelete(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        softDelete(id, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void softDelete(T entity) {
        Assert.notNull(entity, "The entity must not be null!");
        softDelete(entity, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void softDelete(Iterable<? extends T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        for (T entity : entities)
            softDelete(entity);
    }

    @Override
    @Transactional
    public void softDeleteAll() {
        for (T entity : findAllActive())
            softDelete(entity);
    }

    @Override
    @Transactional
    public void scheduleSoftDelete(ID id, LocalDateTime localDateTime) {
        softDelete(id, localDateTime);
    }

    @Override
    @Transactional
    public void scheduleSoftDelete(T entity, LocalDateTime localDateTime) {
        softDelete(entity, localDateTime);
    }

    private void softDelete(ID id, LocalDateTime localDateTime) {
        Assert.notNull(id, "The given id must not be null!");

        T entity = findOneActive(id);

        if (entity == null)
            throw new EmptyResultDataAccessException(String.format(
                    "No %s entity with id %s exists!", entityInformation.getJavaType(), id), 1);

        softDelete(entity, localDateTime);
    }

    private void softDelete(T entity, LocalDateTime localDateTime) {
        Assert.notNull(entity, "The entity must not be null!");

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaUpdate<T> update = cb.createCriteriaUpdate((Class<T>) domainClass);

        Root<T> root = update.from((Class<T>) domainClass);

        update.set(DELETED_FIELD, Timestamp.valueOf(localDateTime));

        update.where(cb.equal(root.<ID>get(entityInformation.getIdAttribute().getName()),
                entityInformation.getId(entity)));

        em.createQuery(update).executeUpdate();
    }

    public long countActive() {
        return super.count(notDeleted());
    }

    @Override
    public boolean existsActive(ID id) {
        Assert.notNull(id, "The entity must not be null!");
        return findOneActive(id) != null ? true : false;
    }

    private static final class ByIdSpecification<T, ID> implements Specification<T> {

        private final JpaEntityInformation<T, ?> entityInformation;
        private final ID id;

        public ByIdSpecification(JpaEntityInformation<T, ?> entityInformation, ID id) {
            this.entityInformation = entityInformation;
            this.id = id;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.equal(root.<ID>get(entityInformation.getIdAttribute().getName()), id);
        }
    }

    @SuppressWarnings("rawtypes")
    private static final class ByIdsSpecification<T> implements Specification<T> {

        private final JpaEntityInformation<T, ?> entityInformation;

        ParameterExpression<Iterable> parameter;

        public ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
            this.entityInformation = entityInformation;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Path<?> path = root.get(entityInformation.getIdAttribute());
            parameter = cb.parameter(Iterable.class);
            return path.in(parameter);
        }
    }

    /*
     * Specification to check if the DELETED_FIELD is null
     */
    private static final class DeletedIsNull<T> implements Specification<T> {
        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.isNull(root.<Timestamp>get(DELETED_FIELD));
        }
    }

    /*
     * Specification to check if the DELETED_FIELD is greather than the current LocalDateTime
     */
    private static final class DeletedTimeGreatherThanNow<T> implements Specification<T> {
        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.greaterThan(root.<Timestamp>get(DELETED_FIELD),
                    Timestamp.valueOf(LocalDateTime.now()));
        }
    }

    /*
     * Combined Specification from DeletedIsNull and DeletedTimeGreatherThanNow to check if the
     * entity is soft deleted or not
     */
    private static final <T> Specification<T> notDeleted() {
        return Specification.where(new DeletedIsNull<T>()).or(new DeletedTimeGreatherThanNow<T>());
    }

    private static final <T> Specification<T> notDeleted(Specification<T> specification) {
        return Specification.where(new DeletedIsNull<T>())
                /* .or(new DeletedTimeGreatherThanNow<T>()) */.and(specification);
    }

    private static final <T> Specification<T> notDeleted(Specification<T> specification,
            Specification<T> specification1) {
        return Specification.where(new DeletedIsNull<T>())
                /* .or(new DeletedTimeGreatherThanNow<T>()) */.and(specification)
                .and(specification1);
    }

    private static final <T> Specification<T> notDeleted(Specification<T> specification,
            Specification<T> specification1, Specification<T> specification2) {
        return Specification.where(new DeletedIsNull<T>())
                /* .or(new DeletedTimeGreatherThanNow<T>()) */.and(specification)
                .and(specification1).and(specification2);
    }

    @Override
    public T saveAndFlushNow(T entity) {
        return super.saveAndFlush(entity);
    }

}
