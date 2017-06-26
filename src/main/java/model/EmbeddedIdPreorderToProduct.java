package model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Embedded Id
 */
@Embeddable
public class EmbeddedIdPreorderToProduct implements Serializable {
    @Column(name = "preorder_id")
    protected Integer preorderId;

    @Column(name = "product_id")
    protected Integer productId;

    public EmbeddedIdPreorderToProduct() {}

    public EmbeddedIdPreorderToProduct(Integer preorderId, Integer productId) {
        this.preorderId = preorderId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EmbeddedIdPreorderToProduct that = (EmbeddedIdPreorderToProduct) o;

        return new EqualsBuilder()
                .append(preorderId, that.preorderId)
                .append(productId, that.productId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(preorderId)
                .append(productId)
                .toHashCode();
    }
}
