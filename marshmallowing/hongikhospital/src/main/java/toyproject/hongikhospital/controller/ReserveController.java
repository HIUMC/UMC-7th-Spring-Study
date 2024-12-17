package toyproject.hongikhospital.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import toyproject.hongikhospital.domain.Reservation;
import toyproject.hongikhospital.service.ReserveService;

@Controller
@RequiredArgsConstructor
public class ReserveController {
    private final ReserveService reserveService;

    // 예약 생성 폼
    @GetMapping("/reservations/new")
    public String newReservationForm(Model model) {
        model.addAttribute("reservationForm", new ReservationForm());
        return "reservations/new";  // 예약 폼 페이지로 이동
    }

    // 예약 생성
    @PostMapping("/reservations/new")
    public String createReservation(@Valid ReservationForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "reservations/new";
        }

        Reservation reservation=new Reservation();
        reservation.setPrice(form.getPrice());
        reservation.setTime(form);
    }
}
