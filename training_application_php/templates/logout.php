<?php require TPL_INC . 'header.php'; ?>

<?php if(isset($error_msg)): ?>
    <div class="error_message"><?= html_escape($error_msg); ?></div>
<?php endif; ?>


<form action="" method="POST">
    <input type="submit" value="Logout">
</form>

<?php require TPL_INC . 'footer.php'; ?>
