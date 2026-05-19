package roomescape.service.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.User;

public record AssembledReservation(
        Reservation reservation,
        ReservationTime time,
        Theme theme,
        User user
) {
}
