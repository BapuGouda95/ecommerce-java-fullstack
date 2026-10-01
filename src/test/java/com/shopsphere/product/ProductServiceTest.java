package com.shopsphere.product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
 @Mock ProductRepository repository;
 @Test void searchAndCategoryAreAppliedTogether(){
  when(repository.findByNameContainingIgnoreCaseAndCategoryIgnoreCase("phone","Electronics")).thenReturn(List.of(new Product("Phone","Electronics",new BigDecimal("100"),"","",5)));
  ProductService service=new ProductService(repository);
  var result=service.findAll("phone","Electronics");
  assertEquals(1,result.size());
  verify(repository).findByNameContainingIgnoreCaseAndCategoryIgnoreCase("phone","Electronics");
  verifyNoMoreInteractions(repository);
 }
}