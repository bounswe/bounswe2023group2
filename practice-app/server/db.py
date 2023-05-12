"""
import psycopg2
import psycopg2.pool
from contextlib import contextmanager

conn = psycopg2.connect(
    host="172.18.0.2",
    database="postgres",
    user="postgres",
    password="postgres",
    port="5432"
)

cur = conn.cursor()
cur.execute("ROLLBACK")
fd = open("database.sql", 'r')
sqlFile = fd.read()
fd.close()

# all SQL commands (split on ';')
sqlCommands = sqlFile.split(';')

# Execute every command from the input file
for command in sqlCommands:
    # This will skip and report errors
    # For example, if the tables do not yet exist, this will skip over
    # the DROP TABLE commands
    try:
        cur.execute(command)
    except Exception as e:
        print("Command skipped: ", str(e))
cur.close()
"""