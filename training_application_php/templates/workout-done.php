<?php require TPL_INC . 'header.php'; ?>

<h1><?= html_escape($workout->workout_name); ?> - <?=html_escape($workout->workout_date);?></h1>


<div><?=html_escape($workout->workout_comment); ?></div>

<dl>
<?php $ex_type = null; ?>
<?php foreach($sets as $set):?>
    <?php if($ex_type === null || $ex_type != $set->exercise_id): ?>
        <?php if($ex_type !== null):?> </ul></dd> <?php endif; ?>
        <dt><?= html_escape($set->exercise_name);?></dt>
        <dd><ul>
    <?php endif; ?>

    <li>
        Set nr <?=html_escape($set->set_nr);?>:
        <?= html_escape($set->set_reps_done); ?> reps,
        <?= html_escape($set->set_weight_done); ?> kg
    </li>

    <?php $ex_type = $set->exercise_id; ?>
<?php endforeach;?>
</ul></dd>
<dl>

<?php require TPL_INC . 'footer.php'; ?>


