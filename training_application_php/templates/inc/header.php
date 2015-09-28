<!DOCTYPE html>
<html>
    <head>
        <title>Training Application</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="/static/jquery.js"></script>
        <link rel="stylesheet" type="text/css" href="/static/training_application.css">
    </head>
    <body>
        <header>
           <ul>
            <a href="/"><li>Home</li></a>
            <a href="/workout"><li>Workout</li></a>
            <a href="/stats"><li>Stats</li></a>
            <a href="/about"><li>About</li></a>
            <?php if($current_user->isAuthenticated()):?>
                <li><?=html_escape($current_user->getName()); ?> - <a href="/logout">logout</a></li>
            <?php endif; ?>
           </ul> 
        </header>
        <main>
