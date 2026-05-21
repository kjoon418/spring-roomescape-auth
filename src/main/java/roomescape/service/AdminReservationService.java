package roomescape.service;

import java.util.List;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.domain.EntityId;

public interface AdminReservationService {

    List<ReservationDetailResponse> findAllIncludeDetailByShopId(EntityId shopId);

    void delete(EntityId reservationId);
}
