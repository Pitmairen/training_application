package no.hials.trainingapp.routing;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.LoaderException;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.DelegatingLoader;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
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
        loadingStrategies.add(new FileLoader());
        loadingStrategies.add(new TemplateLoader());
        DelegatingLoader loader = new DelegatingLoader(loadingStrategies);

        loader.setPrefix("templates");
        loader.setSuffix(".html");
        return loader;

    }

    private static class TemplateLoader extends ClasspathLoader {

        @Override
        public Reader getReader(String templateName) throws LoaderException {
            System.out.print("##### LOADING TEMPLATE #####: ");
            System.out.println(templateName);
            return super.getReader(templateName);
        }

    }
}
