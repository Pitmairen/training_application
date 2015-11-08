
import sqlite3
import random
import urllib.request
import math

from datetime import datetime, timedelta
from collections import namedtuple
from functools import partial


WORD_LIST_URL = "http://svnweb.freebsd.org/csrg/share/dict/words?view=co&content-type=text/plain"


DESCRIPTION = '''Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.'''

ExerciseSet = namedtuple('ExerciseSet', 'workout_id, exercise_id, reps_planned, \
                                        reps_done, weight_planned, weight_done')

Workout = namedtuple('Workout', 'program_id, name, description, date, done, comment')



def connect_to_db(filename):
    return sqlite3.connect(filename)

def download_word_list():
    response = urllib.request.urlopen(WORD_LIST_URL)
    txt = response.read().decode('utf-8')
    return txt.splitlines()

def generate_random_workout_name(word_list):
    name = []
    for i in range(3):
        name.append(random.choice(word_list))
    return ' '.join(name)


def get_random_exercise():
    return random.randrange(1, 24)


def create_exercise_set(workout_id, exercise_id, weight, reps):


    return ExerciseSet(
            workout_id=workout_id,
            exercise_id=exercise_id,
            reps_planned=reps,
            reps_done=reps,
            weight_planned=weight,
            weight_done=weight,
    )

def create_workout(program_id, date, done, name_generator):

    return Workout(
            program_id=program_id,
            name=name_generator(),
            description=DESCRIPTION,
            date=date,
            done=done,
            comment=DESCRIPTION
    )


def reps_generator(start_value):

    start_value = start_value*start_value

    def _inner():
        nonlocal start_value
        start_value += 5

        return int(math.sqrt(start_value))

    return _inner

def weight_generator(start_value):

    def _inner():
        nonlocal start_value
        start_value += random.randint(1, 3)
        return start_value

    return _inner


def generate_exercise_sets(workout_id, count, weight_gen, reps_gen):


    exercise_count = int(count/4)

    exercises = random.sample(range(1, 25), int(count/4))
    exercise_nr = 0

    weight = weight_gen()
    reps = reps_gen()

    for i in range(count):

        yield create_exercise_set(workout_id, exercises[exercise_nr], weight, reps)

        if i > 0 and (i % exercise_count) == 0 and exercise_nr < (len(exercises)-1):
            exercise_nr += 1


def generate_workouts(program_id, count, word_list):

    date = datetime.now().date() - timedelta(days=count+1)

    name_gen = partial(generate_random_workout_name, word_list)

    for i in range(count):

        yield create_workout(program_id, date, name_gen)
        date += timedelta(days=1)




def insert_workouts(db, workouts):

    c = db.cursor()


    weight_gen = weight_generator(20)
    reps_gen = reps_generator(5)

    for workout in workouts:

        c.execute('''INSERT INTO workout (workout_program_id,
            workout_name, workout_description, workout_date, workout_done,
            workout_comment) VALUES(?, ?, ?, ?, ?, ?)''', workout)

        workout_id = c.lastrowid

        for ex_set in generate_exercise_sets(workout_id, random.randint(10, 50), weight_gen, reps_gen):

            c.execute('''INSERT INTO exercise_set (set_workout_id, set_exercise_id, set_reps_planned,
                set_reps_done, set_weight_planned, set_weight_done)
                VALUES(?, ?, ?, ?, ?, ?)''', ex_set)


        db.commit()


    db.commit()





if __name__ == '__main__':

    import sys

    if len(sys.argv) > 1:
        db = connect_to_db(sys.argv[1])
    else:
        db = connect_to_db('/tmp/trainingdbjava.db')


    db.execute('PRAGMA foreign_keys = ON')

    word_list = download_word_list()

    print("Inserting 100 completed workouts")
    insert_workouts(db, generate_workouts(1, 100, True, word_list))


