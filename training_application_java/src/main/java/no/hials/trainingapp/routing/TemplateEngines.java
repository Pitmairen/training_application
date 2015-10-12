package no.hials.trainingapp.routing;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import spark.TemplateEngine;
import spark.template.pebble.PebbleTemplateEngine;

/**
 *
 * @author pitmairen
 */
public class TemplateEngines {

    
    public static TemplateEngine createPebbleEngine()
    {
        Loader loader = new ClasspathLoader();

        loader.setPrefix("templates");
        loader.setSuffix(".html");
        PebbleEngine engine = new PebbleEngine(loader);

        // FIXME: Currently weird errors are happening with cache enabled.
        engine.setTemplateCache(null);

        return new PebbleTemplateEngine(engine);

    }
    
}
