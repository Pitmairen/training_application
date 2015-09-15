<?php require TPL_INC . 'header.php'; ?>

This is all our training programs:
<ul>
<?php foreach($training_programs as $prog): ?>
<li>
    Program <?=html_escape($prog->trainingprogram_id); ?>
    By trainer <?=html_escape($prog->trainer_first_name);?>
    for customer <?=html_escape($prog->customer_first_name);?>
</li>
<?php endforeach; ?>
</ul>

<?php require TPL_INC . 'footer.php'; ?>
