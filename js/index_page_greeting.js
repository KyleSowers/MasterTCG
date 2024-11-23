// This script  displays a greeting to the user based on the current time.

var today = new Date();                 //Create new date object
var hourNow = today.getHours();       //Find current hour
var greetingElement = document.getElementById('indexPageGreeting');

// Function to display the appropriate greeting based on the current time
function indexPageGreeting() {
    if (hourNow > 18) {
        greetingElement.textContent = 'Good Evening Collector!';
    } else if (hourNow > 12) {
        greetingElement.textContent = 'Good Afternoon Collector!';
    } else if (hourNow > 0) {
        greetingElement.textContent = 'Good Morning Collector!';
    } else {
        greetingElement.textContent = 'Welcome Collector!';
    }
};

indexPageGreeting();
// document.write(greeting); //previous to  function created above