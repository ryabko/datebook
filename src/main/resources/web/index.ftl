<#-- @ftlvariable name="tasks" type="java.util.List<ru.kalcho.datebook.model.Task>" -->
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
</head>
<body>
    <ul>
        <#list tasks as task>
            <li>${task.title}</li>
        </#list>
    </ul>
    <form method="post" action="/task">
        <input type="text" name="title"/>
        <input type="submit"/>
    </form>
</body>
</html>