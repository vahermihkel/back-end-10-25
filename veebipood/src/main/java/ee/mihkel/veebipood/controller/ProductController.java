package ee.mihkel.veebipood.controller;

import ee.mihkel.veebipood.entity.Product;
import ee.mihkel.veebipood.repository.ProductRepository;
import ee.mihkel.veebipood.service.ProductCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

//@CrossOrigin("http://localhost:5173") // "*" --> kõik clientid pääsevad ligi
@RestController
public class ProductController {

    @Autowired // Dependency Injection
    private ProductRepository productRepository;

    @Autowired
    private ProductCacheService productCacheService;

    // localhost:8080/products?page=0&size=2&sort=price,desc
    @GetMapping("products")
    public Page<Product> getProducts(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    // localhost:8080/products
    @PostMapping("products")
    public List<Product> addProduct(@RequestBody Product product){
        if (product.getId() != null){
            throw new RuntimeException("Cannot add when id present");
        }
        if (product.getName() == null || product.getName().isEmpty()){
            throw new RuntimeException("Name cannot be empty");
        }
        if (product.getPrice() <= 0){
            throw new RuntimeException("Price cannot be negative");
        }
        productRepository.save(product);
        return productRepository.findAll();
    }

    // VARIANT 1:
    // localhost:8080/products?id=2

    // Mitu argumenti, siis võiks kasutada @RequestParamit
    // Kui on 1 või mitu vabatahtlikku argumenti, siis võiks @RequestParam
    @DeleteMapping("products")
    public List<Product> deleteProduct(@RequestParam Long id){
        productRepository.deleteById(id);
        return productRepository.findAll();
    }

    // VARIANT 2:
    // localhost:8080/products/2

    // Kui on täpselt 1 argument ja saan aru millega on tegemist.
    // DELETE localhost:8080/products/2
    @DeleteMapping("products2/{id}")
    public List<Product> deleteProduct2(@PathVariable Long id){
        productRepository.deleteById(id);
        productCacheService.removeProduct(id);
        return productRepository.findAll();
    }

    // localhost:8080/products/3
    @GetMapping("products/{id}")
    public Product getProduct(@PathVariable Long id) throws ExecutionException {
//        return productRepository.findById(id).orElseThrow();
        return productCacheService.getProduct(id);
    }

    // localhost:8080/products
    @PutMapping("products")
    public List<Product> editProduct(@RequestBody Product product){
        if (product.getId() == null || product.getId() <= 0){
            throw new RuntimeException("Cannot edit when id is null or empty");
        }
        if (product.getName() == null || product.getName().isEmpty()){
            throw new RuntimeException("Name cannot be empty");
        }
        if (product.getPrice() <= 0){
            throw new RuntimeException("Price cannot be negative");
        }
        productRepository.save(product);
        productCacheService.putProduct(product);
        return productRepository.findAll();
    }
}

// 2xx ---> edukas päring
// 4xx ---> päringu tegija viga (front-end viga). client error
// 5xx ---> päringu vastuvõtja viga (back-end viga). server error