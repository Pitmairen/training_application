<?php require TPL_INC . 'header.php'; ?>


<h2>Your next workouts will be:</h2>
<ul>
<?php if($next_workouts):?>
    <?php foreach($next_workouts as $w): ?>
        <li><a href="/workout/<?=html_escape($w->workout_id);?>"><?=html_escape($w->workout_name); ?> - <?=$w->workout_date;?></a></li>
    <?php endforeach; ?>

<?php else: ?>
    <li>You have no workouts scheduled. Please contact your trainer.</li>
<?php endif; ?>
</ul>

<?php require TPL_INC . 'footer.php'; ?>
