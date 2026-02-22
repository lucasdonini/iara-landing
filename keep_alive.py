from dotenv import dotenv
import os
import psycopg2


def poke():
    try:
        with psycopg2.connect(os.getenv('DATABASE_URL')) as connection:
            with connection.cursor() as cursor:
                cursor.execute('SELECT 1')

    except (Exception, psycopg2.Error) as error:
        print(f'Failed to connect: {error}')


dotenv.load_dotenv()
poke()
