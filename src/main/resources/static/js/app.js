// const hiddenSections = document.querySelectorAll('.hidden');
// let observer = new IntersectionObserver((entries) => {
//     entries.forEach(entry => {
//         if (entry.isIntersecting) {
//             entry.target.classList.add('show');
//         } else {
//             entry.target.classList.remove('show');
//         }
//     });
// });
// hiddenSections.forEach(hiddenSection => {
//     observer.observe(hiddenSection);
//     //alert(hiddenSection);
// });

function lightMode() {
    body.style.backgroundColor = "#fff";
    body.style.color = "#000";
    cards.forEach(card => {
        card.style.backgroundColor = "#fff";
        card.style.color = "#000";
    });
}

function darkMode() {
    body.style.backgroundColor = "#333";
    body.style.color = "#fff";
    cards.forEach(card => {
        card.style.backgroundColor = "#333";
        card.style.color = "#fff";
    });
}

window.transitionToPage = function(href) {
    document.querySelector('body').style.opacity = 0
    setTimeout(function() { 
        window.location.href = href
    }, 500)
}

document.addEventListener('DOMContentLoaded', function(event) {
    document.querySelector('body').style.opacity = 1
})
