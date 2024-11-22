package utevn.ff.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import utevn.ff.commom.CommomDataService;
import utevn.ff.entities.Favorite;
import utevn.ff.entities.Product;
import utevn.ff.entities.User;
import utevn.ff.repository.FavoriteRepository;
import utevn.ff.repository.ProductRepository;

@Controller
public class HomeController extends CommomController {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CommomDataService commomDataService;
	
	@Autowired
	FavoriteRepository favoriteRepository;

	@GetMapping(value = "/")
	public String home(Model model, User user) {

		commomDataService.commonData(model, user);
		bestSaleProduct20(model, user);
		bestSaleProduct10(model, user);
		return "web/home";
	}
	
	// list product ở trang chủ limit 10 sản phẩm mới nhất
	@ModelAttribute("listProduct10")
	public List<Product> listproduct10(Model model) {
		List<Product> productList = productRepository.listProductNew20();
		model.addAttribute("productList", productList);
		return productList;
	}
	
	// Top 20 best sale.
	public void bestSaleProduct20(Model model, User customer) {
		List<Object[]> productList = productRepository.bestSaleProduct20();
		if (productList != null) {
			ArrayList<Integer> listIdProductArrayList = new ArrayList<>();
			for (int i = 0; i < productList.size(); i++) {
				String id = String.valueOf(productList.get(i)[0]);
				listIdProductArrayList.add(Integer.valueOf(id));
			}
			List<Product> listProducts = productRepository.findByInventoryIds(listIdProductArrayList);

			List<Product> listProductNew = new ArrayList<>();

			for (Product product : listProducts) {

				Product productEntity = new Product();

				BeanUtils.copyProperties(product, productEntity);

				Favorite save = favoriteRepository.selectSaves(productEntity.getProductId(), customer.getUserId());

				if (save != null) {
					productEntity.favorite = true;
				} else {
					productEntity.favorite = false;
				}
				listProductNew.add(productEntity);

			}

			model.addAttribute("bestSaleProduct20", listProductNew);
		}
	}
	// Top 10 best sale.
    public void bestSaleProduct10(Model model, User customer) {
        List<Object[]> productList = productRepository.bestSaleProduct10();
        if (productList != null) {
            ArrayList<Integer> listIdProductArrayList = new ArrayList<>();
            for (int i = 0; i < productList.size(); i++) {
                String id = String.valueOf(productList.get(i)[0]);
                listIdProductArrayList.add(Integer.valueOf(id));
            }
            List<Product> listProducts = productRepository.findByInventoryIds(listIdProductArrayList);

            List<Product> listProductNew = new ArrayList<>();

            for (Product product : listProducts) {
                Product productEntity = new Product();
                BeanUtils.copyProperties(product, productEntity);
                Favorite save = favoriteRepository.selectSaves(productEntity.getProductId(), customer.getUserId());
                if (save != null) {
                    productEntity.favorite = true;
                } else {
                    productEntity.favorite = false;
                }
                listProductNew.add(productEntity);
            }
            model.addAttribute("bestSaleProduct10", listProductNew);
        }
    }


}
