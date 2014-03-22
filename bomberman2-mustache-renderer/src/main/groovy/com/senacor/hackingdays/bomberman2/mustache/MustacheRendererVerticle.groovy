package com.senacor.hackingdays.bomberman2.mustache

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import org.vertx.groovy.platform.Verticle

/**
 *
 * @author Jochen Mader
 */
class MustacheRendererVerticle extends Verticle {

    DefaultMustacheFactory mf = new DefaultMustacheFactory("com/senacor/hackingdays/bomberman2/mustache");

    @Override
    def start() {
        container.logger.error("HELP ME!!!!!!");
        vertx.eventBus.registerHandler("template.render", { message ->
            Mustache mustache = mf.compile(message.body());
            StringWriter sw = new StringWriter();
            mustache.execute(sw, new Example()).flush();
            message.reply(sw.getBuffer().toString());
        });
    }

}
