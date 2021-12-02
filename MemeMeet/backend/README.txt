<Backend><Meme&meet>
Name: Alex Jialin Shen, Yutong Zhu
Netid: js2642, yz2676


FOR DEVELOPER ONLY:
Home page, default
“GET”  /post/  [Home page, default]
	IF developer wants to add one tag (users not allowed to):
	“POST”  /post/

FOR USER:
Page1 | Page2 | Page3
“GET”  /post/  [Home page, default]
IF user clicks one post:
	“GET”  /post/post_id/
	IF user clicks back button:
		“GET”  /post/  [Home page, default]
ELIF user clicks user profile:
	“GET”  /user/user_id/  [User page]
	IF user clicks back button:
		“GET”  /post/  [Home page]
	ELIF user clicks one post
		“GET”  /user/user_id/post/post_id/
		IF user clicks back button
			“GET”  /user/user_id/  [User page]
ELIF user clicks one tag
	“GET”  /post/tag/tag_id/  [Tag page]
	IF user clicks back button
		“GET”  /post/  [Home page]
	ELIF user clicks one post
		“GET”  /post/tag/tag_id/post_id/
		IF user clicks back button
			“GET”  /post/tag/tag_id/  [Tag page]
	ELIF user clicks add_post button
		“POST”  /post/tag/tag_id/ (after successfully added a post, return to tag page)




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

If the client does not provide a tag, respond with an error message "No tag name!" with status code 400.

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

If the client does not provide a name, respond with an error message "No user name!" with status code 400.
If the client creates a username that has been used by someone else, respond with the error message “Replicate name!” with status code 400.

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
