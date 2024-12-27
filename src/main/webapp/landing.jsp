<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="welcome-message">
        <h1 id="greeting"></h1>
        <p>Welcome, <span id="first-name"></span> <span id="last-name"></span>!</p>
        <form action="/userauthentication/logout" method="post">
            <button type="submit">Logout</button>
        </form>
    </div>

    <script>
        // Fetch session data via JSP and populate the page dynamically
        document.getElementById('greeting').innerText = '<%= session.getAttribute("greeting") %>';
        document.getElementById('first-name').innerText = '<%= session.getAttribute("firstName") %>';
        document.getElementById('last-name').innerText = '<%= session.getAttribute("lastName") %>';
    </script>
</body>
</html>
