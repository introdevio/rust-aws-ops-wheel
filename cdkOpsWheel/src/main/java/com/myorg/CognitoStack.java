package com.myorg;


import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.CfnOutputProps;
import software.amazon.awscdk.CfnParameter;
import software.amazon.awscdk.NestedStack;
import software.amazon.awscdk.NestedStackProps;
import software.amazon.awscdk.services.cognito.*;
import software.constructs.Construct;

import java.util.Collections;
import java.util.List;

public class CognitoStack extends NestedStack {
  public CognitoStack(final Construct scope, final String id) {
    this(scope, id, null);
  }

  public CognitoStack(final Construct scope, final String id, final NestedStackProps props) {
    super(scope, id, props);
    // The code that defines your stack goes here
    CfnParameter adminEmail =
        CfnParameter.Builder.create(this, "AdminEmail").type("String").build();

    AutoVerifiedAttrs autoVerifiedAttrs = AutoVerifiedAttrs.builder().email(true).build();

    PasswordPolicy policy =
        PasswordPolicy.builder()
            .minLength(6)
            .requireLowercase(false)
            .requireUppercase(true)
            .requireSymbols(false)
            .build();

    UserInvitationConfig invitationConfig =
        UserInvitationConfig.builder()
            .emailSubject("Your temporary password for AWS Ops Wheel")
            .emailBody(
                "Your AWS Ops Wheel username is {username} and the temporary password is {####}")
            .build();

    UserPool userPool =
        UserPool.Builder.create(this, "UserPool-" + scope)
            .userInvitation(invitationConfig)
            .accountRecovery(AccountRecovery.EMAIL_ONLY)
            .userPoolName(getStackName())
            .autoVerify(autoVerifiedAttrs)
            .passwordPolicy(policy)
            .build();

    new CfnOutput(
        this,
        "CognitoUserPoolArn",
        CfnOutputProps.builder()
            .exportName("CognitoUserPoolArn")
            .value(userPool.getUserPoolArn())
            .description("Cognito User Pool ARN")
            .build());

    UserPoolClient.Builder.create(this, "CognitoUserPoolClient")
        .userPoolClientName("WheelUIClient")
        .userPool(userPool)
        .build();

    CfnUserPoolUser.Builder.create(this, "CognitoUserPoolAdmin")
        .username("admin")
        .desiredDeliveryMediums(Collections.singletonList("EMAIL"))
        .userPoolId(userPool.getUserPoolId())
        .userAttributes(
            List.of(
                CfnUserPoolUser.AttributeTypeProperty.builder()
                    .name("email")
                    .value(adminEmail.getValueAsString())));
  }
}
