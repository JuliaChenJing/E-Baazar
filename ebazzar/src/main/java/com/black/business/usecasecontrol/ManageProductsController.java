
package com.black.business.usecasecontrol;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import com.black.business.externalinterfaces.Catalog;
import com.black.business.productsubsystem.ProductImpl;
import com.black.business.productsubsystem.ProductSubsystemFacade;
import com.black.middleware.exceptions.DatabaseException;

import com.black.business.exceptions.BackendException;
import com.black.business.externalinterfaces.Product;
import com.black.business.externalinterfaces.ProductSubsystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public enum ManageProductsController   {
    private static final Logger LOG = Logger.getLogger(ManageProductsController.class.getName());


    @Autowired
    ProductSubsystem productSubsystem;

    public List<Catalog> getCatalogsList() throws BackendException {
        return productSubsystem.getCatalogList();
    }


    public List<Product> getProductsList(String catalogName) throws BackendException {
        Catalog catalog = productSubsystem.getCatalogFromName(catalogName);
        return productSubsystem.getProductList(catalog);
    }

    public List<Product> getProductsList(Catalog catalog) throws BackendException {
        return productSubsystem.getProductList(catalog);
    }

    public Product saveNewProduct(Catalog catalog, String productName, String quantityAvail, String unitPrice,
                                  String mfgDate, String desc) throws BackendException {
        Product product = new ProductImpl(catalog, productName, mfgDate, quantityAvail, unitPrice, desc);
        int productId = productSubsystem.saveNewProduct(product);
        product.setProductId(productId);
        return product;
    }

    public int saveNewCatalog(String catName) throws BackendException {
        return productSubsystem.saveNewCatalog(catName);
    }

    public void updateCatalogs(List<Catalog> catalogs) throws BackendException {
        for(Catalog catalog : catalogs){
            productSubsystem.updateCatalog(catalog);
        }
    }

    public void deleteCatalog(Catalog catalog) throws BackendException{
        productSubsystem.deleteCatalog(catalog);
    }

    public void updateProducts(List<Product> products) throws BackendException {
        for(Product product : products){
            productSubsystem.updateProduct(product);
        }
    }

    public void updateProduct(Product product) throws BackendException {
        productSubsystem.updateProduct(product);
    }

    public Product getProductFromId(Integer id) throws BackendException {
        return productSubsystem.getProductFromId(id);
    }

    public void deleteProduct(Product product) throws BackendException{
        productSubsystem.deleteProduct(product);
    }

    public int saveNewProduct(Catalog c, String name, LocalDate date, int numAvail, double price) throws BackendException{
        return productSubsystem.saveNewProduct(ProductSubsystemFacade.createProduct(c, name, date, numAvail, price));
    }



}
