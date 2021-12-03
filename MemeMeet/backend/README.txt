<Backend><Meme&meet>
Name: Alex Jialin Shen, Yutong Zhu
Netid: js2642, yz2676



Variable names:

user_id: int
name: String

post_id: int
tag: String
image: String //url
caption: String



Backend Routes:

FOR DEVELOPER ONLY:
Home page, default
“GET”  /post/  [Home page, default]
	IF developer wants to add one tag (users not allowed to):
	“POST”  /post/ [Home page, default]

FOR USER:
Page1 | Page2 | Page3
“GET”  /post/  [Home page, default]

IF user clicks user profile:
	“GET”  /user/user_id/  [User page]
	IF user clicks back button:
		“GET”  /post/  [Home page]
	ELIF user clicks one post
		“GET”  /user/user_id/post/post_id/ [One post page]
		IF user clicks back button
			“GET”  /user/user_id/  [User page]
ELIF user clicks one tag
	“GET”  /post/tag/tag_id/  [Tag page]
	IF user clicks back button
		“GET”  /post/  [Home page]
		IF user clicks back button
			“GET”  /post/tag/tag_id/  [Tag page]
	ELIF user clicks add_post button
		“POST”  /post/tag/tag_id/ (after successfully add a post, return to Tag page)



Database:

One user to Many posts

One tag to Many posts

One post to One user
One post to One tag

user <--> post <--> tag



API:

1. Get all posts

“GET” /post/
Default homepage with all users’ posts in it.

Response:
<HTTP STATUS CODE 200>
{
    "posts": [
        {
	"id": 1,
	"caption": "I love doge",
	“image”: “https://i.kym-cdn.com/photos/images/original/000/581/296/c09.jpg”,
	“user”: [ <serialized user without posts field>, ... ],
	“tag”: [ <serialized tag without posts field>, ... ]
        },
        {
	"id": 2,
	"caption": "I love cats",
	“image”: “https://i.kym-cdn.com/photos/images/original/001/997/044/841.png”,
	“user”: [ <serialized user without posts field>, ... ],
	“tag”: [ <serialized tag without posts field>, ... ]
        },
        {
	"id": 3,
	"caption": "I love doge, too",
	“image”: “https://i.kym-cdn.com/photos/images/original/000/581/296/c09.jpg”,
	“user”: [ <serialized user without posts field>, ... ],
	“tag”: [ <serialized tag without posts field>, ... ]
        }
        ...
    ]
}

2. Create a tag

“POST” /post/
[Not open to users.] Developers use this to create tags for users to choose from.

Request 1:
{
    "tag": "doge"
}
Response:
<HTTP STATUS CODE 201>
{
    "id": <ID, e.g. 1>,
    "tag": "doge",
    "posts": []
}

Request 2:
{
    "tag": "cat"
}
Response:
<HTTP STATUS CODE 201>
{
    "id": <ID, e.g. 2>,
    "tag": "cat",
    "posts": []
}

If the developer does not provide a tag, respond with an error message "No tag name!" with status code 400.
If the developer creates a tag name that has been created before, respond with failure message "Repeated tag name!" with status code 400.


3. Create a user

“POST” /user/
User profile page -> created a user -> User page of newly created user

Request 1:
{
    "name": "Doge"
}
Response:
<HTTP STATUS CODE 201>
{
    "id": <ID, e.g. 1>,
    "name": "Doge",
    "posts": []
}

Request 2:
{
    "name": "Cat"
}
Response:
<HTTP STATUS CODE 201>
{
    "id": <ID, e.g. 2>,
    "name": "Cat",
    "posts": []
}

If the client creates a username that has been used by someone else, respond with the same response as above with status code 201.

If the client does not provide a name, respond with an error message "No user name!" with status code 400.
// If the client creates a username that has been used by someone else, respond with the error message “Replicate name!” with status code 400.

4. Get all posts by tag

“GET” /post/tag/<int:tag_id>/
Page with posts of the same tag.

Response:
<HTTP STATUS CODE 200>
{
    "id": <tag_id>,
    "tag": <stored tag name for tag with ID tag_id>,
    "posts": [ <serialized posts without tags field>, ... ]
}

If tag is None, respond with an error message "Tag not found!" with status code 404.

5. Post to tag

“POST” /post/tag/<int:tag_id>/
Post to a specific tag (tag_id).

