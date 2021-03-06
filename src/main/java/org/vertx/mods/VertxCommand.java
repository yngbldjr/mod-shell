package org.vertx.mods;

import org.crsh.command.CRaSHCommand;
import org.crsh.command.ScriptException;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.json.DecodeException;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Container;
import org.vertx.java.deploy.impl.Deployment;
import org.vertx.java.deploy.impl.VerticleManager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class VertxCommand extends CRaSHCommand {

  protected final Vertx getVertx() {
    return (Vertx)context.getAttributes().get("vertx");
  }

  protected final Container getContainer() {
    return (Container)context.getAttributes().get("container");
  }

  protected final VerticleManager getManager() {
    try {
      Container container = getContainer();
      Field f = Container.class.getDeclaredField("mgr");
      f.setAccessible(true);
      return (VerticleManager)f.get(container);
    }
    catch (Exception e) {
      throw new ScriptException("Could not access verticle manager");
    }
  }

  protected final  Map<String, Deployment> getDeployments() {
    try {
      VerticleManager mgr = getManager();
      Field d = VerticleManager.class.getDeclaredField("deployments");
      d.setAccessible(true);
      return (Map<String, Deployment>)d.get(mgr);
    }
    catch (Exception e) {
      throw new ScriptException("Could not access deployments");
    }
  }

  public static String join(List<String> parts) {
    StringBuilder sb = new StringBuilder();
    for (String part : parts) {
      sb.append(part);
    }
    return sb.toString();
  }

  public static JsonObject parseJson(List<String> parts) throws ScriptException {
    return parseJson(join(parts));
  }

  public static JsonObject parseJson(String s) throws ScriptException {
    try {
      return new JsonObject(s);
    }
    catch (DecodeException ignore) {
      throw new ScriptException("Invalid JSON:" + s);
    }
  }
}
