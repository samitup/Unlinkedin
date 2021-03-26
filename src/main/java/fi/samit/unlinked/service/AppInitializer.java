
package fi.samit.unlinked.service;

import javax.servlet.ServletContext;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.WebApplicationInitializer;

public class AppInitializer implements WebApplicationInitializer {

@Override
public void onStartup(ServletContext servletContext) {
  
servletContext.addListener(HttpSessionEventPublisher.class);
}
}