function addToCart(productId) {

  fetch('/products' + productId, { method: 'GET' })
    .then(response => response.json())
    .then(data => {
      // Xử lý dữ liệu trả về và cập nhật UI
      updateCartUI(data.totalCartItems);
    })
    .catch(error => console.error('Error:', error));
}

function updateCartUI(totalCartItems) {
  // Cập nhật số lượng sản phẩm trên icon giỏ hàng
  document.getElementById('cart-item-count').innerText = totalCartItems;
}