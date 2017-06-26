package beans;

import dao.ProductDao;
import model.Product;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductBean
 */
@Named
@SessionScoped
public class ProductBean implements Serializable {
    @EJB
    private ProductDao productDao;

    private List<Product> products;

    @PostConstruct
    private void init() {
        products = new ArrayList<>(productDao.products());
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
