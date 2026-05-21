package roomescape.controller.mapper;

import org.springframework.stereotype.Component;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationUpdateRequest;
import roomescape.domain.EntityId;
import roomescape.service.dto.ReservationCreateCommand;
import roomescape.service.dto.ReservationUpdateCommand;

@Component
public class ReservationMapper {

    public ReservationCreateCommand mapToCreateCommand(
            ReservationCreateRequest request,
            EntityId userId
    ) {
        return new ReservationCreateCommand(
                request.date(),
                EntityId.fromUuid(request.timeId()),
                EntityId.fromUuid(request.themeId()),
                userId,
                EntityId.fromUuid(request.shopId())
        );
    }

    public ReservationUpdateCommand mapToUpdateCommand(
            EntityId reservationId,
            ReservationUpdateRequest request
    ) {
        return new ReservationUpdateCommand(
                reservationId,
                request.date(),
                EntityId.fromUuid(request.timeId())
        );
    }
}
