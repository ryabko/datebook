<#-- @ftlvariable name="calendar" type="ru.kalcho.datebook.helper.DatebookCalendar" -->
<#-- @ftlvariable name="tasks" type="java.util.List<ru.kalcho.datebook.model.Task>" -->
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
</head>
<body>
    <div>
        <a href="/?day=${calendar.prevDay}">&lt; Prev</a>
        ${calendar.currentDate.toLocalDate()}
        <a href="/?day=${calendar.nextDay}">Next &gt;</a>
    </div>
    <ul>
        <#list tasks as task>
            <li>${task.title} ${task.scheduledTime.toLocalDate()}</li>
        </#list>
    </ul>
    <form method="post" action="/task">
        <input type="hidden" name="day" value="${calendar.currentDate}"/>
        <input type="text" name="title"/>
        <input type="submit"/>
    </form>
</body>
</html>