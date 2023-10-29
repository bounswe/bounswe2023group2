from fastapi import APIRouter, Response, Depends
from http import HTTPStatus
from Models.user_profile_model import *
import Services.uprofile_languages_service as uprofile_languages_service
from Services.build_API_returns import *
import Services.authentication_service as authentication_service



router = APIRouter()


@router.get("/languages", )
async def get_user_and_language_level(response: Response, anyuser:str= None, language:str = None, current_username: str = Depends(authentication_service.get_current_username)):
    """
    Get a user's language skills OR users (with their language level) with a given language OR current user's language skills

    **Parameters**

    - **anyuser**: A User's name
    - **language**: Language to be searched for
    - **current_username**; Automatically given by token. Used for authorization either.
    - Note that:
        - if anyuser is None and language is set, a list of users (in Language info model) will be retured
        - if anyuser is set but language is None, the language skills of anyuser will be returned

    **Returns**

    In a structured json
    - **username**
    - **language**: Any language like German, Spanish, Zulu etc.
    - **language_level**: One of     beginner, intermediate, advanced. native

    Like:
    ~~~
    {
        "user_languages": [
            {
                "username": "mehmetk",
                "language": "Turkish",
                "language_level": "native"
            }
        ]
    }
    ~~~
    Note that any result with no data will return with success but will be an empty array. Like:
    ~~~
    {
        "user_languages": []
    }
    ~~~

    **Error conditions**

    The responce code of the API result is set according to the error.

    Possible codes for structured response for now is:
    - **HTTPStatus.NOT_FOUND** (404) <br>
    And the result is formatted like<br>
    ~~~
    {
      "ErrorMessage": "Login required",
      "ErrorDetails": "Login to create resources"
    }
    ~~~

    - **HTTPStatus.UNPROCESSABLE_ENTITY** (422)<br>
    This is FastAPI response when parameters do not comply with the data model<br>
    The result is formatted like<br>
    ~~~
    {
      "detail": [
        {
          "loc": [
            "string",
            0
          ],
          "msg": "string",
          "type": "string"
        }
      ]
    }
    ~~~
    """
    if anyuser is None:
        if language is None:
            username = current_username
        else:
            username = None
    else:
        username = anyuser

    try:
        result = uprofile_languages_service.get_user_language(username, language)
        response.status_code = HTTPStatus.OK
        return json.loads(result)
    except ValueError as err:
        response.status_code = HTTPStatus.NOT_FOUND
        return create_json_for_error("User language not fetched", str(err))


@router.post("/languages/add-language", )
async def add_a_language_currentuser(user_language: UserLanguage, response: Response, anyuser:str= None, current_username: str = Depends(authentication_service.get_current_username)):
    """
    Add a language skill to the current user. If the language skill is allready set for the user, level is updated (if different)

    **Parameters**

    - **Language info++: such that:
        - **language**: Any language like German, Spanish, Zulu etc.
        - **language_level**: One of     beginner, intermediate, advanced. native

    **Returns**

    - **Language Info**: As set by the operation
    """

    try:
        user_language.username = current_username
        result = uprofile_languages_service.add_user_language(user_language)
        response.status_code = HTTPStatus.OK
        return json.loads(result)
    except ValueError as err:
        response.status_code = HTTPStatus.NOT_FOUND
        err_json =  create_json_for_error("User language not updated", str(err))
        return json.loads(err_json)

@router.delete("/languages/delete-language", )
async def delete_current_users_language(user_language: UserLanguage, response: Response, anyuser:str= None, current_username: str = Depends(authentication_service.get_current_username)):
    """
    Delete a language skill of the current user

    **Parameters**

    - **Language info++: such that:
        - **language**: Any language like German, Spanish, Zulu etc.

    **Returns**
    - **Language Info**: The deleted info (without skill)
    """

    try:
        user_language.username = current_username
        result = uprofile_languages_service.delete_user_language(user_language)
        response.status_code = HTTPStatus.OK
        return json.loads(result)
    except ValueError as err:
        response.status_code = HTTPStatus.NOT_FOUND
        return create_json_for_error("User language not fetched", str(err))


