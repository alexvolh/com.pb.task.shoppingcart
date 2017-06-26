package beans;

import dao.PreorderDao;
import dao.PreorderToProductDao;
import dao.ProductDao;
import model.Preorder;
import model.PreorderToProduct;
import model.Product;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PreorderBean
 */
@Named
@SessionScoped
public class PreorderBean implements Serializable {
    @EJB
    private PreorderDao preorderDao;
    @EJB
    private ProductDao productDao;
    @EJB
    private PreorderToProductDao preorderToProductDao;

    private List<Preorder> preorders;
    private List<Preorder> preordersCopy;
    private List<PreorderToProduct> selectedProducts;
    private Preorder selectedPreorder;
    private Product selectedProduct;
    private NumberFormat decimalFormatter;
    private BigDecimal sumOfPreorderedLimit;
    private Integer quantityLimit;
    private boolean isFilterPreorderBySum;
    private Integer countAssignProduct;
    private HashMap<String, Product> selectMenu;
    private List<Product> products;
    private List<Product> selectedProductsForPreorder;
    private Integer numberPreorder;

    @PostConstruct
    private void init() {
        preorders = new ArrayList<>(preorderDao.preorders());
        preordersCopy = new ArrayList<>(preorders);
        decimalFormatter = new DecimalFormat("#000.00");
        if (preorders.size() != 0) {
            selectedPreorder = preorders.get(0);
            setSelectedProducts();
        }
        selectMenu = new HashMap<>();
        products = new ArrayList<>(productDao.products());
        for (Product product : products) {
            selectMenu.put(product.getName(), product);
        }
        sumOfPreorderedLimit = new BigDecimal(0);
        quantityLimit = 1;
    }

    public List<Preorder> getPreorders() {
        return preorders;
    }

    public void setPreorders(List<Preorder> preorders) {
        this.preorders = preorders;
    }

    public Preorder getSelectedPreorder() {
        return selectedPreorder;
    }

    public void setSelectedPreorder(Preorder selectedPreorder) {
        this.selectedPreorder = selectedPreorder;
    }

    public List<PreorderToProduct> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts() {
        this.selectedProducts = selectedPreorder.getProducts();
    }

    public BigDecimal getSumOfPreorderedLimit() {
        return sumOfPreorderedLimit;
    }

