package com.myorg;

import software.amazon.awscdk.NestedStack;
import software.amazon.awscdk.NestedStackProps;
import software.constructs.Construct;

public class ApiGatewayStack extends NestedStack {
  public ApiGatewayStack(final Construct scope, final String id) {
    this(scope, id, null);
  }

  public ApiGatewayStack(final Construct scope, final String id, final NestedStackProps props) {
    super(scope, id, props);


  }
}
