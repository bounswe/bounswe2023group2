import datetime
from Database.mongo import MongoDB
import random
from Models.email_verification_model import EmailVerification
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
import config

# Function to get current time in GMT+3
def current_time_gmt3():
    return datetime.datetime.now() + datetime.timedelta(hours=3)

# Get the resources collection using the MongoDB class.
email_verification_collection = MongoDB.get_collection('email_verification')
user_collection = MongoDB.get_collection('authenticated_user')

# Creates a 6-digit long numerical verification token.
def create_token():
    return '{:06d}'.format(random.randint(0, 999999))

# Saves username, email, token, expiration (auto-generated) to database.
def create_verification_entry(username, email, token):
    # Delete old entries if any.
    delete_verification_entry(username)
    
    verification_entry = EmailVerification(
        username=username,
        email=email,
        token=token
    )
    email_verification_collection.insert_one(verification_entry.dict())

# Deletes verification entry.
def delete_verification_entry(username):
    email_verification_collection.delete_one({"username": username})

# Sends mail to the email with verification code.
def send_verification_email(username, email):
    # Generate token
    token = create_token()

    # Save the verification entry
    create_verification_entry(username, email, token)

    # Email details
    outlook_user = config.DAPP_EMAIL
    outlook_password = config.DAPP_EMAIL_PASSWORD

    # Email content
    subject = "DAPP Email Verification"
    body = f"""
    <html>
        <head></head>
        <body>
            <p>Hello <strong>{username}</strong>,</p>
            <p>Welcome to <strong>DAPP</strong>! Your verification code is: <strong style='color:#007FFF;'>{token}</strong></p>
            <p>Please enter this code in the application to complete your email verification. This code will expire in 10 minutes.</p>
            <p>Thank you for helping us to ensure the security of our communication and data by verifying your email address.</p>
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

        print('Verification email sent successfully.')
        return True
    except Exception as e:
        print('Failed to send verification email:', e)
        return False

# Checks if the token expired. If so, deletes entry.
def check_expiration(username):
    entry = email_verification_collection.find_one({"username": username})
    if entry and current_time_gmt3() > entry['expiration']:
        delete_verification_entry(username)
        return False
    return True

# Checks if the provided token matches the generated one, stored in database.
def check_token(username, provided_token):
    entry = email_verification_collection.find_one({"username": username})
    if entry and entry['token'] == provided_token:
        return check_expiration(username)
    return False

# Main function that checks the token and verifies the email of user.
def verify_user(username, provided_token):
    if check_token(username, provided_token):
        # Update the is_email_verified field of user
        query = {"username": username}
        update_operation = {"$set": {"is_email_verified": True}}
        result = user_collection.update_one(query, update_operation)

        user_entry = user_collection.find_one({"username": username})
        if user_entry['user_role'] == "GUEST":
            update_operation_role = {"$set": {"user_role": "AUTHENTICATED"}}
            user_collection.update_one(query, update_operation_role)

        if result.modified_count == 0:
            # Handle the case where the update didn't go through, e.g., user not found
            raise ValueError("Email verification update failed or was unnecessary.")

        # Delete the verification entry after successful verification
        delete_verification_entry(username)

        return True

    return False

# Check if the user is verified or not
def check_verified(username):
    user_entry = user_collection.find_one({"username": username})
    if user_entry:
        if user_entry['is_email_verified'] == True:
            return "verified"
        else:
            return "not verified"
    else:
        return "user not found"
