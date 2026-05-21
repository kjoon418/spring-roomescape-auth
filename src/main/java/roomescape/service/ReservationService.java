package roomescape.service;

import java.util.List;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.controller.dto.ReservationSummaryResponse;
import roomescape.domain.EntityId;
import roomescape.service.dto.ReservationCreateCommand;
import roomescape.service.dto.ReservationUpdateCommand;

public interface ReservationService {

    ReservationSummaryResponse create(ReservationCreateCommand command);

    List<ReservationDetailResponse> findAllByShopId(EntityId managerId, EntityId shopId);

    List<ReservationDetailResponse> findAllByUserIdAndShopId(EntityId userId, EntityId shopId);

    ReservationSummaryResponse update(ReservationUpdateCommand command);

    void delete(EntityId managerId, EntityId shopId, EntityId reservationId);

    ReservationSummaryResponse cancel(EntityId shopId, EntityId reservationId);
}
