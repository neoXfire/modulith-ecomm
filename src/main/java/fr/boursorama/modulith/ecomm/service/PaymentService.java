package fr.boursorama.modulith.ecomm.service;

public interface PaymentService {

    /**
     * Method to be used to attempt to charge de debit/credit card based on its details.
     * @return true if payment has succeeded, false if not
     */
    boolean proceedWithPayment(String nom, String numero, String s, String ccv);
}
