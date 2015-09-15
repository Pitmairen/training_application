<?php require TPL_INC . 'header.php'; ?>

<?php if(isset($error_msg)): ?>
    <div class="error_message"><?= html_escape($error_msg); ?></div>
<?php endif; ?>


<form action="" method="POST">
    <input type="text" name="username" placeholder="Username">
    <input type="password" name="password" placeholder="Password">
    <input type="submit" value="Login">
</form>

<?php require TPL_INC . 'footer.php'; ?>
