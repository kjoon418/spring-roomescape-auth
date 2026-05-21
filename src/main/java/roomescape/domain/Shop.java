package roomescape.domain;

import java.util.Objects;
import org.springframework.util.StringUtils;
import roomescape.exception.ErrorCode;
import roomescape.exception.InvalidDomainStateException;

public record Shop(
        EntityId id,
        String name
) {

    public Shop {
        validateId(id);
        validateName(name);
    }

    private void validateId(EntityId id) {
        if (id == null) {
            throw new InvalidDomainStateException(
                    ErrorCode.INVALID_SHOP,
                    "매장엔 식별자가 존재해야 합니다."
            );
        }
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new InvalidDomainStateException(
                    ErrorCode.INVALID_SHOP,
                    "매장엔 이름이 존재해야 합니다."
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shop shop = (Shop) o;
        return Objects.equals(id, shop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
