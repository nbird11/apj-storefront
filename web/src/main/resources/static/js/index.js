// Featured pioneers data
function fetchFeaturedCards() {
    if (document.getElementById("homepage")) {
        fetch(`http://localhost:8080/mongo/cards/search?query=java`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`)
                }
                return response.json()
            })
            .then(data => {
                console.log("Search results:", data)
                if (data.length > 4){
                    data = data.slice(0, 4)
                }
                renderCards(data)
            })
            .catch(error => {
                console.error("Error fetching featured cards:", error)
            })
    }
}

// Render featured cards
const featuredCardsContainer = document.getElementById('featured-cards');

function renderCards(featuredCards) {

    featuredCards.forEach(card => {
        const cardElement = document.createElement('div');
        cardElement.className = 'card';
        cardElement.innerHTML = `
                <img src="${card.imageUrl}" alt="${card.name}">
                <h4>${card.name}</h4>
                <p class="set">${card.specialty}</p>
                <div class="card-footer">
                    <span class="price">$${card.price}</span>                    
                    <button class="add-to-cart" onclick="addToCart('${card._id}', ${card.price}, '${card.name}')">Add to Cart</button>                    
                </div>
                <p class="contribution">${card.contribution}</p>
            `;
        featuredCardsContainer.appendChild(cardElement);
    });
}

let searchObj = document.getElementById("search");
searchObj.addEventListener("keydown", (event) => {
    if (event.key === 'Enter') {
        // Prevent default behavior
        event.preventDefault();

        // Get the search term from the input
        const searchTerm = event.target.value.trim();

        // Redirect to the product-listing page with the search parameter
        if (searchTerm) {
            // Encode the search term to handle special characters properly
            const encodedSearchTerm = encodeURIComponent(searchTerm);

            // Redirect to the product listing page with the search parameter
            window.location.href = `/product-listing.html?search=${encodedSearchTerm}`;
        }
    }
})

document.querySelector(".cta-button")?.addEventListener("click", () => {
        window.location.href = "product-listing.html";
    }
)

document.querySelector(".logo")?.addEventListener("click", () => {
        window.location.href = "index.html";
    }
)

document.querySelector("#java-pioneers-card")?.addEventListener("click", () => {
    window.location.href = "product-listing.html?search=java";
})

document.querySelector("#programming-languages-card")?.addEventListener("click", () => {
    window.location.href = "product-listing.html?specialty=Programming+Languages";
})

document.querySelector("#theory-card")?.addEventListener("click", () => {
    window.location.href = "product-listing.html?specialty=Algorithms_Theory";
})

function getUser(){
    fetch(`http://localhost:8080/user/me`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error getting username! Status: ${response.status}`)
            }
            return response.text()
        })
        .then(data => {
            if (data.length !== 0) {
                username = data
            }
            else{
                console.log("No data found for login - not logged in.")
            }
            document.getElementById("username").innerText = username
        })
        .catch(error => {
            console.error("Error getting login info:", error)
        })
}

let username = "Not Logged In"

fetchFeaturedCards()
getUser()