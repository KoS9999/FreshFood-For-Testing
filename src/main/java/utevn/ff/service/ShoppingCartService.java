package utevn.ff.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import utevn.ff.entities.CartItem;
import utevn.ff.entities.Product;

@Service
public interface ShoppingCartService {

	int getCount();

	double getAmount();

	void clear();

	Collection<CartItem> getCartItems();

	void remove(CartItem item);

	void add(CartItem item);

	void remove(Product product);

	void updateItemQuantity(Long productId, int quantity);

	void update(Long productId, int quantity);

}
