package dao;

import model.Preorder;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * PreorderDao
 */
@Stateless
public class PreorderDao {
    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Preorder preorder) {
        entityManager.persist(preorder);
    }

    public Preorder getPreorder(Integer id) {
        Preorder preorder;
        preorder = entityManager.find(Preorder.class, id);
        return preorder;
    }

    public void removePreorder(Preorder preorder) {
        Preorder detachPreorder = getPreorder(preorder.getId());
        entityManager.remove(detachPreorder);
    }

    public List<Preorder> preorders() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Preorder> criteriaQuery = criteriaBuilder.createQuery(Preorder.class);
        Root<Preorder> root = criteriaQuery.from(Preorder.class);
        criteriaQuery.select(root);

        TypedQuery<Preorder> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}
