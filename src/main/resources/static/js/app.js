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

function isEmptyOrSpaces(str){
  return !str.value || str.value === null || str.value.match(/^ *$/) !== null || str.value.match(/\s/g) !== null;
}

function isDateSmaller(startDate, endDate) {
	//alert(endDate.value);
	if(!endDate.value||endDate.value === null||!startDate.value||startDate.value===null){
		return true;
	}
  return new Date(startDate.value) <= new Date(endDate.value);
}


function isNotValidTitle(str){
	if(str.value.startsWith(' ')){
		return true;
	}
	
  if (str.value.endsWith(' ')) {
    return true;
  }
  if (!str.value) {
	return true
}
if(str.value===null){
	return true
}
  
}

function isValidEmail(email) {
  var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(String(email.value).toLowerCase());
}

function removeAllSpans(element) {
  const spans = element.parentNode.querySelectorAll('span');
  for (let i = 0; i < spans.length; i++) {
    const span = spans[i];
    span.parentNode.removeChild(span);
  }
}

/**
 *  Initializes auto-remove (removes when input changes) of spans of input element list.
 *  @param {HTMLElement[]} elements an array of the wanted elements.
 */
function initSpanRemover(elements) {
  for (let i = 0; i < elements.length; i++) {
    const element = elements[i];
	element.addEventListener("input", function(e){
		removeAllSpans(element);			
	});
  }
}



function darkMode() {
    body.style.backgroundColor = "#333";
    body.style.color = "#fff";
    cards.forEach(card => {
        card.style.backgroundColor = "#333";
        card.style.color = "#fff";
    });
}

/**
 *  Fades in all input hiddenSections.
 *  @param {HTMLElement} hiddenSections the hidden section to fade in.
 */
function fadeInAll(hiddenSections) {
	let observer = new IntersectionObserver((entries) => {
		entries.forEach(entry => {
	    	if (entry.isIntersecting) {
	        	entry.target.classList.add('show');
	         } else {
	         	entry.target.classList.remove('show');
	         }
	 	});
	});
	hiddenSections.forEach(hiddenSection => {
		observer.observe(hiddenSection);
	});
}

/**
 *  Initializes the brightness slider.
 *  @param {HTMLElement} slider The slider of the page.
 */
function initializeBrightnessSlider(slider){
	slider.oninput = function(){localStorage.setItem("sliderValue", this.value);}	
	slider.addEventListener("input", function() {
		if (slider.value < 50) {
			lightMode();
		} else {
			darkMode();
		}
	});
	
	// Retrieve the value from local storage when the page loads -> changes to previous brightness
	document.addEventListener("DOMContentLoaded", function() {
	  var storedValue = localStorage.getItem("sliderValue");
	  if (storedValue) {
	    slider.value = storedValue;
		if (slider.value < 50) {
			lightMode();
		} else {
			darkMode();
		}
	  }
	});
	
}

// transitioning to next page:
window.transitionToPage = function(href) {
    document.querySelector('body').style.opacity = 0
    setTimeout(function() { 
        window.location.href = href
    }, 500)
}

document.addEventListener('DOMContentLoaded', function(event) {
    document.querySelector('body').style.opacity = 1
})
