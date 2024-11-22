package utevn.ff.controller.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import utevn.ff.entities.Category;
import utevn.ff.entities.Product;
import utevn.ff.entities.User;
import utevn.ff.repository.CategoryRepository;
import utevn.ff.repository.ProductRepository;
import utevn.ff.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class ProductController {

    @Value("${upload.path}")
    private String pathUploadImage;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @ModelAttribute(value = "user")
    public User user(Model model, Principal principal, User user) {
        if (principal != null) {
            model.addAttribute("user", new User());
            user = userRepository.findByEmail(principal.getName());
            model.addAttribute("user", user);
        }
        return user;
    }

    public ProductController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @ModelAttribute("products")
    public List<Product> showProduct(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return products;
    }

    @GetMapping(value = "/products")
    public String products(Model model, Principal principal) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "admin/products";
    }

    @PostMapping(value = "/addProduct")
    public String addProduct(@ModelAttribute("product") Product product, ModelMap model,
                             @RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {

        try {
            File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        product.setProductImage(file.getOriginalFilename());
        Product p = productRepository.save(product);
        if (null != p) {
            model.addAttribute("message", "Update success");
            model.addAttribute("product", product);
        } else {
            model.addAttribute("message", "Update failure");
            model.addAttribute("product", product);
        }
        return "redirect:/admin/products";
    }

    @ModelAttribute("categoryList")
    public List<Category> showCategory(Model model) {
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categoryList", categoryList);
        return categoryList;
    }

    @GetMapping(value = "/editProduct/{id}")
    public String editCategory(@PathVariable("id") Long id, ModelMap model) {
        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product);
        return "admin/editProduct";
    }

    @GetMapping("/deleteProduct/{id}")
    public String delProduct(@PathVariable("id") Long id, Model model) {
        productRepository.deleteById(id);
        model.addAttribute("message", "Delete successful!");
        return "redirect:/admin/products";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }

    @PostMapping(value = "/updateProduct")
    public String updateProduct(@ModelAttribute("product") Product product, ModelMap model,
                                @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest httpServletRequest) {

        Product existingProduct = productRepository.findById(product.getProductId()).orElse(null);
        if (existingProduct != null) {
            if (file != null && !file.isEmpty()) {
                try {
                    // Lưu file mới được tải lên
                    File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
                    FileOutputStream fos = new FileOutputStream(convFile);
                    fos.write(file.getBytes());
                    fos.close();
                    existingProduct.setProductImage(file.getOriginalFilename());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            existingProduct.setProductName(product.getProductName());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setQuantity(product.getQuantity());
            existingProduct.setDiscount(product.getDiscount());
            existingProduct.setEnteredDate(product.getEnteredDate());
            existingProduct.setDescription(product.getDescription());

            productRepository.save(existingProduct);
            model.addAttribute("message", "Update success");
            model.addAttribute("product", existingProduct);
        } else {
            model.addAttribute("message", "Update failure");
            model.addAttribute("product", product);
        }
        return "redirect:/admin/products";
    }
}
