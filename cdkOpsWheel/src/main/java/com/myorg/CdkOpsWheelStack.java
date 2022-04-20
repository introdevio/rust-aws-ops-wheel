package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;


public class CdkOpsWheelStack extends Stack {
    public CdkOpsWheelStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public CdkOpsWheelStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        new CognitoStack(this,"CognitoStack");
        new ApiGatewayStack(this, "ApiGatewayStack");
        // The code that defines your stack goes here


    }
}
