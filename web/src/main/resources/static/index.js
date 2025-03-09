// Featured pioneers data
const featuredCards = [
    {
        id: 1,
        name: "James Gosling",
        specialty: "Java Creator",
        contribution: "Led the creation of the Java programming language at Sun Microsystems in 1995.",
        price: 299.99,
        imageUrl: "https://placecats.com/200/280"
    },
    {
        id: 2,
        name: "Patrick Naughton",
        specialty: "Java Co-Creator",
        contribution: "Key member of the Green Team that developed Java, created the original Java GUI library.",
        price: 199.99,
        imageUrl: "https://placecats.com/200/280"
    },
    {
        id: 3,
        name: "Mike Sheridan",
        specialty: "Java Co-Creator",
        contribution: "Original member of the Green Team, helped define Java's business requirements.",
        price: 199.99,
        imageUrl: "https://placecats.com/200/280"
    },
    {
        id: 4,
        name: "Bill Joy",
        specialty: "Sun Co-Founder",
        contribution: "Co-founder of Sun Microsystems, instrumental in Java's adoption and strategy.",
        price: 249.99,
        imageUrl: "https://placecats.com/200/280"
    }
];

// Render featured cards
const featuredCardsContainer = document.getElementById('featured-cards');

if (document.getElementById("homepage")) {
    featuredCards.forEach(card => {
        const cardElement = document.createElement('div');
        cardElement.className = 'card';
        cardElement.innerHTML = `
                <img src="${card.imageUrl}" alt="${card.name}">
                <h4>${card.name}</h4>
                <p class="set">${card.specialty}</p>
                <div class="card-footer">
                    <span class="price">$${card.price}</span>
                    <button class="add-to-cart">Add to Cart</button>                    
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