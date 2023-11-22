from fastapi import APIRouter, Response, Depends, status
from http import HTTPStatus
from Models.user_profile_model import *
from Models.user_model import Error
import Services.uprofile_languages_service as uprofile_languages_service
from Services.build_API_returns import *
import Services.authentication_service as authentication_service



router = APIRouter()


@router.get("/languages", responses={
    status.HTTP_200_OK: {"model": Languages},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
})
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


@router.post("/languages/add-language", responses={
    status.HTTP_200_OK: {"model": Languages},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
})
async def add_a_language_currentuser(user_language: UserLanguage, response: Response, anyuser:str= None, current_username: str = Depends(authentication_service.get_current_username)):
    """
    Add a language skill to the current user. If the language skill is allready set for the user, level is updated (if different)

    **Parameters**

    - **Language info++: such that:
        - **language**: Any language like German, Spanish, Zulu etc.
        - **language_level**: One of     beginner, intermediate, advanced. native

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

@router.post("/languages/delete-language", responses={
    status.HTTP_200_OK: {"model": Languages},
    status.HTTP_404_NOT_FOUND: {"model": Error},
    status.HTTP_401_UNAUTHORIZED: {"model": Error}
})
async def delete_current_users_language(user_language: UserLanguage, response: Response, anyuser:str= None, current_username: str = Depends(authentication_service.get_current_username)):
    """
    Delete a language skill of the current user

    **Parameters**

    - **Language info++: such that:
        - **language**: Any language like German, Spanish, Zulu etc.

    """

    try:
        user_language.username = current_username
        result = uprofile_languages_service.delete_user_language(user_language)
        response.status_code = HTTPStatus.OK
        return json.loads(result)
    except ValueError as err:
        response.status_code = HTTPStatus.NOT_FOUND
        err_json =  create_json_for_error("User language not fetched", str(err))
        return json.loads(err_json)
