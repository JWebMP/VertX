package com.jwebmp.vertx;

import com.google.common.base.Strings;
import com.google.inject.*;
import com.google.inject.name.Names;
import com.guicedee.client.Environment;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.scopes.CallScoper;
import com.guicedee.client.scopes.CallScope;
import com.guicedee.client.scopes.CallScopeProperties;
import com.guicedee.client.scopes.CallScopeSource;
import com.guicedee.client.services.lifecycle.IGuiceModule;
import com.guicedee.services.jsonrepresentation.IJsonRepresentation;
import com.guicedee.vertx.web.spi.VertxHttpServerConfigurator;
import com.jwebmp.core.annotations.PageConfiguration;
import com.jwebmp.core.base.ajax.*;
import com.jwebmp.core.base.interfaces.IComponentStyleBase;
import com.jwebmp.core.base.servlets.interfaces.IDataComponent;
import com.jwebmp.core.exceptions.InvalidRequestException;
import com.jwebmp.core.generics.FileTemplates;
import com.jwebmp.core.htmlbuilder.javascript.events.interfaces.IEvent;
import com.jwebmp.core.services.IPage;
import com.jwebmp.interception.services.AjaxCallIntercepter;
import com.jwebmp.interception.services.DataCallIntercepter;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.java.Log;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.logging.Level;

import static com.guicedee.client.IGuiceContext.get;
import static com.guicedee.services.jsonrepresentation.json.StaticStrings.CHAR_DOT;
import static com.guicedee.services.jsonrepresentation.json.StaticStrings.CHAR_UNDERSCORE;
import static com.jwebmp.interception.services.JWebMPInterceptionBinder.AjaxCallInterceptorKey;
import static com.jwebmp.interception.services.JWebMPInterceptionBinder.DataCallInterceptorKey;
import static com.jwebmp.interception.services.StaticStrings.*;
import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

@Log
@Singleton
public class JWebMPVertx extends AbstractModule implements IGuiceModule<JWebMPVertx>, VertxHttpServerConfigurator
{
        private static <T> T withCallScope(java.util.concurrent.Callable<T> task, CallScopeSource source)
        {
                CallScoper callScoper = IGuiceContext.get(CallScoper.class);
                boolean startedScope = callScoper.isStartedScope();
                if (!startedScope)
                {
                        callScoper.enter();
                }
                try
                {
                        CallScopeProperties props = IGuiceContext.get(CallScopeProperties.class);
                        if (props.getSource() == null || props.getSource() == CallScopeSource.Unknown)
                        {
                                props.setSource(source);
                        }
                        return task.call();
                }
                catch (RuntimeException re)
                {
                        throw re;
                }
                catch (Exception e)
                {
                        throw new RuntimeException(e);
                }
                finally
                {
                        if (!startedScope)
                        {
                                callScoper.exit();
                        }
                }
        }
		@Inject
		private Vertx vertx;
		
		
		@Override
		public HttpServer builder(HttpServer builder)
		{
				Router router = Router.router(vertx);
				configureDataServlet(router);
				configureCSSServlet(router);
				configureAjaxReceiveServlet(router);
				configureInternalDataServlet(router);
				
				configurePageServlet(router);
				
				return builder;
		}
		
