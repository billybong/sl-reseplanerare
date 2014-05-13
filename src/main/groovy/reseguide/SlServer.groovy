package reseguide

import groovy.json.JsonSlurper
import reseguide.domain.Trip

import java.text.SimpleDateFormat

class SlServer {
    String apiKey

    SlServer(String apiKey) {
        this.apiKey = apiKey
    }

    String rss() {
        List<Trip> trips = fetchTrips()
        String pubDate = new SimpleDateFormat('EEE, d MMM yyyy HH:mm:ss Z', Locale.US).format(new Date())

        String rss = """<?xml version="1.0" encoding="UTF-8" ?>
            <rss version="2.0">
            <channel>
             <title>SL Reseplanerare</title>
             <description>Shows which buses are appropriate for Billy</description>
             <link>http://reseplanerare.sl.se/</link>
             <lastBuildDate>$pubDate</lastBuildDate>
             <pubDate>$pubDate</pubDate>
             <ttl>300</ttl>
             """

        trips.each {
            def title = "($it.departure - $it.arrival) $it.transports - $it.tripTime"
            def description = """
                Departure: ${it.departure}
                Arrival: ${it.arrival}
                Trip time: $it.tripTime
                Transports:
                    ${it.transports.join("\n")}"""

            rss += """<item>
              <title>$title</title>
              <description>Lorum ipsum</description>
              <link>http://reseplanerare.sl.se/</link>
              <guid>$title</guid>
              <pubDate>$pubDate</pubDate>
             </item>"""
        }
        rss += """</channel>
           </rss>"""
    }

    List<Trip> fetchTrips() {
        def base = 'https://api.trafiklab.se/sl/reseplanerare.json?'
        def params = [S:'9200', Z:'2817', key:apiKey, journeyProducts:'8']
        def url = base + params.collect{it}.join('&')

        String jsonText = url.toURL().text
        def json = new JsonSlurper().parseText(jsonText.replaceAll('#', ''))

        json.HafasResponse.Trip.collect {
            new Trip(
                    departure: it.Summary.DepartureTime.text,
                    arrival: it.Summary.ArrivalTime.text,
                    tripTime: it.Summary.Duration,
                    transports: it.SubTrip.collect {"$it.Transport.Name ($it.Origin.text -> $it.Destination.text)"})
        }
    }
}
