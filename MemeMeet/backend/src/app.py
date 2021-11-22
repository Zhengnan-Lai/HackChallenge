from collections import namedtuple
import json

from db import db
from db import Posts
from db import Users
from db import Tags
from flask import Flask
from flask import request

app = Flask(__name__)
db_filename = "meme.db"

app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///%s" % db_filename
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
app.config["SQLALCHEMY_ECHO"] = False

db.init_app(app)
with app.app_context():
    db.create_all()

def success_response(data, code=200):
    return json.dumps(data), code


def failure_response(message, code=404):
    return json.dumps({"error": message}), code

# your routes here

@app.route("/")
@app.route("/post/", methods=["GET"])
def get_posts():
    list = []
    posts = Posts.query.all()
    for p in posts:
        to_user_id = p.user_id
        to_user = Users.query.filter_by(id=to_user_id).first()
        to_tag_id = p.tag_id
        to_tag = Tags.query.filter_by(id=to_tag_id).first()
        list.append(p.serialize(to_user, to_tag))
    return success_response(
        {"posts": list}
    )

# This Route is for DEVELOPER Only
# Users are NOT Allowed to Add Tags
# Do NOT Show this Route to Users
@app.route("/post/", methods=["POST"])
def create_tag():
    body = json.loads(request.data)
    new_tagname =body.get("tag")
    if new_tagname is None:
        return failure_response("No tag name!", 400)
    new_tag = Tags(
        tag = new_tagname
    )
    db.session.add(new_tag)
    db.session.commit()
    return success_response(new_tag.serialize(), 201)

@app.route("/user/", methods=["POST"])
def create_user():
    body = json.loads(request.data)
    new_username = body.get("name")
    if new_username is None:
        return failure_response("No user name!", 400)
    elif Users.query.filter_by(name=new_username).first() is not None:
      	return failure_response("Replicate name!", 400)
    new_user = Users(
        name = new_username
    )
    db.session.add(new_user)
    db.session.commit()
    return success_response(new_user.serialize(), 201)

@app.route("/post/tag/<int:tag_id>/", methods=["GET"])
def get_posts_by_tag(tag_id):
    tag = Tags.query.filter_by(id=tag_id).first()
    if tag is None:
        return failure_response("Tag not found!")
    return success_response(tag.serialize())

@app.route("/post/tag/<int:tag_id>/", methods=["POST"])
def post_to_tag(tag_id):
    to_tag = Tags.query.filter_by(id=tag_id).first()
    if to_tag is None:
        return failure_response("Tag not found!")

    body = json.loads(request.data)

    new_caption = body.get("caption")
    if new_caption is None:
        return failure_response("No caption provided by user!", 400)
    new_image = body.get("image")
    if new_image is None:
        return failure_response("No image provided by user!", 400)
    to_user_id = body.get("user_id")
    if to_user_id is None:
        return failure_response("No user_id provided by front-end developer!", 400)
    if Users.query.filter_by(id=to_user_id).first() is None:
      	return failure_response("No such user!")

    new_post = Posts(
        caption = new_caption,
        image = new_image,
      	user_id = to_user_id,
      	tag_id = tag_id
    )
    db.session.add(new_post)

    to_user = Users.query.filter_by(id=to_user_id).first()
    to_user.posts.append(new_post)
    to_tag.posts.append(new_post)

    db.session.commit()
    return success_response(new_post.serialize(to_tag, to_user), 201)

# @app.route("/post/post_id/", methods=["POST"])
# def create_course():
#     body = json.loads(request.data)
#     new_code=body.get("code")
#     if new_code is None:
#         return failure_response("No course code!", 400)
#     new_name=body.get("name")
#     if new_name is None:
#         return failure_response("No course name!", 400)
#     new_course = Courses(
#         code = new_code,
#         name = new_name
#     )
#     db.session.add(new_course)
#     db.session.commit()
#     return success_response(new_course.serialize(), 201)



# @app.route("/api/courses/<int:course_id>/", methods=["DELETE"])
# def delete_course(course_id):
#     course = Courses.query.filter_by(id=course_id).first()
#     if course is None:
#         return failure_response("Course not found!")
#     db.session.delete(course)
#     db.session.commit()
#     return success_response(course.serialize())

# @app.route("/api/users/", methods=["POST"])
# def create_user():
#     body = json.loads(request.data)
#     new_name = body.get("name")
#     if new_name is None:
#         return failure_response("No user name!", 400)
#     new_netid = body.get("netid")
#     if new_netid is None:
#         return failure_response("No user netid!", 400)
#     new_user = Users(
#         name = new_name,
#         netid = new_netid
#     )
#     db.session.add(new_user)
#     db.session.commit()
#     return success_response(new_user.serialize(), 201)

# @app.route("/api/users/<int:user_id>/", methods=["GET"])
# def get_user(user_id):
#     user = Users.query.filter_by(id=user_id).first()
#     if user is None:
#         return failure_response("User not found!")
#     return success_response(user.serialize())

# @app.route("/api/courses/<int:course_id>/add/", methods=["POST"])
# def add_user_to_course(course_id):
#     body = json.loads(request.data)

#     add_user_id = body.get("user_id")
#     if add_user_id is None:
#         return failure_response("No user id!", 400)

#     add_type = body.get("type")
#     if add_type is None:
#         return failure_response("No user type!", 400)

#     add_user = Users.query.filter_by(id=add_user_id).first()
#     if add_user is None:
#         return failure_response("User not found!")

#     to_course = Courses.query.filter_by(id=course_id).first()
#     if to_course is None:
#         return failure_response("Course not found!")

#     if add_type == "student":
#         to_course.students.append(add_user)
#     else:
#         to_course.instructors.append(add_user)

#     db.session.commit()
#     return success_response(to_course.serialize())


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)