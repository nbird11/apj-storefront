let user_cart;
let cartVisible = false

function initCart() {
    let cartid = localStorage.getItem("cartid");
    if (!cartid) {
        cartid = self.crypto.randomUUID()
        localStorage.setItem("cartid", cartid);
        user_cart = {
            "id": cartid,
            "personId": null,
            "items": []
        }
        createCart(user_cart)
    } else {
        fetchCart(cartid)
            .then((data) => {
                user_cart = data
                if (!user_cart) {
                    console.error('No data returned. Creating new cart.')
                    localStorage.removeItem("cartid")
                    initCart()
                } else {
                    localStorage.setItem("cartid", user_cart.id)
                    updateCartDisplay(user_cart)
                }
            })
            .catch(error => {
                console.error('Error in fetchCart resetting to new cart')
                localStorage.removeItem("cartid");
                initCart()
            });
    }
}

function createCart(cart) {
    fetch('http://localhost:8083/cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cart)
    })
        .then(response => response.json())
        .then(data => console.log('Success:', data))
        .catch(error => console.error('Error:', error));
}

function saveCart(cart) {
    fetch('http://localhost:8083/cart/' + cart.id, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cart)
    })
        .then(response => response.json())
        .then(data => console.log('Success:', data))
        .catch(error => console.error('Error:', error));
}

function fetchCart(cartId) {
    return fetch(`http://localhost:8083/cart/${cartId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error fetching cart: ${response.status} ${response.statusText}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('Cart fetched successfully:', data);
            return data;
        })
        .catch(error => {
            console.error('Error:', error)
            return null;
        });
}

function addToCart(cardId, price, cardName) {
    let existsCard = user_cart.items.filter(x => x.cardId === cardId)
    if (existsCard.length === 0) {
        let newitem = {
            "id": Date.now(),
            "name": cardName,
            "cardId": cardId,
            "price": price,
            "quantity": 1
        }
        user_cart.items.push(newitem)
        addItemToCartDB(newitem)
    }
    else{
        existsCard[0].quantity = existsCard[0].quantity + 1
        updateItem(existsCard[0])
    }

    updateCartDisplay(user_cart)
}

function addItemToCartDB(item) {
    fetch(`http://localhost:8083/cart/${user_cart.id}/item`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(item)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to add item: ${response.status} ${response.statusText}`);
            }
            return response.json();
        })
        .then(updatedCart => {
            console.log("Updated Cart:", updatedCart);
        })
        .catch(error => {
            console.error("Error adding item to cart:", error);
        });
}

function updateItem(modifiedItem){
    fetch(`http://localhost:8083/cart/${user_cart.id}/item`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(modifiedItem)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to add item: ${response.status} ${response.statusText}`);
            }
            return response.json();
        })
        .then(responseBody => {
            console.log("Updated Item:", responseBody);
        })
        .catch(error => {
            console.error("Error adding item to cart:", error);
        });
}

function updateCartDisplay(cart) {
    document.getElementById("cart-item-count").innerText = `${cart.items.length}`
    displayCartItems(cart.items)
}

function displayCartItems(cartItemsData) {
    console.log("Displaying: " + JSON.stringify(cartItemsData))
    const cartItems = document.getElementById("cartItems");
    cartItems.innerHTML = ""; // Clear previous cart items

    if (cartItemsData.length === 0) {
        cartItems.innerHTML = "<li>Your cart is empty</li>";
        return;
    }

    cartItemsData.forEach(item => {
        const li = document.createElement("li");
        li.innerHTML = `
                    <span><strong>${item.name}</strong> - $${item.price} x ${item.quantity}</span>
                    <button class="remove-btn" onclick="removeItemFromCart(${item.id})">❌</button>
                `;
        cartItems.appendChild(li);
    });
}

function removeItemFromCart(itemId) {
    // Filter out the removed item
    user_cart.items = user_cart.items.filter(item => item.id !== itemId);

    // Update the UI
    updateCartDisplay(user_cart);

    // Call function to save updated cart (you can implement this)
    console.log(`Item ${itemId} removed. Updating DB.`);
    removeItemFromCartDB(itemId)
}

function removeItemFromCartDB(itemId) {
    fetch(`http://localhost:8083/cart/${user_cart.id}/item/${itemId}`, {
        method: "DELETE"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to remove item: ${response.status} ${response.statusText}`);
            }
            return response.json();
        })
        .then(updatedCart => {
            console.log("Updated Cart:", updatedCart);
        })
        .catch(error => {
            console.error("Error removing item from cart:", error);
        });
}

document.querySelector(".cart-button").addEventListener("click", () => {
    toggleCart()
})

function toggleCart() {
    const cartWidget = document.getElementById("cartWidget");

    if (cartVisible) {
        cartWidget.style.display = "none";
        cartVisible = false;
    } else {
        cartWidget.style.display = "block";
        cartVisible = true;
    }
}

initCart();