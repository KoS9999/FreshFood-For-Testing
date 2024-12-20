package utevn.ff.controller;

import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import utevn.ff.commom.CommomDataService;
import utevn.ff.config.PaypalPaymentIntent;
import utevn.ff.config.PaypalPaymentMethod;
import utevn.ff.entities.CartItem;
import utevn.ff.entities.Order;
import utevn.ff.entities.OrderDetail;
import utevn.ff.entities.Product;
import utevn.ff.entities.User;
import utevn.ff.repository.OrderDetailRepository;
import utevn.ff.repository.OrderRepository;
import utevn.ff.service.PaypalService;
import utevn.ff.service.ShoppingCartService;
import utevn.ff.util.Utils;

@Controller
public class CartController extends CommomController {

	@Autowired
	HttpSession session;

	@Autowired
	CommomDataService commomDataService;

	@Autowired
	ShoppingCartService shoppingCartService;

	@Autowired
	private PaypalService paypalService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	public Order orderFinal = new Order();

	public static final String URL_PAYPAL_SUCCESS = "pay/success";
	public static final String URL_PAYPAL_CANCEL = "pay/cancel";
	private Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping(value = "/shoppingCart_checkout")
	public String shoppingCart(Model model) {
	    Collection<CartItem> cartItems = shoppingCartService.getCartItems();
	    model.addAttribute("cartItems", cartItems);
	    model.addAttribute("total", shoppingCartService.getAmount());
	    
	    double totalPrice = 0;
	    for (CartItem cartItem : cartItems) {
	        double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
	        totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
	    }

	    model.addAttribute("totalPrice", totalPrice);
	    model.addAttribute("totalCartItems", shoppingCartService.getCount());

	    return "web/shoppingCart_checkout";
	}
	// Add Cart Item
	@GetMapping(value = "/addToCart")
	public ResponseEntity<Map<String, Object>> addToCart(@RequestParam("productId") Long productId, HttpSession session) {
	    Product product = productRepository.findById(productId).orElse(null);
	    Map<String, Object> response = new HashMap<>();

	    if (product != null) {
	        CartItem item = new CartItem();
	        BeanUtils.copyProperties(product, item);
	        item.setQuantity(1);
	        item.setProduct(product);
	        item.setId(productId);

	        // Add to shopping cart service
	        shoppingCartService.add(item);

	        // Update session
	        session.setAttribute("cartItems", shoppingCartService.getCartItems());
	        session.setAttribute("totalCartItems", shoppingCartService.getCount());

	        response.put("status", "success");
	        response.put("totalCartItems", shoppingCartService.getCount());
	        response.put("cartItems", shoppingCartService.getCartItems());
	        response.put("totalPrice", shoppingCartService.getAmount());
	    } else {
	        response.put("status", "error");
	        response.put("message", "Product not found");
	    }

	    return ResponseEntity.ok(response);
	}
	@GetMapping(value = "/loadCart")
	public ResponseEntity<Map<String, Object>> loadCart(HttpSession session) {
	    Map<String, Object> response = new HashMap<>();
	    Collection<CartItem> cartItems = (Collection<CartItem>) session.getAttribute("cartItems");
	    double totalPrice = 0;
	    
	    if (cartItems != null) {
	        for (CartItem cartItem : cartItems) {
	            double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
	            totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
	        }
	    }

	    response.put("cartItems", cartItems);
	    response.put("totalPrice", totalPrice);
	    response.put("totalCartItems", (cartItems != null) ? cartItems.size() : 0);

	    return ResponseEntity.ok(response);
	}



	@GetMapping(value = "/getCartItems")
	public ResponseEntity<Map<String, Object>> getCartItems() {
	    Map<String, Object> response = new HashMap<>();
	    Collection<CartItem> cartItems = shoppingCartService.getCartItems();
	    double totalPrice = 0;
	    
	    for (CartItem cartItem : cartItems) {
	        double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
	        totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
	    }

	    response.put("cartItems", cartItems);
	    response.put("totalPrice", totalPrice);
	    response.put("totalCartItems", shoppingCartService.getCount());

	    return ResponseEntity.ok(response);
	}
	@GetMapping("/cartDetails")
	public ResponseEntity<Map<String, Object>> getCartDetails() {
	    Map<String, Object> response = new HashMap<>();
	    Collection<CartItem> cartItems = shoppingCartService.getCartItems();
	    double totalPrice = 0;
	    for (CartItem cartItem : cartItems) {
	        double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
	        totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
	    }
	    response.put("cartItems", cartItems);
	    response.put("totalPrice", totalPrice);
	    response.put("totalCartItems", shoppingCartService.getCount());
	    return ResponseEntity.ok(response);
	}
	@PostMapping("/updateCartItem")
    public ResponseEntity<Map<String, Object>> updateCartItem(@RequestParam Long productId, @RequestParam int quantity) {
        shoppingCartService.update(productId, quantity);
        Map<String, Object> response = new HashMap<>();
        Collection<CartItem> cartItems = shoppingCartService.getCartItems();
        double totalPrice = 0;

        for (CartItem cartItem : cartItems) {
            double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
            totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
        }

        response.put("status", "success");
        response.put("totalCartItems", shoppingCartService.getCount());
        response.put("cartItems", cartItems);
        response.put("totalPrice", totalPrice);

        return ResponseEntity.ok(response);
    }




