const urlParams = new URLSearchParams(window.location.search)
const searchQuery = urlParams.get("search")
const specialtyQuery = urlParams.get("specialty")
const maxPriceQuery = urlParams.get("maxPrice")
const minPriceQuery = urlParams.get("minPrice")
const sortQuery = urlParams.get("sort")
const pageQuery = urlParams.get("page")
const productListingCardsContainer = document.getElementById('product-listing-cards');
if (pageQuery){
    document.getElementById("page-number").innerText = `${parseInt(pageQuery) + 1}`
}
if (searchQuery) {
    executeSearch(searchQuery)
}
else if (specialtyQuery || maxPriceQuery || minPriceQuery) {
    filter(maxPriceQuery, minPriceQuery, specialtyQuery)
}
else{
    displayAllCards(pageQuery, sortQuery)
}

function filter(maxPriceQuery, minPriceQuery, specialtyQuery) {
    let myurl = "http://localhost:8080/mongo/cards/filter"
    if (specialtyQuery) {
        myurl += `/specialty?specialty=${specialtyQuery}`
        document.getElementById("specialty").value = specialtyQuery
    }
    else if (minPriceQuery || maxPriceQuery) {
        myurl += "/price?"
        if (minPriceQuery) {
            myurl += `&minPrice=${minPriceQuery}`
            document.getElementById("minPrice").value = minPriceQuery
        }
        if (maxPriceQuery) {
            myurl += `&maxPrice=${maxPriceQuery}`
            document.getElementById("maxPrice").value = maxPriceQuery
        }
    }
    doFetch(myurl)
}

function displayAllCards(page, sort) {
    let myurl = "http://localhost:8080/mongo/cards?"
    document.querySelector(".pagination").style.display = "block";
    if (!page){
        page=0
    }
    myurl+=`page=${page}&size=8`
    if (sort){
        myurl+=`&sort=${sort}`
        document.getElementById("sort").value = sort;
    }
    document.getElementById("page-number").innerText = `${page+1}`;
    doFetch(myurl)
}

function executeSearch(searchQuery) {
    doFetch(`http://localhost:8080/mongo/cards/search?query=${encodeURIComponent(searchQuery)}`)
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
                    <button class="add-to-cart" onclick="addToCart('${card._id}', ${card.price}, '${card.name}')">Add to Cart</button>                    
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
    displayAllCards(page_number-2, sortQuery)
})

document.getElementById("forward-page").addEventListener('click', () => {
    let page_number = parseInt(document.getElementById("page-number").innerText)
    if (page_number === 13){
        return;
    }
    displayAllCards(page_number, sortQuery)
})

function doFilter(){
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
    window.location.href = `product-listing.html?${params.toString()}`;
}

document.getElementById("apply-specialty-filter").addEventListener("click", () => {
    document.getElementById("sort").value = ""
    document.getElementById("minPrice").value = ""
    document.getElementById("maxPrice").value = ""
    doFilter()
});

document.getElementById("apply-price-filter").addEventListener("click", () => {
    document.getElementById("sort").value = ""
    document.getElementById("specialty").value = ""
    doFilter()
});


document.getElementById("apply-sort").addEventListener("click", () => {
    document.getElementById("specialty").value = ""
    document.getElementById("minPrice").value = ""
    document.getElementById("maxPrice").value = ""
    doFilter()
});
