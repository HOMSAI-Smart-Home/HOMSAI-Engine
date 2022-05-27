package app.homsai.engine.common.domain.specifications;

import app.homsai.engine.common.domain.models.BaseEntity;
import app.homsai.engine.common.domain.models.SystemConsts;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agostini on 26/01/2017.
 */
public class BaseEntitySpecificationsBuilder<T extends BaseEntity> {

    public final static String andDivider = SystemConsts.andDivider;
    public final static String orDivider = SystemConsts.orDivider;

    private final List<SearchCriteria> params;

    public BaseEntitySpecificationsBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public BaseEntitySpecificationsBuilder with(String key, String operation, Object value,
            String concat) {
        params.add(new SearchCriteria(key, operation, value, concat));
        return this;
    }

    public Specification<T> build() {
        if (params.size() == 0) {
            return null;
        }
        List<Specification<T>> specs = new ArrayList<Specification<T>>();
        for (SearchCriteria param : params) {
            specs.add(new BaseEntitySpecifications(param));
        }

        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            if (params.get(i - 1).getConcat().equals(orDivider))
                result = Specification.where(result).or(specs.get(i));
            else
                result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
