import json
import os
from const import *
import psycopg2
from psycopg2.extras import RealDictCursor
from add_activity_api.secrets import *

conn = psycopg2.connect(database=db_name, # postgres
                        host='172.18.0.2',
                        user=db_user, # postgres
                        password=db_password, # postgres
                        port=5432)

cursor = conn.cursor(cursor_factory=RealDictCursor)

# cursor.execute("DROP TABLE Resources")
# cursor.execute("DROP TABLE Needs")
# cursor.execute("DROP TABLE Events")
# cursor.execute("DROP TABLE Actions")
# conn.commit()

# creator id should be made foreign key
cursor.execute("""\
CREATE TABLE IF NOT EXISTS Resources(
    id CHAR(36) PRIMARY KEY,
    type VARCHAR(20),
    location VARCHAR(20),
    notes VARCHAR(200),
    updated_at VARCHAR(20),
    is_active BOOLEAN,
    upvotes INT,
    downvotes INT,
    creator_id VARCHAR(40),
    creation_date VARCHAR(20),
    condition VARCHAR(20),
    quantity INT
)\
""")

cursor.execute("""\
CREATE TABLE IF NOT EXISTS Events(
    id CHAR(36) PRIMARY KEY,
    type VARCHAR(20),
    location VARCHAR(20),
    notes VARCHAR(200),
    updated_at VARCHAR(20),
    is_active BOOLEAN,
    upvotes INT,
    downvotes INT,
    creator_id VARCHAR(40),
    creation_date VARCHAR(20),
    duration INT
)
""")

cursor.execute("""\
CREATE TABLE IF NOT EXISTS Needs(
    id CHAR(36) PRIMARY KEY,
    type VARCHAR(20),
    location VARCHAR(20),
    notes VARCHAR(200),
    updated_at VARCHAR(20),
    is_active BOOLEAN,
    upvotes INT,
    downvotes INT,
    creator_id VARCHAR(40),
    creation_date VARCHAR(20),
    urgency INT,
    quantity INT
)
""")

cursor.execute("""\
CREATE TABLE IF NOT EXISTS Actions(
    id CHAR(36) PRIMARY KEY,
    notes VARCHAR(200),
    updated_at VARCHAR(20),
    is_active BOOLEAN,
    upvotes INT,
    downvotes INT,
    creator_id VARCHAR(40),
    creation_date VARCHAR(20),
    start_location VARCHAR(20),
    end_location VARCHAR(20),
    status VARCHAR(20),
    used_resources VARCHAR(200),
    created_resources VARCHAR(200),
    fulfilled_needs VARCHAR(200),
    emerged_needs VARCHAR(200),
    related_events VARCHAR(200)
)
""")


def add_resource(uuid, type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, condition, quantity):
    # cursor.execute("INSERT INTO Resources(id, type, location, notes, updated_at, upvotes, downvotes, creator_id, creation_date, condition, quantity) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", (uuid, type, location, notes, updated_at, str(is_active), str(upvotes), str(downvotes), creator_id, creation_date, condition, str(quantity)))
    cursor.execute("INSERT INTO Resources(id, type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, condition, quantity) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", (uuid, type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, condition, quantity))
    conn.commit()

def add_need(uuid, type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, urgency, quantity):
    cursor.execute("INSERT INTO Needs(id, type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, urgency, quantity) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", (uuid, type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, urgency, quantity))
    conn.commit()

def add_event(uuid, type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, duration):
    cursor.execute("INSERT INTO Events(id, type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, duration) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", (uuid, type, location, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, duration))
    conn.commit()

def add_action(uuid, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, start_location, end_location, status, used_resources, created_resources, fulfilled_needs, emerged_needs, related_events):
    cursor.execute("INSERT INTO Actions(id, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, start_location, end_location, status, used_resources, created_resources, fulfilled_needs, emerged_needs, related_events) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", (uuid, notes, updated_at, is_active, upvotes, downvotes, creator_id, creation_date, start_location, end_location, status, used_resources, created_resources, fulfilled_needs, emerged_needs, related_events))
    conn.commit()

def get_resource(uuid):
    cursor.execute("SELECT * FROM Resources WHERE id = %s", (uuid,))
    result = cursor.fetchall()
    if result:
        return dict(result[0])

def list_resource():
    cursor.execute("SELECT * FROM Resources")
    result = cursor.fetchall()
    return list(map(dict, result))

def get_need(uuid):
    cursor.execute("SELECT * FROM Needs WHERE id = %s", (uuid,))
    result = cursor.fetchall()
    if result:
        return dict(result[0])

def list_need():
    cursor.execute("SELECT * FROM Needs")
    result = cursor.fetchall()
    return list(map(dict, result))

def get_event(uuid):
    cursor.execute("SELECT * FROM Events WHERE id = %s", (uuid,))
    result = cursor.fetchall()
    if result:
        return dict(result[0])

def list_event():
    cursor.execute("SELECT * FROM Events")
    result = cursor.fetchall()
    return list(map(dict, result))

def get_action(uuid):
    cursor.execute("SELECT * FROM Actions WHERE id = %s", (uuid,))
    result = cursor.fetchall()
    if result:
        return dict(result[0])

def list_action():
    cursor.execute("SELECT * FROM Actions")
    result = cursor.fetchall()
    return list(map(dict, result))

def delete_resource(id):
    cursor.execute("DELETE FROM Resources WHERE id = %s", (id,))
    conn.commit()
    if cursor.statusmessage.split()[1] == '0':
        return "Resource not found"
    else:
        return "Successfully deleted"


def delete_need(id):
    cursor.execute("DELETE FROM Needs WHERE id = %s", (id,))
    conn.commit()
    if cursor.statusmessage.split()[1] == '0':
        return "Need not found"
    else:
        return "Successfully deleted"

def delete_event(id):
    cursor.execute("DELETE FROM Events WHERE id = %s", (id,))
    conn.commit()
    if cursor.statusmessage.split()[1] == '0':
        return "Event not found"
    else:
        return "Successfully deleted"

def delete_action(id):
    cursor.execute("DELETE FROM Actions WHERE id = %s", (id,))
    conn.commit()
    if cursor.statusmessage.split()[1] == '0':
        return "Action not found"
    else:
        return "Successfully deleted"
