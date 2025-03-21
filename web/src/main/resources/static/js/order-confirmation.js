function getOrder(url) {
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`)
            }
            return response.json()
        })
        .then(orderData => {
            console.log("Search results:", orderData)
            displayConfirmation(orderData)
            sendConfirmationRequest(orderData.id)
            resetCart()
        })
        .catch(error => {
            console.error("Error fetching search results:", error)
        })
}

function sendConfirmationRequest(orderid) {
    console.log("Requesting confirmatoin for order " + orderid)
    fetch('http://localhost:8084/confirm/' + orderid, {
        method: 'GET',
    })
        .then(response => {
            if (response.status === 200) {
                return response.text()
            } else {
                console.error('Confirmation request failed with status:', response.status);
            }
        })
        .then(
            data => {
                console.log('Confirmation request successful.' + data);
            }
        )
        .catch(error => {
            console.error('Error sending confirmation request:', error);
        });
}

function displayConfirmation(orderData) {
// Format the date
    const orderDate = new Date(orderData.orderDate);
    const formattedDate = orderDate.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });

// Populate customer information
    document.getElementById('customer-email').textContent = orderData.customer.email;
    document.getElementById('customer-email-info').textContent = orderData.customer.email;
    document.getElementById('customer-name').textContent = `${orderData.customer.firstName} ${orderData.customer.lastName}`;
    document.getElementById('customer-phone').textContent = orderData.customer.phone;

// Populate order information
    document.getElementById('order-date').textContent = `Order Date: ${formattedDate}`;
    document.getElementById('order-id').textContent = `Order ID: ${orderData.id}`;

// Populate shipping address
    const shippingAddress = orderData.shippingAddress;
    document.getElementById('shipping-address').innerHTML = `
                ${shippingAddress.addressLine1}<br>
                ${shippingAddress.addressLine2 ? shippingAddress.addressLine2 + '<br>' : ''}
                ${shippingAddress.city}, ${shippingAddress.state} ${shippingAddress.zipCode}<br>
                ${shippingAddress.country}
            `;

// Populate order items
    const orderItemsContainer = document.getElementById('order-items');
    orderData.cart.items.forEach(item => {
        const itemTotal = (item.price * item.quantity).toFixed(2);
        const row = document.createElement('tr');
        row.innerHTML = `
                    <td class="card-name">${item.name}</td>
                    <td>${item.cardId}</td>
                    <td class="quantity">${item.quantity}</td>
                    <td class="price">$${item.price.toFixed(2)}</td>
                    <td class="price">$${itemTotal}</td>
                `;
        orderItemsContainer.appendChild(row);
    });

// Populate order totals
    document.getElementById('subtotal').textContent = `$${orderData.subtotal}`;
    document.getElementById('tax').textContent = `$${orderData.tax}`;
    document.getElementById('total').textContent = `$${orderData.total}`;

// Populate order notes
    document.getElementById('order-notes').textContent = orderData.orderNotes || 'No notes provided';
}

const urlParams = new URLSearchParams(window.location.search)
const orderId = urlParams.get("orderId")
getOrder("http://localhost:8083/order/" + orderId)