<?php require TPL_INC . 'header.php'; ?>

<?php if(isset($error_msg)): ?>
    <div class="error_message"><?= html_escape($error_msg); ?></div>
<?php endif; ?>


<h2>Login required</h2>
<div id="login">
<form action="" method="POST">
    <input type="text" name="username" placeholder="Username">
    <input type="password" name="password" placeholder="Password">
    <input type="submit" value="Login">
</form>
</div>

<?php require TPL_INC . 'footer.php'; ?>
