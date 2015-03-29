from flask import Flask, request
from flask.ext.restful import Resource, Api
from alchemyapi import AlchemyAPI
import json
import os
import twitter
import requests

app = Flask(__name__)
api = Api(app)

twitterapi = twitter.Api(
	consumer_key = 'ZAiicOaBcaS2GqVuxq4xhK6di',
	consumer_secret = 'XLbONRT87x5tPYu3tjihZ1NFeAYbNU7SjAbA4ih7lu7Wg7i8OM',
	access_token_key = '2248276236-C5oUCCMlrvFsDN8qJUDxFkKEbLulZwGQtOjqw9r',
	access_token_secret = 'BMYyeZou19RFbNfQ8NoqUqyr1F5xtECniVbIP1gmqKYc4')
alchemyapi=AlchemyAPI()
MYALCHEMYKEY="fb9b56ac6225345d9e70b85ded0036279119b828"

def retrieveTweets():
	#returns a list of lists of the tweets about each movie
	moviesname = ["Get Hard","The Imitation Game","Cinderella","American Sniper"]
	tweets=[]
	for movie in moviesname:
		movietweets = twitterapi.GetSearch(term=movie, lang='en', result_type='recent', count=10, max_id='')
		mt=[]
		for t in movietweets:
			mt.append(t.text.encode('utf-8'))
		tweets.append(mt)
		#print(tweets)
	return tweets 								#[["Iron Man is awesome","Iron Man sux"],["Batman rox","Batman is cool"]]

def retrieveMovieTweets(moviename):
	tweets=[]
	users=[]
	userimage=[]
	movietweets = twitterapi.GetSearch(term=moviename, lang='en', result_type='recent', count=10, max_id='')
	
	for t in movietweets:
		tweets.append(t.text.encode('utf-8').replace("\"",""))
		users.append(t.user.name)
		userimage.append(t.user.profile_image_url)
	return [tweets,users,userimage]


def overallRatings(tweets):
	#returns movie-overall rating pair for each movie
	imageurls = {"cinderella":"http://www.impawards.com/2015/posters/cinderella_ver4.jpg",\
	"get hard":"https://s.yimg.com/cd/resizer/2.0/FIT_TO_WIDTH-w500/19141496561e14ab3b41ea38d31af3280009b227.jpg",\
	"the imitation game":"http://cdn.hitfix.com/photos/5794803/Poster-art-for-The-Imitation-Game_event_main.jpg",\
	"american sniper":"http://www.impawards.com/2014/posters/american_sniper.jpg"}

	bannerurls = {"get hard":"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg",\
	"cinderella":"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg",\
	"the imitation game":"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg",\
	"american sniper":"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg"}

	moviesname = ["Get Hard","The Imitation Game","Cinderella","American Sniper"]
	overallratings=[]
	for i in range(len(moviesname)):
		sentimenttotal=0
		sentimentcount=0
		for j in range(len(tweets[i])):
			tweettext=tweets[i][j]
			print(tweettext)
			sentimentparams= {"apikey":MYALCHEMYKEY, "text":tweettext, "target":moviesname[i], "outputMode":"json"}
			response = (requests.post("http://access.alchemyapi.com/calls/text/TextGetTargetedSentiment", params=sentimentparams)).text
			jsonresponse = json.loads(response)
			#print(jsonresponse)
			if jsonresponse["status"]=="OK" and jsonresponse["docSentiment"]["type"]!="neutral":
				tweetscore = float(jsonresponse["docSentiment"]["score"])
				sentimenttotal+=tweetscore
				sentimentcount+=1
		#print (sentimentcount)
		obj = {"name":moviesname[i],"rating":str(round(((sentimenttotal/sentimentcount)+1)*50,1)), \
			"image_url":imageurls[moviesname[i].lower()], "banner_url":bannerurls[moviesname[i].lower()]}
		overallratings.append(obj)

	print({"movies":overallratings})
	return {"movies":overallratings,"succes":"true"}


def tweetRatings(tweets, users, imageurls, moviename):
	#this gets just a list of tweets from a particular movie, and returns the list of tweets and their respective ratings
	sentiments=[]


	for i in range(len(tweets)):
		tweettext=tweets[i]
		tweetuser=users[i]
		tweetimageurl=imageurls[i]
		sentimentparams= {"apikey":MYALCHEMYKEY, "text":tweettext, "target":moviename, "outputMode":"json"}
		response = (requests.post("http://access.alchemyapi.com/calls/text/TextGetTargetedSentiment", params=sentimentparams)).text
		jsonresponse = json.loads(response)

		if jsonresponse["status"]=="OK" and jsonresponse["docSentiment"]["type"]!="neutral":

			tweetscore = str(round((float(jsonresponse["docSentiment"]["score"])+1)*50,1))
			sentiments.append({"username":tweetuser, "text":tweettext, "imageurl":tweetimageurl , "rating":tweetscore})

	return {"tweets":sentiments}

class GetMovies(Resource):
	def get(self):
		return json.dumps(overallRatings(retrieveTweets()))

class GetMovie(Resource):
	def get(self, moviename):
		mv=moviename.replace("%20"," ")

		tweetsfrommovie,usersfrommovie,userimagesfrommovie=retrieveMovieTweets(mv)
		return json.dumps(tweetRatings(tweetsfrommovie, usersfrommovie, userimagesfrommovie, mv))

class MainRoute(Resource):
	def get(self):
		return "Hello World"

api.add_resource(MainRoute, "/")
api.add_resource(GetMovies, "/movies")
api.add_resource(GetMovie, "/movies/<string:moviename>")

if __name__ == '__main__':
	port = int(os.environ.get('PORT', 5000))
	app.run(host='0.0.0.0', port=port)
	#app.run(debug=True)
