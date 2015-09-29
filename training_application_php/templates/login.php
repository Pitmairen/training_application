<?php require TPL_INC . 'header.php'; ?>

<?php if (isset($error_msg)): ?>
    <div class="error_message"><?= html_escape($error_msg); ?></div>
<?php endif; ?>


<h2>Login required</h2>
<div id="login">

    <?= $login_form->renderErrors(TPL_INC . 'form_errors.php'); ?>
    <?= $login_form; ?>

</div>

<?php require TPL_INC . 'footer.php'; ?>
