import datetime
import random
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from Services.authentication_service import get_password_hash
from fastapi import HTTPException
from Database.mongo import MongoDB
import config

# Function to get current time in GMT+3
def current_time_gmt3():
    return datetime.datetime.now() + datetime.timedelta(hours=3)

# Get the resources collection using the MongoDB class.
password_reset_collection = MongoDB.get_collection('password_reset')
user_collection = MongoDB.get_collection('authenticated_user')

# Creates a 6-digit long numerical verification token.
def create_token():
    return '{:06d}'.format(random.randint(0, 999999))

# Saves email, token, and expiration (auto-generated) to the password reset collection.
def create_reset_token_entry(email, token):
    # Delete old entries if any.
    delete_reset_token_entry(email)

    expiration_time = current_time_gmt3() + datetime.timedelta(minutes=10)

    reset_token_entry = {
        "email": email,
        "token": token,
        "expiration": expiration_time
    }
    password_reset_collection.insert_one(reset_token_entry)

# Deletes password reset token entry.
def delete_reset_token_entry(email):
    password_reset_collection.delete_one({"email": email})

# Sends mail to the email with password reset code.
def send_reset_password_email(email):
    # Generate token
    token = create_token()

    # Save the reset token entry
    create_reset_token_entry(email, token)

    # Email details
    outlook_user = config.DAPP_EMAIL
    outlook_password = config.DAPP_EMAIL_PASSWORD

    # Email content
    subject = "DAPP Password Reset"
    body = f"""
    <html>
        <head></head>
        <body>
            <p>Hello,</p>
            <p>Welcome to <strong>DAPP</strong>! Your password reset code is: <strong style='color:#007FFF;'>{token}</strong></p>
            <p>Please enter this code in the application to reset your password. This code will expire in 10 minutes.</p>
            <p>If you did not request a password reset, please ignore this email.</p>
            <p>Thank you for using our service.</p>
            <p>Sincerely,<br>
            DAPP Team</p>
        </body>
    </html>
    """

    # Setting up the message
    message = MIMEMultipart('alternative')
    message['Subject'] = subject
    message['From'] = outlook_user
    message['To'] = email
    message.attach(MIMEText(body, 'html'))

    # Sending the email
    try:
        server = smtplib.SMTP('smtp-mail.outlook.com', 587)
        server.ehlo()
        server.starttls()  # Start TLS encryption
        server.ehlo()
        server.login(outlook_user, outlook_password)
        server.send_message(message)
        server.quit()

        print('Password reset email sent successfully.')
        return True
    except Exception as e:
        print('Failed to send password reset email:', e)
        return False

# Checks if the token expired. If so, deletes the entry.
def check_expiration(email):
    entry = password_reset_collection.find_one({"email": email})
    if entry and current_time_gmt3() > entry['expiration']:
        delete_reset_token_entry(email)
        return False
    return True

# Checks if the provided token matches the generated one, stored in the database.
def check_reset_token(email, provided_token):
    entry = password_reset_collection.find_one({"email": email})
    if entry and entry['token'] == provided_token:
        return check_expiration(email)
    return False

# Reset user password
def reset_user_password(email, new_password):
    hashed_password = get_password_hash(new_password)
    query = {"email": email}
    update_operation = {"$set": {"password": hashed_password}}
    result = user_collection.update_one(query, update_operation)

    if result.modified_count == 0:
        raise HTTPException(status_code=400, detail="Password reset failed.")

    delete_reset_token_entry(email)

# Main function that checks the token and resets the user's password.
def reset_password(email, token, new_password):
    if check_reset_token(email, token):
        reset_user_password(email, new_password)
        return True
    return False

