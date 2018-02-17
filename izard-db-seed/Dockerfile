FROM mongo:3.7.1
MAINTAINER matek2305@gmail.com
COPY events.json /events.json
CMD mongoimport --host izard-db --db izard --collection events --drop --file /events.json --jsonArray
