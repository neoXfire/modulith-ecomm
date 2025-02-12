package fr.boursorama.modulith.ecomm.order.impl;

import org.springframework.stereotype.Component;

@Component
public class FakePaimentService implements PaymentService {

    @Override
    public boolean proceedWithPayment(double totalPrice, String nom, String numero, String s, String ccv) {
        double failDraw = Math.random();
        if (failDraw >= 0.9) {
            return false;
        } else {
            return true;
        }
    }
}
