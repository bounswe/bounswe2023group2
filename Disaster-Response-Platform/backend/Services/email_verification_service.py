# imports
import datetime

# Function to get current time in GMT+3
def current_time_gmt3():
    return datetime.datetime.now() + datetime.timedelta(hours=3)

# Get the resources collection using the MongoDB class.
resources_collection = MongoDB.get_collection('email_verification')

# TODO: email config

# Creates 6-character long verification token.
def create_token():
    # TODO: logic
    return

# Saves username, email, token to database.
def create_verification_entry(username, email, token):
    # TODO: logic
    # expiration is generated in model
    return

# After verification, deletes verification entry.
def delete_verification_entry():
    return

# Sends mail to the email with verification code.
def send_verification_email():
    message = "Welcome to DAPP, <username>! here is your verification code: 12F5A2 it will expire in 10 min etc..." # TODO: proper message
    # TODO: logic, use create token & create_verification_entry
    return

# Checks if the token expired. If so, deletes entry.
def check_expiration():
    #TODO: logic
    return

# Checks if the provided token matches the generated one, stored in database.
def check_token():
    # TODO: logic
    # TODO: check expiration vs current
    # TODO: check token validity
    return


# Main function that 
def verify_user():
    # TODO: logic, use check_token
    # TODO: update is_email_verified field of user.
    # TODO: delete verification entry
    return 

