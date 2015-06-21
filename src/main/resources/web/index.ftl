<#-- @ftlvariable name="calendar" type="ru.kalcho.datebook.helper.DatebookCalendar" -->
<#-- @ftlvariable name="tasks" type="java.util.List<ru.kalcho.datebook.model.Task>" -->
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
    <style>
        form.single-button {
            display: inline;
        }
        .status-done {
            text-decoration: line-through;
        }
        .status-removed {
            color: gray;
        }
    </style>
</head>
<body>
    <div>
        <a href="/?day=${calendar.prevDay}">&lt; Prev</a>
        ${calendar.currentDate.toLocalDate()}
        <a href="/?day=${calendar.nextDay}">Next &gt;</a>
    </div>
    <ul>
        <#list tasks as task>
            <li>
                <span class="status-${task.status?lower_case}">${task.title}</span>
                <#if task.status == "ACTIVE">
                    <form method="post" action="/task-update" class="single-button">
                        <input type="hidden" name="id" value="${task.id}"/>
                        <input type="hidden" name="status" value="DONE"/>
                        <input type="hidden" name="returnDay" value="${calendar.currentDate}"/>
                        <input type="submit" value="Done"/>
                    </form>
                </#if>
                <#if task.status != "REMOVED">
                    <form method="post" action="/task-update" class="single-button">
                        <input type="hidden" name="id" value="${task.id}"/>
                        <input type="hidden" name="status" value="REMOVED"/>
                        <input type="hidden" name="returnDay" value="${calendar.currentDate}"/>
                        <input type="submit" value="Remove"/>
                    </form>
                </#if>
                <#if task.status != "ACTIVE">
                    <form method="post" action="/task-update" class="single-button">
                        <input type="hidden" name="id" value="${task.id}"/>
                        <input type="hidden" name="status" value="ACTIVE"/>
                        <input type="hidden" name="returnDay" value="${calendar.currentDate}"/>
                        <input type="submit" value="Restore"/>
                    </form>
                </#if>
            </li>
        </#list>
    </ul>
    <form method="post" action="/task">
        <input type="hidden" name="day" value="${calendar.currentDate}"/>
        <input type="text" name="title"/>
        <input type="submit"/>
    </form>
</body>
</html>