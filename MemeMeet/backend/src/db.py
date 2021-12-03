# Hackathon
import datetime
import hashlib
import os
import bcrypt
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.sql import func
# from datatime import datetime

db = SQLAlchemy()

# Tags: like Courses
# Tags: one tag to many posts
# id name posts users


class Tags(db.Model):
    __tablename__ = "tags"
    id = db.Column(db.Integer, primary_key=True)
    tag = db.Column(db.String, nullable=False)
    posts = db.relationship("Posts", cascade="delete")
    # users = db.relationship(
    #     "Users",
    #     secondary=association_table_instructors,
    #     back_populates="courses_to_instructors"
    # )

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

# Users: like Users
# Users: one user to many posts
# id name posts


class Users(db.Model):
    __tablename__ = "users"
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String, nullable=False)
    posts = db.relationship("Posts", cascade="delete")
    # posts_to_user = db.relationship(
    #     "Posts",
    #     secondary = association_table_users,
    #     back_populates="users"
    # )

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


# Posts: like Assignments
# Posts: one post to only one user
# Posts: one post to only one tag
# id caption image post_time tag_id user_id

class Posts(db.Model):
    __tablename__ = "posts"
    id = db.Column(db.Integer, primary_key=True)
    caption = db.Column(db.String, nullable=False)
    image = db.Column(db.String, nullable=False)
    # Reference: to implement post_time, we referenced these pages:
    # https://stackoverflow.com/questions/13370317/sqlalchemy-default-datetime
    # https://stackoverflow.com/questions/10201648/sqlalchemy-wont-accept-datetime-datetime-now-value-in-a-datetime-column
    # post_time = db.Column(DateTime(timezone=True), server_default=func.now(), nullable=False)
    user_id = db.Column(db.Integer, db.ForeignKey("users.id"))
    tag_id = db.Column(db.Integer, db.ForeignKey("tags.id"))

    def __init__(self, **kwargs):
        self.caption = kwargs.get("caption")
        self.image = kwargs.get("image")
        # self.post_time = datetime.datetime.utcnow()
        self.user_id = kwargs.get("user_id")
        self.tag_id = kwargs.get("tag_id")

    def serialize(self, to_user, to_tag):
        return {
            "id": self.id,
            "caption": self.caption,
            "image": self.image,
            # "post_time": self.post_time
            "user": to_user.sub_p_serialize(),
            "tag": to_tag.sub_p_serialize()
        }

    def sub_t_serialize(self, to_user):
        return {
            "id": self.id,
            "caption": self.caption,
            "image": self.image,
            # "post_time": self.post_time
            "user": to_user.sub_p_serialize()
        }

    def sub_u_serialize(self):
        return {
            "id": self.id,
            # "caption": self.caption,
            "image": self.image
            # "post_time": self.post_time
            # "tag": to_tag.sub_p_serialize()
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