		@Override
		protected void configure()
		{
				super.configure();
				if (Boolean.parseBoolean(Environment.getProperty("BIND_JW_PAGES", "true")))
				{
						ScanResult scanResult = IGuiceContext
																															.instance()
																															.getScanResult();
						
						for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(PageConfiguration.class))
						{
								if (classInfo.isAbstract() || classInfo.isInterface() || classInfo.isStatic())
								{
										continue;
								}
								
								@SuppressWarnings("unchecked")
								Class<IPage<?>> pageClass = (Class<IPage<?>>) classInfo.loadClass();
								PageConfiguration pc = pageClass.getAnnotation(PageConfiguration.class);
								String url = pc.url();
								if (Strings.isNullOrEmpty(url))
								{
										url = "/";
								}
								bind(Key.get(IPage.class, Names.named(url)))
									.to(pageClass)
									.in(CallScope.class)
								;
						}
				}
		}
		
		private void configureDataServlet(Router router)
		{
				router
					.route(DATA_LOCATION)
					.handler(routingContext -> {
                            vertx.executeBlocking(() -> withCallScope(() -> {
                                    configureScopeProperties(routingContext);

                                    HttpServerRequest request = routingContext.request();
                                    HttpServerResponse response = routingContext.response();

                                    String componentID = request
                                                                                                                            .params()
                                                                                                                            .get("component");
                                    StringBuilder responseString = new StringBuilder();
                                    try
                                    {
                                            @SuppressWarnings("unchecked")
                                            Class<? extends IDataComponent> clazz = (Class<? extends IDataComponent>)
                                                                                                                                                                                                                Class.forName(componentID.replace(CHAR_UNDERSCORE, CHAR_DOT));
                                            IDataComponent component = get(clazz);
                                            StringBuilder renderData = component.renderData();
                                            responseString.append(renderData);
                                    }
                                    catch (Exception e)
                                    {
                                            log.log(Level.SEVERE, MessageFormat.format("Cannot render data for component {0}", componentID), e);
                                            return null;
                                    }
                                    for (DataCallIntercepter<?> dataCallIntercepter : get(DataCallInterceptorKey))
                                    {
                                            dataCallIntercepter.intercept(get(AjaxCall.class), get(AjaxResponse.class));
                                    }
                                    response.putHeader(CONTENT_TYPE, HTML_HEADER_JSON);
                                    response.write(responseString.toString(), StandardCharsets.UTF_8.toString());
                                    return null;
                            }, CallScopeSource.Http));
                    });
                }
		
		private void configureCSSServlet(Router router)
		{
				router
					.route(CSS_LOCATION)
					.handler(routingContext -> {
                            vertx.executeBlocking(() -> withCallScope(() -> {
                                    configureScopeProperties(routingContext);
                                    HttpServerResponse response = routingContext.response();
                                    IPage<?> page = IGuiceContext.get(IPage.class);
                                    @SuppressWarnings("rawtypes")
                                    StringBuilder css = ((IComponentStyleBase) page.getBody()).renderCss(0);
                                    response.putHeader(CONTENT_TYPE, HTML_HEADER_CSS);
                                    response.write(css.toString(), StandardCharsets.UTF_8.toString());
                                    return null;
                            }, CallScopeSource.Http));
                    });
                }
		
		private void configureAjaxReceiveServlet(Router router)
		{
				router
					.route(AJAX_SCRIPT_LOCATION)
					.handler(routingContext -> {
							// Prepare per-request scope and request object
							configureScopeProperties(routingContext);
							HttpServerRequest request = routingContext.request();
							
							// Read the body asynchronously (don’t block the event loop)
							request.bodyHandler(bodyBuffer -> {
									try
									{
											String bodyString = bodyBuffer.toString();
											
											// Deserialize inbound call
											AjaxCall<?> ajaxCallIncoming = IJsonRepresentation.From(bodyString, AjaxCall.class);
											AjaxCall<?> ajaxCall = get(AjaxCall.class);
											ajaxCall.fromCall(ajaxCallIncoming);
											ajaxCall.setPageCall(true);
											
											// Prepare response holder and event
											AjaxResponse<?> ajaxResponse = get(AjaxResponse.class);
											IEvent<?, ?> triggerEvent = processEvent();
											
											// Interceptors
											for (AjaxCallIntercepter<?> ajaxCallIntercepter : get(AjaxCallInterceptorKey))
											{
													ajaxCallIntercepter.intercept(ajaxCall, ajaxResponse);
											}
											
											// Fully reactive: do not block/await here. Subscribe and end response in the terminal callbacks.
											// Note: We intentionally avoid executeBlocking here. bodyHandler runs on the Vert.x event loop and fireEvent() returns a reactive Uni.
											// The Uni is subscribed below; any errors will be routed to the failure handler which returns a JSON error response.
											triggerEvent.fireEvent(ajaxCall, ajaxResponse)
																							// Optional timeout to guard long/never-completing chains
																							.ifNoItem()
																							.after(Duration.ofSeconds(10))
																							.fail()
																							.subscribe()
																							.with(
																								unused -> {
																										routingContext
																											.response()
																											.putHeader(CONTENT_TYPE, HTML_HEADER_JSON)
																											.end(ajaxResponse.toJson())
																										;
																								},
																								failure -> {
																										AjaxResponse<?> err = new AjaxResponse<>();
																										err.setSuccess(false);
																										
																										if (failure instanceof InvalidRequestException ie)
																										{
																												AjaxResponseReaction<?> arr = new AjaxResponseReaction<>(
																													"Invalid Request Value",
																													"A value in the request was found to be incorrect.<br>" + ie.getMessage(),
																													ReactionType.DialogDisplay
																												);
																												arr.setResponseType(AjaxResponseType.Danger);
																												err.addReaction(arr);
																												log.log(Level.SEVERE, "[SessionID]-[" + request.streamId() + "];[Exception]-[Invalid Request]", ie);
																										}
																										else
																										{
																												AjaxResponseReaction<?> arr = new AjaxResponseReaction<>(
																													"Unknown Error",
																													"An AJAX call resulted in an unknown server error<br>" + failure.getMessage() +
																														"<br>" + ExceptionUtils.getStackTrace(failure),
																													ReactionType.DialogDisplay
																												);
																												arr.setResponseType(AjaxResponseType.Danger);
																												err.addReaction(arr);
																												log.log(Level.SEVERE, "Unknown in ajax reply\n", failure);
																										}
																										
																										routingContext
																											.response()
																											.putHeader(CONTENT_TYPE, HTML_HEADER_JSON)
																											.end(err.toJson())
																										;
																								}
																							)
											;
									}
									catch (InvalidRequestException ie)
									{
											// Synchronous failure before we could subscribe (e.g., JSON mapping of the envelope)
											AjaxResponse<?> ajaxResponse = new AjaxResponse<>();
											ajaxResponse.setSuccess(false);
											AjaxResponseReaction<?> arr = new AjaxResponseReaction<>(
												"Invalid Request Value",
												"A value in the request was found to be incorrect.<br>" + ie.getMessage(),
												ReactionType.DialogDisplay
											);
											arr.setResponseType(AjaxResponseType.Danger);
											ajaxResponse.addReaction(arr);
											log.log(Level.SEVERE, "[SessionID]-[" + request.streamId() + "];[Exception]-[Invalid Request]", ie);
											routingContext
												.response()
												.putHeader(CONTENT_TYPE, HTML_HEADER_JSON)
												.end(ajaxResponse.toJson())
											;
									}
									catch (Exception T)
									{
											// Other synchronous failures before subscription
											AjaxResponse<?> ajaxResponse = new AjaxResponse<>();
											ajaxResponse.setSuccess(false);
											AjaxResponseReaction<?> arr = new AjaxResponseReaction<>(
												"Unknown Error",
												"An AJAX call resulted in an unknown server error<br>" + T.getMessage() +
													"<br>" + ExceptionUtils.getStackTrace(T),
												ReactionType.DialogDisplay
											);
											arr.setResponseType(AjaxResponseType.Danger);
											ajaxResponse.addReaction(arr);
											log.log(Level.SEVERE, "Unknown in ajax reply\n", T);
											routingContext
												.response()
												.putHeader(CONTENT_TYPE, HTML_HEADER_JSON)
												.end(ajaxResponse.toJson())
											;
									}
									
									catch (Throwable swallow)
									{
											// Last-resort guard (avoid leaking scope on unexpected errors in above blocks)
											AjaxResponse<?> ajaxResponse = new AjaxResponse<>();
											ajaxResponse.setSuccess(false);
											AjaxResponseReaction<?> arr = new AjaxResponseReaction<>(
												"Unknown Error",
												"An unexpected server error occurred<br>" + swallow.getMessage() +
													"<br>" + ExceptionUtils.getStackTrace(swallow),
												ReactionType.DialogDisplay
											);
											arr.setResponseType(AjaxResponseType.Danger);
											ajaxResponse.addReaction(arr);
											routingContext
												.response()
												.putHeader(CONTENT_TYPE, HTML_HEADER_JSON)
												.end(ajaxResponse.toJson())
											;
									}
							});
					});
		}
		
		private void configurePageServlet(Router router)
		{
				if (Boolean.parseBoolean(Environment.getProperty("BIND_JW_PAGES", "true")))
				{
						ScanResult scanResult = IGuiceContext
																															.instance()
																															.getScanResult();
						
						for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(PageConfiguration.class))
						{
								if (classInfo.isAbstract() || classInfo.isInterface() || classInfo.isStatic())
								{
										continue;
								}
								
								@SuppressWarnings("unchecked")
								Class<IPage<?>> pageClass = (Class<IPage<?>>) classInfo.loadClass();
								PageConfiguration pc = pageClass.getAnnotation(PageConfiguration.class);
								String url = pc.url();
								if (Strings.isNullOrEmpty(url))
								{
										url = "/";
								}
								String finalUrl = url;
								router
									.getWithRegex(url + "*")
									.handler(routingContext -> {
                                            vertx.executeBlocking(() -> withCallScope(() -> {
                                                    configureScopeProperties(routingContext);
                                                    IPage<?> page = IGuiceContext.get(Key.get(IPage.class, Names.named(finalUrl)));
                                                    String pageHtml = page.toString(true);
                                                    routingContext
                                                        .response()
                                                        .putHeader(CONTENT_TYPE, HTML_HEADER_DEFAULT_CONTENT_TYPE);
                                                    routingContext
                                                        .response()
                                                        .write(pageHtml, StandardCharsets.UTF_8.toString());

                                                    return null;
                                            }, CallScopeSource.Http));
                                    });
                        }
                }
        }
		
		private void configureScopeProperties(RoutingContext routingContext)
		{
				CallScopeProperties callScopeProperties = IGuiceContext.get(CallScopeProperties.class);
				callScopeProperties.setSource(CallScopeSource.Http);
				callScopeProperties
					.getProperties()
					.put("RoutingContext", routingContext);
				callScopeProperties
					.getProperties()
					.put("HttpServerRequest", routingContext.request());
				callScopeProperties
					.getProperties()
					.put("HttpServerResponse", routingContext.response());
				callScopeProperties
					.getProperties()
					.put("StreamId",
										routingContext
											.request()
											.streamId());
				
		}
		
		private void configureInternalDataServlet(Router router)
		{
				router
					.get(JW_SCRIPT_LOCATION)
					.handler(routingContext -> {
                            vertx.executeBlocking(() -> withCallScope(() -> {
                                    configureScopeProperties(routingContext);
                                    FileTemplates.getFileTemplate(JWebMPVertx.class, "jwscript", "siteloader");
                                    FileTemplates
                                        .getTemplateVariables()
                                        .put("SITEADDRESSINSERT",
                                                            new StringBuilder(routingContext
                                                                                                                                        .request()
                                                                                                                                        .authority()
                                                                                                                                        .host()));
                                    FileTemplates
                                        .getTemplateVariables()
                                        .put("ROOTADDRESSINSERT",
                                                            new StringBuilder(routingContext
                                                                                                                                        .request()
                                                                                                                                        .authority()
                                                                                                                                        .host()));
                                    try
                                    {
                                            FileTemplates
                                                .getTemplateVariables()
                                                .put("PAGECLASS",
                                                                    new StringBuilder(IGuiceContext
                                                                                                                                                .get(IPage.class)
                                                                                                                                                .getClass()
                                                                                                                                                .getCanonicalName()));

                                    }
                                    catch (ProvisionException | OutOfScopeException e)
                                    {
                                            FileTemplates
                                                .getTemplateVariables()
                                                .put("PAGECLASS", new StringBuilder());
                                    }

                                    try
                                    {
                                            HttpServerRequest hsr = get(HttpServerRequest.class);
                                            FileTemplates
                                                .getTemplateVariables()
                                                .put("%USERAGENT%", new StringBuilder(hsr.getHeader("user-agent")));
                                            FileTemplates
                                                .getTemplateVariables()
                                                .put("%MYIP%",
                                                                    new StringBuilder(hsr
                                                                                                                                                .remoteAddress()
                                                                                                                                                .host()));
                                            FileTemplates
                                                .getTemplateVariables()
                                                .put("%REFERER%", new StringBuilder(hsr.getHeader("referer")));
                                    }
                                    catch (Throwable T)
                                    {

                                    }

                                    StringBuilder output = FileTemplates.renderTemplateScripts("jwscript");
                                    routingContext
                                        .response()
                                        .putHeader(CONTENT_TYPE, HTML_HEADER_JAVASCRIPT);
                                    routingContext
                                        .response()
                                        .write(output.toString(), StandardCharsets.UTF_8.toString());
                                    return null;
                            }, CallScopeSource.Http));
                    });
                }
		
		
		protected IEvent<?, ?> processEvent() throws InvalidRequestException
		{
				IEvent<?, ?> triggerEvent = null;
				try
				{
						AjaxCall<?> call = get(AjaxCall.class);
						Class<?> eventClass = Class.forName(call
																																											.getClassName()
																																											.replace(CHAR_UNDERSCORE, CHAR_DOT));
						triggerEvent = (IEvent<?, ?>) get(eventClass);
				}
				catch (ClassNotFoundException cnfe)
				{
						log.log(Level.FINEST, "Unable to find the event class specified", cnfe);
						throw new InvalidRequestException("The Event To Be Triggered Could Not Be Found");
				}
				return triggerEvent;
		}
		
		
}
