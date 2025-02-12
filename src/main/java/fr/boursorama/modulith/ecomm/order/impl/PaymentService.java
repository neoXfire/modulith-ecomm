package fr.boursorama.modulith.ecomm.order.impl;

public interface PaymentService {

    /**
     * Method to be used to attempt to charge de debit/credit card based on its details.
     * @return true if payment has succeeded, false if not
     */
    boolean proceedWithPayment(double totalPrice, String nom, String numero, String s, String ccv);
}
