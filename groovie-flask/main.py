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
MYALCHEMYKEY="70facd1b39e4d273bc178918fb38946b551d7944"

def retrieveTweets():
	#returns a list of lists of the tweets about each movie
	moviesname = ["The Imitation Game","Cinderella","American Sniper"]
	tweets=[]
	for movie in moviesname:
		movietweets = twitterapi.GetSearch(term=movie, lang='en', result_type='mixed', count=10, max_id='')
		mt=[]
		for t in movietweets:
			mt.append(t.text.encode('utf-8'))
		tweets.append(mt)
	return tweets 								#[["Iron Man is awesome","Iron Man sux"],["Batman rox","Batman is cool"]]

def retrieveMovieTweets(moviename):
	tweets=[]
	users=[]
	userimage=[]
	movietweets = twitterapi.GetSearch(term=moviename, lang='en', result_type='mixed', count=10, max_id='')
	
	for t in movietweets:
		tweets.append(t.text.encode('utf-8'))
		users.append(t.user.name)
		userimage.append(t.user.profile_image_url)
	return [tweets,users,userimage]


def overallRatings(tweets):
	#returns movie-overall rating pair for each movie
	moviesname = ["The Imitation Game","Cinderella","American Sniper"]
	overallratings=[]
	for i in range(len(moviesname)):
		sentimenttotal=0
		sentimentcount=0
		for j in range(len(tweets[i])):
			tweettext=tweets[i][j]

			sentimentparams= {"apikey":MYALCHEMYKEY, "text":tweettext, "target":moviesname[i], "outputMode":"json"}
			response = (requests.post("http://access.alchemyapi.com/calls/text/TextGetTargetedSentiment", params=sentimentparams)).text
			jsonresponse = json.loads(response)
			print(jsonresponse)
			if jsonresponse["status"]=="OK" and jsonresponse["docSentiment"]["type"]!="neutral":
				tweetscore = float(jsonresponse["docSentiment"]["score"])
				sentimenttotal+=tweetscore
				sentimentcount+=1
		overallratings.append({"name":moviesname[i],"rating":str(round(((sentimenttotal/sentimentcount)+1)*50,1))})
	return {"movies":overallratings}


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
