from flask import Flask, request
from flask.ext.restful import Resource, Api
from alchemyapi import AlchemyAPI
import json
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

def retrieveTweets():
	#returns a list of lists of the tweets about each movie
	moviesname = ["The Imitation Game","Cinderella","American Sniper"]
	tweets=[]
	for movie in moviesname:
		movietweets = twitterapi.GetSearch(term=movie, lang='en', result_type='mixed', count=20, max_id='')
		mt=[]
		for t in movietweets:
			mt.append(t.text.encode('utf-8'))
		tweets.append(mt)
	return tweets 								#[["Iron Man is awesome","Iron Man sux"],["Batman rox","Batman is cool"]]

def retrieveMovieTweets(moviename):
	tweets=[]
	movietweets = twitterapi.GetSearch(term=moviename, lang='en', result_type='mixed', count=20, max_id='')
	for t in movietweets:
		tweets.append(t.text.encode('utf-8'))
	return tweets


def overallRatings(tweets):
	#returns movie-overall rating pair for each movie
	moviesname = ["The Imitation Game","Cinderella","American Sniper"]
	overallratings=[]

	for i in range(len(moviesname)):
		sentimenttotal=0
		sentimentcount=0
		for j in range(len(tweets[i])):
			tweettext=tweets[i][j]
			sentimentparams= {"apikey":"284c0cc5558c1b4e78c505777be3b964c9dfa2f4", "text":tweettext, "target":moviesname[i], "outputMode":"json"}
			response = (requests.post("http://access.alchemyapi.com/calls/text/TextGetTargetedSentiment", params=sentimentparams)).text
			jsonresponse = json.loads(response)
			if jsonresponse["status"]=="OK" and jsonresponse["docSentiment"]["type"]!="neutral":
				tweetscore = float(jsonresponse["docSentiment"]["score"])
				sentimenttotal+=tweetscore
				sentimentcount+=1
		overallratings.append({moviesname[i]:((sentimenttotal/sentimentcount)+1)*50})
	return overallratings


def tweetRatings(tweets, moviename):
	#this gets just a list of tweets from a particular movie, and returns the list of tweets and their respective ratings
	sentiments=[]
	
	for i in range(len(tweets)):
		tweettext=tweets[i]
		sentimentparams= {"apikey":"284c0cc5558c1b4e78c505777be3b964c9dfa2f4", "text":tweettext, "target":moviename, "outputMode":"json"}
		response = (requests.post("http://access.alchemyapi.com/calls/text/TextGetTargetedSentiment", params=sentimentparams)).text
		jsonresponse = json.loads(response)

		if jsonresponse["status"]=="OK" and jsonresponse["docSentiment"]["type"]!="neutral":

			tweetscore = (float(jsonresponse["docSentiment"]["score"])+1)*50
			sentiments.append({tweettext:tweetscore})

	return sentiments

class GetMovies(Resource):
	def get(self):
		return json.dumps(overallRatings(retrieveTweets()))

class GetMovie(Resource):
	def get(self, moviename):
		mv=moviename.replace("%20"," ")
		tweetsfrommovie=retrieveMovieTweets(mv)
		return json.dumps(tweetRatings(tweetsfrommovie, mv))


api.add_resource(GetMovies, "/movies")
api.add_resource(GetMovie, "/movies/<string:moviename>")

if __name__ == '__main__':
	app.run(debug=True)