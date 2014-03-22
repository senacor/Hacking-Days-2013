In vertx/sys-mods

mkdir com.senacor.hackingdays~bomberman2-web-server-module~1.0.0-final
in the created directory add module.link with following content
<path_to_project_dir>/bomberman2-web-server

bin/vertx runMod com.senacor.hackingdays~bomberman2-web-server-module~1.0.0-final -conf <path_to_project_dir>/bomberman2-web-server/conf.json

###########

oder vorerst einfach schnell das JS-verticle starten ... vertx run src/main/js/app.js -cluster