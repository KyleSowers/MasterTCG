var today = new Date();
var hourNow = today.getHours();
var greeting;

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