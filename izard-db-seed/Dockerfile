FROM mongo:3.7.1
MAINTAINER matek2305@gmail.com
COPY competitions.json /competitions.json
CMD mongoimport --host izard-db --db izard --collection competitions --drop --file /competitions.json --jsonArray
