# import datetime
# from unittest import mock
# from fastapi import HTTPException
# from Services.forgot_password_service import (
#     create_token,
#     create_reset_token_entry,
#     delete_reset_token_entry,
#     send_reset_password_email,
#     check_reset_token,
#     reset_user_password,
#     check_expiration,
# )
# from Services.authentication_service import get_password_hash

# # Mock MongoDB collection
# class MockMongoCollection:
#     def __init__(self):
#         self.data = []

#     def find_one(self, query):
#         for item in self.data:
#             if all(item[key] == query[key] for key in query):
#                 return item
#         return None

#     def insert_one(self, item):
#         self.data.append(item)

#     def delete_one(self, query):
#         self.data = [item for item in self.data if not all(item[key] == query[key] for key in query)]

#     def update_one(self, query, update_operation):
#         for item in self.data:
#             if all(item[key] == query[key] for key in query):
#                 item.update(update_operation["$set"])
#                 return MockUpdateResult()


# class MockUpdateResult:
#     modified_count = 1


# # Mock get_password_hash function
# def mock_get_password_hash(password):
#     return password  # Mock for simplicity


# def test_create_token():
#     token = create_token()
#     assert len(token) == 6
#     assert token.isdigit()


# def test_create_reset_token_entry():
#     email = "test@example.com"
#     token = "123456"
#     expiration_time = datetime.datetime.now() + datetime.timedelta(minutes=10)
#     reset_token_entry = {
#         "email": email,
#         "token": token,
#         "expiration": expiration_time,
#     }

#     with mock.patch("Services.forgot_password_service.user_collection", new_callable=MockMongoCollection):
#         result = create_reset_token_entry(email, token)
#         assert result is True
#         assert len(MockMongoCollection().data) == 1
#         assert MockMongoCollection().data[0] == reset_token_entry

#         # Test with invalid email
#         result = create_reset_token_entry("nonexistent@example.com", token)
#         assert result is False


# def test_delete_reset_token_entry():
#     email = "test@example.com"
#     token = "123456"
#     expiration_time = datetime.datetime.now() + datetime.timedelta(minutes=10)
#     reset_token_entry = {
#         "email": email,
#         "token": token,
#         "expiration": expiration_time,
#     }

#     with mock.patch("Services.forgot_password_service.forgot_password_collection", new_callable=MockMongoCollection):
#         create_reset_token_entry(email, token)
#         assert len(MockMongoCollection().data) == 1
#         delete_reset_token_entry(email)
#         assert len(MockMongoCollection().data) == 0


# def test_send_reset_password_email():
#     email = "test@example.com"

#     with mock.patch("Services.forgot_password_service.create_reset_token_entry"):
#         result = send_reset_password_email(email)
#         assert result == "Email sent successfully"

#     with mock.patch("Services.forgot_password_service.create_reset_token_entry", return_value=False):
#         result = send_reset_password_email(email)
#         assert result == "Invalid email address"


# def test_check_expiration():
#     email = "test@example.com"
#     token = "123456"
#     expiration_time = datetime.datetime.now() + datetime.timedelta(minutes=10)
#     reset_token_entry = {
#         "email": email,
#         "token": token,
#         "expiration": expiration_time,
#     }

#     with mock.patch("Services.forgot_password_service.forgot_password_collection", new_callable=MockMongoCollection):
#         create_reset_token_entry(email, token)
#         assert check_expiration(email) is True

#         # Test with expired token
#         expiration_time = datetime.datetime.now() - datetime.timedelta(minutes=10)
#         reset_token_entry["expiration"] = expiration_time
#         MockMongoCollection().data[0] = reset_token_entry
#         assert check_expiration(email) is False

#         # Test with nonexistent token
#         assert check_expiration("nonexistent@example.com") is True


# def test_check_reset_token():
#     email = "test@example.com"
#     token = "123456"
#     expiration_time = datetime.datetime.now() + datetime.timedelta(minutes=10)
#     reset_token_entry = {
#         "email": email,
#         "token": token,
#         "expiration": expiration_time,
#     }

#     with mock.patch("Services.forgot_password_service.forgot_password_collection", new_callable=MockMongoCollection):
#         create_reset_token_entry(email, token)
#         assert check_reset_token(email, token) is True

#         # Test with incorrect token
#         assert check_reset_token(email, "654321") is False

#         # Test with nonexistent token
#         assert check_reset_token("nonexistent@example.com", token) is False


# def test_reset_user_password():
#     email = "test@example.com"
#     token = "123456"
#     new_password = "new_password"
#     expiration_time = datetime.datetime.now() + datetime.timedelta(minutes=10)
#     reset_token_entry = {
#         "email": email,
#         "token": token,
#         "expiration": expiration_time,
#     }

#     with mock.patch("Services.forgot_password_service.forgot_password_collection", new_callable=MockMongoCollection):
#         create_reset_token_entry(email, token)
#         with mock.patch("Services.forgot_password_service.user_collection", new_callable=MockMongoCollection):
#             reset_user_password(email, new_password)
#             user = MockMongoCollection().find_one({"email": email})
#             assert user is not None
#             assert user["password"] == get_password_hash(new_password)

#         # Test with nonexistent email
#         with mock.patch("Services.forgot_password_service.user_collection", new_callable=MockMongoCollection):
#             reset_user_password("nonexistent@example.com", new_password)
#             user = MockMongoCollection().find_one({"email": "nonexistent@example.com"})
#             assert user is None


# if __name__ == "__main__":
#     import pytest

#     pytest.main()
