import os
from airflow import configuration as conf

from flask_appbuilder.security.manager import AUTH_OAUTH

PREFERRED_URL_SCHEME = "https"

SQLALCHEMY_DATABASE_URI = conf.get("core", "SQL_ALCHEMY_CONN")

AUTH_TYPE = AUTH_OAUTH

AUTH_USER_REGISTRATION = True

AUTH_USER_REGISTRATION_ROLE = "UserBase"

OAUTH_PROVIDERS = [
    {
        "name": "google",
        "icon": "fa-google",
        "token_key": "access_token",
        "whitelist": ["@tiki.vn"],
        "remote_app": {
            "client_id": os.environ.get("GOOGLE_CLIENT_KEY"),
            "client_secret": os.environ.get("GOOGLE_CLIENT_SECRET"),
            "client_kwargs": {"scope": "email profile"},
            "request_token_url": None,
            "access_token_url": "https://accounts.google.com/o/oauth2/token",
            "authorize_url": "https://accounts.google.com/o/oauth2/auth",
            "api_base_url": "https://www.googleapis.com/oauth2/v2/",
        },
    },
]
