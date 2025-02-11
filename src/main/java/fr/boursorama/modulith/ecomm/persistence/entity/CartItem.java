package fr.boursorama.modulith.ecomm.persistence.entity;

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

    @ManyToOne
    @JoinColumn(name="PRODUCT_ID")
    private Product product;

    public static CartItem of(Cart cart, Product product) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
