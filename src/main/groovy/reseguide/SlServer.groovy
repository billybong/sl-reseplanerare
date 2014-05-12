package reseguide

import groovy.json.JsonSlurper
import reseguide.domain.Trip

import java.text.SimpleDateFormat

class SlServer {
    String rss() {
        List<Trip> trips = fetchTrips()
        String pubDate = new SimpleDateFormat('EEE, d MMM yyyy HH:mm:ss Z', Locale.US).format(new Date())

        String rss = """<?xml version="1.0" encoding="UTF-8" ?>
            <rss version="2.0">
            <channel>
             <title>SL Reseplanerare</title>
             <description>Shows which buses are appropriate for Billy</description>
             <link>http://www.sl.se</link>
             <lastBuildDate>$pubDate</lastBuildDate>
             <pubDate>$pubDate</pubDate>
             <ttl>300</ttl>
             """

        trips.each {
            def title = "($it.departure - $it.arrival) $it.transports - $it.tripTime"

            rss += """<item>
              <title>$title</title>
              <description>Lorum ipsum</description>
              <link>http://www.sl.se/</link>
              <guid>$title</guid>
              <pubDate>$pubDate</pubDate>
             </item>"""
        }
        rss += """</channel>
           </rss>"""
    }

    List<Trip> fetchTrips() {
        def slurper = new JsonSlurper()

        String jsonText = "https://api.trafiklab.se/sl/reseplanerare.json?S=9200&key=9lFYCnjzkxEoLxKJHaiCMFOM42wk7GhG&Z=2817&journeyProducts=8".toURL().text

        def json = slurper.parseText(jsonText.replaceAll('#', ''))

        def trips = json.HafasResponse.Trip

        trips.collect {
            new Trip(
                    departure: it.Summary.DepartureTime.text,
                    arrival: it.Summary.ArrivalTime.text,
                    tripTime: it.Summary.Duration,
                    transports: it.SubTrip.collect { it.Transport.Name })
        }
    }
}
