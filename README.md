sl-reseplanerare
================
Generates RSS feeds from the Stockholm SL Reseguiden for consumption in your favorite RSS reader.
Departure and arrival station id's are hardcoded right now (my work and home), but can easily be changed to a query parameter
to be forwarded from the RSS reader.

Usage:
deploy to i.e. heroku and point your rss reader to:
http://<yourapp>.herokuapp.com/reseplanerare.rss?key=XYZ

... where XYZ is your API key to the Reseplanerare API which you'll need to get from:
http://www.trafiklab.se/api/sls-reseplanerare

Frameworks:
Ratpack (micro groovy http server)
Gradle (awesome build tool)
Standard groovy
