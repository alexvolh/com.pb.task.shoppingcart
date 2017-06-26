package dao;

import model.Preorder;
import model.PreorderToProduct;
import model.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * PreorderToProductDao
 */
@Stateless
public class PreorderToProductDao {
    @PersistenceContext
    private EntityManager entityManager;

    public void persist(PreorderToProduct preorderToProduct) {
        entityManager.persist(preorderToProduct);
    }

    public List<PreorderToProduct> preorderToProductList() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PreorderToProduct> criteriaQuery = criteriaBuilder.createQuery(PreorderToProduct.class);
        Root<PreorderToProduct> root = criteriaQuery.from(PreorderToProduct.class);
        criteriaQuery.select(root);

        TypedQuery<PreorderToProduct> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}
