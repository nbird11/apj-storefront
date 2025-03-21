function submitOrder() {
    // Get form elements
    const email = document.getElementById('email').value;
    const firstName = document.getElementById('firstName').value;
    const lastName = document.getElementById('lastName').value;
    const phone = document.getElementById('phone').value;
    const address = document.getElementById('address').value;
    const address2 = document.getElementById('address2').value;
    const city = document.getElementById('city').value;
    const state = document.getElementById('state').value;
    const zipcode = document.getElementById('zipcode').value;
    const country = document.getElementById('country').value;
    const notes = document.getElementById('notes').value;


    let subtotal = user_cart.items.reduce((subtotal, item) => subtotal + item.price * item.quantity, 0);
    let tax = Math.round(subtotal * 6) / 100
    let total = subtotal + tax

    // Build the order data object
    const orderData = {
        cart: user_cart,
        personId: null, // Would be populated if user is logged in
        cartItems: user_cart.items,
        customer: {
            email: email,
            firstName: firstName,
            lastName: lastName,
            phone: phone
        },
        shippingAddress: {
            addressLine1: address,
            addressLine2: address2,
            city: city,
            state: state,
            zipCode: zipcode,
            country: country
        },
        orderNotes: notes,
        subtotal: subtotal.toFixed(2),
        tax: tax.toFixed(2),
        total: total.toFixed(2),
        orderDate: new Date().toISOString()
    };

    // Post the order data to the server
    fetch('http://localhost:8083/order', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(orderData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Order submitted successfully:', data);
            window.location.href = "order-confirmation.html?orderId=" + data.id
        })
        .catch(error => {
            console.error('Error submitting order:', error);
        });
}

// Add this event listener to the form
document.addEventListener('DOMContentLoaded', function () {
    const checkoutForm = document.getElementById('checkoutForm');
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', function (e) {
            e.preventDefault();
            submitOrder();
        });
    }
});

// Mock cart data (in a real implementation, this would come from your cart system)
function displayOrderSummary() {
    let cartitems = document.querySelector(".cart-items")
    user_cart.items.forEach(item => {
        let displayitem = document.createElement("div")
        displayitem.innerHTML =
            `<div class="cart-item">
                <img src="img/placeholder.webp" alt="${item.cardId}" class="item-image">
                <div class="item-details">
                    <div class="item-name">${item.name}</div>
                    <div class="item-price">$${item.price}</div>
                    <div class="item-quantity">Quantity: ${item.quantity}</div>
                </div>
            </div>`
        cartitems.appendChild(displayitem)
    })
    let subtotal = user_cart.items.reduce((subtotal, item) => subtotal + item.price * item.quantity, 0);
    let tax = Math.round(subtotal * 6) / 100
    let total = subtotal + tax
    document.getElementById("summary-subtotal").innerText = `$${subtotal.toFixed(2)}`
    document.getElementById("summary-tax").innerText = `$${tax.toFixed(2)}`
    document.getElementById("summary-total").innerText = `$${total.toFixed(2)}`
}

function init() {
    if (user_cart) {
        displayOrderSummary()
    }
    else{
        setTimeout(init, 1000);
    }
}

init()