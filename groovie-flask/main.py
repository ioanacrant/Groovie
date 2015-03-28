from flask import Flask, request
from flask.ext.restful import Resource, Api
import json
import twitter

app = Flask(__name__)
api = Api(app)
twitterapi = twitter.Api(
	consumer_key = 'ZAiicOaBcaS2GqVuxq4xhK6di',
	consumer_secret = 'XLbONRT87x5tPYu3tjihZ1NFeAYbNU7SjAbA4ih7lu7Wg7i8OM',
	access_token_key = '2248276236-C5oUCCMlrvFsDN8qJUDxFkKEbLulZwGQtOjqw9r',
	access_token_secret = 'BMYyeZou19RFbNfQ8NoqUqyr1F5xtECniVbIP1gmqKYc4')

movies = ["Batman","Iron Man"]
tweets=[]
for i in range(len(movies)):
	movietweets = twitterapi.GetSearch(term=movies[i], lang='en', result_type='mixed', count=10, max_id='')
	mt=[]
	for t in movietweets:
		print t.user.screen_name + ' (' + t.created_at + ')'
		#Add the .encode to force encoding
		print t.text.encode('utf-8')
		print ''
		mt.append({t:"10"})
	tweets.append(mt)



class GetMovies(Resource):
	def get(self):
		return json.loads(movies)



api.add_resource(GetMovies, "/movies")

if __name__ == '__main__':
	app.run(debug=True)