FROM mongo

COPY competitions.json /competitions.json
CMD mongoimport --host izard-db --db izard --collection competitions --drop --file /competitions.json --jsonArray
