package cc.ayakurayuki.csa.starter.core.exception

import cc.ayakurayuki.csa.starter.core.exception.ExceptionHandlerFactory
import cc.ayakurayuki.csa.starter.core.exception.handler.AuthExceptionHandler
import com.zandero.rest.context.ContextProviderFactory
import com.zandero.rest.data.ClassFactory
import com.zandero.rest.exception.*
import com.zandero.rest.injection.InjectionProvider
import com.zandero.utils.Assert
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

import javax.ws.rs.WebApplicationException
import java.lang.reflect.Type

class ExceptionHandlerFactory extends ClassFactory<ExceptionHandler> {

  private static final def log = LoggerFactory.getLogger ExceptionHandlerFactory.class

  static Map<Class, Class<? extends ExceptionHandler>> defaultHandlers
  {
    defaultHandlers = new LinkedHashMap<>()
    defaultHandlers[ConstraintException.class] = ConstraintExceptionHandler.class
    defaultHandlers[WebApplicationException.class] = WebApplicationExceptionHandler.class
    defaultHandlers[Throwable.class] = GenericExceptionHandler.class
    defaultHandlers[AuthException.class] = AuthExceptionHandler.class
  }

  @Override
  protected void init() {
    classTypes = new LinkedHashMap<>()
  }

  ExceptionHandler getExceptionHandler(Class<? extends Throwable> aClass,
                                       Class<? extends ExceptionHandler>[] definitionExHandlers,
                                       InjectionProvider provider,
                                       RoutingContext context) throws ClassFactoryException, ContextException {

    // trickle down ... from definition to default handler
    Class<? extends ExceptionHandler> found = null

    // search definition add as given in REST (class or method annotation)
    if (definitionExHandlers != null && definitionExHandlers.length > 0) {

      for (Class<? extends ExceptionHandler> handler : definitionExHandlers) {
        Type type = getGenericType handler
        if (checkIfCompatibleTypes(aClass, type)) {
          found = handler
          log.info "Found matching exception handler: ${found.getName()}".toString()
          break
        }
      }
    }

    // get by exception type from classTypes list
    if (found == null) {
      found = super.get aClass

      if (found != null) {
        log.info "Found matching class type exception handler: ${found.getName()}".toString()
      }
    }

    // nothing found provide default or generic
    if (found == null) {
      found = defaultHandlers.get aClass

      if (found == null) {
        found = GenericExceptionHandler.class
      }
      log.info "Resolving to generic exception handler: ${found.getName()}".toString()
    }

    // create class instance
    super.getClassInstance(found, provider, context)
  }

  @SafeVarargs
  final void register(Class<? extends ExceptionHandler>... handlers) {

    Assert.notNullOrEmpty handlers, 'Missing exception handler(s)!'

    for (Class<? extends ExceptionHandler> handler : handlers) {

      Type type = getGenericType handler
      Assert.notNull type, "Can't extract generic class type for exception handler: ${handler.getClass().name}".toString()

      checkIfAlreadyRegistered((Class) type)

      classTypes[(Class) type] = handler
    }
  }

  final void register(ExceptionHandler... handlers) {

    Assert.notNullOrEmpty handlers, 'Missing exception handler(s)!'
    for (ExceptionHandler handler : handlers) {

      Assert.isFalse(ContextProviderFactory.hasContext(handler.class),
          'Exception handler utilizing @Context must be registered as class type not as instance!')

      Type generic = getGenericType handler.class
      Assert.notNull generic, "Can't extract generic class type for exception handler: ${handler.class.name}".toString()

      // check if already registered
      checkIfAlreadyRegistered((Class) generic)

      // register
      classTypes[(Class) generic] = handler.class

      // cache instance by handler class type
      super.register(handler)
    }
  }

  private void checkIfAlreadyRegistered(Class clazz) {

    // check if already registered
    Class<? extends ExceptionHandler> found = classTypes.get clazz
    if (found != null) {
      throw new IllegalArgumentException("Exception handler for: ${clazz.name} already registered with: ${found.name}".toString())
    }
  }

}
