// Featured pioneers data
const featuredCards = [
    {
        id: 1,
        name: "James Gosling",
        role: "Java Creator",
        contribution: "Led the creation of the Java programming language at Sun Microsystems in 1995.",
        price: 299.99,
        image: "https://placecats.com/200/280"
    },
    {
        id: 2,
        name: "Patrick Naughton",
        role: "Java Co-Creator",
        contribution: "Key member of the Green Team that developed Java, created the original Java GUI library.",
        price: 199.99,
        image: "https://placecats.com/200/280"
    },
    {
        id: 3,
        name: "Mike Sheridan",
        role: "Java Co-Creator",
        contribution: "Original member of the Green Team, helped define Java's business requirements.",
        price: 199.99,
        image: "https://placecats.com/200/280"
    },
    {
        id: 4,
        name: "Bill Joy",
        role: "Sun Co-Founder",
        contribution: "Co-founder of Sun Microsystems, instrumental in Java's adoption and strategy.",
        price: 249.99,
        image: "https://placecats.com/200/280"
    },
    {
        id: 5,
        name: "Arthur van Hoff",
        role: "Java Pioneer",
        contribution: "Wrote the Java compiler in Java and helped create the Java platform.",
        price: 179.99,
        image: "https://placecats.com/200/280"
    },
    {
        id: 6,
        name: "Jonathan Payne",
        role: "Java Pioneer",
        contribution: "Created the WebRunner browser, predecessor to HotJava, demonstrating Java's capabilities.",
        price: 179.99,
        image: "https://placecats.com/200/280"
    },
    {
        id: 7,
        name: "Chris Warth",
        role: "Java Pioneer",
        contribution: "Key contributor to Java's development, worked on the original Java team.",
        price: 169.99,
        image: "https://placecats.com/200/280"
    },
    {
        id: 8,
        name: "Tim Lindholm",
        role: "Java Architect",
        contribution: "Led the Java Virtual Machine specification and implementation.",
        price: 189.99,
        image: "https://placecats.com/200/280"
    },
    {
        id: 9,
        name: "Frank Yellin",
        role: "Java Pioneer",
        contribution: "Key contributor to Java's security model and class libraries.",
        price: 169.99,
        image: "https://placecats.com/200/280"
    },
    {
        id: 10,
        name: "Guy L. Steele",
        role: "Java Specification",
        contribution: "Wrote the original Java Language Specification with James Gosling.",
        price: 189.99,
        image: "https://placecats.com/200/280"
    },
    {
        id: 11,
        name: "Scott Oaks",
        role: "Java Security",
        contribution: "Pioneered Java's security architecture and wrote influential Java security books.",
        price: 159.99,
        image: "https://placecats.com/200/280"
    },
    {
        id: 12,
        name: "Amy Fowler",
        role: "Java GUI Pioneer",
        contribution: "Core developer of Swing GUI components and AWT architecture.",
        price: 179.99,
        image: "https://placecats.com/200/280"
    }
];

// Render featured cards
const featuredCardsContainer = document.getElementById('featured-cards');

featuredCards.slice(0, 4).forEach(card => {
    const cardElement = document.createElement('div');
    cardElement.className = 'card';
    cardElement.innerHTML = `
                <img src="${card.image}" alt="${card.name}">
                <h4>${card.name}</h4>
                <p class="set">${card.role}</p>
                <div class="card-footer">
                    <span class="price">$${card.price}</span>
                    <button class="add-to-cart">Add to Cart</button>                    
                </div>
                <p class="contribution">${card.contribution}</p>
            `;
    featuredCardsContainer.appendChild(cardElement);
});

// Cart functionality
let cartCount = 0;
const cartButton = document.querySelector('.cart-button');
const addToCartButtons = document.querySelectorAll('.add-to-cart');

addToCartButtons.forEach(button => {
    button.addEventListener('click', () => {
        cartCount++;
        cartButton.textContent = `🛒 Cart (${cartCount})`;
    });
});