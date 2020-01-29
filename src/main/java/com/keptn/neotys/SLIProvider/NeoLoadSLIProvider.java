package com.keptn.neotys.SLIProvider;

import com.keptn.neotys.SLIProvider.cloudevent.CloudSLIEventNeoload;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.concurrent.TimeUnit;

public class NeoLoadSLIProvider {


        private static final int MAX=24;
        private static final int MAXBLOCK=10;
        public static void main(String[] args) {

            VertxOptions options=new VertxOptions().setMaxWorkerExecuteTime(MAX).setMaxWorkerExecuteTimeUnit(TimeUnit.HOURS).setWarningExceptionTime(MAXBLOCK).setWarningExceptionTimeUnit(TimeUnit.MINUTES);

            Vertx.vertx(options).deployVerticle(new CloudSLIEventNeoload());


        }


}
