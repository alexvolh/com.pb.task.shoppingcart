package model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

/**
 * PreorderToProduct
 */
@Entity
@Table(name = "preorder_to_product")
public class PreorderToProduct {
    @EmbeddedId
    private EmbeddedIdPreorderToProduct embeddedIdPreorderToProduct;

    @ManyToOne(cascade= {CascadeType.REFRESH,CascadeType.REMOVE}, fetch=FetchType.EAGER)
    @JoinColumn(name = "preorder_id", insertable = false, updatable = false)
    private Preorder preorder;

    @ManyToOne()
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Column(name = "quantity", nullable = true)
    private Integer quantity;

    public PreorderToProduct() {}

    public PreorderToProduct(Preorder preorder, Product product, Integer quantity) {
        this.embeddedIdPreorderToProduct = new EmbeddedIdPreorderToProduct(preorder.getId(), product.getId());
        this.preorder = preorder;
        this.product = product;
        this.quantity = quantity;

        preorder.getProducts().add(this);
        product.getPreorders().add(this);
    }

    public EmbeddedIdPreorderToProduct getEmbeddedIdPreorderToProduct() {
        return embeddedIdPreorderToProduct;
    }

    public void setEmbeddedIdPreorderToProduct(EmbeddedIdPreorderToProduct embeddedIdPreorderToProduct) {
        this.embeddedIdPreorderToProduct = embeddedIdPreorderToProduct;
    }

    public Preorder getPreorder() {
        return preorder;
    }

    public void setPreorder(Preorder preorder) {
        this.preorder = preorder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PreorderToProduct that = (PreorderToProduct) o;

        return new EqualsBuilder()
                .append(quantity, that.quantity)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(quantity)
                .toHashCode();
    }
}
