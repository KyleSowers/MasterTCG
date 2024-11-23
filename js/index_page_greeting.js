// This script  displays a greeting to the user based on the current time.

var today = new Date();                 //Create new date object
var hourNow = today.getHours();       //Find current hour
var greeting;

// Display the  appropriate greeting based on the current time
if (hourNow > 18) {
    greeting = 'Good Evening Collector!';
} else if (hourNow > 12) {
    greeting = 'Good Afternoon Collector!';
} else if (hourNow > 0 ) {
    greeting = 'Good Morning Collector!';
} else {
    greeting = 'Welcome Collector!';
}

document.write(greeting);