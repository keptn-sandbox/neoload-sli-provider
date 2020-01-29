package com.keptn.neotys.SLIProvider.KeptnEvents;

public class TestFinished extends KeptnEventGetSLI {


    public TestFinished( String project, String teststrategy, String deploymentstrategy, String stage, String service ) {
        super( project, teststrategy, deploymentstrategy, stage, service );
    }
}
