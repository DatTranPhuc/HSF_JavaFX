package javafx.demojavafxs3.dto;

public class TopStockItem {
    private final String productName;
    private final int stock;
    private final String categoryName;

    public TopStockItem(String productName, int stock, String categoryName) {
        this.productName = productName;
        this.stock = stock;
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public int getStock() {
        return stock;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
