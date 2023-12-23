from fastapi import APIRouter, HTTPException, Depends, Body, Query, Request
from fastapi.responses import HTMLResponse
from Services.forgot_password_service import send_reset_password_email, reset_password

router = APIRouter()

@router.post("/send", status_code=200)
async def send_reset_password_verification_email(email: str = Query(None, description="Email associated with the user")):
    result = send_reset_password_email(email)
    
    if result == "Email sent successfully":
        return {"message": "Password reset verification email sent."}
    elif result == "Invalid email address":
        raise HTTPException(status_code=400, detail="Invalid email address")
    else:
        raise HTTPException(status_code=500, detail="Failed to send password reset verification email")

@router.get("/reset_password", status_code=200, response_class=HTMLResponse)
async def reset_password_page(request: Request, email: str = Query(None, description="Email associated with the user"), token: str = Query(None, description="Verification token")):
    # Render the HTML page for resetting the password

    # change based on the server url.
    base_url_backend = "http://3.218.226.215:8000"
    base_url_frontend = "http://3.218.226.215:3000"
     
    template = f"""
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>DAPP | Reset Password</title>

        <style>
            body {{
                font-family: Arial, sans-serif;
                background-color: #f4f4f4;
                text-align: center;
                padding: 50px;
            }}

            h1 {{
                color: #333;
            }}

            div {{
                background-color: white;
                margin: auto;
                width: 300px;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            }}

            input[type=password] {{
                width: 100%;
                padding: 10px;
                margin: 10px 0;
                border: 1px solid #ddd;
                border-radius: 4px;
                box-sizing: border-box;
            }}

            button {{
                width: 100%;
                padding: 10px;
                background-color: #104abf;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 16px;
                margin-top: 5px;
            }}

            button:hover {{
                background-color: #104569;
            }}

            #resultMessage {{
                margin-top: 20px;
                color: red;
            }}
            </style>
    </head>
    <body>

        <?xml version="1.0" encoding="UTF-8"?>
        <svg id="Layer_1" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1116.53 293.14" style="width: 200px; height: auto;">
        <defs>
            <style>
            .cls-1 {{
                fill: #104abf;
                stroke-width: 0px;
            }}
            </style>
        </defs>
        <path class="cls-1" d="m268.52,95.09c-4.55,1.36-9.12,2.66-13.69,3.97-2.39.69-4.79,1.37-7.18,2.07-.27.08-.48.12-.63.12-.12,0-.23-.02-.4-.12-7.78-4.45-16.58-7.4-28.53-9.59-5.39-.98-9.8-1.44-13.89-1.44-.71,0-1.42.01-2.13.04-5.38.22-10.05,2.56-12.8,6.43-2.38,3.35-3.16,7.54-2.26,11.95-.85-.34-1.71-.71-2.59-1.09-.85-.37-1.7-.73-2.56-1.07l-1.93-.79c-4.23-1.74-8.61-3.54-13.42-4.15-1.26-.16-2.41-.24-3.52-.24-3.26,0-6.08.68-8.61,2.09-5.28,2.93-7.9,8.41-7.05,14.7-1.39-.36-2.76-.65-4.22-.76-.39-.03-.78-.04-1.17-.04-4.25,0-8.27,1.82-11.02,4.98-2.76,3.18-4,7.44-3.4,11.71.38,2.76,1.31,5.38,2.76,7.79-1.8,1.54-3.26,3.39-4.36,5.48-2.45,4.65-1.86,9.36,1.69,13.63,4.08,4.89,9.02,8.6,15.19,12.66,1.15.76,2.33,1.48,3.51,2.2.97.59,1.94,1.18,2.89,1.8.44.28.77.87,1.15,1.55.16.29.33.58.5.85l1.49,2.38c1.73,2.76,3.51,5.62,5.51,8.31l-.85,2.12c-3.63,9.1-7.38,18.51-13.19,26.6-2.02,2.82-3.86,4.27-6.13,4.83-.59.14-1.2.22-1.83.22-2.45,0-4.89-1.1-6.35-2.87-.75-.91-1.57-2.4-1.2-4.37.31-1.65,1.04-3.28,1.82-5.02.34-.75.67-1.49.98-2.25,1.65-4.06,3.09-8.25,4.47-12.3l.82-2.4c.84-2.44-.13-3.93-1.1-4.74-.69-.59-1.53-.9-2.4-.9-.56,0-1.13.13-1.71.38-1.53.67-1.97,2.05-2.24,2.87-2.46,7.33-4.47,12.78-6.55,17.78l-.08.19c-1.29,3.09-2.62,6.29-4.31,9.2-1.82,3.15-4.21,4.68-7.3,4.68,0,0-.3,0-.45-.01-3.61-.17-6.12-1.84-7.67-5.11-.79-1.67-.9-3.35-.32-5.13,1.84-5.68,4.22-13.19,6.33-20.89.43-1.55.36-2.8-.22-3.81-.33-.58-1-1.35-2.3-1.71-.39-.1-.77-.16-1.14-.16-1.27,0-2.98.61-3.85,3.53l-1.28,4.32c-3.21,10.87-6.53,22.11-11.04,32.69-1.46,3.43-3.93,5.18-7.55,5.35-.19,0-.38.01-.57.01-3.13,0-5.54-1.26-7.14-3.75-1.18-1.83-1.49-3.8-.93-5.85,3.4-12.3,6.84-24.57,10.32-36.84.82-2.88-.71-4.5-2.42-5.06-.45-.15-.91-.23-1.36-.23-1.72,0-3.12,1.15-3.66,3.01l-1.38,4.76c-1.31,4.53-2.62,9.07-3.89,13.62-1.58,5.65-3.14,11.31-4.71,16.97-1.59,5.74-3.18,11.49-4.78,17.23-1.24,4.46-2.53,8.9-3.81,13.35l-.41,1.41c-.55,1.9-2.43,3.18-4.68,3.18-.61,0-1.23-.1-1.83-.29-2.9-.92-5.07-4.11-5.3-7.76-.46-7.17.84-14.38,2.1-21.35l.08-.43c1.74-9.59,3.88-19.26,6.38-28.72,1.48-5.62,3.14-11.31,4.74-16.81.58-1.98,1.15-3.97,1.72-5.95,1.64-5.7,3.3-11.41,4.95-17.11l3.95-13.65c.1-.36.1-.66.1-.93v-.37s.07-3.2.07-3.2l-6.28,3.3c-2.43,1.28-4.79,2.52-7.16,3.75l-1.17.6c-2.84,1.47-5.78,2.99-8.7,4.36-.6.28-1.22.42-1.84.42-2.27,0-4.11-1.82-4.31-4.24.02,0,.07-.07.17-.16,1.36-1.4,2.93-2.56,4.6-3.77,2.32-1.71,4.53-3.63,5.59-6.58,1.76-4.95,4.82-9.07,8.07-13.43l1.31-1.77c4.53-6.17,9.47-12.38,14.66-18.44,3.55-4.13,8.23-6.53,12.33-8.32,10.09-4.42,20.58-8.4,30.72-12.26l3.38-1.28c1.88-.72,3.78-1.37,5.68-2.03,4.79-1.66,9.73-3.37,14.29-6.01,2.05-1.19,4.1-2.4,6.14-3.6,4.34-2.57,8.83-5.22,13.34-7.63,7.29-3.88,15.31-7.66,24.54-11.55,7.05-2.96,14.25-5.6,20.36-7.79,8.83-3.15,17.89-5.93,25.58-8.22,2.46-.73,4.95-1.53,7.53-2.41C212.6,11.89,180.99,0,146.57,0H0v184.9c1.81-.93,3.6-1.82,5.43-2.78,6.14-3.19,12.68-6.65,19.99-10.58,9.79-5.25,20.27-11.08,31.99-17.8-1.4,4.88-2.74,9.53-4.1,14.18l-.12.41c-4.94,17.01-10.06,34.6-13.44,52.38l-.3,1.59c-1.48,7.77-3.02,15.8-2.58,23.99.09,1.7-.3,2.54-1.73,3.72-7.52,6.21-15.01,12.46-22.5,18.7-4.22,3.52-8.43,7.03-12.66,10.54v13.89h146.57c80.95,0,146.57-65.62,146.57-146.57h0c0-19.68-3.91-38.44-10.94-55.58-4.44,1.33-8.97,2.69-13.68,4.1Zm-77.81,104.83c-2.22,3.52-4.93,6.96-9.74,8.31-6.2,1.73-12.44,2.58-19.08,2.58-1.28,0-2.58-.03-3.89-.1,2.67-5.3,4.75-10.88,6.76-16.29.49-1.31.98-2.63,1.48-3.94.96-2.29-.38-3.36-1.01-3.71-1.16-.66-1.89-1.82-2.73-3.17-.23-.37-.46-.73-.69-1.09-1.79-2.67-3.46-5.34-4.97-7.94-1.09-1.89-2.64-3.37-4.87-4.68-6.63-3.86-11.76-7.6-16.13-11.77-1.41-1.33-2.48-2.5-3.18-3.9-.44-.89-.47-1.75-.1-2.78.53-1.46,1.52-2.78,3.11-4.13.33-.28.69-.42,1.1-.42.3,0,.63.07.98.22l1.45.6c12.82,5.27,26.08,10.72,37.63,18.83,4.65,3.27,9.36,6.94,11.41,12.67,1.05,2.93,1.75,6.04,2.02,9.01.59,4,.67,7.55.67,10.51,0,.57-.07.96-.22,1.18Zm16.95-20.73c-.29,2.43-1.52,4.01-4,5.13-3.07,1.39-4.28,1.98-5.63,3.29-.26-1.97-.55-3.92-.92-5.85-1.51-7.86-5.91-14.37-13.46-19.91-8.1-5.95-17.56-11.21-28.92-16.07-3.4-1.45-6.8-2.88-10.21-4.31l-3.38-1.41c-.31-.15-.67-.33-1.16-.37-2.69-.3-3.6-2.1-4.49-5.78-.59-2.47-.2-4.75,1.1-6.4,1.25-1.58,3.2-2.45,5.5-2.45.62,0,1.26.06,1.91.19,3.27.63,6.3,2,9.49,3.45l1.86.84c2.03.9,4.07,1.78,6.1,2.67,5.02,2.18,10.2,4.44,15.2,6.84,4.55,2.18,8.92,4.89,13.38,7.71,2.87,1.81,5.72,3.66,8.57,5.51l1.57,1.02c3.38,2.2,5.34,5.01,6.19,8.84,1.46,6.63,1.87,12.05,1.28,17.06Zm17-29.22c-.49,2.18-1.44,3.83-2.93,5.02-2.52,2.02-5.04,4.03-7.61,6.1-.16-.65-.31-1.29-.45-1.93-1.02-4.62-3.61-8.37-7.92-11.48-11.07-7.97-21.32-13.98-31.35-18.39-2.05-.9-4.1-1.8-6.15-2.69-3.63-1.59-7.27-3.17-10.89-4.79-.65-.29-.84-.5-.98-.84-1.47-3.45-2.08-6.29-.24-8.95,1.42-2.06,3.49-2.94,6.91-2.94.5,0,1.01.02,1.53.05,3.7.21,7.17,1.5,10.07,2.74,3.84,1.64,7.78,3.19,11.59,4.69,5.76,2.27,11.71,4.61,17.38,7.28,6.1,2.86,12.41,5.82,18.25,9.44,1.8,1.12,2.71,2.48,3.05,4.57.69,4.19.6,8.16-.27,12.13Zm27.05-28.32c-.88,2.77-2.33,5.35-4.42,7.87-.91,1.11-2.13,1.79-3.82,2.15-2.53.55-5.06,1.12-7.5,1.67l-2.81.63-.36.05c-.29.04-.52.07-.74.13-.55-2.57-2.03-4.72-4.4-6.38-5.48-3.85-11.52-6.71-17.35-9.48l-1.18-.56c-2.79-1.33-5.66-2.57-8.44-3.77l-1.03-.45c-3.88-1.68-6.2-7.17-4.67-11.08.85-2.19,2.47-3.49,5.1-4.08,1.37-.31,2.84-.46,4.48-.46,2.39,0,4.87.33,7.06.61,12.33,1.63,21.88,4.44,30.06,8.84,4.34,2.34,7.43,5.19,9.47,8.73,1.02,1.78,1.2,3.56.56,5.58Z"/>
        <path class="cls-1" d="m489.4,66.39c-15.21-7.6-32.77-11.4-52.7-11.4h-83.37v183.53h83.37c19.93,0,37.49-3.8,52.7-11.41,15.21-7.6,27.09-18.26,35.66-31.99,8.56-13.72,12.85-29.84,12.85-48.37s-4.28-34.65-12.85-48.37c-8.57-13.72-20.45-24.38-35.66-31.99Zm-1.97,110.64c-4.98,8.65-11.97,15.3-20.97,19.93-9.01,4.63-19.62,6.95-31.86,6.95h-38.54v-114.31h38.54c12.23,0,22.85,2.32,31.86,6.95,9,4.63,15.99,11.23,20.97,19.79,4.98,8.57,7.47,18.71,7.47,30.41s-2.49,21.63-7.47,30.28Z"/>
        <path class="cls-1" d="m624.68,54.98l-81.54,183.53h43.52l16.35-39.33h85.01l16.35,39.33h44.57l-82.33-183.53h-41.95Zm-8.25,111.95l29.1-69.96,29.09,69.96h-58.19Z"/>
        <path class="cls-1" d="m890.01,62.98c-11.89-5.33-26.05-8-42.47-8h-79.44v183.53h42.74v-50.6h36.71c16.43,0,30.58-2.71,42.47-8.13,11.88-5.42,21.06-13.07,27.53-22.94,6.46-9.87,9.7-21.72,9.7-35.52s-3.24-25.43-9.7-35.4c-6.47-9.96-15.65-17.61-27.53-22.94Zm-15.47,81.93c-6.65,5.6-16.43,8.39-29.37,8.39h-34.35v-63.71h34.35c12.93,0,22.72,2.75,29.37,8.26,6.64,5.51,9.96,13.33,9.96,23.47s-3.32,18-9.96,23.6Z"/>
        <path class="cls-1" d="m1106.83,85.92c-6.47-9.96-15.65-17.61-27.53-22.94-11.89-5.33-26.05-8-42.47-8h-79.44v183.53h42.73v-50.6h36.71c16.43,0,30.58-2.71,42.47-8.13,11.88-5.42,21.06-13.07,27.53-22.94,6.47-9.87,9.7-21.72,9.7-35.52s-3.23-25.43-9.7-35.4Zm-43,58.99c-6.64,5.6-16.43,8.39-29.36,8.39h-34.35v-63.71h34.35c12.93,0,22.72,2.75,29.36,8.26s9.97,13.33,9.97,23.47-3.32,18-9.97,23.6Z"/>
        </svg>

        <h1>Reset Password</h1>
    
        <div>
            <p>Enter your new password and confirm it.<br>
            It must contain at least one digit.</p>
            <input type="password" id="newPassword" placeholder="Enter your new password">
            <input type="password" id="confirmPassword" placeholder="Confirm your new password">
            <button onclick="resetPassword()">Reset Password</button>
        </div>

        <div id="resultMessage" style="display: none;"></div>

        <script>

            function is_valid_password(password) {{
                return /[0-9]/.test(password); // Checks if there is at least one digit
            }}

            async function resetPassword() {{
                const email = "{email}"; // Retrieve email from the context
                const token = "{token}"; // Retrieve token from the context
                const newPassword = document.getElementById("newPassword").value;
                const confirmPassword = document.getElementById("confirmPassword").value;

                if (newPassword !== confirmPassword) {{
                    document.getElementById("resultMessage").style.display = "block"
                    document.getElementById("resultMessage").innerText = "Passwords do not match.";
                    return;
                }}

                if (!is_valid_password(newPassword)) {{
                     document.getElementById("resultMessage").style.display = "block"
                    resultMessage.innerText = "Password is not valid. It must contain at least one digit.";
                    return;
                }}

                const data = {{ new_password: newPassword }};

                const response = await fetch(`/api/forgot_password/reset?email=${{encodeURIComponent(email)}}&token=${{encodeURIComponent(token)}}`, {{
                    method: "POST",
                    body: JSON.stringify(data),
                    headers: {{
                        'Content-Type': 'application/json'
                    }}
                }});

                if (response.status === 200) {{
                     document.getElementById("resultMessage").style.display = "block"
                    document.getElementById("resultMessage").innerText = "Password reset successful. You are being redirected to the login page unless you close this tab...";
                    document.getElementById("resultMessage").style.color = "#104569";
                    setTimeout(function() {{
                        window.location.href = '{base_url_frontend}/login';
                    }}, 3000);
                }} else {{
                     document.getElementById("resultMessage").style.display = "block"
                    document.getElementById("resultMessage").innerText = "Password reset failed: Invalid token or token expired. Please close this tab and resend reset request.";
                }}
            }}
    </script>
    </body>
    </html>
    """
    return HTMLResponse(content=template)

@router.post("/reset", status_code=200)
async def reset_user_password(email: str = Query(..., description="Email associated with the user"),
                             token: str = Query(..., description="Verification token obtained from email"),
                             new_password_data: dict = Body(..., description="New password data")):
    
    new_password = new_password_data.get('new_password')

    if reset_password(email, token, new_password):
        return {"message": "Password reset successful."}
    else:
        raise HTTPException(status_code=400, detail="Invalid token or token expired")