	// delete cartItem
	@GetMapping(value = "/remove/{id}")
	public ResponseEntity<Map<String, Object>> remove(@PathVariable("id") Long id, HttpServletRequest request) {
	    Product product = productRepository.findById(id).orElse(null);
	    Map<String, Object> response = new HashMap<>();
	    if (product != null) {
	        Collection<CartItem> cartItems = shoppingCartService.getCartItems();
	        CartItem item = new CartItem();
	        BeanUtils.copyProperties(product, item);
	        item.setProduct(product);
	        item.setId(id);
	        
	        shoppingCartService.remove(item);
	        session.setAttribute("cartItems", shoppingCartService.getCartItems());
	        session.setAttribute("totalCartItems", shoppingCartService.getCount());

	        double totalPrice = 0;
	        for (CartItem cartItem : cartItems) {
	            double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
	            totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
	        }

	        response.put("status", "success");
	        response.put("cartItems", shoppingCartService.getCartItems());
	        response.put("totalPrice", totalPrice);
	        response.put("totalCartItems", shoppingCartService.getCount());
	    } else {
	        response.put("status", "error");
	        response.put("message", "Product not found");
	    }
	    return ResponseEntity.ok(response);
	}

	// show check out
	@GetMapping(value = "/checkout")
	public String checkOut(Model model, User user) {

		Order order = new Order();
		model.addAttribute("order", order);

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());
		model.addAttribute("NoOfItems", shoppingCartService.getCount());
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
			totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
		}

		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		commomDataService.commonData(model, user);

		return "web/shoppingCart_checkout";
	}

	// submit checkout
	@PostMapping(value = "/checkout")
	@Transactional
	public String checkedOut(Model model, Order order, HttpServletRequest request, User user)
			throws MessagingException {

		String checkOut = request.getParameter("checkOut");

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();

		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
			totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
		}

		BeanUtils.copyProperties(order, orderFinal);
		if (StringUtils.equals(checkOut, "paypal")) {

			String cancelUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
			String successUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
			try {
				totalPrice = totalPrice / 22;
				Payment payment = paypalService.createPayment(totalPrice, "USD", PaypalPaymentMethod.paypal,
						PaypalPaymentIntent.sale, "payment description", cancelUrl, successUrl);
				for (Links links : payment.getLinks()) {
					if (links.getRel().equals("approval_url")) {
						return "redirect:" + links.getHref();
					}
				}
			} catch (PayPalRESTException e) {
				log.error(e.getMessage());
			}

		}

		session = request.getSession();
		Date date = new Date();
		order.setOrderDate(date);
		order.setStatus(0);
		order.getOrderId();
		order.setAmount(totalPrice);
		order.setUser(user);

		orderRepository.save(order);

		for (CartItem cartItem : cartItems) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setQuantity(cartItem.getQuantity());
			orderDetail.setOrder(order);
			orderDetail.setProduct(cartItem.getProduct());
			double unitPrice = cartItem.getProduct().getPrice();
			orderDetail.setPrice(unitPrice);
			orderDetailRepository.save(orderDetail);
		}

		// sendMail
		commomDataService.sendSimpleEmail(user.getEmail(), "Fresh Food Xác Nhận Đơn hàng", "....", cartItems,
				totalPrice, order);

		shoppingCartService.clear();
		session.removeAttribute("cartItems");
		model.addAttribute("orderId", order.getOrderId());

		return "redirect:/checkout_success";
	}

	// paypal
	@GetMapping(URL_PAYPAL_SUCCESS)
	public String successPay(@RequestParam("" + "" + "") String paymentId, @RequestParam("PayerID") String payerId,
			HttpServletRequest request, User user, Model model) throws MessagingException {
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());

		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
			totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
		}
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());

		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {

				session = request.getSession();
				Date date = new Date();
				orderFinal.setOrderDate(date);
				orderFinal.setStatus(2);
				orderFinal.getOrderId();
				orderFinal.setUser(user);
				orderFinal.setAmount(totalPrice);
				orderRepository.save(orderFinal);

				for (CartItem cartItem : cartItems) {
					OrderDetail orderDetail = new OrderDetail();
					orderDetail.setQuantity(cartItem.getQuantity());
					orderDetail.setOrder(orderFinal);
					orderDetail.setProduct(cartItem.getProduct());
					double unitPrice = cartItem.getProduct().getPrice();
					orderDetail.setPrice(unitPrice);
					orderDetailRepository.save(orderDetail);
				}

				// sendMail
				commomDataService.sendSimpleEmail(user.getEmail(), "Fresh Food Xác Nhận Đơn hàng", "....", cartItems,
						totalPrice, orderFinal);

				shoppingCartService.clear();
				session.removeAttribute("cartItems");
				model.addAttribute("orderId", orderFinal.getOrderId());
				orderFinal = new Order();
				return "redirect:/checkout_paypal_success";
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "redirect:/";
	}

	// done checkout ship cod
	@GetMapping(value = "/checkout_success")
	public String checkoutSuccess(Model model, User user) {
		commomDataService.commonData(model, user);

		return "web/checkout_success";

	}

	// done checkout paypal
	@GetMapping(value = "/checkout_paypal_success")
	public String paypalSuccess(Model model, User user) {
		commomDataService.commonData(model, user);

		return "web/checkout_paypal_success";

	}

}
