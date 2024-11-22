package utevn.ff.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import utevn.ff.entities.CartItem;
import utevn.ff.entities.Product;
import utevn.ff.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private Map<Long, CartItem> map = new HashMap<Long, CartItem>(); // <Long, CartItem>

    @Override
    public void add(CartItem item) {
        CartItem existedItem = map.get(item.getId());

        if (existedItem != null) {
            existedItem.setQuantity(item.getQuantity() + existedItem.getQuantity());
            existedItem.setTotalPrice(item.getTotalPrice() + existedItem.getUnitPrice() * existedItem.getQuantity());
        } else {
            map.put(item.getId(), item);
        }
    }

    @Override
    public void remove(CartItem item) {
        map.remove(item.getId());
    }

    @Override
    public void updateItemQuantity(Long productId, int quantity) {
        CartItem item = map.get(productId);
        if (item != null) {
            item.setQuantity(quantity);
            item.setTotalPrice(item.getUnitPrice() * quantity);
        }
    }

    @Override
    public Collection<CartItem> getCartItems() {
        return map.values();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public double getAmount() {
        return map.values().stream().mapToDouble(item -> item.getQuantity() * item.getUnitPrice()).sum();
    }

    @Override
    public int getCount() {
        if (map.isEmpty()) {
            return 0;
        }
        return map.values().size();
    }

    @Override
    public void remove(Product product) {
        map.values().removeIf(item -> item.getProduct().equals(product));
    }

    @Override
    public void update(Long productId, int quantity) {
        CartItem item = map.get(productId);
        if (item != null) {
            item.setQuantity(quantity);
            item.setTotalPrice(item.getUnitPrice() * quantity);
            if (quantity <= 0) {
                map.remove(productId);
            }
        }
    }
}
