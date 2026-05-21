package roomescape.controller.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ReservationTimeCreateRequest;
import roomescape.domain.EntityId;
import roomescape.service.dto.ReservationTimeCreateCommand;

@Component
public class ReservationTimeMapper {

    public ReservationTimeCreateCommand mapToCommand(
            ReservationTimeCreateRequest request,
            EntityId shopId
    ) {
        return new ReservationTimeCreateCommand(
                request.startAt(),
                shopId
        );
    }
}
