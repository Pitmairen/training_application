package no.hials.trainingapp.routing;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.LoaderException;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.DelegatingLoader;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.TemplateEngine;
import spark.template.pebble.PebbleTemplateEngine;

/**
 * Factory methods for template engines.
 *
 * Currently we only use the pebble engine.
 *
 * @author pitmairen
 */
public class TemplateEngines {

    /**
     * Creates a new pebble template engine
     *
     * Pebble templates @see
     * <a href="http://www.mitchellbosecke.com/pebble/home">Pebble templates</a>
     *
     * @return
     */
    public static TemplateEngine createPebbleEngine() {

        PebbleEngine engine = new PebbleEngine(createTemplateLoader());

        // FIXME: Currently weird errors are happening with cache enabled.
        engine.setTemplateCache(null);

        return new PebbleTemplateEngine(engine);

    }

    private static Loader createTemplateLoader() {

        List<Loader> loadingStrategies = new ArrayList<>();
        loadingStrategies.add(new TemplateLoader());
        loadingStrategies.add(new FileLoader());
        DelegatingLoader loader = new DelegatingLoader(loadingStrategies);

        loader.setPrefix("templates");
        loader.setSuffix(".html");
        return loader;

    }

    private static class TemplateLoader extends ClasspathLoader {

        @Override
        public Reader getReader(String templateName) throws LoaderException {

            // Quick fix to make it work on windows. Don't know why the 
            // original pebble classpath loader doesn't work.
            ClassLoader classLoader = TemplateEngines.class.getClassLoader();
            InputStream stream = classLoader.getResourceAsStream("templates/" + templateName + ".html");

            if (stream == null) {
                throw new LoaderException(null, "Could not find template \"" + templateName + "\"");
            }

            try {
                return new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(TemplateEngines.class.getName()).log(Level.SEVERE, null, ex);
            }

            throw new LoaderException(null, "Failed to render template \"" + templateName + "\"");

        }

    }
}
