import reseguide.SlServer

import static ratpack.groovy.Groovy.*

ratpack {
    handlers {
        get "reseplanerare.rss", {

            String apiKey = request.queryParams.key;

            if(apiKey){
                response.send('application/rss+xml', new SlServer(apiKey).rss())
            }
            else{
                response.status(400)
                response.send('Missing API key')
            }
        }
    }
}