    public void setSumOfPreorderedLimit(BigDecimal sumOfPreorderedLimit) {
        this.sumOfPreorderedLimit = sumOfPreorderedLimit;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public Integer getQuantityLimit() {
        return quantityLimit;
    }

    public boolean isFilterPreorderBySum() {
        return isFilterPreorderBySum;
    }

    public void setFilterPreorderBySum(boolean filterPreorderBySum) {
        isFilterPreorderBySum = filterPreorderBySum;
    }

    public void setQuantityLimit(Integer quantityLimit) {
        this.quantityLimit = quantityLimit;
    }

    public HashMap<String, Product> getSelectMenu() {
        return selectMenu;
    }

    public void setSelectMenu(HashMap<String, Product> selectMenu) {
        this.selectMenu = selectMenu;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Integer getCountAssignProduct() {
        return countAssignProduct;
    }

    public void setCountAssignProduct(Integer countAssignProduct) {
        this.countAssignProduct = countAssignProduct;
    }

    public List<Product> getSelectedProductsForPreorder() {
        return selectedProductsForPreorder;
    }

    public void setSelectedProductsForPreorder(List<Product> selectedProductsForPreorder) {
        this.selectedProductsForPreorder = selectedProductsForPreorder;
    }

    public Integer getNumberPreorder() {
        return numberPreorder;
    }

    public void setNumberPreorder(Integer numberPreorder) {
        this.numberPreorder = numberPreorder;
    }

    public BigDecimal getTotalSum() {
        if (selectedProducts.size() != 0) {
            Integer sumOfQuantity = selectedProducts.stream()
                    .map(PreorderToProduct::getQuantity)
                    .mapToInt(value -> value)
                    .sum();
            BigDecimal sumOfPrice = selectedProducts
                    .stream()
                    .map(preorderToProduct -> preorderToProduct.getProduct().getPrice())
                    .reduce(BigDecimal::add)
                    .orElse(new BigDecimal(0));

            return new BigDecimal(sumOfPrice.doubleValue() * sumOfQuantity.doubleValue());
        }
        return new BigDecimal(0);
    }

    public String getDecimalFormatter(BigDecimal formattedDecimal) {
        return decimalFormatter.format(formattedDecimal);
    }

    public void getFilterPreorderBySum() {
        preorders = filterPreorderBySum();
         if (!preorders.isEmpty() & isFilterPreorderBySum) {
             selectedPreorder = preorders.get(0);
             selectedProducts = selectedPreorder.getProducts();
        } else if (!isFilterPreorderBySum){
            preorders = preordersCopy;
            selectedPreorder = preorders.get(0);
            selectedProducts = selectedPreorder.getProducts();
            sumOfPreorderedLimit = BigDecimal.ZERO;
            quantityLimit = 1;
        }
        if (preorders.isEmpty()) {
            selectedProducts = null;
        }
    }

    public void getFilterPreorderByContainsProduct() {
        preorders = filterPreorderByContainsProduct();
        selectedPreorder = preorders.get(0);
        setSelectedProducts();
    }

    private List<Preorder> filterPreorderBySum() {
        return preordersCopy
                .stream()
                .filter(preorder -> preorder
                        .getProducts()
                        .stream()
                        .map(preorderToProduct -> preorderToProduct
                                .getProduct()
                                .getPrice())
                        .reduce(BigDecimal::add)
                        .orElse(new BigDecimal(0))
                        .multiply(new BigDecimal(preorder.getProducts().stream().mapToInt(PreorderToProduct::getQuantity).sum())).doubleValue() <  sumOfPreorderedLimit.doubleValue())
                .filter(preorder -> preorder.getProducts().stream()
                        .mapToInt(PreorderToProduct::getQuantity).sum() == quantityLimit)
                .collect(Collectors.toList());
    }

    private List<Preorder> filterPreorderByContainsProduct() {
        return preordersCopy.stream()
                .filter(preorder -> preorder.getProducts()
                        .stream()
                        .map(PreorderToProduct::getProduct)
                        .filter(product -> product.equals(selectedProduct)).count() > 0)
                .collect(Collectors.toList());
    }

    public void removePreordersWithAssignProduct() {
        List<Preorder> filtered = preordersCopy.stream()
                            .filter(preorder -> preorder.getProducts()
                                    .stream()
                                    .map(PreorderToProduct::getProduct)
                                    .filter(product -> product.equals(selectedProduct)).count() > 0)
                            .filter(preorder -> preorder.getProducts()
                                    .stream()
                                    .map(PreorderToProduct::getQuantity)
                                    .filter(quantity -> quantity.equals(countAssignProduct)).count() > 0)
                            .collect(Collectors.toList());
        if (!filtered.isEmpty()) {
            for (Preorder preorder : filtered) {
                preorderDao.removePreorder(preorder);
            }
        }
        preorders = preorderDao.preorders();
        preordersCopy = preorders;
    }

    public void addPreorder() {
        Preorder preorder = new Preorder();
        preorder.setPreorderNumber(numberPreorder);
        preorder.setReceiptDate(new Timestamp(System.currentTimeMillis()));

        System.out.println("count - 1: "+preorders.stream().map(Preorder::getProducts).count());

        preorderDao.persist(preorder);

        for (Product product : selectedProductsForPreorder) {
            preorderToProductDao.persist(new PreorderToProduct(preorder, product, product.getQuantityProduct()));
        }

        preorders.add(preorder);
        preordersCopy = preorders;
    }
}
