package fr.boursorama.modulith.ecomm.service;

import org.springframework.stereotype.Component;

@Component
public class FakePaimentService implements PaymentService {

    @Override
    public boolean proceedWithPayment(String nom, String numero, String s, String ccv) {
        double failDraw = Math.random();
        if (failDraw >= 0.9) {
            return false;
        } else {
            return true;
        }
    }
}
