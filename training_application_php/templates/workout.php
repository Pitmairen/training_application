<?php require TPL_INC . 'header.php'; ?>

<?php if (isset($error_msg)): ?>
    <div class="error_message"><?= html_escape($error_msg); ?></div>
<?php endif; ?>


<h1><?= html_escape($workout->workout_name); ?></h1>

<div><?=html_escape($workout->workout_description); ?></div>

<?= $workout_form->renderErrors(TPL_INC . 'form_errors.php'); ?>
<?= $workout_form; ?>


<?php require TPL_INC . 'footer.php'; ?>


