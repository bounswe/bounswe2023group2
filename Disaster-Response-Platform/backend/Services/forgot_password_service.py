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
forgot_password_collection = MongoDB.get_collection('forgot_password')
user_collection = MongoDB.get_collection('authenticated_user')

# Creates a 6-digit long numerical verification token.
def create_token():
    return '{:06d}'.format(random.randint(0, 999999))

# Saves email, token, and expiration (auto-generated) to the password reset collection.
def create_reset_token_entry(email, token):
    # Check if a user with the given email exists in the user_collection
    existing_user = user_collection.find_one({"email": email})
    if not existing_user:
        return False  # User with the given email does not exist

    # Delete old entries if any.
    delete_reset_token_entry(email)

    expiration_time = current_time_gmt3() + datetime.timedelta(minutes=10)

    reset_token_entry = {
        "email": email,
        "token": token,
        "expiration": expiration_time
    }
    forgot_password_collection.insert_one(reset_token_entry)
    return True

# Deletes password reset token entry.
def delete_reset_token_entry(email):
    forgot_password_collection.delete_one({"email": email})

# Sends mail to the email with password reset code.
def send_reset_password_email(email):
    # Generate token
    token = create_token()

    # Attempt to save the reset token entry and check if the email is valid
    valid_email = create_reset_token_entry(email, token)
    
    if not valid_email:
        return "Invalid email address"  # Return a string indicating an invalid email address

    # Email details
    outlook_user = config.DAPP_EMAIL
    outlook_password = config.DAPP_EMAIL_PASSWORD

    # change based on the server url.
    base_url = "http://3.218.226.215:8000"

    # Create a reset link with email and token as query parameters
    reset_link = f"{base_url}/api/forgot_password/reset_password?email={email}&token={token}"

    # Email content
    subject = "DAPP Password Reset"
    body = f"""
    <html>
        <head></head>
        <body>
            <p>Hello,</p>
            <p>To reset your password, please click the following link:</p>
            <p><a href="{reset_link}" style="color:#007FFF;">Reset Password</a></p>
            <p>This link will expire in 10 minutes. If you are unable to use the provided link, please use your reset password token manually: <strong style='color:#007FFF;'>{token}</strong></p>
            <p>If you did not request a password reset, please ignore this email.</p>
            <p>Thank you for using our service.</p>
            <p>Sincerely,<br>
            <strong>DAPP Team</strong></p>
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
        return "Email sent successfully"  # Return a success message
    except Exception as e:
        print('Failed to send password reset email:', e)
        return "Failed to send email"  # Return a failure message

# Checks if the token expired. If so, deletes the entry.
def check_expiration(email):
    entry = forgot_password_collection.find_one({"email": email})
    if entry and current_time_gmt3() > entry['expiration']:
        delete_reset_token_entry(email)
        return False
    return True

# Checks if the provided token matches the generated one, stored in the database.
def check_reset_token(email, provided_token):
    entry = forgot_password_collection.find_one({"email": email})
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

