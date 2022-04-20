package com.myorg;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.CfnOutputProps;
import software.amazon.awscdk.Fn;
import software.amazon.awscdk.NestedStack;
import software.amazon.awscdk.NestedStackProps;
import software.amazon.awscdk.services.apigateway.Deployment;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.Stage;
import software.amazon.awscdk.services.elasticloadbalancingv2.targets.LambdaTarget;
import software.amazon.awscdk.services.events.targets.LambdaFunction;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Handler;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.jsii.JsiiObject;
import software.constructs.Construct;

import java.util.Collections;

public class ApiGatewayStack extends NestedStack {

    public ApiGatewayStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public ApiGatewayStack(final Construct scope, final String id, final NestedStackProps props) {
        super(scope, id, props);

        Function handler = Function.Builder
                .create(this,"TODO")
                .code(Code.fromDockerBuild("hello-world:latest"))
                .handler("TODO.entrypoint")
                .runtime(Runtime.PROVIDED_AL2)
                .build();

        RestApi api = LambdaRestApi.Builder.create(this,"AWSOpsWheelAPI")
                .restApiName("AWSOpsWheel")
                .handler(handler)
                .build();

        Deployment deployment = Deployment.Builder.create(this, "AWSOpsWheelApi")
                .description("Deployment of the AWS Ops Wheel API")
                .api(api)
                .build();

        createRole(deployment);

        new CfnOutput(this, "ApiGatewayUrl", CfnOutputProps.builder()
                .exportName("Endpoint")
                .value(Fn.sub("https://${AWSOpsWheelAPI}.execute-api.${AWS::Region}.amazonaws.com/app/"))
                .description("Cognito User Pool ARN")
                .build());


    }

    private void createRole(Deployment deployment) {
        Stage.Builder.create(this, "AppStage").deployment(deployment).stageName("app").build();

        PrincipalBase apiGatewayService = ServicePrincipal.Builder.create("apigateway.amazonaws.com").build();

        IManagedPolicy s3ManagedPolicy = ManagedPolicy.fromManagedPolicyArn(this, "s3ManagedPolicy",
                "arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess");

        Role.Builder.create(this, "AWSOpsWheelS3Role")
                .path("/service-role/")
                .managedPolicies(Collections.singletonList(s3ManagedPolicy))
                .assumedBy(apiGatewayService)
                .build();
    }
}