Request 1:
{
    "caption": "I love doge",
    "image": "https://i.kym-cdn.com/photos/images/original/000/581/296/c09.jpg",
    "user_id": <user_id>
}
Response:
<HTTP STATUS CODE 201>
{
    "id": <post_id, e.g. 1>,
    "caption": "I love doge",
    "image": "https://i.kym-cdn.com/photos/images/original/000/581/296/c09.jpg",
    "user":[ <serialized user without posts field>, ... ],
    "tag": [ <serialized tag without posts field>, ... ]
}

Request 2:
{
    "caption": "I love cat",
    "image": "https://i.kym-cdn.com/photos/images/original/001/997/044/841.png",
    "user_id": <user_id>
}
Response:
<HTTP STATUS CODE 201>
{
    "id": <post_id, e.g. 2>,
    "caption": "I love cat",
    "image": "https://i.kym-cdn.com/photos/images/original/001/997/044/841.png",
    "user":[ <serialized user without posts field>, ... ],
    "tag": [ <serialized tag without posts field>, ... ]
}

Request 3:
{
    "caption": "I love doge, too",
    "image": "https://i.kym-cdn.com/photos/images/original/000/581/296/c09.jpg",
    "user_id": <user_id>
}
Response:
<HTTP STATUS CODE 201>
{
    "id": <post_id, e.g. 3>,
    "caption": "I love doge, too",
    "image": "https://i.kym-cdn.com/photos/images/original/000/581/296/c09.jpg",
    "user":[ <serialized user without posts field>, ... ],
    "tag": [ <serialized tag without posts field>, ... ]
}

If tag is None, respond with an error message "Tag not found!" with status code 404.
If the client does not provide a caption, respond with an error message "No caption provided by user!" with status code 400.
If the client does not provide an image (url), respond with an error message "No image provided by user!" with status code 400.
If the front-end developer does not provide a user_id, respond with an error message "No user_id provided by front-end developer!" with status code 400.
If the user_id provided by front-end developer is not a user created before, respond with an error message "No such user!") with status code 404.


6. Register a user

"POST" /register/

Request:
{
    "username": "user1",
    "password": "pw1"
}
Response:
<HTTP STATUS CODE 201>
{
    "Session_token": "<user.session_token>",
    "session_expiration": "<user.session_expiration>",
    "update_token": "<user.update_token>"
}

if username is None or password is None: return failure_response("Invalid username or password", 400).
if username is not None and password is not None, but a new user is still not successfully created (which indicates the same user has already been created before): return failure_response("User already exists", 403).


7. User log-in

"POST" /login/

Request:
{
    "username": "user1",
    "password": "pw1"
}
Response:
<HTTP STATUS CODE 201>
{
    "Session_token": "<user.session_token>",
    "session_expiration": "<user.session_expiration>",
    "update_token": "<user.update_token>"
}

if username is None or password is None: return failure_response("Invalid username or password", 400).
if not valid_creds (username and password do not match): return failure_response("Invalid username or password", 400)


8. Update session (for user log-in authentication)

"POST" /session/

Request:
	Headers:
	Authorization:
	"Bearer <user.update_token>"
Response:
<HTTP STATUS CODE 201>
{
    "Session_token": "<user.session_token>",
    "session_expiration": "<user.session_expiration>",
    "update_token": "<user.update_token>"
} 

if session_token not successfully extracted: return failure_response("<user.session_token>", 404).
if session_token not verified as valid: return failure_response("Invalid session token", 404).


9. Secret message page (for user log-in authentication)

"GET" /secret/"

Request:
	Headers:
	Authorization:
	"Bearer <user.session_token>"
Response:
<HTTP STATUS CODE 200>
"Valid session token"

if session_token not successfully extracted: return failure_response("<user.session_token>", 404).
if session_token not verified as valid: return failure_response("Invalid session token", 404).


10. User profile page

"GET" /user/<int:user_id>/
Page with posts from the same user.

Response:
<HTTP STATUS CODE 200>
{
    "id": <tag_id>,
    "name": <stored user name for user with ID user_id>,
    "posts": [ <serialized posts without user or tag field>, ... ]
}

if user is None (user does not exist, so cannot find user with given user_id): return failure_response("User not found!"). 404.


11. User Profile -> One post page

"GET" /user/<int:user_id>/post/<int:post_id>/

Response:
<HTTP STATUS CODE 200>
{
    "id": <post_id, e.g. 1>,
    "caption": "I love doge",
    "image": "https://i.kym-cdn.com/photos/images/original/000/581/296/c09.jpg",
    "user":[ <serialized user without posts field>, ... ],
    "tag": [ <serialized tag without posts field>, ... ]
}

if user is None (user does not exist, so cannot find user with given user_id): return failure_response("User not found!"). 404.

if post is None (post does not exist, so cannot find post with given post_id): return failure_response("Post not found!"). 404.
