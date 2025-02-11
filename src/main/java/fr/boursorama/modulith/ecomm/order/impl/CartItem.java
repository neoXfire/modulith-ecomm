package fr.boursorama.modulith.ecomm.order.impl;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"CART_ID", "PRODUCT_ID"}))
public class CartItem {


    @Id
    private UUID cartItemId;

    @ManyToOne
    @JoinColumn(name="CART_ID")
    private Cart cart;

    private int quantity;

    private UUID productId;

    public static CartItem of(Cart cart, UUID productId) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProductId(productId);
        return cartItem;
    }

    public UUID getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(UUID cartEntryId) {
        this.cartItemId = cartEntryId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }
}
