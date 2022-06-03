package app.homsai.engine.common.domain.specifications;

import app.homsai.engine.common.domain.models.BaseEntity;
import app.homsai.engine.common.domain.models.Gender;
import app.homsai.engine.entities.domain.models.Area;
import app.homsai.engine.entities.domain.models.HomsaiEntityType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;

/**
 * Created by Giacomo Agostini on 20/01/2017.
 */

public class BaseEntitySpecifications<T extends BaseEntity> implements Specification<T> {

    private SearchCriteria criteria;

    public BaseEntitySpecifications(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        try {
            if (criteria.getOperation().equalsIgnoreCase(">")) {
                if (root.get(criteria.getKey()).getJavaType() == Instant.class) {
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()),
                            Instant.parse(criteria.getValue().toString() + " 00:00:00"));
                }
                return builder.greaterThanOrEqualTo(root.<String>get(criteria.getKey()),
                        criteria.getValue().toString());
            } else if (criteria.getOperation().equalsIgnoreCase("<")) {
                if (root.get(criteria.getKey()).getJavaType() == Instant.class) {
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()),
                            Instant.parse(criteria.getValue().toString() + " 23:59:59"));
                }
                return builder.lessThanOrEqualTo(root.<String>get(criteria.getKey()),
                        criteria.getValue().toString());
            } else if (criteria.getOperation().equalsIgnoreCase(":")) {
                if (root.get(criteria.getKey()).getJavaType() == String.class) {

                    if (criteria.getValue().equals("null")) {
                        return builder.isNull(root.get(criteria.getKey()));
                    }
                    return builder.like(builder.upper(root.<String>get(criteria.getKey())),
                            "%" + criteria.getValue().toString().toUpperCase() + "%");
                }
                if (root.get(criteria.getKey()).getJavaType() == Boolean.class) {
                    return builder.equal(root.get(criteria.getKey()),
                            Boolean.valueOf(criteria.getValue().toString()));
                }
                if (root.get(criteria.getKey()).getJavaType() == Gender.class) {
                    return builder.equal(root.get(criteria.getKey()),
                            Gender.valueOf(criteria.getValue().toString()));
                }
                if (root.get(criteria.getKey()).getJavaType() == HomsaiEntityType.class) {
                    return builder.equal(root.get(criteria.getKey()).get("name"),
                            criteria.getValue().toString());
                }
                if (root.get(criteria.getKey()).getJavaType() == Area.class) {
                    return builder.equal(root.get(criteria.getKey()).get("name"),
                            criteria.getValue().toString());
                }
                if (root.get(criteria.getKey()).getJavaType() == Instant.class) {
                    if (criteria.getValue().equals("null")) {
                        return builder.isNull(root.get(criteria.getKey()));
                    }
                    return builder.between(root.get(criteria.getKey()),
                            Instant.parse(criteria.getValue().toString() + " 00:00:00"),
                            Instant.parse(criteria.getValue().toString() + " 23:59:59"));
                } else {
                    if (criteria.getValue().equals("null")) {
                        return builder.isNull(root.get(criteria.getKey()));
                    } else {
                        return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                    }
                }
            } else if (criteria.getOperation().equalsIgnoreCase("!:")) {
                if (criteria.getValue().equals("null"))
                    criteria.setValue(null);
                if (criteria.getValue().equals("null")) {
                    return builder.isNotNull(root.get(criteria.getKey()));
                } else {
                    return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
                }

            }
        } catch (Exception invalidDataAccessApiUsageException) {
        }
        return null;
    }
}
