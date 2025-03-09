const urlParams = new URLSearchParams(window.location.search)
const searchQuery = urlParams.get("search")
const specialtyQuery = urlParams.get("specialty")
const maxPriceQuery = urlParams.get("maxPrice")
const minPriceQuery = urlParams.get("minPrice")
const sortQuery = urlParams.get("sort")
const pageQuery = urlParams.get("page")
const size = 8;
const productListingCardsContainer = document.getElementById('product-listing-cards');
if (pageQuery){
    document.getElementById("page-number").innerText = `${parseInt(pageQuery) + 1}`
}
if (searchQuery) {
    executeSearch(searchQuery)
}
else if (specialtyQuery || maxPriceQuery || minPriceQuery || sortQuery) {
    sortAndFilter(maxPriceQuery, minPriceQuery, specialtyQuery, sortQuery)
}
else if (pageQuery){
    displayAllCards(pageQuery)
}
else {
    displayAllCards()
}

function sortAndFilter(maxPriceQuery, minPriceQuery, specialtyQuery, sortQuery) {
    let myurl = "http://localhost:8080/api/cards/filter?"
    if (sortQuery) {
        myurl += `&sort=${sortQuery}`
        document.getElementById("sort").value = sortQuery
    }
    if (specialtyQuery) {
        myurl += `&specialty=${specialtyQuery}`
        document.getElementById("specialty").value = specialtyQuery
    }
    if (minPriceQuery) {
        myurl += `&minPrice=${minPriceQuery}`
        document.getElementById("minPrice").value = minPriceQuery
    }
    if (maxPriceQuery) {
        myurl += `&maxPrice=${maxPriceQuery}`
        document.getElementById("maxPrice").value = maxPriceQuery
    }
    doFetch(myurl)
}

function displayAllCards(page) {
    document.querySelector(".pagination").style.display = "block";
    if (!page){
        page = 0
    }
    document.getElementById("page-number").innerText = `${page+1}`;
    doFetch(`http://localhost:8080/api/cards?page=${page}&size=8`)
}

function executeSearch(searchQuery) {
    doFetch(`http://localhost:8080/api/cards/search?query=${encodeURIComponent(searchQuery)}`)
}

function doFetch(url) {
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`)
            }
            return response.json()
        })
        .then(data => {
            console.log("Search results:", data)
            renderCards(data)
        })
        .catch(error => {
            console.error("Error fetching search results:", error)
        })
}

function renderCards(cards) {
    productListingCardsContainer.innerHTML= ""
    cards.forEach(card => {
        const cardElement = document.createElement('div')
        cardElement.className = 'card'
        cardElement.innerHTML = `
                <img src="${card.imageUrl}" alt="${card.name}">
                <h4>${card.name}</h4>
                <p class="set">${card.specialty}</p>
                <div class="card-footer">
                    <span class="price">$${card.price}</span>
                    <button class="add-to-cart">Add to Cart</button>                    
                </div>
                <p class="contribution">${card.contribution}</p>
            `
        productListingCardsContainer.appendChild(cardElement)
    })
}

document.getElementById("back-page").addEventListener('click', () => {
    let page_number = parseInt(document.getElementById("page-number").innerText)
    if (page_number === 1){
        return;
    }
    displayAllCards(page_number-2)
})

document.getElementById("forward-page").addEventListener('click', () => {
    let page_number = parseInt(document.getElementById("page-number").innerText)
    if (page_number === 13){
        return;
    }
    displayAllCards(page_number)
})

document.getElementById("apply-filters").addEventListener("click", () => {
    const params = new URLSearchParams();

    // Get sort value
    const sort = document.getElementById("sort").value;
    if (sort) params.append("sort", sort);

    // Get min and max price values
    const minPrice = document.getElementById("minPrice").value;
    if (minPrice) params.append("minPrice", minPrice);

    const maxPrice = document.getElementById("maxPrice").value;
    if (maxPrice) params.append("maxPrice", maxPrice);

    // Get specialty value
    const specialty = document.getElementById("specialty").value;
    if (specialty) params.append("specialty", specialty);

    // Redirect to the product details page with query parameters
    window.location.href = `http://localhost:8080/product-listing.html?${params.toString()}`;
});

document.getElementById("clear-filters").addEventListener("click", () => {
    window.location.href = `http://localhost:8080/product-listing.html`;
})