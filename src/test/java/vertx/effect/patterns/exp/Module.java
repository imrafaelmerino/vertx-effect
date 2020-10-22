package vertx.effect.patterns.exp;

import io.vertx.core.DeploymentOptions;
import vertx.effect.VertxModule;
import vertx.effect.exp.Cons;
import vertx.effect.λ;

public class Module extends VertxModule {

    public static λ<String, String> toLowerCase;
    public static λ<String, String> toUpperCase;
    public static λ<String, String> head;
    public static λ<String, String> tail;

    @Override
    protected void initialize() {
        toLowerCase = this.ask("toLowerCase");
        toUpperCase = this.ask("toUpperCase");
        head = this.ask("head");
        tail = this.ask("tail");
    }

    @Override
    protected void deploy() {
        λ<String, String> toLowerCaseAfter100ms = s -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            return Cons.success(s.toLowerCase());
        };
        λ<String, String> toUpperCaseAfter200ms = s -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
            return Cons.success(s.toUpperCase());
        };

        λ<String, String> head = s -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
            return Cons.success(s.substring(0,1));
        };
        λ<String, String> tail = s -> {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
            }
            return Cons.success(s.substring(1));
        };
        this.deploy("head",
                    head,
                    new DeploymentOptions().setWorker(true));
        this.deploy("tail",
                    tail,
                    new DeploymentOptions().setWorker(true));
        this.deploy("toLowerCase",
                    toLowerCaseAfter100ms,
                    new DeploymentOptions().setWorker(true));
        this.deploy("toUpperCase",
                    toUpperCaseAfter200ms,
                    new DeploymentOptions().setWorker(true));

    }
}
