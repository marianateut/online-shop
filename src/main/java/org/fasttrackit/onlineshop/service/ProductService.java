package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persisteance.ProductRepository;
import org.fasttrackit.onlineshop.transfer.GetProductsRequest;
import org.fasttrackit.onlineshop.transfer.SaveProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private  static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    //IoC -inversion of Control
    private final ProductRepository productRepository;

    //dependency injection
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(SaveProductRequest request) {

        LOGGER.info("Creating product {} {}",request);
        Product product = new Product();
        product.setDescription(request.getDescription());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());

        return productRepository.save(product);

    }
    public Product getProduct(long id){
        LOGGER.info("Retrieving product {}", id);
        //using Optional
       return productRepository.findById(id)
               //lambda expression
               .orElseThrow(() -> new ResourceNotFoundException("Product " + id +"does not exist."));
    }
    public Page<Product> getProducts(GetProductsRequest request, Pageable pageable){
        LOGGER.info("Retrieving products:{}", request);
        if (request !=null && request.getPartialName() !=null && request.getMinQuantity() != null){
            return productRepository.findByNameContainingAndQuantityGreaterThanEqual(request.getPartialName(),
                    request.getMinQuantity(), pageable);

        }else  if (request !=null && request.getPartialName() !=null ){
            return productRepository.findByNameContaining(request.getPartialName(), pageable);
        }else
        {
            return productRepository.findAll(pageable);
        }

    }
    public Product updateProduct(long id ,SaveProductRequest request){
        LOGGER.info("Updating product {}: {}", id, request);

        Product product = getProduct(id);

        BeanUtils.copyProperties(request, product);
        return productRepository.save(product);
    }

    public void deleteProduct(long id){
        LOGGER.info("Deleting product {}", id);
        productRepository.deleteById(id);
        LOGGER.info("Deleting product{}", id);
    }
}
