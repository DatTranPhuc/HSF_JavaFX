package javafx.demojavafxs3.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.demojavafxs3.dto.TopStockItem;
import javafx.demojavafxs3.entity.SonyAccount;
import javafx.demojavafxs3.entity.SonyCategory;
import javafx.demojavafxs3.entity.SonyProduct;
import javafx.demojavafxs3.service.CategoryService;
import javafx.demojavafxs3.service.ProductService;
import javafx.demojavafxs3.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SonyManagementController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SessionManager sessionManager;
    private final ApplicationContext applicationContext;

    @FXML
    private TableView<SonyProduct> productTable;
    @FXML
    private TableColumn<SonyProduct, Long> colId;
    @FXML
    private TableColumn<SonyProduct, String> colName;
    @FXML
    private TableColumn<SonyProduct, Integer> colPrice;
    @FXML
    private TableColumn<SonyProduct, Integer> colStock;
    @FXML
    private TableColumn<SonyProduct, String> colCreatedAt;
    @FXML
    private TableColumn<SonyProduct, String> colCategory;

    @FXML
    private TextField txtProductName;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtStock;
    @FXML
    private ComboBox<SonyCategory> cboCategory;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    @FXML
    private Label statusLabel;
    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<TopStockItem> topProductsTable;
    @FXML
    private TableColumn<TopStockItem, String> colTopProductName;
    @FXML
    private TableColumn<TopStockItem, Integer> colTopStock;
    @FXML
    private TableColumn<TopStockItem, String> colTopCategory;

    private final ObservableList<SonyProduct> productData = FXCollections.observableArrayList();
    private final ObservableList<TopStockItem> topStockItems = FXCollections.observableArrayList();

    public SonyManagementController(ProductService productService,
                                    CategoryService categoryService,
                                    SessionManager sessionManager,
                                    ApplicationContext applicationContext) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.sessionManager = sessionManager;
        this.applicationContext = applicationContext;
    }

    @FXML
    public void initialize() {
        configureProductTable();
        configureTopProductsTable();
        loadUserInformation();
        loadCategories();
        loadProducts();
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
        refreshTopProducts();
    }

    private void configureProductTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colCreatedAt.setCellValueFactory(cell -> {
            SonyProduct product = cell.getValue();
            String value = product.getCreatedAt() == null ? "" : DATE_TIME_FORMATTER.format(product.getCreatedAt());
            return new ReadOnlyStringWrapper(value);
        });
        colCategory.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getCategory().getCateName()));

        setWhiteTextCellFactory(colId);
        setWhiteTextCellFactory(colName);
        setWhiteTextCellFactory(colPrice);
        setWhiteTextCellFactory(colStock);
        setWhiteTextCellFactory(colCreatedAt);
        setWhiteTextCellFactory(colCategory);

        productTable.setItems(productData);
    }

    private <S, T> void setWhiteTextCellFactory(TableColumn<S, T> column) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
                setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            }
        });
    }

    private void configureTopProductsTable() {
        colTopProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colTopStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colTopCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        setWhiteTextCellFactory(colTopProductName);
        setWhiteTextCellFactory(colTopStock);
        setWhiteTextCellFactory(colTopCategory);

        topProductsTable.setItems(topStockItems);
    }

    private void loadUserInformation() {
        Optional<SonyAccount> accountOptional = sessionManager.getCurrentAccount();
        String roleDisplay = accountOptional.map(account -> account.getRole().name()).orElse("GUEST");
        String phone = accountOptional.map(SonyAccount::getPhone).orElse("-");
        welcomeLabel.setText(String.format("Welcome %s (%s)", roleDisplay, phone));

        boolean isAdmin = sessionManager.isAdmin();
        btnAdd.setDisable(!isAdmin);
        btnUpdate.setDisable(!isAdmin);
        btnDelete.setDisable(!isAdmin);
        txtProductName.setDisable(!isAdmin);
        txtPrice.setDisable(!isAdmin);
        txtStock.setDisable(!isAdmin);
        cboCategory.setDisable(!isAdmin);
    }

    private void loadCategories() {
        List<SonyCategory> categories = categoryService.findAll();
        cboCategory.setItems(FXCollections.observableArrayList(categories));
    }

    private void loadProducts() {
        productData.setAll(productService.findAllOrdered());
    }

    private void populateForm(SonyProduct product) {
        txtProductName.setText(product.getProductName());
        txtPrice.setText(String.valueOf(product.getPrice()));
        txtStock.setText(String.valueOf(product.getStock()));
        cboCategory.getSelectionModel().select(product.getCategory());
    }

    @FXML
    public void onAddProduct() {
        if (!sessionManager.isAdmin()) {
            statusLabel.setText("You do not have permission to access this function!");
            return;
        }

        Optional<String> validationError = validateInput();
        if (validationError.isPresent()) {
            statusLabel.setText(validationError.get());
            return;
        }

        SonyProduct product = new SonyProduct();
        product.setProductName(txtProductName.getText().trim());
        product.setPrice(Integer.parseInt(txtPrice.getText().trim()));
        product.setStock(Integer.parseInt(txtStock.getText().trim()));
        product.setCategory(cboCategory.getValue());

        SonyProduct savedProduct = productService.save(product);
        productData.add(0, savedProduct);
        productTable.getSelectionModel().select(savedProduct);
        statusLabel.setText("Product added successfully.");
        refreshTopProducts();
        clearForm();
    }

    @FXML
    public void onUpdateProduct() {
        if (!sessionManager.isAdmin()) {
            statusLabel.setText("You do not have permission to access this function!");
            return;
        }

        SonyProduct selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            statusLabel.setText("Please select a product to update.");
            return;
        }

        Optional<String> validationError = validateInput();
        if (validationError.isPresent()) {
            statusLabel.setText(validationError.get());
            return;
        }

        selectedProduct.setProductName(txtProductName.getText().trim());
        selectedProduct.setPrice(Integer.parseInt(txtPrice.getText().trim()));
        selectedProduct.setStock(Integer.parseInt(txtStock.getText().trim()));
        selectedProduct.setCategory(cboCategory.getValue());

        SonyProduct updatedProduct = productService.update(selectedProduct);
        int index = productData.indexOf(selectedProduct);
        productData.set(index, updatedProduct);
        productTable.getSelectionModel().select(updatedProduct);
        statusLabel.setText("Product updated successfully.");
        refreshTopProducts();
        clearForm();
    }

    @FXML
    public void onDeleteProduct() {
        if (!sessionManager.isAdmin()) {
            statusLabel.setText("You do not have permission to access this function!");
            return;
        }

        SonyProduct selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            statusLabel.setText("Please select a product to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText("Delete Product");
        confirmation.setContentText("Are you sure you want to delete " + selectedProduct.getProductName() + "?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            productService.deleteById(selectedProduct.getProductId());
            productData.remove(selectedProduct);
            statusLabel.setText("Product deleted successfully.");
            refreshTopProducts();
            clearForm();
        }
    }

    @FXML
    public void onClearForm() {
        clearForm();
        statusLabel.setText("");
    }

    private void clearForm() {
        txtProductName.clear();
        txtPrice.clear();
        txtStock.clear();
        cboCategory.getSelectionModel().clearSelection();
        productTable.getSelectionModel().clearSelection();
    }

    private Optional<String> validateInput() {
        String name = txtProductName.getText() == null ? "" : txtProductName.getText().trim();
        String priceText = txtPrice.getText() == null ? "" : txtPrice.getText().trim();
        String stockText = txtStock.getText() == null ? "" : txtStock.getText().trim();
        SonyCategory category = cboCategory.getValue();

        if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty() || category == null) {
            return Optional.of("All fields are required.");
        }

        if (name.length() < 5 || name.length() > 50) {
            return Optional.of("Product name must be between 5 and 50 characters.");
        }

        int price;
        int stock;
        try {
            price = Integer.parseInt(priceText);
        } catch (NumberFormatException ex) {
            return Optional.of("Price must be a number.");
        }

        try {
            stock = Integer.parseInt(stockText);
        } catch (NumberFormatException ex) {
            return Optional.of("Stock must be a number.");
        }

        if (price < 100) {
            return Optional.of("Price must be greater than or equal to 100.");
        }

        if (stock < 0 || stock > 1000) {
            return Optional.of("Stock must be between 0 and 1000.");
        }

        return Optional.empty();
    }

    private void refreshTopProducts() {
        Map<String, List<SonyProduct>> topProductsByCategory = productService.findTopProductsByStockPerCategory();
        List<TopStockItem> items = new ArrayList<>();
        topProductsByCategory.forEach((category, products) ->
                products.forEach(product -> items.add(new TopStockItem(
                        product.getProductName(),
                        product.getStock(),
                        category))));
        topStockItems.setAll(items);
    }

    @FXML
    public void onLogout() {
        sessionManager.logout();
        Stage stage = (Stage) productTable.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/demojavafxs3/hello-view.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Scene scene = new Scene(loader.load(), 960, 640);
            stage.setTitle("StudentName - Login Page");
            stage.setScene(scene);
        } catch (IOException e) {
            statusLabel.setText("Unable to load login page.");
        }
    }
}
