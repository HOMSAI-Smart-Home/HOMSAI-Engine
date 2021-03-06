package app.homsai.engine.common.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by Giacomo Agostini on 26/01/16.
 */

public class CustomJpaRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable>
        extends JpaRepositoryFactoryBean<T, S, ID> {

    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public CustomJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new CustomJpaRepositoryFactory<T, ID>(entityManager);
    }

    private static class CustomJpaRepositoryFactory<T, ID extends Serializable>
            extends JpaRepositoryFactory {

        private final EntityManager entityManager;

        public CustomJpaRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
            this.entityManager = entityManager;
        }

        // @Override
        // @SuppressWarnings("unchecked")
        // protected Object getTargetRepository(RepositoryInformation information) {
        // return new SoftDeletesRepositoryImpl<T, ID>((Class<T>) information.getDomainType(),
        // entityManager);
        // }


        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return SoftDeletesRepositoryImpl.class;
        }
    }
}
