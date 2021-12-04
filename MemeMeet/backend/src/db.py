# Hackathon
import datetime
import hashlib
import os
import bcrypt
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.sql import func

db = SQLAlchemy()

# Tags: one tag to many posts
# id, tag, posts

class Tags(db.Model):
    __tablename__ = "tags"
    id = db.Column(db.Integer, primary_key=True)
    tag = db.Column(db.String, nullable=False)
    posts = db.relationship("Posts", cascade="delete")

    def __init__(self, **kwargs):
        self.tag = kwargs.get("tag")

    def serialize(self):
        list = []
        for p in self.posts:
            to_user_id = p.user_id
            to_user = Users.query.filter_by(id=to_user_id).first()
            list.append(p.sub_t_serialize(to_user))
        return {
            "id": self.id,
            "tag": self.tag,
            "posts": list
        }

    # return the serialized relationships WITHOUT the posts field
    def sub_p_serialize(self):
        return {
            "id": self.id,
            "tag": self.tag
        }


# Users: one user to many posts
# id, name, posts

class Users(db.Model):
    __tablename__ = "users"
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String, nullable=False)
    posts = db.relationship("Posts", cascade="delete")

    def __init__(self, **kwargs):
        self.name = kwargs.get("name")

    def serialize(self):
        return {
            "id": self.id,
            "name": self.name,
            "posts": [p.sub_u_serialize() for p in self.posts]
        }

    # return the serialized relationships WITHOUT the posts field
    def sub_p_serialize(self):
        return {
            "id": self.id,
            "name": self.name
        }


# Posts: one post to only one user
# Posts: one post to only one tag
# id, caption, image, user_id, tag_id

class Posts(db.Model):
    __tablename__ = "posts"
    id = db.Column(db.Integer, primary_key=True)
    caption = db.Column(db.String, nullable=False)
    image = db.Column(db.String, nullable=False)
    user_id = db.Column(db.Integer, db.ForeignKey("users.id"))
    tag_id = db.Column(db.Integer, db.ForeignKey("tags.id"))

    def __init__(self, **kwargs):
        self.caption = kwargs.get("caption")
        self.image = kwargs.get("image")
        self.user_id = kwargs.get("user_id")
        self.tag_id = kwargs.get("tag_id")

    def serialize(self, to_user, to_tag):
        return {
            "id": self.id,
            "caption": self.caption,
            "image": self.image,
            "user": to_user.sub_p_serialize(),
            "tag": to_tag.sub_p_serialize()
        }

    def sub_t_serialize(self, to_user):
        return {
            "id": self.id,
            "caption": self.caption,
            "image": self.image,
            "user": to_user.sub_p_serialize()
        }

    def sub_u_serialize(self):
        return {
            "id": self.id,
            "caption": self.caption,
            "image": self.image
        }


class User(db.Model):
    __tablename__ = "user"
    id = db.Column(db.Integer, primary_key=True)

    # User information
    username = db.Column(db.String, nullable=False, unique=True)
    password_digest = db.Column(db.String, nullable=False)

    # Session information
    session_token = db.Column(db.String, nullable=False, unique=True)
    session_expiration = db.Column(db.DateTime, nullable=False)
    update_token = db.Column(db.String, nullable=False, unique=True)

    def __init__(self, **kwargs):
        self.username = kwargs.get("username")
        self.password_digest = bcrypt.hashpw(kwargs.get(
            "password").encode("utf8"), bcrypt.gensalt(rounds=13))
        self.renew_session()

    # Used to randomly generate session/update tokens
    def _urlsafe_base_64(self):
        return hashlib.sha1(os.urandom(64)).hexdigest()

    # Generates new tokens, and resets expiration time
    def renew_session(self):
        self.session_token = self._urlsafe_base_64()
        self.session_expiration = datetime.datetime.now() + datetime.timedelta(days=1)
        self.update_token = self._urlsafe_base_64()

    def verify_password(self, password):
        return bcrypt.checkpw(password.encode("utf8"), self.password_digest)

    # Checks if session token is valid and hasn't expired
    def verify_session_token(self, session_token):
        return session_token == self.session_token and datetime.datetime.now() < self.session_expiration

    def verify_update_token(self, update_token):
        return update_token == self.update_token
