package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.strategy.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        DiscountContext context = new DiscountContext();

        for (Product p : products) {
            if (p.getDiscountType() != null) {
                switch (p.getDiscountType()) {
                    case "MEMBER":
                        context.setStrategy(new MemberDiscountStrategy());
                        break;
                    case "SEASONAL":
                        context.setStrategy(new SeasonalSaleStrategy());
                        break;
                    default:
                        context.setStrategy(new NoDiscountStrategy());
                }
                
                double calculatedDiscount = context.executeDiscount(p.getPrice());
                
                p.setDiscountedPrice(calculatedDiscount);
            } else {
                p.setDiscountedPrice(p.getPrice());
            }
        }
        return products;
    }

    public void saveProduct(Product product) {
        if (product.getDetail() != null) {
            product.getDetail().setProduct(product);
        }
        if (product.getReviews() != null) {
            product.getReviews().forEach(review -> review.setProduct(product));
        }
        productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}