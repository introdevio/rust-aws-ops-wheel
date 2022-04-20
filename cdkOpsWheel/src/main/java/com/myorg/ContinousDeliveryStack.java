package com.myorg;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.cognito.*;
import software.constructs.Construct;

import java.util.Collections;
import java.util.List;

public class ContinousDeliveryStack extends NestedStack {
  public ContinousDeliveryStack(final Construct scope, final String id) {
    this(scope, id, null);
  }

  public ContinousDeliveryStack(final Construct scope, final String id, final NestedStackProps props) {
    super(scope, id, props);


  }
}
