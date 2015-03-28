from flask import Flask, request
from flask.ext.restful import Resource, Api
import json
import os
import twitter

app = Flask(__name__)
api = Api(app)
twitterapi = twitter.Api(
	consumer_key = 'ZAiicOaBcaS2GqVuxq4xhK6di',
	consumer_secret = 'XLbONRT87x5tPYu3tjihZ1NFeAYbNU7SjAbA4ih7lu7Wg7i8OM',
	access_token_key = '2248276236-C5oUCCMlrvFsDN8qJUDxFkKEbLulZwGQtOjqw9r',
	access_token_secret = 'BMYyeZou19RFbNfQ8NoqUqyr1F5xtECniVbIP1gmqKYc4')

movies = ["Batman","Iron Man"]
# tweets=[]
# for i in range(len(movies)):
# 	movietweets = twitterapi.GetSearch(term=movies[i], lang='en', result_type='mixed', count=10, max_id='')
# 	mt=[]
# 	for t in movietweets:
# 		print t.user.screen_name + ' (' + t.created_at + ')'
# 		#Add the .encode to force encoding
# 		print t.text.encode('utf-8')
# 		print ''
# 		mt.append({t:"10"})
# 	tweets.append(mt)

def getTweets(name):
	movietweets = twitterapi.GetSearch(term=name, lang='en', result_type='mixed', count=10, max_id='')
	mt=[]
	for t in movietweets:
		tweet = {};
		tweet["created_at"] = t.created_at.encode('utf-8')
		tweet["screen_name"] = t.user.screen_name.encode('utf-8')
		tweet["message"] = t.text.encode('utf-8');
		# print t.user.screen_name + ' (' + t.created_at + ')'
		# #Add the .encode to force encoding
		# print t.text.encode('utf-8')
		# print ''
		# mt.append({t:"10"})
		mt.append(tweet);
	data = {};
	data["tweets"] = mt;
	return data;


class GetMovies(Resource):
	def get(self):
		return movies

class MovieRating(Resource):
	def get(self,movie_name):
		return json.dumps(getTweets(movie_name))

class MainRoute(Resource):
	def get(self):
		return "Hello World"

api.add_resource(MainRoute, "/")
api.add_resource(GetMovies, "/movies")
api.add_resource(MovieRating,"/movies/<string:movie_name>")

if __name__ == '__main__':
	port = int(os.environ.get('PORT', 5000))
	app.run(host='0.0.0.0', port=port)
	#app.run(debug=True)