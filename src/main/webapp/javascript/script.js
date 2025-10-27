$(document).ready(function () {
    $('#mobile_btn').on('click', function () {
        $('#mobile_menu').toggleClass('active');
        $('#mobile_btn').find('i').toggleClass('fa-bars fa-xmark');
    });
});

// ANIMATION HEADER
// Pega o header
const header = document.querySelector("header");

// Escuta o scroll
window.addEventListener("scroll", () => {
    if (window.scrollY > 0) {
        header.classList.add("scrolled");
    } else {
        header.classList.remove("scrolled");
    }
});

document.addEventListener('DOMContentLoaded', function() {
    function setupDatePlaceholders() {
        const dateInputs = document.querySelectorAll('input[type="date"]');
        
        dateInputs.forEach(input => {
            // Verifica se está vazio
            function checkEmpty() {
                if (!input.value) {
                    input.classList.add('empty');
                } else {
                    input.classList.remove('empty');
                }
            }
            
            // Verifica inicialmente
            checkEmpty();
            
            // Adiciona listeners
            input.addEventListener('change', checkEmpty);
            input.addEventListener('input', checkEmpty);
            input.addEventListener('focus', function() {
                this.classList.remove('empty');
            });
            input.addEventListener('blur', checkEmpty);
        });
    }
    
    setupDatePlaceholders();
});