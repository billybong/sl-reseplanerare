import reseguide.SlServer

import static ratpack.groovy.Groovy.*

ratpack {
    handlers {
        get "reseplanerare.rss", {
            response.send('application/rss+xml', new SlServer().rss())
        }
    }
}