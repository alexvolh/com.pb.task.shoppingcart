package model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.FetchAttribute;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Preorder
 */
@Entity
public class Preorder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "preorder_number", nullable = false)
    private Integer preorderNumber;

    @Column(name = "receipt_date")
    private Timestamp receiptDate;

    @OneToMany(mappedBy = "preorder",cascade= {CascadeType.REFRESH, CascadeType.REMOVE}, fetch=FetchType.EAGER)
    private List<PreorderToProduct> products = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPreorderNumber() {
        return preorderNumber;
    }

    public void setPreorderNumber(Integer preorderNumber) {
        this.preorderNumber = preorderNumber;
    }

    public Timestamp getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Timestamp receiptDate) {
        this.receiptDate = receiptDate;
    }

    public List<PreorderToProduct> getProducts() {
        return products;
    }

    public void setProducts(List<PreorderToProduct> preorders) {
        this.products = preorders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Preorder preorder = (Preorder) o;

        return new EqualsBuilder()
                .append(id, preorder.id)
                .append(preorderNumber, preorder.preorderNumber)
                .append(receiptDate, preorder.receiptDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(preorderNumber)
                .append(receiptDate)
                .toHashCode();
    }
}
