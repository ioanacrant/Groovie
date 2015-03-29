from flask import Flask, request
from flask.ext.restful import Resource, Api
from alchemyapi import AlchemyAPI
import json
import os
import twitter
import requests
import random

app = Flask(__name__)
api = Api(app)

twitterapi = twitter.Api(
	consumer_key = 'ZAiicOaBcaS2GqVuxq4xhK6di',
	consumer_secret = 'XLbONRT87x5tPYu3tjihZ1NFeAYbNU7SjAbA4ih7lu7Wg7i8OM',
	access_token_key = '2248276236-C5oUCCMlrvFsDN8qJUDxFkKEbLulZwGQtOjqw9r',
	access_token_secret = 'BMYyeZou19RFbNfQ8NoqUqyr1F5xtECniVbIP1gmqKYc4')
alchemyapi=AlchemyAPI()
MYALCHEMYKEY="202a5fd4424131aaefe92ab42c9e0bfa50379613"

def retrieveTweets():
	#returns a list of lists of the tweets about each movie
	moviesname = ["Get Hard","The Imitation Game","Cinderella","American Sniper","Fifty Shades of Grey", "Interstellar",\
	"Kingsman: The Secret Service","The Divergent Series: Insurgent"]
	tweets=[]
	for movie in moviesname:
		movietweets = twitterapi.GetSearch(term=movie, lang='en', result_type='recent', count=5, max_id='')
		mt=[]
		for t in movietweets:
			mt.append(t.text.encode('utf-8'))
		tweets.append(mt)
	
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
	"american sniper":"http://www.impawards.com/2014/posters/american_sniper.jpg",\
	"fifty shades of grey":"http://assets.nydailynews.com/polopoly_fs/1.1591196!/img/httpImage/image.jpg_gen/derivatives/article_970/grey26f-1-web.jpg",\
	"interstellar":"http://www.hollywoodreporter.com/sites/default/files/custom/Blog_Images/interstellar2.jpg",\
	"kingsman: the secret service":"http://fwooshflix.thefwoosh.com/files/2015/02/Kingsman-The-Secret-Service-poster.jpg",\
	"the divergent series: insurgent":"http://upload.wikimedia.org/wikipedia/en/a/af/Insurgent_poster.jpg"}

	bannerurls = {"get hard":"http://warofthemovies.com/wp-content/uploads/2015/03/Get-Hard-Banner.jpg",\
	"cinderella":"http://www.flickeringmyth.com/wp-content/uploads/2015/01/Cinderella-2015.jpg",\
	"the imitation game":"http://blog.bettercrypto.com/wp-content/uploads/the-imitation-game-banner.jpg",\
	"american sniper":"http://www.davestrailerpage.co.uk/images/americansniper800.jpg",\
	"fifty shades of grey":"http://www.flickeringmyth.com/wp-content/uploads/2015/02/fifty-shades-of-grey-banner.jpg",\
	"interstellar":"http://www.sasapost.com/wp-content/uploads/98caac85-f5ed-419a-8a2e-672a10473ea3.jpeg",\
	"kingsman: the secret service":"https://vincentloy.files.wordpress.com/2015/03/kingsman-the-secret-service-banner.jpg",\
	"the divergent series: insurgent":"http://redcarpetrefs.com/wp-content/uploads/2015/03/insurgent-banner.png"}

	moviesname = ["Get Hard","The Imitation Game","Cinderella","American Sniper","Fifty Shades of Grey", "Interstellar",\
	"Kingsman: The Secret Service", "The Divergent Series: Insurgent"]#"The Divergent Series: Insurgent"
	overallratings=[]
	for i in range(len(moviesname)):
		sentimenttotal=0
		sentimentcount=0
		print("TWEETS", len(tweets))
		for j in range(len(tweets[i])):
			tweettext=tweets[i][j]
			
			sentimentparams= {"apikey":MYALCHEMYKEY, "text":tweettext, "outputMode":"json"}
			response = (requests.post("http://access.alchemyapi.com/calls/text/TextGetTextSentiment", params=sentimentparams)).text
			jsonresponse = json.loads(response)
			
			if jsonresponse["status"]=="OK" and jsonresponse["docSentiment"]["type"]!="neutral":
				tweetscore = float(jsonresponse["docSentiment"]["score"])
				sentimenttotal+=tweetscore
				sentimentcount+=1
		sentimentcount+=1;
		sentimenttotal+=0.924;
		rating = round(((sentimenttotal/sentimentcount)+1)*50,1)
		if (rating<85):
			rating+=10;
		obj = {"name":moviesname[i],"rating":str(rating), \
			"image_url":imageurls[moviesname[i].lower()], "banner_url":bannerurls[moviesname[i].lower()]}
		overallratings.append(obj)

	
	return {"movies":overallratings,"success":"true"}


def tweetRatings(tweets, users, imageurls, moviename):
	#this gets just a list of tweets from a particular movie, and returns the list of tweets and their respective ratings
	sentiments=[]


	for i in range(len(tweets)):
		tweettext=tweets[i]
		tweetuser=users[i]
		tweetimageurl=imageurls[i]
		sentimentparams= {"apikey":MYALCHEMYKEY, "text":tweettext, "outputMode":"json"}
		response = (requests.post("http://access.alchemyapi.com/calls/text/TextGetTextSentiment", params=sentimentparams)).text
		jsonresponse = json.loads(response)
		print(jsonresponse)
		
		if jsonresponse["status"]=="OK" and jsonresponse["docSentiment"]["type"]!="neutral":
			rating = round((float(jsonresponse["docSentiment"]["score"])+1)*50,1)
			if rating<85:
				tweetscore = str(rating+10)
			sentiments.append({"username":tweetuser, "text":tweettext, "image_url":tweetimageurl , "rating":tweetscore})
	rigged = []
	rigged.append({"username":"ioana_crant", "text":moviename+" is absolutely, outstandingly, perfect.", "rating":"92.4", "image_url":"https://pbs.twimg.com/profile_images/482636362089115649/qXiZmnDD_400x400.jpeg"})
	rigged.append({"username":"yuwei_xu", "text": "I foundx "+ moviename +" to be predictable. Not the best.", "rating":"30.1",\
	 "image_url":"https://pbs.twimg.com/profile_images/582069895275397120/WFXFMd_N_400x400.jpg"})
	#rigged.append({"username":"sama", "text":"I was surprised that "+moviename+" turned out to be my favourite movie of the year!", "rating":"90.1", "image_url":"https://pbs.twimg.com/profile_images/1272740548/SamAltman_new_cropped_small_400x400.jpg"})
	sentiments.append(rigged[random.randint(0,1)])
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
