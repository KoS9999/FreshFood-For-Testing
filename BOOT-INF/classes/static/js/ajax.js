$(document).ready(function() {
	loadCartFromServer();

	function loadCartFromServer() {
		$.ajax({
			url: '/loadCart',
			type: 'GET',
			success: function(response) {
				updateCartHeader(response.totalCartItems);
				updateCartSidebar(response.cartItems, response.totalPrice);
			},
			error: function(error) {
				console.log(error);
				alert('An error occurred. Please try again.');
			}
		});
	}

	$('.product-add1').on('click', function(event) {
		event.preventDefault();
		var productId = $(this).data('product-id');

		$.ajax({
			url: '/addToCart',
			type: 'GET',
			data: { productId: productId },
			success: function(response) {
				if (response.status === 'success') {
					updateCartHeader(response.totalCartItems);
					updateCartSidebar(response.cartItems, response.totalPrice);
					openCartSidebar(); // Mở giỏ hàng sau khi cập nhật
				} else {
					alert(response.message);
				}
			},
			error: function(error) {
				console.log(error);
				alert('An error occurred. Please try again.');
			}
		});
	});

	function updateCartHeader(totalCartItems) {
		$('#totalCartItems').text(totalCartItems);
	}

	function updateCartSidebar(cartItems, totalPrice) {
		if (cartItems.length === 0) {
			$('.cart-list').html('<li>Hiện tại bạn chưa có sản phẩm nào trong giỏ hàng!</li>');
			$('.cart-footer').hide();
		} else {
			var cartHtml = '';
			$.each(cartItems, function(index, item) {
				var unitPrice = (item.product.price - (item.product.price * item.product.discount / 100)).toFixed(0);
				var totalPriceForItem = (unitPrice * item.quantity).toFixed(0);

				cartHtml += '<li class="cart-item">' +
					'<div class="cart-media">' +
					'<a href="/productDetail?id=' + item.product.productId + '">' +
					'<img src="/loadImage?imageName=' + item.product.productImage + '" alt="product" />' +
					'</a>' +
					'</div>' +
					'<div class="cart-info-group">' +
					'<div class="cart-info">' +
					'<h6><label>Tên sản phẩm :</label>' +
					'<a href="/productDetail?id=' + item.product.productId + '" style="color: #119744">' + item.product.productName + '</a>' +
					'</h6>' +
					'<label>Đơn giá :</label>' +
					'<p>' + parseInt(unitPrice).toLocaleString('vi-VN') + ' đ</p>' +
					'</div>' +
					'<div class="cart-action-group">' +
					'<div class="product-action">' +
					'<label>Số lượng :</label>' +
					'<input class="action-input quantity-input" title="Quantity Number" type="number" name="quantity" value="' + item.quantity + '" data-product-id="' + item.product.productId + '" />' +
					'</div>' +
					'<h6 class="total-price">' + parseInt(totalPriceForItem).toLocaleString('vi-VN') + ' đ</h6>' +
					'</div>' +
					'</div>' +
					'</li>';
			});
			$('.cart-list').html(cartHtml);
			$('.cart-footer').show();

			// Thêm sự kiện lắng nghe thay đổi cho các ô nhập số lượng
			$('.quantity-input').on('change', function() {
				var newQuantity = $(this).val();
				var productId = $(this).data('product-id');
				updateCartItem(productId, newQuantity);
			});
		}
		$('#totalCartItemsSidebar').text(cartItems.length);
		$('.checkout-label').text('Thanh Toán');
	}


	function updateCartItem(productId, newQuantity) {
		$.ajax({
			url: '/updateCartItem',
			type: 'POST',
			data: { productId: productId, quantity: newQuantity },
			success: function(response) {
				if (response.status === 'success') {
					updateCartHeader(response.totalCartItems);
					updateCartSidebar(response.cartItems, response.totalPrice);
				} else {
					alert(response.message);
				}
			},
			error: function(error) {
				console.log(error);
				alert('An error occurred. Please try again.');
			}
		});
	}

	function openCartSidebar() {
		$('.cart-sidebar').addClass('open');
	}
});
