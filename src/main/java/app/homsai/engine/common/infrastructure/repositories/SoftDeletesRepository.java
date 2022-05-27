package app.homsai.engine.common.infrastructure.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@NoRepositoryBean
public interface SoftDeletesRepository<T, ID extends Serializable>
        extends PagingAndSortingRepository<T, ID>, JpaSpecificationExecutor<T> {

    Iterable<T> findAllActive();

    Iterable<T> findAllActive(Sort sort);


    Page<T> findAllActive(Pageable pageable);

    Page<T> findAllActive(Pageable pageable, String search);

    Page<T> findAllActive(Pageable pageable, Specification<T> specification);

    Page<T> findAllActive(Pageable pageable, String search, Specification<T> specification);

    List<T> findAllActiveList(Pageable pageable, String search, Specification<T> specification);

    Page<T> findAllActive(Pageable pageable, String search, Specification<T> specification,
            Specification<T> specification1);


    Iterable<T> findAllActive(Iterable<ID> ids);

    T findOneActive(ID id);

    T findOneActive(ID id, String search);

    @Modifying
    void softDelete(ID id);

    @Modifying
    void softDelete(T entity);

    @Modifying
    void softDelete(Iterable<? extends T> entities);

    @Modifying
    void softDeleteAll();

    @Modifying
    void scheduleSoftDelete(ID id, LocalDateTime localDateTime);

    @Modifying
    void scheduleSoftDelete(T entity, LocalDateTime localDateTime);

    long countActive();

    boolean existsActive(ID id);

}
