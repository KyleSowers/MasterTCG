// This script  displays a greeting to the user based on the current time.

var today = new Date();                 //Create new date object
var hourNow = today.getHours();       //Find current hour
var greeting = document.getElementById('indexPageGreeting');

// Function to display the appropriate greeting based on the current time
function indexPageGreeting() {
    if (hourNow > 18) {
        greeting.textContent = 'Good Evening Collector!';
    } else if (hourNow > 12) {
        greeting.textContent = 'Good Afternoon Collector!';
    } else if (hourNow > 0) {
        greeting.textContent = 'Good Morning Collector!';
    } else {
        greeting.textContent = 'Welcome Collector!';
    }
};

indexPageGreeting();
// document.write(greeting); //previous to  function created above