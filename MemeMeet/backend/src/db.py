# Hackathon
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.sql import func
# from datatime import datetime

db = SQLAlchemy()


# PA5

# from flask_sqlalchemy import SQLAlchemy

# db = SQLAlchemy()

# your classes here

# Association Table: Instructors
# association_table_instructors = db.Table(
#     "association_instructors",
#     db.Model.metadata,
#     db.Column("instructor_user_id", db.Integer, db.ForeignKey("users.id")),
#     db.Column("course_id", db.Integer, db.ForeignKey("courses.id")),
# )

# Association Table: Students
# association_table_students = db.Table(
#     "association_students",
#     db.Model.metadata,
#     db.Column("student_user_id", db.Integer, db.ForeignKey("users.id")),
#     db.Column("course_id", db.Integer, db.ForeignKey("courses.id")),
# )


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

    def sub_u_serialize(self, to_tag):
        return {
            "id": self.id,
            "caption": self.caption,
          	"image": self.image,
            # "post_time": self.post_time
          	"tag": to_tag.sub_p_serialize()
        }
