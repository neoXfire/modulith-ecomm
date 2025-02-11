package fr.boursorama.modulith.ecomm.order;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="PURCHASE_ORDER")
public class Order {

    @Id
    private UUID orderId;

    @OneToOne(optional = false)
    @JoinColumn(name="CART_ID")
    private Cart cart;

    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private Instant createdOn;

    @Column(nullable = false)
    private Instant cancelledOn;

    private Instant cartConfirmedOn;

    private Instant paymentConfirmedOn;

    private Instant shippedOn;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getCartConfirmedOn() {
        return cartConfirmedOn;
    }

    public void setCartConfirmedOn(Instant cartCompletedOn) {
        this.cartConfirmedOn = cartCompletedOn;
    }

    public Instant getPaymentConfirmedOn() {
        return paymentConfirmedOn;
    }

    public void setPaymentConfirmedOn(Instant paidOn) {
        this.paymentConfirmedOn = paidOn;
    }

    public Instant getShippedOn() {
        return shippedOn;
    }

    public void setShippedOn(Instant shippedOn) {
        this.shippedOn = shippedOn;
    }

    public Instant getCancelledOn() {
        return cancelledOn;
    }

    public void setCancelledOn(Instant cancelledOn) {
        this.cancelledOn = cancelledOn;
    }
}
