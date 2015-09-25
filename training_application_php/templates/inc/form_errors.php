<?php if($form->hasErrors()): ?>
<ul class="form-errors">
    <?php foreach($form->getErrors() as $err): ?>
        <li><?= $err; ?></li>
    <?php endforeach; ?>
</ul>
<?php endif; ?>

