(function(document) {

    function pad(i) {
        return (i < 10) ? "0" + i : "" + i;
    }

    function formatDateShort(date) {
        return date.getFullYear() + "-" + pad(date.getMonth() + 1)+ "-" + pad(date.getDate());
    }

    function formatDateWide(date) {
        return date.getFullYear() + "-" + pad(date.getMonth() + 1)+ "-" + pad(date.getDate()) + "T00:00:00";
    }

    function addDays(date, n) {
        var newDate = new Date(date);
        newDate.setDate(date.getDate() + n);
        return newDate;
    }

    function loadTasks(day) {
        var request = new XMLHttpRequest();
        request.open("GET", "/api/tasks?day=" + encodeURIComponent(formatDateWide(day)), true);
        request.onreadystatechange = function() {
            if (request.readyState == 4) {
                if (request.status == 200) {
                    tasksContainer.innerHTML = request.responseText;
                }
            }
        };
        request.send(null);
    }

    function setCurrentDate(date) {
        currentDate = date;
        currentDaySpan.innerHTML = formatDateShort(date);
    }

    var tasksContainer = document.getElementById("tasks-container");
    var currentDaySpan = document.getElementById("current-day");
    var prevDayLink = document.getElementById("prev-day");
    var nextDayLink = document.getElementById("next-day");
    var currentDate;

    setCurrentDate(new Date());
    loadTasks(currentDate);

    prevDayLink.addEventListener("click", function() {
        setCurrentDate(addDays(currentDate, -1));
        loadTasks(currentDate);
    });

    nextDayLink.addEventListener("click", function() {
        setCurrentDate(addDays(currentDate, 1));
        loadTasks(currentDate);
    });

})(document);