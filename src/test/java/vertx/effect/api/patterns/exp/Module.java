package vertx.effect.api.patterns.exp;

import io.vertx.core.DeploymentOptions;
import vertx.effect.VIO;
import vertx.effect.VertxModule;
import vertx.effect.Lambda;

public class Module extends VertxModule {

    public static Lambda<String, String> toLowerCase;
    public static Lambda<String, String> toUpperCase;
    public static Lambda<String, String> head;
    public static Lambda<String, String> tail;

    @Override
    protected void initialize() {
        toLowerCase = this.ask("toLowerCase");
        toUpperCase = this.ask("toUpperCase");
        head = this.ask("head");
        tail = this.ask("tail");
    }

    @Override
    protected void deploy() {
        Lambda<String, String> toLowerCaseAfter100ms = s -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            return VIO.succeed(s.toLowerCase());
        };
        Lambda<String, String> toUpperCaseAfter200ms = s -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
            return VIO.succeed(s.toUpperCase());
        };

        Lambda<String, String> head = s -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
            return VIO.succeed(s.substring(0, 1));
        };
        Lambda<String, String> tail = s -> {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
            }
            return VIO.succeed(s.substring(1));
        };
        this.deploy("head",
                    head,
                    new DeploymentOptions().setWorker(true)
                   );
        this.deploy("tail",
                    tail,
                    new DeploymentOptions().setWorker(true)
                   );
        this.deploy("toLowerCase",
                    toLowerCaseAfter100ms,
                    new DeploymentOptions().setWorker(true)
                   );
        this.deploy("toUpperCase",
                    toUpperCaseAfter200ms,
                    new DeploymentOptions().setWorker(true)
                   );

    }
}
